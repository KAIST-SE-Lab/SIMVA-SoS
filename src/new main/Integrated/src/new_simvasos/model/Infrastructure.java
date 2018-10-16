package new_simvasos.model;

import java.util.ArrayList;

public class Infrastructure {
    String InfraName;

    ArrayList<InfraService> infraServices;
    ArrayList<InfraResource> infraResources;

    public Infrastructure(){
        InfraName = "noName";

        infraServices = new ArrayList<>();
        infraResources = new ArrayList<>();
    }

    public Infrastructure(String infraName) {
        InfraName = infraName;

        infraServices = new ArrayList<>();
        infraResources = new ArrayList<>();
    }

    public Infrastructure(String infraName, ArrayList<InfraService> infraServices, ArrayList<InfraResource> infraResources) {
        InfraName = infraName;
        this.infraServices = infraServices;
        this.infraResources = infraResources;
    }

    /* ADDERS */

    public void addInfraService(InfraService infraService) {
        infraServices.add(infraService);
    }

    public void addInfraResource(InfraResource infraResource) {
        infraResources.add(infraResource);
    }


    /* GETTERS & SETTERS */

    public String getInfraName() {
        return InfraName;
    }

    public void setInfraName(String infraName) {
        InfraName = infraName;
    }

    public ArrayList<InfraService> getInfraServices() {
        return infraServices;
    }

    public void setInfraServices(ArrayList<InfraService> infraServices) {
        this.infraServices = infraServices;
    }

    public ArrayList<InfraResource> getInfraResources() {
        return infraResources;
    }

    public void setInfraResources(ArrayList<InfraResource> infraResources) {
        this.infraResources = infraResources;
    }
}
