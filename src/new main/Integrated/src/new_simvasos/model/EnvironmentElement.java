package new_simvasos.model;

/**
 * @author  ymbaek
 *
 * EnvironmentElement is an interface to describe environment(-al) objects or factors.
 * Since EnvironmentElement is defined as an interface,
 * it can be simultaneously used (inhereted) with other class.
 *
 * For example, environment element can be either ActionableObject or NonActionableObject.
 * If the element is an Actionable EnvironmentElement,
 * we can define a new class like [class EnvFactor_A extends ActionableObject implements EnvironmentElement].
 */
public interface EnvironmentElement {

    /**
     * runEnvElement is executed by the simulation engine.
     * When an environmental factor is declared, EnvironmentElement should be implemented (using implements keyword)
     * It means that runEnvElement(tick) should be implemented with what to be executed by the simulation engine.
     *
     * (This is an interface, so this method is automatically compiled as 'public abstract'.)
     *
     * @param tick
     */
    void runEnvElement(int tick);
}
