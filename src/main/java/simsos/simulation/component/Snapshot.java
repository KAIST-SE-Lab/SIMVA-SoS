package simsos.simulation.component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mgjin on 2017-06-23.
 */
public class Snapshot {
    private ArrayList<PropertyValue> properties = new ArrayList<PropertyValue>();

    public void addProperties(Agent agent, HashMap<String, Object> propertyValues) {
        for (Map.Entry<String, Object> property : propertyValues.entrySet()) {
            this.properties.add(new PropertyValue(agent, property.getKey(), property.getValue()));
        }
    }

    public ArrayList<PropertyValue> getProperties() {
        return this.properties;
    }
}
