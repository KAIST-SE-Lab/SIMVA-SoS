package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Object;
import new_simvasos.model.Enums.EnumServiceType;

import java.util.ArrayList;

/**
 * @author ymbaek
 *
 * InfraService is an abstract class that autonomously provide a service to orgs & CSs of an SoS.
 * Each InfraService has its own id, name, type, and it can be occupied by multiple CSs.
 * InfraService can run its own service autonomously, so it has an abstract method runService(...).
 * runService() method is called by the simulation engine.
 */
public abstract class InfraService {
    String serviceId;                       //id of infrastructure service
    String serviceName;                     //name of infrastructure service
    EnumServiceType serviceType;            //type of infrastructure service
    ArrayList<String> serviceOccupyingIds;  //CSs' ids who are occupying this infrastructure service

    public InfraService() {
        serviceId = "noId";
        serviceName = "noName";
        serviceType = null;
        serviceOccupyingIds = new ArrayList<>();
    }

    /**
     * Run this service for providing infrastructure-level functionalities to Orgs/CSs
     * (This method is abstract, so it should be implemented by child classes)
     *
     * @param SoSEnvironment    SoS-level environmental factors
     * @return execution log Message
     */
    abstract String runService(ArrayList<SIMVASoS_Object> SoSEnvironment);

    /* ADDERS */

    void addOccupyingId(String csId) {
        serviceOccupyingIds.add(csId);
    }


    /* GETTERS & SETTERS */

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public EnumServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(EnumServiceType serviceType) {
        this.serviceType = serviceType;
    }

    public ArrayList<String> getServiceOccupyingIds() {
        return serviceOccupyingIds;
    }

    public void setServiceOccupyingIds(ArrayList<String> serviceOccupyingIds) {
        this.serviceOccupyingIds = serviceOccupyingIds;
    }
}
