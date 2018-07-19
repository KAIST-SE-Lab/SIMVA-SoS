package Abstract;

import Enums.EnumObjState;
import Enums.EnumObjType;

public abstract class SIMVASoS_Object {
    String objName;
    EnumObjType objType;
    EnumObjState objState;
    boolean isObjActivated;


    public void activate(){
        isObjActivated = true;
    }
    public void deactivate(){
        isObjActivated = false;
    }
}
