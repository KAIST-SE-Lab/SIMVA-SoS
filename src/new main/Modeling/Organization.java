import Enums.EnumOrgType;

import java.util.ArrayList;


public class Organization {
    int orgId;
    String orgName;
    EnumOrgType orgType;

    ArrayList<Organization> orgSubOrgs;
    ArrayList<CS> orgCSs;
    ArrayList<Organization_Task> orgTasks;

    public Organization() {
        orgId = -1;
        orgName = "noName";
        orgType = null;
        orgSubOrgs = new ArrayList<Organization>();
        orgCSs = new ArrayList<CS>();
        orgTasks = new ArrayList<Organization_Task>();
    }
}


