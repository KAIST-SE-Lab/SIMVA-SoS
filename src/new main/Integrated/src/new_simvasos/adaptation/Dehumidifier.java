package new_simvasos.adaptation;

import java.util.ArrayList;

public class Dehumidifier extends CS {
    public Dehumidifier(String name) {
        super(name);
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";
        return ret;
    }
}
