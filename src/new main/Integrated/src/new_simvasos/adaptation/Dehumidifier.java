package new_simvasos.adaptation;

import java.util.ArrayList;

public class Dehumidifier extends SmartHomeCS {
    private Double opThresholdSummer;
    private Double opThresholdWinter;
    private int timeThresholdSummer;
    private int timeThresholdWinter;
    private Double humidityControlPower;
    private int controlDegree;

    public Dehumidifier(String name, String configFile) {
        super(name, configFile);

        humidityControlPower = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "dehumidifier_humidify_control_per_tick"));
        controlDegree = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "dehumidifier_control_degree"));
        opThresholdSummer = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "operation_threshold_summer"));
        opThresholdWinter = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "operation_threshold_winter"));
        timeThresholdSummer = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "time_threshold_summer"));
        timeThresholdWinter = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "time_threshold_winter"));
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";

        if(tick < timeThresholdSummer || tick > timeThresholdWinter){   //winter
            if(monitor(environment) > opThresholdWinter){ //on
                //increaseHumidity(environment, (-1)*humidityControlPower);
                sophisticatedControl(environment, opThresholdWinter);
                ret = ret + "ON_W";
            }
            else{   //off
                ret = ret + "OFF_W";
            }
        }
        else{   //summer
            if(monitor(environment) > opThresholdSummer){ //on
                //increaseHumidity(environment, (-1)*humidityControlPower);
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
        Double realHumidity = environment.get(1);

        //uncertainty operator
        Double monitoredHumidity = realHumidity;

        return monitoredHumidity;
    }

    private void sophisticatedControl(ArrayList<Double> environment, Double threshold){
        for(int i = 0; i < controlDegree; i++){
            if(monitor(environment) > threshold){
                increaseHumidity(environment, (-1)*(humidityControlPower/controlDegree));
            }
            else{
                //increaseHumidity(environment, (-1)*(humidityControlPower/controlDegree));
                break;
            }
        }
    }
}
