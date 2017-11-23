package simvasos.scenario.mciresponse.entity;

import simvasos.modelparsing.modeling.ABCPlus.ABCItem;
import simvasos.modelparsing.modeling.ABCPlus.ABCPlusCS;
import simvasos.scenario.mciresponse.MCIResponseScenario.SoSType;
import simvasos.scenario.mciresponse.MCIResponseWorld;
import simvasos.scenario.mciresponse.Patient;
import simvasos.simulation.component.Action;
import simvasos.simulation.component.Message;
import simvasos.simulation.component.World;
import simvasos.simulation.util.Location;
import simvasos.simulation.util.Maptrix;

import java.util.*;

public class FireFighter extends ABCPlusCS {

    Maptrix<Integer> expectedPatientsMap = (Maptrix<Integer>) this.world.getResources().get("ExpectedPatientsMap");
    Maptrix<Boolean> beliefMap = new Maptrix<Boolean>(Boolean.TYPE, MCIResponseWorld.MAP_SIZE.getLeft(), MCIResponseWorld.MAP_SIZE.getRight());

    private Location location = new Location(MCIResponseWorld.MAP_SIZE.getLeft() / 2, MCIResponseWorld.MAP_SIZE.getRight() / 2);;
    private Location headingLocation = null;
    private int headingBenefit = 0;

    public enum Direction {NONE, LEFT, RIGHT, UP, DOWN}
    private Direction lastDirection;

    private enum Status {Pullout, Complete}
    private int idleTime;
    private Status status;

    private Patient pulledoutPatient = null;

    public FireFighter(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Override
    protected void observeEnvironment() {
        // FireFighter observe current location and update already pulled-out patients
    }

    @Override
    protected void consumeInformation() {
        for (Message message : this.incomingInformation) {
            // PullOut belief share from FireFighters
            if (message.sender.startsWith("ControlTower") && message.purpose == Message.Purpose.Delivery && message.data.containsKey("PulloutBelief")) {
                Maptrix<Boolean> othersBeliefMap = (Maptrix<Boolean>) message.data.get("PulloutBelief");

                boolean localBelief;
                for (int x = 0; x < MCIResponseWorld.MAP_SIZE.getLeft(); x++)
                    for (int y = 0; y < MCIResponseWorld.MAP_SIZE.getRight(); y++) {
                        Location location = new Location(x, y);
                        localBelief = this.beliefMap.getValue(location);
                        localBelief = localBelief || othersBeliefMap.getValue(location);
                        this.beliefMap.setValue(location, localBelief);
                    }

                switch ((SoSType) this.world.getResources().get("Type")) {
                    case Collaborative:
                    case Virtual:
                        if (this.headingLocation != null)
                            if (this.beliefMap.getValue(this.headingLocation))
                                this.headingLocation = null;
                }
            }
        }
    }

    @Override
    protected void generateActiveImmediateActions() {
        // Do pullout patients at this location
        this.immediateActionList.add(new ABCItem(this.pulloutPatient, 10, 1));

        if ((SoSType) this.world.getResources().get("Type") != SoSType.Virtual) {
            // Report pullout belief to others
            Message beliefShare = new Message();
            beliefShare.name = "Report Pullout belief";
            beliefShare.sender = this.getName();
            beliefShare.receiver = "ControlTower";
            beliefShare.purpose = Message.Purpose.Delivery;
            beliefShare.data.put("PulloutLocation", this.location);

            this.immediateActionList.add(new ABCItem(new SendMessage(beliefShare), 9, 1));
        }

        // Set heading location to incomplete area
        if ((SoSType) this.world.getResources().get("Type") != SoSType.Directed) {
            if (this.idleTime >= 4)
                this.immediateActionList.add(new ABCItem(this.setHeadingLocation, 5, 0));
        }
    }

    @Override
    protected void generatePassiveImmediateActions() {
        for (Message message : this.incomingRequests) {
            // N-Directed Moves
            if (message.sender.equals("ControlTower") && message.purpose == Message.Purpose.Order) {
                boolean accepted = false;
                Location newLocation = (Location) message.data.get("HeadingLocation");

                if (this.headingLocation == null && this.status == Status.Complete) {
                    this.headingLocation = newLocation;
                    accepted = true;
                }

                Message beliefShare = new Message();
                beliefShare.name = "Direction acception";
                beliefShare.sender = this.getName();
                beliefShare.receiver = message.sender;
                beliefShare.purpose = Message.Purpose.Response;
                beliefShare.data.put("Accepted", accepted);
                beliefShare.data.put("HeadingLocation", newLocation);

                this.immediateActionList.add(new ABCItem(new SendMessage(beliefShare), 0, 0));

            // N-Acked Moves
            } else if (message.sender.equals("ControlTower") && message.purpose == Message.Purpose.ReqAction) {
                boolean accepted = false;
                Location newLocation = (Location) message.data.get("HeadingLocation");

                if (this.headingLocation == null && this.status == Status.Complete) {
                    if (this.idleTime >= 4) {
                        this.headingLocation = newLocation;
                        this.headingBenefit = (Integer) message.data.get("AdditionalBenefit");
                        accepted = true;
                    }
                }

                Message beliefShare = new Message();
                beliefShare.name = "Direction acception";
                beliefShare.sender = this.getName();
                beliefShare.receiver = message.sender;
                beliefShare.purpose = Message.Purpose.Response;
                beliefShare.data.put("Accepted", accepted);
                beliefShare.data.put("HeadingLocation", newLocation);

                this.immediateActionList.add(new ABCItem(new SendMessage(beliefShare), 0, 0));
            }
        }
    }

    @Override
    protected void generateNormalActions() {
        // N-Autonomous Moves
        if (this.status == Status.Complete) {
            switch ((SoSType) this.world.getResources().get("Type")) {
                case Directed:
                    addFourDirectionMoves(this.directedNormalActionList, 0, true);
                    break;
                case Acknowledged:
                    addFourDirectionMoves(this.normalActionList, headingBenefit, true);
                case Collaborative:
                case Virtual:
                    addFourDirectionMoves(this.normalActionList, 0, false);
            }
        }
    }

    public void addFourDirectionMoves(ArrayList<ABCItem> actionList, int additionalBenefit, boolean directMove) {
        if (this.location.getX() > 0)
            actionList.add(new ABCItem(new Move(Direction.LEFT), additionalBenefit, calculateMoveCost(Direction.LEFT, directMove)));
        if (this.location.getX() < MCIResponseWorld.MAP_SIZE.getLeft() - 1)
            actionList.add(new ABCItem(new Move(Direction.RIGHT), additionalBenefit, calculateMoveCost(Direction.RIGHT, directMove)));
        if (this.location.getY() > 0)
            actionList.add(new ABCItem(new Move(Direction.UP), additionalBenefit, calculateMoveCost(Direction.UP, directMove)));
        if (this.location.getY() < MCIResponseWorld.MAP_SIZE.getRight() - 1)
            actionList.add(new ABCItem(new Move(Direction.DOWN), additionalBenefit, calculateMoveCost(Direction.DOWN, directMove)));
    }

    public int calculateMoveCost(Direction direction, boolean directMove) {
        if (direction == Direction.LEFT)
            return calculateMoveCost(-1, 0, directMove);
        else if (direction == Direction.RIGHT)
            return calculateMoveCost(1, 0, directMove);
        else if (direction == Direction.UP)
            return calculateMoveCost(0, -1, directMove);
        else
            return calculateMoveCost(0, 1, directMove);
    }

    public int calculateMoveCost(int deltaX, int deltaY, boolean directMove) {
        Location nextLocation = this.location.add(deltaX, deltaY);
        return calculateMoveCost(nextLocation, directMove);
    }

    public int calculateMoveCost(Location nextLocation, boolean directMove) {
        int totalCost = 0;

        // Directed or Acknowledged
        if (directMove && this.headingLocation != null) {
            // Headindg cost
            totalCost += nextLocation.distanceTo(this.headingLocation);

        // Voluntary heading location
        } else if (this.headingLocation != null) {
            // Uncertainty
            totalCost += this.world.random.nextInt(8);

            // Headindg cost
            totalCost += nextLocation.distanceTo(this.headingLocation);
        } else {
            // Uncertainty
            totalCost += this.world.random.nextInt(8);

            // Belief cost
            totalCost += this.beliefMap.getValue(nextLocation.getX(), nextLocation.getY()) ? 4 : 0;
            totalCost -= this.expectedPatientsMap.getValue(nextLocation.getX(), nextLocation.getY()) * 4;
        }

        return totalCost;
    }

    @Override
    public void reset() {
        this.beliefMap.reset();
        for (int x = 0; x < MCIResponseWorld.MAP_SIZE.getLeft(); x++)
            for (int y = 0; y < MCIResponseWorld.MAP_SIZE.getRight(); y++)
                this.beliefMap.setValue(x, y, false);

        this.status = Status.Pullout;
        this.idleTime = 0;
        this.location.setLocation(0,0);

        this.lastDirection = Direction.NONE;
        this.pulledoutPatient = null;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSymbol() {
        return this.name.replace("FireFighter", "F");
    }

    @Override
    public HashMap<String, Object> getProperties() {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("Location", this.location);
        return properties;
    }

    private final Action pulloutPatient = new Action(0) {

        @Override
        public void execute() {
            ArrayList<Patient> trappedPatients = ((MCIResponseWorld) FireFighter.this.world).getTrappedPatients(FireFighter.this.location);

            if (trappedPatients.size() > 1)
                FireFighter.this.status = Status.Pullout;
            else
                FireFighter.this.status = Status.Complete;

            if (trappedPatients.size() > 0) {
                FireFighter.this.pulledoutPatient = trappedPatients.get(0);
                pulledoutPatient.setStatus(Patient.Status.Pulledout);
                FireFighter.this.idleTime = 0;
//                System.out.println(FireFighter.this.getName() + ": idle time reset");
            } else {
                FireFighter.this.beliefMap.setValue(FireFighter.this.location, true);
                FireFighter.this.pulledoutPatient = null;
                FireFighter.this.idleTime++;
//                System.out.println(FireFighter.this.getName() + ": idle " + FireFighter.this.idleTime + " times");
            }
        }

        @Override
        public String getName() {
            return FireFighter.this.getName() + ": Pull out a patient";
        }
    };

    private final Action setHeadingLocation = new Action(0) {

        @Override
        public void execute() {
            if (FireFighter.this.headingLocation == null) {
                int mapX = ((MCIResponseWorld) FireFighter.this.world).MAP_SIZE.getLeft();
                int mapY = ((MCIResponseWorld) FireFighter.this.world).MAP_SIZE.getRight();

                PriorityQueue<Location> targetLocations = new PriorityQueue<Location>(mapX * mapY, new Comparator<Location>() {

                    @Override
                    public int compare(Location o1, Location o2) {
                        int v1 = calculateMoveCost(FireFighter.this.location, o1);
                        int v2 = calculateMoveCost(FireFighter.this.location, o2);

                        return v1 - v2;
                    }
                });

                for (int x = 0; x < mapX; x++)
                    for (int y = 0; y < mapY; y++)
                        if (!FireFighter.this.beliefMap.getValue(x, y))
                            targetLocations.offer(new Location(x, y));

                if (targetLocations.size() == 0)
                    return;

                FireFighter.this.headingLocation = targetLocations.poll();
            }
        }

        @Override
        public String getName() {
            return FireFighter.this.getName() + ": Set heading location to incomplete area";
        }
    };

    public int calculateMoveCost(Location currentLocation, Location headingLocation) {
        int totalCost = 0;
        // Distance cost
        totalCost += currentLocation.distanceTo(headingLocation);
        // Belief cost
        totalCost -= this.expectedPatientsMap.getValue(headingLocation.getX(), headingLocation.getY()) * 4;

        return totalCost;
    }

    private class Move extends Action {
        Direction direction = Direction.NONE;

        public Move(Direction direction) {
            super(1);

            this.direction = direction;
        }

        @Override
        public void execute() {
            switch (this.direction) {
                case LEFT:
                    FireFighter.this.location.moveX(-1);
                    break;
                case RIGHT:
                    FireFighter.this.location.moveX(1);
                    break;
                case UP:
                    FireFighter.this.location.moveY(-1);
                    break;
                case DOWN:
                    FireFighter.this.location.moveY(1);
                    break;
                default:
                    System.out.println("FireFighter: Move Error"); // Error
            }

            if (FireFighter.this.headingLocation != null)
                if (FireFighter.this.headingLocation.equals(FireFighter.this.location))
                    FireFighter.this.headingLocation = null;

            FireFighter.this.lastDirection = this.direction;
        }

        @Override
        public String getName() {
            return null;
        }
    }
}
