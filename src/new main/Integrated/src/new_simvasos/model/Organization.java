package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Object;
import new_simvasos.model.Enums.EnumOrgType;

import java.util.ArrayList;


public class Organization {
    int orgId;
    String orgName;
    EnumOrgType orgType;
    boolean isOrgActivated;
    ArrayList<SIMVASoS_Object> SoSEnvironment;

    ArrayList<Organization> orgSubOrgs;
    ArrayList<CS> orgCSs;
    ArrayList<Organization_Task> orgTasks;

    /**
     * @param SoSEnvironment    A list of SoS-level environmental factors
     *                          it could be null.
     */
    public Organization(ArrayList<SIMVASoS_Object> SoSEnvironment) {
        orgId = -1;
        orgName = "noName";
        orgType = null;
        isOrgActivated = false;
        orgSubOrgs = new ArrayList<Organization>();
        orgCSs = new ArrayList<CS>();
        orgTasks = new ArrayList<Organization_Task>();
        this.SoSEnvironment = SoSEnvironment;

    }

    public void activate(){
        isOrgActivated = true;
    }
    public void deactivate(){
        isOrgActivated = false;
    }

    public void addCS(CS cs) {
        orgCSs.add(cs);
    }

    //@param LocationMap
    //@param Environment[]
    public void doOperation(int tick){
        System.out.println("[Org] " + orgName + ": doOperation() at " + tick);
        for(CS cs : orgCSs) {
            cs.doAction(tick);
        }
    }

    public void reset(){

    }

}


