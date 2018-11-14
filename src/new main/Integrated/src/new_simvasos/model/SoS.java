package new_simvasos.model;

import new_simvasos.model.Abstract.SIMVASoS_Object;
import new_simvasos.model.Enums.EnumOrgType;

import java.util.ArrayList;

/**
 * @author ymbaek
 *
 * SoS is the highest-level of system class.
 * SoS has one or more Organizations, and each Organization can have multiple CSs.
 * Also, SoS has its Infrastructure (SoSInfra) and manages it.
 */
public class SoS {
    String SoSName;                             //Name of an SoS
    EnumOrgType SoSType;                        //Type of an SoS following OrganizationType (EnumOrgType)

    ArrayList<Organization> SoSOrgs;            //Constituent organizations
    Infrastructure SoSInfra;                    //Infrastructure of an SoS
    ArrayList<SIMVASoS_Object> SoSEnvironment;  //SoS-level environmental factors

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


    /**
     * @param tick              current tick of simulation
     * @param SoSEnvironment    SoS-level EnvironmentElements
     * @return                  Execution log after running a single tick of SoS
     */
    public String runSoS(int tick, ArrayList<SIMVASoS_Object> SoSEnvironment) {
        return null;    //TODO: return accumulated log messages
    }

    public void resetSoS(boolean isRestOrgs, boolean isResetInfraServices, boolean isResetInfraResources) {
        if (isRestOrgs) {
            //SoSOrgs.reset();
            for(Organization org:SoSOrgs) {
                org.reset(true, true, true);
            }
        }
        if (isResetInfraServices) {
            //SoSInfra.resetServices();
        }
        if (isResetInfraResources) {
            //SoSInfra.resetResources();
        }
    }


    /* ADDERS */

    public void addOrg(Organization org) {
        SoSOrgs.add(org);
    }

    //TODO: To decide whether we will include addEnvFactor(...) in SoS class
    public void addEnvFactor(SIMVASoS_Object obj) {
        SoSEnvironment.add(obj);
    }

    public void addSoSInfraService(InfraService infraService) {
        SoSInfra.addInfraService(infraService);
    }

    public void addSoSInfraResource(InfraResource infraResource) {
        SoSInfra.addInfraResource(infraResource);
    }


    /* GETTERS & SETTERS */

    public void setSoSOrgs(ArrayList<Organization> orgs) {
        SoSOrgs = orgs;
    }

    public String getSoSName() {
        return SoSName;
    }

    public void setSoSName(String soSName) {
        SoSName = soSName;
    }

    public EnumOrgType getSoSType() {
        return SoSType;
    }

    public void setSoSType(EnumOrgType soSType) {
        SoSType = soSType;
    }

    public ArrayList<Organization> getSoSOrgs() {
        return SoSOrgs;
    }

    public Infrastructure getSoSInfra() {
        return SoSInfra;
    }

    public ArrayList<SIMVASoS_Object> getSoSEnvironment() {
        return SoSEnvironment;
    }

    public void setSoSInfra(Infrastructure soSInfra) {
        SoSInfra = soSInfra;
    }

    public void setSoSEnvironment(ArrayList<SIMVASoS_Object> soSEnvironment) {
        SoSEnvironment = soSEnvironment;
    }
}
