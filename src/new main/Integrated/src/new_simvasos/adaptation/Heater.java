package new_simvasos.adaptation;

import java.util.ArrayList;

public class Heater extends CS {
    public Heater(String name) {
        super(name);
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";
        return ret;
    }
}
