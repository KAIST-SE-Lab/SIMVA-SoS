package new_simvasos.adaptation;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;


/**
 * Class System-of-Systems
 * will be replaced by the newly defined SoS
 */
public class SoS {
    public ArrayList <CS> CSs;
    ArrayList <Double> environment;
    ArrayList <Double> initialEnv;

    public SoS(ArrayList <CS> CSs, String configFile) {
        this.CSs = CSs;
        this.environment = new ArrayList<Double>();
        ArrayList<Pair<String, String>> config = FileManager.readConfiguration(configFile);
        Double initialIndoorTemperatureMin = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_indoor_temperature_min"));
        Double initialIndoorTemperatureMax = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_indoor_temperature_max"));
        Double initialIndoorHumidityMin = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_indoor_humidity_min"));
        Double initialIndoorHumidityMax = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_indoor_humidity_max"));

        Random random = new Random();
        this.environment.add((random.nextDouble() * (initialIndoorTemperatureMax - initialIndoorTemperatureMin)) + initialIndoorTemperatureMin);
        this.environment.add((random.nextDouble() * (initialIndoorHumidityMax - initialIndoorHumidityMin)) + initialIndoorHumidityMin);

        this.initialEnv = new ArrayList<Double>();
        this.initialEnv.addAll(this.environment);
    }

    public String run(int tick) {
        String logs = "";
        //Collections.shuffle(CSs);

        for(CS cs : CSs) {
            String result = cs.act(tick, this.environment);

            if(result != null && !result.isEmpty()) {
                logs += result;
                logs += " ";
            }
        }
        //logs.add(String (environment));

        //logs += this.environment.toString();
        //logs += " ";
        logs += "indoorTemperature:" + getTemperature();
        logs += " ";
        logs += "indoorHumidity:" + getHumidity();
        logs += " ";
        logs += "comfort:" + isInComfortZone(tick, getTemperature(), getHumidity());
        logs += " ";
        logs += "TICK:" + tick;

        return logs;
    }

    private boolean isInComfortZone(Integer tick, Double indoorTemperature, Double indoorHumidity){
        // https://www.mathsisfun.com/straight-line-graph-calculate.html 사용
        int time_threshold_summer = 50;
        int time_threshold_winter = 6500;

        if(tick > time_threshold_summer && tick < time_threshold_winter){   // summer
            if(indoorHumidity > equation(-55.1, 1319.25, indoorTemperature) &&
                    indoorHumidity > equation(-1.314, 55.2857, indoorTemperature) &&
                    indoorHumidity < equation(-6.3429, 222.214, indoorTemperature) &&
                    indoorHumidity < equation(-37.5, 1032.3, indoorTemperature)){
                return true;
            }
            else{
                return false;
            }
        }
        else{   // winter
            if(indoorHumidity > equation(-57.2, 1201.9, indoorTemperature) &&
                    indoorHumidity > equation(-1.575, 61.5875, indoorTemperature) &&
                    indoorHumidity < equation(-7.05, 223.975, indoorTemperature) &&
                    indoorHumidity < equation(-35.3, 887.85, indoorTemperature)){
                return true;
            }
            else{
                return false;
            }
        }
    }

    private Double equation(Double c1, Double c0, Double x){
        return (c1 * x) + c0;
    }

    // reset all cs's attributes ex) firefighter
    public void reset() {
        for (int i = 0; i < CSs.size(); i++) {
            CSs.get(i).reset();
        }
        this.resetEnvironment();
    }

    private void resetEnvironment() {
        for (int i = 0; i < this.environment.size(); i++) {
            this.environment.set(i, this.initialEnv.get(i));
        }
    }

    private double getHumidity(){
        return Math.round(environment.get(1)*100)/100.0;
    }

    private double getTemperature(){
        return Math.round(environment.get(0)*100)/100.0;
    }

    public ArrayList<Double> getEnvironment() {
        return environment;
    }

}
