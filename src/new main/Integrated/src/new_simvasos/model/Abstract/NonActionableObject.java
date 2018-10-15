package new_simvasos.model.Abstract;

import java.util.ArrayList;

/**
 * @author ymbaek
 *
 * Abstract class for every non-actionable object class in SIMVA-SoS.
 * Actionable ojbects cannot perform any actions during the simulation,
 * but the state or value can be accessed and modified by others.
 *
 * NonActionableObject only has its value (objectValue) as a specialized attribute.
 */
public abstract class NonActionableObject extends SIMVASoS_Object {

    float objNumValue;                  //Numeric value of NonActionableObject
    String objStringValue;              //String value of NonActionableObject
    boolean isAvailable;                //Available or not
    ArrayList<String> objOccupyingIds;  //CSs' ids who are occupying this object

    public NonActionableObject() {
        objNumValue = -1;
        objStringValue = "-1";
        isAvailable = true;
        objOccupyingIds = new ArrayList<>();
    }

    /**
     * Add a CS's id who occupies this NonActionableObject
     * Multiple CSs can occupy a single NonActionableObject
     * @param csId  an Id of a CS
     */
    public void addOccupyingId(String csId) {
        objOccupyingIds.add(csId);
    }

    /**
     * Check if this NonActionableObject is available or not
     * @return isAvailable
     */
    public boolean isAvailable() {
        return isAvailable;
    }


    /** GETTERS & SETTERS */

    public float getObjNumValue() {
        return objNumValue;
    }

    public void setObjNumValue(float objNumValue) {
        this.objNumValue = objNumValue;
    }

    public String getObjStringValue() {
        return objStringValue;
    }

    public void setObjStringValue(String objStringValue) {
        this.objStringValue = objStringValue;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
}
