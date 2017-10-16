package simvasos.scenario.mciresponse;

import simvasos.propcheck.pattern.ExistenceChecker;
import simvasos.simulation.analysis.PropertyValue;
import simvasos.simulation.analysis.Snapshot;

import java.util.ArrayList;

public class PulloutChecker extends ExistenceChecker {
    int expectedPullout;

    public PulloutChecker(int expectedPullout) {
        super();

        this.expectedPullout = expectedPullout;
    }

    @Override
    public boolean evaluate(Snapshot snapshot) {
        // Override me to evaluate whatever you want
        ArrayList<PropertyValue> properties = snapshot.getProperties();

        boolean res = true;
        for (PropertyValue pv : properties) {
            if (!pv.isAgent && pv.propertyName.equals("Pulledout"))
                res = (int) pv.value >= this.expectedPullout ? true : false;
        }

        // Return
        return res;
    }
}
