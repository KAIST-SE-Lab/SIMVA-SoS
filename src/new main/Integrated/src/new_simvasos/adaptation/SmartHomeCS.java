package new_simvasos.adaptation;

import javafx.util.Pair;

import java.util.ArrayList;

public class SmartHomeCS extends CS{
    ArrayList<Pair<String, String>> config;

    public SmartHomeCS(String name, String configFile){
        super(name);
        config = FileManager.readConfiguration(configFile);
    }

    public void increaseTemperature(ArrayList<Double> environment, Double degree){
        environment.set(0, environment.get(0) + degree);
    }

    public void increaseHumidity(ArrayList<Double> environment, Double degree) {
        environment.set(1, environment.get(1) + degree);
    }
}
