package simsos.simulation.component;

/**
 * Created by mgjin on 2017-06-23.
 */
public class PropertyValue {
    public PropertyValue(Agent agent, String propertyName, Object value) {
        this.agent = agent;
        this.propertyName = propertyName;
        this.value = value;

        if (agent != null) {
            isAgent = true;
            this.subjectName = agent.getName();
        } else {
            isAgent = false;
            this.subjectName = "World";
        }
    }

    public final String subjectName;
    public final String propertyName;
    public final Object value;

    public final Agent agent;
    public final boolean isAgent;
}
