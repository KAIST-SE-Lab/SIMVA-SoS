package new_simvasos.model.Abstract;

/**
 * @author ymbaek
 *
 * Every SIMVASoS_Object has to have its own ObjectLocation.
 * ObjectLocation basically consists of 3-dimensional values (x, y, z positions) and its location information.
 *
 * If it is not needed to specifically include three dimensional values,
 * we can use locationInfo instead of (x,y,z) values.
 */
public class ObjectLocation {
    float xPos;             //x position of an object
    float yPos;             //y position of an object
    float zPos;             //z position of an object
    String locationInfo;    //Location description of an object


    public ObjectLocation() {
        xPos = -1;
        yPos = -1;
        zPos = -1;
        locationInfo = "noInfo";
    }

    public ObjectLocation(float xPos, float yPos, float zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }

    public ObjectLocation(String locationInfo) {
        this.locationInfo = locationInfo;
    }

    public ObjectLocation(float xPos, float yPos, float zPos, String locationInfo) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.locationInfo = locationInfo;
    }

    /**
     * Set 3-dimensional object location
     *
     * @param xPos  x position of an object
     * @param yPos  y position of an object
     * @param zPos  z position of an object
     */
    public void setXYZLocation(float xPos, float yPos, float zPos) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
    }


    /** GETTERS & SETTERS */

    public float getxPos() {
        return xPos;
    }

    public void setxPos(float xPos) {
        this.xPos = xPos;
    }

    public float getyPos() {
        return yPos;
    }

    public void setyPos(float yPos) {
        this.yPos = yPos;
    }

    public float getzPos() {
        return zPos;
    }

    public void setzPos(float zPos) {
        this.zPos = zPos;
    }

    public String getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(String locationInfo) {
        this.locationInfo = locationInfo;
    }

}
