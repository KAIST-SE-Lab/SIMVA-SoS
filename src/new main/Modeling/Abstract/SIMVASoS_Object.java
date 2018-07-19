package Abstract;

import Enums.EnumObjState;
import Enums.EnumObjType;

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
