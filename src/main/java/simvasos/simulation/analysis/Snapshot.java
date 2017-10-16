package simvasos.simulation.analysis;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mgjin on 2017-06-23.
 */
public class Snapshot {
    protected ArrayList<PropertyValue> properties = new ArrayList<PropertyValue>();

    public void addProperties(HasName subject, HashMap<String, Object> propertyValues) {
        for (Map.Entry<String, Object> property : propertyValues.entrySet()) {
            this.properties.add(new PropertyValue(subject, property.getKey(), property.getValue()));
        }
    }

    public void addProperty(HasName subject, String propertyName, Object propertyValue) {
        this.properties.add(new PropertyValue(subject, propertyName, propertyValue));
    }

    public ArrayList<PropertyValue> getProperties() {
        return this.properties;
    }
}
