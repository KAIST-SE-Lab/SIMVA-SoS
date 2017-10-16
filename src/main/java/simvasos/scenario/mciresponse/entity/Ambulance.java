package simvasos.scenario.mciresponse.entity;

import simvasos.modelparsing.modeling.ABCPlus.ABCItem;
import simvasos.modelparsing.modeling.ABCPlus.ABCPlusCS;
import simvasos.scenario.mciresponse.MCIResponseWorld;
import simvasos.scenario.mciresponse.Patient;
import simvasos.scenario.mciresponse.MCIResponseScenario.SoSType;
import simvasos.simulation.util.*;
import simvasos.simulation.component.Action;
import simvasos.simulation.component.Message;
import simvasos.simulation.component.World;

import java.util.*;

public class Ambulance extends ABCPlusCS {
    LinkedHashMap<String, Location> hospitalLocations = new LinkedHashMap<String, Location>();
    LinkedHashMap<String, Integer> hospitalCapacities = new LinkedHashMap<String, Integer>();

    private Location initialLocation;
    private Location location;

    private enum Direction {NONE, LEFT, RIGHT, UP, DOWN}
    private Direction lastDirection;

    private enum Status {EMPTY, OCCUPIED}
    private Status status;

    private Patient targetPatient = null;
    private Location headingLocation = null;

    public Ambulance(World world, String name, Location location) {
        super(world);

        this.name = name;
        this.initialLocation = location;
        this.reset();
    }

    public void setHospitalInformation(HashMap<String, Location> hospitalLocations, HashMap<String, Integer> hospitalCapacities) {
        this.hospitalLocations.clear();
        this.hospitalCapacities.clear();

        this.hospitalLocations.putAll(hospitalLocations);
        this.hospitalCapacities.putAll(hospitalCapacities);
    }

    @Override
    protected void observeEnvironment() {

    }

    @Override
    protected void consumeInformation() {
        for (Message message : this.incomingInformation) {
            // Capacity response from hospitals at this location
            if (message.sender.startsWith("Hospital") && message.purpose == Message.Purpose.Response && message.data.containsKey("Capacity")) {
                this.hospitalCapacities.put(message.sender, (Integer) message.data.get("Capacity"));

            // Hospitalize response from the hospital
            } else if (message.sender.startsWith("Hospital") && message.purpose == Message.Purpose.Response && message.data.containsKey("Hospitalized")) {
                boolean isHospitalized = (boolean) message.data.get("Hospitalized");
                if (isHospitalized) {
                    this.targetPatient = null;
                    this.headingLocation = null;

                    this.status = Status.EMPTY;
                    this.lastDirection = Direction.NONE;
                } else {
                    this.headingLocation = this.getBestHospitalLocation();
                    this.lastDirection = Direction.NONE;
                }
            }
        }
    }

    @Override
    protected void generateActiveImmediateActions() {
        // Request hospital capacity
        switch ((SoSType) this.world.getResources().get("Type")) {
            default:
                Message capacityRequest = new Message();
                capacityRequest.name = "Request hospital capacity";
                capacityRequest.sender = this.getName();
                capacityRequest.receiver = "Hospital";
                capacityRequest.purpose = Message.Purpose.ReqInfo;
                capacityRequest.data.put("Capacity", null);

                this.immediateActionList.add(new ABCItem(new SendMessage(capacityRequest), 10, 1));
        }

        // Set a target patient at this location
        if (this.targetPatient == null) {
            switch ((SoSType) this.world.getResources().get("Type")) {
                default:
                    this.immediateActionList.add(new ABCItem(new SetTargetPatient(null), 5, 0));
                    this.immediateActionList.add(new ABCItem(this.pickUpAndStartTransport, 4, 0));
            }

        // Try to pick up a patient and start transporting (when it meets the patient
        } else if (this.headingLocation.equals(this.location) && this.status == Status.EMPTY) {
            switch ((SoSType) this.world.getResources().get("Type")) {
                default:
                    this.immediateActionList.add(new ABCItem(this.pickUpAndStartTransport, 4, 0));
                    break;
            }

        // Try to release the patient to the hospital
        } else if (this.headingLocation.equals(this.location) && this.status == Status.OCCUPIED) {
            switch ((SoSType) this.world.getResources().get("Type")) {
                default:
                    Message releasePatientToHospital = new Message();
                    releasePatientToHospital.name = "Request hospitalizing this patient";
                    releasePatientToHospital.sender = this.getName();
                    releasePatientToHospital.receiver = "Hospital";
                    releasePatientToHospital.location = this.location;
                    releasePatientToHospital.purpose = Message.Purpose.ReqAction;
                    releasePatientToHospital.data.put("Patient", this.targetPatient);

                    this.immediateActionList.add(new ABCItem(new SendMessage(releasePatientToHospital), 3, 1));
                    break;
            }
        }
    }

    @Override
    protected void generatePassiveImmediateActions() {

    }

    @Override
    protected void generateNormalActions() {
        // N-Autonomous Moves
        // Search for patient to transport
        if (this.targetPatient == null) {
            // Random search
            if (Ambulance.this.location.getX() > 0 && lastDirection != Direction.RIGHT)
                normalActionList.add(new ABCItem(new Move(Direction.LEFT), 0, calculateMoveCost(Direction.LEFT, false)));
            if (Ambulance.this.location.getX() < MCIResponseWorld.MAP_SIZE.getLeft() - 1 && lastDirection != Direction.LEFT)
                normalActionList.add(new ABCItem(new Move(Direction.RIGHT), 0, calculateMoveCost(Direction.RIGHT, false)));
            if (Ambulance.this.location.getY() > 0 && lastDirection != Direction.DOWN)
                normalActionList.add(new ABCItem(new Move(Direction.UP), 0, calculateMoveCost(Direction.UP, false)));
            if (Ambulance.this.location.getY() < MCIResponseWorld.MAP_SIZE.getRight() - 1 && lastDirection != Direction.UP)
                normalActionList.add(new ABCItem(new Move(Direction.DOWN), 0, calculateMoveCost(Direction.DOWN, false)));

        // Heading to the patient to transport
        // OR Heading to the hospital to deliever
        } else {
            if (Ambulance.this.location.getX() > 0)
                normalActionList.add(new ABCItem(new Move(Direction.LEFT), 0, calculateMoveCost(Direction.LEFT, true)));
            if (Ambulance.this.location.getX() < MCIResponseWorld.MAP_SIZE.getLeft() - 1)
                normalActionList.add(new ABCItem(new Move(Direction.RIGHT), 0, calculateMoveCost(Direction.RIGHT, true)));
            if (Ambulance.this.location.getY() > 0)
                normalActionList.add(new ABCItem(new Move(Direction.UP), 0, calculateMoveCost(Direction.UP, true)));
            if (Ambulance.this.location.getY() < MCIResponseWorld.MAP_SIZE.getRight() - 1)
                normalActionList.add(new ABCItem(new Move(Direction.DOWN), 0, calculateMoveCost(Direction.DOWN, true)));
        }
    }

    public int calculateMoveCost(Direction direction, boolean headingTo) {
        int totalCost = 0;

        int deltaX, deltaY;
        if (direction == Direction.LEFT) {
            deltaX = -1; deltaY = 0;
        } else if (direction == Direction.RIGHT) {
            deltaX = 1; deltaY = 0;
        } else if (direction == Direction.UP) {
            deltaX = 0; deltaY = -1;
        } else {
            deltaX = 0; deltaY = 1;
        }

        if (headingTo) {
            totalCost += this.headingLocation.distanceTo(this.location.add(deltaX, deltaY));
        } else {
            // Uncertainty
            totalCost += this.world.random.nextInt(2);
        }

        return totalCost;
    }

    @Override
    public void reset() {
        this.lastDirection = Direction.NONE;
        this.location = new Location(this.initialLocation);
        this.status = Status.EMPTY;
        this.targetPatient = null;
        this.headingLocation = null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSymbol() {
        return this.name.replace("Ambulance", "A");
    }

    @Override
    public HashMap<String, Object> getProperties() {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("Location", this.location);
        return properties;
    }

    protected class SetTargetPatient extends Action {
        Patient targetPatient;

        public SetTargetPatient(Patient patient) {
            super(0);

            this.targetPatient = targetPatient;
        }

        @Override
        public void execute() {
            if (Ambulance.this.targetPatient != null)
                return;

            if (this.targetPatient != null) {
                Ambulance.this.targetPatient = this.targetPatient;
                Ambulance.this.headingLocation = new Location(this.targetPatient.getLocation());
            } else {
                Set<Patient> pulledoutPatients = ((MCIResponseWorld) Ambulance.this.world).getPulledoutPatients();

                if (!pulledoutPatients.isEmpty()) {
                    List<Patient> list = new ArrayList();
                    list.addAll(pulledoutPatients);
                    Collections.shuffle(list, Ambulance.this.world.random);
                    list.sort(new Comparator<Patient>() {

                        @Override
                        public int compare(Patient o1, Patient o2) {
                            int v1 = o1.getLocation().distanceTo(Ambulance.this.location);
                            int v2 = o2.getLocation().distanceTo(Ambulance.this.location);

                            return v1 - v2;
                        }
                    });

                    this.targetPatient = list.get(0);
                    Ambulance.this.targetPatient = this.targetPatient;
                    Ambulance.this.headingLocation = new Location(this.targetPatient.getLocation());
                }
            }
        }

        @Override
        public String getName() {
            return null;
        }
    }

    private final Action pickUpAndStartTransport = new Action(0) {

        @Override
        public void execute() {
            if (Ambulance.this.targetPatient == null)
                return;

            // Pick up and start transporting
            if (Ambulance.this.targetPatient.getStatus() == Patient.Status.Pulledout) {
                if (Ambulance.this.headingLocation.equals(Ambulance.this.location) && Ambulance.this.status == Status.EMPTY) {
                    Ambulance.this.targetPatient.setStatus(Patient.Status.OnTransport);
                    Ambulance.this.targetPatient.setLocation(Ambulance.this.location);
                    Ambulance.this.headingLocation = Ambulance.this.getBestHospitalLocation();

                    Ambulance.this.status = Status.OCCUPIED;
                }

            // Fail to pick up and start transporting
            } else {
                Ambulance.this.targetPatient = null;
                Ambulance.this.headingLocation = null;

                Ambulance.this.status = Status.EMPTY;
            }
        }

        @Override
        public String getName() {
            return Ambulance.this.getName() + ": Pick up a patient and Start transporting";
        }
    };

    private Location getBestHospitalLocation() {
        if (this.hospitalLocations.isEmpty())
            return null;

        List<Map.Entry<String, Location>> list = new ArrayList();
        for (Map.Entry<String, Location> hospitalInfo : this.hospitalLocations.entrySet())
            if (this.hospitalCapacities.get(hospitalInfo.getKey()) > 0)
                list.add(hospitalInfo);

        Collections.shuffle(list, this.world.random);
        list.sort(new Comparator<Map.Entry<String, Location>>() {

            @Override
            public int compare(Map.Entry<String, Location> o1, Map.Entry<String, Location> o2) {
                int v1 = o1.getValue().distanceTo(Ambulance.this.location);
                int v2 = o2.getValue().distanceTo(Ambulance.this.location);

                return v1 - v2;
            }
        });

        return list.get(0).getValue();
    }

    protected class Move extends Action {
        Direction direction = Direction.NONE;

        public Move(Direction direction) {
            super(1);

            this.direction = direction;
        }

        @Override
        public void execute() {
            switch (this.direction) {
                case LEFT:
                    Ambulance.this.location.moveX(-1);
                    break;
                case RIGHT:
                    Ambulance.this.location.moveX(1);
                    break;
                case UP:
                    Ambulance.this.location.moveY(-1);
                    break;
                case DOWN:
                    Ambulance.this.location.moveY(1);
                    break;
                default:
                    System.out.println("FireFighter: Move Error"); // Error
            }

            Ambulance.this.lastDirection = this.direction;
        }

        @Override
        public String getName() {
            return null;
        }
    }
}
