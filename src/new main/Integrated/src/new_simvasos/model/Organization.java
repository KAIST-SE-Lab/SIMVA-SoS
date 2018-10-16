package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Object;
import new_simvasos.model.Enums.EnumOrgType;

import java.util.ArrayList;


public class Organization {
    int orgId;                              //id of an organization
    String orgName;                         //name of an organization
    EnumOrgType orgType;                    //type of an organization (DIR, ACK, COL, VIR)
    boolean isOrgActivated;                 //activated or not
//    ArrayList<SIMVASoS_Object> SoSEnvironment;    //TODO: SoS Environment should not be a local variable

    ArrayList<Organization> orgSubOrgs;     //list of suborganizations
    ArrayList<CS> orgCSs;                   //constituent systems of an organization
    ArrayList<Organization_Task> orgTasks;  //goal-based tasks of an organization

    /**
     *
     */
    public Organization() {
        orgId = -1;
        orgName = "noName";
        orgType = null;
        isOrgActivated = false;
        orgSubOrgs = new ArrayList<Organization>();
        orgCSs = new ArrayList<CS>();
        orgTasks = new ArrayList<Organization_Task>();
//        this.SoSEnvironment = SoSEnvironment;

    }

    public Organization(int orgId, String orgName, EnumOrgType orgType, boolean isOrgActivated) {
        this.orgId = orgId;
        this.orgName = orgName;
        this.orgType = orgType;
        this.isOrgActivated = isOrgActivated;
        orgSubOrgs = new ArrayList<Organization>();
        orgCSs = new ArrayList<CS>();
        orgTasks = new ArrayList<Organization_Task>();
    }

    //@param LocationMap
    //@param Environment[]
    public void runOrganization(int tick, ArrayList<SIMVASoS_Object> SoSEnvironment){
        System.out.println("[Org] " + orgName + ": doOperation() at " + tick);
        for(CS cs : orgCSs) {
            cs.doAction(tick); //TODO: include SoSEnvironment as a parameter
        }
    }


    /**
     * Activate the organization
     * by setting isOrgActivated as true
     */
    public void activate(){
        isOrgActivated = true;
    }

    /**
     * Deactivate the organization
     * by setting isOrgActivated as false
     */
    public void deactivate(){
        isOrgActivated = false;
    }


    /* ADDERS */

    /**
     * Add a new sub-organization into the organization
     * @param subOrg    suborganization to be added into orgSubOrgs[]
     */
    public void addSubOrgs(Organization subOrg) {
        orgSubOrgs.add(subOrg);
    }

    /**
     * Add a new CS into the organization
     * @param cs    CS to be added into orgCSs[]
     */
    public void addCS(CS cs) {
        orgCSs.add(cs);
    }

    /**
     * Add a new Organization_Task into the organization
     * @param task  Organization_Task to be added into orgTasks[]
     */
    public void addTask(Organization_Task task) {
        orgTasks.add(task);
    }



    /* RESETTER */

    public void reset(boolean isResetSubOrgs, boolean isResetCSs, boolean isResetTasks){
        //Reset suborganizations
        if (isResetSubOrgs) {
            for (Organization org : orgSubOrgs) {
                org.reset(false, false, false);
            }
        }

        //Reset constituent systems
        if (isResetCSs) {
            for (CS cs : orgCSs) {
                //cs.reset();   //TODO: implement CS's reset() method
            }
        }

        //Reset organizational tasks
        if (isResetTasks) {
            for (Organization_Task orgTask : orgTasks) {
                //orgTask.reset();  //TODO: implement Organization_Task's reset() method
            }
        }

    }

    /* GETTERS & SETTERS */

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public EnumOrgType getOrgType() {
        return orgType;
    }

    public void setOrgType(EnumOrgType orgType) {
        this.orgType = orgType;
    }

    public boolean isOrgActivated() {
        return isOrgActivated;
    }

//    public void setOrgActivated(boolean orgActivated) {
//        isOrgActivated = orgActivated;
//    }

    public ArrayList<Organization> getOrgSubOrgs() {
        return orgSubOrgs;
    }

    public void setOrgSubOrgs(ArrayList<Organization> orgSubOrgs) {
        this.orgSubOrgs = orgSubOrgs;
    }

    public ArrayList<CS> getOrgCSs() {
        return orgCSs;
    }

    public void setOrgCSs(ArrayList<CS> orgCSs) {
        this.orgCSs = orgCSs;
    }

    public ArrayList<Organization_Task> getOrgTasks() {
        return orgTasks;
    }

    public void setOrgTasks(ArrayList<Organization_Task> orgTasks) {
        this.orgTasks = orgTasks;
    }
}


