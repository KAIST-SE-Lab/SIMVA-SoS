package new_simvasos.adaptation;

import java.util.ArrayList;

public class Humidifier extends SmartHomeCS {
    private Double opThresholdSummer;
    private Double opThresholdWinter;
    private int timeThresholdSummer;
    private int timeThresholdWinter;
    private Double humidityControlPower;

    public Humidifier(String name, String configFile) {
        super(name, configFile);

        humidityControlPower = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "humidifier_humidity_control_per_tick"));
        opThresholdSummer = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "operation_threshold_summer"));
        opThresholdWinter = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "operation_threshold_winter"));
        timeThresholdSummer = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "time_threshold_summer"));
        timeThresholdWinter = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "time_threshold_winter"));
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";

        if(tick < timeThresholdSummer || tick > timeThresholdWinter){   //winter
            if(monitor(environment) < opThresholdWinter){ //on
                increaseHumidity(environment, humidityControlPower);
                ret = ret + "ON_W";
            }
            else{   //off
                ret = ret + "OFF_W";
            }
        }
        else{   //summer
            if(monitor(environment) < opThresholdSummer){ //on
                increaseHumidity(environment, humidityControlPower);
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
}
