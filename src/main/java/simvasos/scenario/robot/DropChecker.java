package simvasos.scenario.robot;

import simvasos.propcheck.pattern.ExistenceChecker;
import simvasos.simulation.analysis.PropertyValue;
import simvasos.simulation.analysis.Snapshot;

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
