package new_simvasos.model.Abstract;

import new_simvasos.model.Enums.EnumObjState;
import new_simvasos.model.Enums.EnumObjType;

/**
 * @author ymbaek
 *
 * Abstract class for every object class in SIMVA-SoS.
 * SIMVASoS_Object basically includes its name, type, state, and activation.
 *
 * If a SIMVASoS_Object should be excluded for the simulation,
 * isObjActivated should be 'false', then it is automatically excluded by the simulator.
 */
public class SIMVASoS_Object {
    String objId;                   //Id of every SIMVASoS_Object
    String objName;                 //Name of SIMVASoS_Object
//    EnumObjType objType;            //Type of SIMVASoS_Object
//    EnumObjState objState;          //State of SIMVASoS_Object
    boolean isActivated;            //If an object is activated, simulation engine can access to the object
    ObjectLocation objLocation;

    public SIMVASoS_Object() {
        objId = "noId";
        objName = "noName";
//        objType = null;
//        objState = null;
        isActivated = false;        //Default value is false, so every SIMVASoS_Object should be activated() first
        objLocation = null;
    }

    public SIMVASoS_Object(String objId, String objName) {
        this.objId = objId;
        this.objName = objName;
        isActivated = false;        //Default value is false, so every SIMVASoS_Object should be activated() first
        objLocation = null;
    }

    public SIMVASoS_Object(String objId, String objName, boolean isActivated, ObjectLocation objLocation) {
        this.objId = objId;
        this.objName = objName;
        this.isActivated = isActivated;
        this.objLocation = objLocation;
    }

    /**
     * Activate an object
     */
    public void activate(){
        isActivated = true;
    }

    /**
     * Deactivate an object
     */
    public void deactivate(){
        isActivated = false;
    }


    /* GETTERS & SETTERS */

    public String getObjId() {
        return objId;
    }

    public void setObjId(String objId) {
        this.objId = objId;
    }

    public String getObjName() {
        return objName;
    }

    public void setObjName(String objName) {
        this.objName = objName;
    }

    public boolean isActivated() {
        return isActivated;
    }

    public ObjectLocation getObjLocation() {
        return objLocation;
    }

    public void setObjLocation(ObjectLocation objLocation) {
        this.objLocation = objLocation;
    }
}
