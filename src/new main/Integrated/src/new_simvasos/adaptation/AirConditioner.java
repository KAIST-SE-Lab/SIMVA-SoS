package new_simvasos.adaptation;

import java.util.ArrayList;

public class AirConditioner extends SmartHomeCS {
    private Double opThresholdSummer;
    private Double opThresholdWinter;
    private int timeThresholdSummer;
    private int timeThresholdWinter;
    private Double temperatureControlPower;
    private int controlDegree;
    private String uncertaintyOption;

    public AirConditioner(String name, String configFile) {
        super(name, configFile);

        temperatureControlPower = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "air_conditioner_temperature_control_per_tick"));
        controlDegree = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "air_conditioner_control_degree"));
        opThresholdSummer = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "operation_threshold_summer"));
        opThresholdWinter = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "operation_threshold_winter"));
        timeThresholdSummer = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "time_threshold_summer"));
        timeThresholdWinter = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "time_threshold_winter"));
        uncertaintyOption = FileManager.getValueFromConfigDictionary(super.config, "uncertainty_option");
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";

        Double monitored = monitor(environment);
        if(tick < timeThresholdSummer || tick > timeThresholdWinter){   //winter
            if(monitored != null && monitored > opThresholdWinter){ //on
                //increaseTemperature(environment, (-1)*temperatureControlPower);
                sophisticatedControl(environment, monitored, opThresholdWinter);
                ret = ret + "ON_W";
            }
            else{   //off
                ret = ret + "OFF_W";
            }
        }
        else{   //summer
            if(monitored != null && monitored > opThresholdSummer){ //on
                //increaseTemperature(environment, (-1)*temperatureControlPower);
                sophisticatedControl(environment, monitored, opThresholdSummer);
                ret = ret + "ON_S";

            }
            else{   //off
                ret = ret + "OFF_S";
            }
        }

        ret += " ";
        ret += "ac_m:" + monitored;
        return ret;
    }

    private Double monitor(ArrayList<Double> environment){
        Double realTemperature = environment.get(0);

        //uncertainty operator
        Double monitoredTemperature = realTemperature;

        if(uncertaintyOption.contains("1")){
            monitoredTemperature = uncertaintyUniformDistributionNoise(monitoredTemperature, -1., 1.);
        }
        if(uncertaintyOption.contains("2")){
            monitoredTemperature = uncertaintyMonitoringImprecision(monitoredTemperature, 1.);
        }
        if(uncertaintyOption.contains("3")){
            monitoredTemperature = uncertaintyMonitoringFrequency(monitoredTemperature, 2);
        }
        if(uncertaintyOption.contains("4")){
            monitoredTemperature = uncertaintyMonitoringFailure(monitoredTemperature, 0.05);
        }

        return monitoredTemperature;
    }

    private void sophisticatedControl(ArrayList<Double> environment, Double monitored, Double threshold){
        int controlNum = (int)((monitored - threshold) / (temperatureControlPower/controlDegree)) + 1;
        if(controlNum > controlDegree){
            controlNum = controlDegree;
        }
        for(int i = 0; i < controlNum; i++){
            increaseTemperature(environment, (-1)*(temperatureControlPower/controlDegree));
//            if(monitored > threshold){
//
//            }
//            else{
//                //increaseTemperature(environment, (-1)*(temperatureControlPower/controlDegree));
//                break;
//            }
        }
    }
}
