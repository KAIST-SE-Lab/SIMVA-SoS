package simvasos.scenario.mciresponse.entity;

import simvasos.modelparsing.modeling.ABCPlus.ABCItem;
import simvasos.modelparsing.modeling.ABCPlus.ABCPlusCS;
import simvasos.scenario.mciresponse.Patient;
import simvasos.scenario.mciresponse.MCIResponseScenario;
import simvasos.simulation.component.Message;
import simvasos.simulation.component.World;
import simvasos.simulation.util.Location;

import java.util.HashMap;

public class Hospital extends ABCPlusCS {

    private Location location;

    private final int maxCapacity;
    private int capacity;

    public Hospital(World world, String name, Location location, int maxCapacity) {
        super(world);

        this.name = name;
        this.location = location;
        this.maxCapacity = maxCapacity;

        this.reset();
    }

    @Override
    protected void observeEnvironment() {

    }

    @Override
    protected void consumeInformation() {

    }

    @Override
    protected void generateActiveImmediateActions() {

    }

    @Override
    protected void generatePassiveImmediateActions() {
        for (Message message : this.incomingRequests) {
            // I-ReportCapacity
            if (message.sender.startsWith("Ambulance") && message.purpose == Message.Purpose.ReqInfo && message.data.containsKey("Capacity")) {
                switch ((MCIResponseScenario.SoSType) this.world.getResources().get("Type")) {
                    default:
                        Message locationReport = new Message();
                        locationReport.name = "Respond hospital capacity";
                        locationReport.sender = this.getName();
                        locationReport.receiver = message.sender;
                        locationReport.purpose = Message.Purpose.Response;
                        locationReport.data.put("Capacity", this.capacity);

                        this.immediateActionList.add(new ABCItem(new SendMessage(locationReport), 0, 1));
                        break;
                }

            // Request Hospitalizing
            } else if (message.sender.startsWith("Ambulance") && message.purpose == Message.Purpose.ReqAction && message.data.containsKey("Patient")) {
                switch ((MCIResponseScenario.SoSType) this.world.getResources().get("Type")) {
                    default:
                        boolean isHospitalized = false;
                        if (this.capacity > 0) {
                            this.capacity--;
                            Patient patient = (Patient) message.data.get("Patient");
                            patient.setStatus(Patient.Status.Hospitalized);
                            patient.setLocation(this.location);
                            isHospitalized = true;
                        }

                        Message hospitalizePatient = new Message();
                        hospitalizePatient.name = "Respond hospitalizing the patient";
                        hospitalizePatient.sender = this.getName();
                        hospitalizePatient.receiver = message.sender;
                        hospitalizePatient.purpose = Message.Purpose.Response;
                        hospitalizePatient.data.put("Hospitalized", isHospitalized);

                        this.immediateActionList.add(new ABCItem(new SendMessage(hospitalizePatient), 3, 1));
                }
            }
        }
    }

    @Override
    protected void generateNormalActions() {

    }

    @Override
    public void reset() {
        this.capacity = this.maxCapacity;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSymbol() {
        return this.name.replace("Hospital", "H");
    }

    @Override
    public HashMap<String, Object> getProperties() {
        HashMap<String, Object> properties = new HashMap<String, Object>();
        properties.put("Capacity", this.capacity);
        properties.put("Location", this.location);
        return properties;
    }
}
