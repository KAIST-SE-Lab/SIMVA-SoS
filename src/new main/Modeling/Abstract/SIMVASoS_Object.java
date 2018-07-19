package Abstract;

import Enums.EnumObjState;
import Enums.EnumObjType;

/**
 * @author ymbaek
 *
 * Abstract class for every object class in SIMVA-SoS.
 * SIMVASoS_Object represents a non-system object.
 * SIMVASoS_Object basically includes its name, type, state, and activation.
 *
 * If a SIMVASoS_Object should be excluded for the simulation,
 * isObjActivated should be 'false', then it is automatically excluded by the simulator.
 */
public abstract class SIMVASoS_Object {
    String objName;
    EnumObjType objType;
    EnumObjState objState;
    boolean isObjActivated;

    public SIMVASoS_Object() {
        objName = "noName";
        objType = null;
        objState = null;
        isObjActivated = false;
    }


    public void activate(){
        isObjActivated = true;
    }
    public void deactivate(){
        isObjActivated = false;
    }
}
