package new_simvasos.adaptation;

import javafx.util.Pair;
import new_simvasos.scenario.Scenario;

import java.util.ArrayList;

public class SmartHomeSimulation extends Simulation {
    private int simulationStartTime;
    private int simulationEndTime;
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

        simulationStartTime = Integer.parseInt(FileManager.getValueFromConfigDictionary(config, "simulation_start_time"));
        simulationEndTime = Integer.parseInt(FileManager.getValueFromConfigDictionary(config, "simulation_end_time"));
        configPath = FileManager.getValueFromConfigDictionary(config, "configuration_path");
        smartHomeConfigFile = FileManager.getValueFromConfigDictionary(config, "smart_home_config_file");
        environmentControllerConfigFile = FileManager.getValueFromConfigDictionary(config, "environment_controller_config_file");
        airConditionerConfigFile = FileManager.getValueFromConfigDictionary(config, "air_conditioner_config_file");
        heaterConfigFile = FileManager.getValueFromConfigDictionary(config, "heater_config_file");
        humidifierConfigFile = FileManager.getValueFromConfigDictionary(config, "humidifier_config_file");
        dehumidifierConfigFile = FileManager.getValueFromConfigDictionary(config, "dehumidifier_config_file");

        super.initSimulation(simulationStartTime, simulationEndTime);
    }

    /*public void initSimulation() {
        super.initSimulation(simulationEndTime);
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
