import Enums.EnumOrgType;

import java.util.ArrayList;


public class Organization {
    int orgId;
    String orgName;
    EnumOrgType orgType;
    boolean isOrgActivated;

    ArrayList<Organization> orgSubOrgs;
    ArrayList<CS> orgCSs;
    ArrayList<Organization_Task> orgTasks;

    public Organization() {
        orgId = -1;
        orgName = "noName";
        orgType = null;
        isOrgActivated = false;
        orgSubOrgs = new ArrayList<Organization>();
        orgCSs = new ArrayList<CS>();
        orgTasks = new ArrayList<Organization_Task>();
    }

    public void activate(){
        isOrgActivated = true;
    }
    public void deactivate(){
        isOrgActivated = false;
    }

    //@param LocationMap
    //@param Environment[]
    public void doOperation(){

    }

    public void reset(){

    }


}


