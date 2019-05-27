package new_simvasos.adaptation;

import new_simvasos.scenario.Event;
import new_simvasos.scenario.Scenario;

import java.util.ArrayList;

public class SmartHomeSimulation extends Simulation {
    private int simulationTime;


    public SmartHomeSimulation(String configurationFile) {
        System.out.println("Simulation > Constructor 2");
        readConfiguration(configurationFile);
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
