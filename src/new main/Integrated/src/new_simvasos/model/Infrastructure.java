package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Object;

import java.util.ArrayList;

/**
 * @author ymbaek
 *
 * Infrastructure is one of the major components of an SoS.
 * The Infrastructure mainly provides two things: (1) Services, (2) Resources,
 * so it has two lists for managing services and resources.
 */
public class Infrastructure {
    String InfraName;                       //Name of infrastructure

    ArrayList<InfraService> infraServices;  //List of infrastructure services
    ArrayList<InfraResource> infraResources;//List of infrastructure resources

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

    public String runInfrastructure(int tick, ArrayList<SIMVASoS_Object> SoSEnvironment) {
        return "";
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
