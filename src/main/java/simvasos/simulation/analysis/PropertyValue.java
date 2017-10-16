package simvasos.simulation.analysis;

import simvasos.simulation.component.Agent;

/**
 * Created by mgjin on 2017-06-23.
 */
public class PropertyValue {
    public PropertyValue(HasName subject, String propertyName, Object value) {
        this.subject = subject;
        this.propertyName = propertyName;
        this.value = value;

        if (this.subject instanceof Agent) {
            isAgent = true;
            this.subjectName = subject.getName();
        } else if (this.subject == null) {
            isAgent = false;
            this.subjectName = "World";
        } else {
            isAgent = false;
            this.subjectName = subject.getName();
        }
    }

    public final String subjectName;
    public final String propertyName;
    public final Object value;

    public final HasName subject;
    public final boolean isAgent;
}
