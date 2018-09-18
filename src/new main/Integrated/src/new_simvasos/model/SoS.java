package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Object;
import new_simvasos.model.Enums.EnumOrgType;

import java.util.ArrayList;

public class SoS {
    String SoSName;
    EnumOrgType SoSType;    //TODO: Definition of SoS types

    ArrayList<Organization> SoSOrgs;
    Infrastructure SoSInfra;
    ArrayList<SIMVASoS_Object> SoSEnvironment;

    public SoS(){
        SoSName = "noName";
        SoSType = null;
        SoSOrgs = new ArrayList<Organization>();
        SoSInfra = null;
        SoSEnvironment = new ArrayList<SIMVASoS_Object>();
    }

    public SoS(String soSName, EnumOrgType soSType) {
        SoSName = soSName;
        SoSType = soSType;
        SoSOrgs = new ArrayList<Organization>();
        SoSInfra = null;
        SoSEnvironment = new ArrayList<SIMVASoS_Object>();
    }

    public SoS(String soSName, EnumOrgType soSType, ArrayList<Organization> soSOrgs, Infrastructure soSInfra, ArrayList<SIMVASoS_Object> soSEnvironment) {
        SoSName = soSName;
        SoSType = soSType;
        SoSOrgs = soSOrgs;
        SoSInfra = soSInfra;
        SoSEnvironment = soSEnvironment;
    }

    public void addOrg(Organization org) {
        SoSOrgs.add(org);
    }

    public void addEnvFactor(SIMVASoS_Object obj) {
        SoSEnvironment.add(obj);
    }

    public void setSoSOrgs(ArrayList<Organization> orgs) {
        SoSOrgs = orgs;
    }

    public void setSoSInfra(Infrastructure infra) {
        SoSInfra = infra;
    }

    public void setSoSEnvironment(ArrayList<SIMVASoS_Object> environment) {
        SoSEnvironment = environment;
    }

}
