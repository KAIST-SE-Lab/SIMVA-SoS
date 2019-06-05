package new_simvasos.adaptation;

import java.util.ArrayList;

public class Heater extends SmartHomeCS {
    private Double opThresholdSummer;
    private Double opThresholdWinter;
    private int timeThresholdSummer;
    private int timeThresholdWinter;
    private Double temperatureControlPower;

    public Heater(String name, String configFile) {
        super(name, configFile);

        temperatureControlPower = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "heater_temperature_control_per_tick"));
        opThresholdSummer = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "operation_threshold_summer"));
        opThresholdWinter = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "operation_threshold_winter"));
        timeThresholdSummer = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "time_threshold_summer"));
        timeThresholdWinter = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "time_threshold_winter"));
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";

        if(tick < timeThresholdSummer || tick > timeThresholdWinter){   //winter
            if(monitor(environment) < opThresholdWinter){ //on
                //increaseTemperature(environment, temperatureControlPower);
                sophisticatedControl(environment, opThresholdWinter);
                ret = ret + "ON_W";
            }
            else{   //off
                ret = ret + "OFF_W";
            }
        }
        else{   //summer
            if(monitor(environment) < opThresholdSummer){ //on
                //increaseTemperature(environment, temperatureControlPower);
                sophisticatedControl(environment, opThresholdSummer);
                ret = ret + "ON_S";
            }
            else{   //off
                ret = ret + "OFF_S";
            }
        }

        return ret;
    }

    private Double monitor(ArrayList<Double> environment){
        Double realTemperature = environment.get(0);

        //uncertainty operator
        Double monitoredTemperature = realTemperature;

        return monitoredTemperature;
    }

    private void sophisticatedControl(ArrayList<Double> environment, Double threshold){
        int controlDegree = 5;

        for(int i = 0; i < controlDegree; i++){
            if(monitor(environment) < threshold){
                increaseTemperature(environment, (temperatureControlPower/controlDegree));
            }
            else{
                increaseTemperature(environment, (temperatureControlPower/controlDegree));
                break;
            }
        }
    }
}
