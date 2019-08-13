package new_simvasos.adaptation.runtimeVerification;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static java.lang.Math.abs;

public class smartHomeController {
    //environment
    private static Double currentOutdoorTemperature;
    private static Double currentOutdoorHumidity;
    private static Double currentIndoorTemperature;
    private static Double currentIndoorHumidity;

    //knowledge
    //environment
    private static ArrayList<Double> indoorTemperatureList;
    private static ArrayList<Double> outdoorTemperatureList;
    private static ArrayList<Double> indoorHumidityList;
    private static ArrayList<Double> outdoorHumidityList;

    //plan
    private static Double commandTemperature;
    private static Double commandHumidity;
    private static ArrayList<Double> commandTemperatureList;
    private static ArrayList<Double> commandHumidityList;

    //configuration
    private static Double windowOpenness;
    private static Double energyEfficiencyOfTemperatureControl;
    private static Double energyEfficiencyOfHumidityControl;

    private static Double maxTemperatureControl;
    private static Double maxHumidityControl;

    public smartHomeController(){
        currentOutdoorTemperature = 0.;
        currentOutdoorHumidity = 0.;
        currentIndoorTemperature = 0.;
        currentIndoorHumidity = 0.;

        indoorTemperatureList = new ArrayList<>();
        outdoorTemperatureList = new ArrayList<>();
        indoorHumidityList = new ArrayList<>();
        outdoorHumidityList = new ArrayList<>();

        commandTemperature = 0.;
        commandHumidity = 0.;
        commandTemperatureList = new ArrayList<>();
        commandHumidityList = new ArrayList<>();

        windowOpenness = 0.5;
        energyEfficiencyOfTemperatureControl = 1.;
        energyEfficiencyOfHumidityControl = 1.;

        maxTemperatureControl = 20.;
        maxHumidityControl = 20.;
    }

    public static void main (String[] args){
        smartHomeController controller = new smartHomeController();
        int maxSimulationTime = 4;


        for(int t = 0; t < maxSimulationTime; t++){
            environmentalChange(t);
            monitor(t);
            analyze(t);
            plan(t);
            effect(t);

        }

        System.out.println(outdoorTemperatureList);
        System.out.println(commandTemperatureList);
        System.out.println(indoorTemperatureList);
    }

    private static void environmentalChange(int time){
        double[] environmentalChanges = {0., 10., 20., 10.};
        currentOutdoorTemperature = environmentalChanges[time];
        currentOutdoorHumidity = environmentalChanges[time];

        currentIndoorTemperature = currentIndoorTemperature + windowOpenness * (currentOutdoorTemperature - currentIndoorTemperature);
        currentIndoorHumidity = currentIndoorHumidity + windowOpenness * (currentOutdoorHumidity - currentIndoorHumidity);

        if (time!=0){
            currentIndoorTemperature = currentIndoorTemperature + commandTemperatureList.get(time-1);
            currentIndoorHumidity = currentIndoorHumidity + commandHumidityList.get(time-1);
        }
    }

    private static void monitor(int time){
        indoorTemperatureList.add(currentIndoorTemperature);
        indoorHumidityList.add(currentIndoorHumidity);
        outdoorTemperatureList.add(currentOutdoorTemperature);
        outdoorHumidityList.add(currentOutdoorHumidity);
    }

    private static void analyze(int time){

    }

    private static void plan(int time){
        Double goal = 0.;
        Double curInTemp = indoorTemperatureList.get(time);
        Double curOutTemp = outdoorTemperatureList.get(time);
        Double curInHumi = indoorHumidityList.get(time);
        Double curOutHumi = outdoorHumidityList.get(time);

        commandTemperature = goal - (curInTemp + windowOpenness * (curOutTemp - curInTemp));
        commandHumidity = goal - (curInHumi + windowOpenness * (curOutHumi - curInHumi));
    }

    private static void effect(int time){
        if (abs(commandTemperature) > maxTemperatureControl){
            commandTemperature = maxTemperatureControl * (abs(commandTemperature)/commandTemperature);
        }
        if (abs(commandHumidity) > maxHumidityControl){
            commandHumidity = maxHumidityControl * (abs(commandHumidity)/commandHumidity);
        }

        commandTemperatureList.add(commandTemperature);
        commandHumidityList.add(commandHumidity);
    }
}
