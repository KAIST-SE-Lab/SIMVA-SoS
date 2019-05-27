package new_simvasos.adaptation;

import javafx.util.Pair;

import java.util.ArrayList;

public class Heater extends CS {
    public Heater(String name, String configFile) {
        super(name);
        ArrayList<Pair<String, String>> config = FileManager.readConfiguration(configFile);
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";
        return ret;
    }
}
