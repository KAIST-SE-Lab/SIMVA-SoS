package Abstract;

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

    float objectValue;

    public NonActionableObject() {
        objectValue = -1;
    }
}
