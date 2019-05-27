package new_simvasos.adaptation;

import java.util.ArrayList;

public class EnvironmentController extends CS {
    Double temperature;
    Double humidity;

    public EnvironmentController(String name) {
        super(name);
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";
        return ret;
    }
}
