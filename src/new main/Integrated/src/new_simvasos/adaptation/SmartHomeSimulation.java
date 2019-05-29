package new_simvasos.adaptation;

import javafx.util.Pair;
import new_simvasos.scenario.Scenario;

import java.util.ArrayList;

public class SmartHomeSimulation extends Simulation {
    private int simulationTime;
    private String configPath;
    private String environmentControllerConfigFile;
    private String smartHomeConfigFile;
    private String airConditionerConfigFile;
    private String heaterConfigFile;
    private String humidifierConfigFile;
    private String dehumidifierConfigFile;


    public SmartHomeSimulation(String configurationFile) {
        System.out.println("Simulation > Constructor 2");
        ArrayList<Pair<String, String>> config = FileManager.readConfiguration(configurationFile);

        simulationTime = Integer.parseInt(FileManager.getValueFromConfigDictionary(config, "simulationTime"));
        configPath = FileManager.getValueFromConfigDictionary(config, "configurationPath");
        smartHomeConfigFile = FileManager.getValueFromConfigDictionary(config, "smartHomeConfigFile");
        environmentControllerConfigFile = FileManager.getValueFromConfigDictionary(config, "environmentControllerConfigFile");
        airConditionerConfigFile = FileManager.getValueFromConfigDictionary(config, "airConditionerConfigFile");
        heaterConfigFile = FileManager.getValueFromConfigDictionary(config, "heaterConfigFile");
        humidifierConfigFile = FileManager.getValueFromConfigDictionary(config, "humidifierConfigFile");
        dehumidifierConfigFile = FileManager.getValueFromConfigDictionary(config, "dehumidifierConfigFile");

        super.initSimulation(simulationTime);
    }

    /*public void initSimulation() {
        super.initSimulation(simulationTime);
    }*/


    @Override
    public void initModels(){
        //initialize parameters with configurations
        ArrayList<CS> devices = new ArrayList<CS>();
        devices.add(new EnvironmentController("outdoorEnvironment", configPath+environmentControllerConfigFile));
        devices.add(new AirConditioner("airConditioner", configPath+airConditionerConfigFile));
        devices.add(new Heater("heater", configPath+heaterConfigFile));
        devices.add(new Humidifier("humidifier", configPath+humidifierConfigFile));
        devices.add(new Dehumidifier("dehumidifier", configPath+dehumidifierConfigFile));

        targetSoS = new SoS(devices, configPath+smartHomeConfigFile);
    }

    @Override
    void initScenario() {
        targetScenario = new Scenario(new ArrayList());
    }
}
