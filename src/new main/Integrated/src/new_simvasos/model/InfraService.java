package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Object;
import new_simvasos.model.Enums.EnumServiceType;

import java.util.ArrayList;

public abstract class InfraService {
    String serviceId;
    String serviceName;
    EnumServiceType serviceType;
    ArrayList<String> serviceOccupyingIds;

    public InfraService() {
        serviceId = "noId";
        serviceName = "noName";
        serviceType = null;
        serviceOccupyingIds = new ArrayList<>();
    }

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
