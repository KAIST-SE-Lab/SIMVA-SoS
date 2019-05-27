package new_simvasos.adaptation;

import javafx.util.Pair;
import new_simvasos.scenario.Scenario;

import java.util.ArrayList;

public class SmartHomeSimulation extends Simulation {
    private int simulationTime;
    private String configurationPath;
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
        configurationPath = FileManager.getValueFromConfigDictionary(config, "configurationPath");
        smartHomeConfigFile = FileManager.getValueFromConfigDictionary(config, "smartHomeConfigFile");
        environmentControllerConfigFile = FileManager.getValueFromConfigDictionary(config, "environmentControllerConfigFile");
        airConditionerConfigFile = FileManager.getValueFromConfigDictionary(config, "airConditionerConfigFile");
        heaterConfigFile = FileManager.getValueFromConfigDictionary(config, "heaterConfigFile");
        humidifierConfigFile = FileManager.getValueFromConfigDictionary(config, "humidifierConfigFile");
        dehumidifierConfigFile = FileManager.getValueFromConfigDictionary(config, "dehumidifierConfigFile");
    }

    public void initSimulation() {
        super.initSimulation(simulationTime);
    }


    @Override
    public void initModels(){
        //initialize parameters with configurations
        ArrayList<CS> devices = new ArrayList<CS>();
        devices.add(new EnvironmentController("outdoorEnvironment"));
        devices.add(new AirConditioner("airConditioner"));
        devices.add(new Heater("heater"));
        devices.add(new Humidifier("humidifier"));
        devices.add(new Dehumidifier("dehumidifier"));

        ArrayList<Double> tempAndHumi = new ArrayList<Double>();
        tempAndHumi.add(0.);
        tempAndHumi.add(0.);
        targetSoS = new SoS(devices, tempAndHumi);
    }

    @Override
    void initScenario() {
        targetScenario = new Scenario(new ArrayList());
    }

    private void readConfiguration(String fileName){
        simulationTime = 100;
    }

}
