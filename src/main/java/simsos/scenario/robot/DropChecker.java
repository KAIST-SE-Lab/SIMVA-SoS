package simsos.scenario.robot;

import simsos.propcheck.pattern.ExistenceChecker;
import simsos.simulation.component.PropertyValue;
import simsos.simulation.component.Snapshot;
import simsos.simulation.component.World;

import java.util.ArrayList;

/**
 * Created by mgjin on 2017-06-23.
 */
public class DropChecker extends ExistenceChecker {

    @Override
    public boolean evaluate(Snapshot snapshot) {
        // Override me to evaluate whatever you want
        ArrayList<PropertyValue> properties = snapshot.getProperties();

        // Token
        // True: Agent has a token
        // False: Agent doesn't have a token
        boolean res = true;
        for (PropertyValue pv : properties) {
            if (pv.isAgent && pv.propertyName.equals("token"))
                res = res && ((boolean) pv.value);
        }

        // Return
        // True: At least one agent has no token
        // False: All agents have a token
        return !res;
    }
}
