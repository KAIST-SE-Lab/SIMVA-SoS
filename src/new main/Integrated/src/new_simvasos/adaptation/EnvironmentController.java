package new_simvasos.adaptation;

import javafx.util.Pair;

import java.util.ArrayList;

public class EnvironmentController extends CS {
    Double temperature;
    Double humidity;

    public EnvironmentController(String name, String configFile) {
        super(name);
        ArrayList<Pair<String, String>> config = FileManager.readConfiguration(configFile);
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";
        return ret;
    }
}
