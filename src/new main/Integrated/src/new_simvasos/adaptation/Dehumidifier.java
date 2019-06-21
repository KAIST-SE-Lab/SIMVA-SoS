package new_simvasos.adaptation;

import java.util.ArrayList;

public class Dehumidifier extends SmartHomeCS {
    private Double opThresholdSummer;
    private Double opThresholdWinter;
    private int timeThresholdSummer;
    private int timeThresholdWinter;
    private Double humidityControlPower;
    private int controlDegree;
    private String uncertaintyOption;

    public Dehumidifier(String name, String configFile) {
        super(name, configFile);

        humidityControlPower = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "dehumidifier_humidify_control_per_tick"));
        controlDegree = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "dehumidifier_control_degree"));
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
                //increaseHumidity(environment, (-1)*humidityControlPower);
                sophisticatedControl(environment, monitored, opThresholdWinter);
                ret = ret + "ON_W";
            }
            else{   //off
                ret = ret + "OFF_W";
            }
        }
        else{   //summer
            if(monitored != null && monitored > opThresholdSummer){ //on
                //increaseHumidity(environment, (-1)*humidityControlPower);
                sophisticatedControl(environment, monitored, opThresholdSummer);
                ret = ret + "ON_S";
            }
            else{   //off
                ret = ret + "OFF_S";
            }
        }

        ret += " ";
        ret += "dh_m:" + monitored;
        return ret;
    }

    private Double monitor(ArrayList<Double> environment){
        Double realHumidity = environment.get(1);

        //uncertainty operator
        Double monitoredHumidity = realHumidity;

        if(uncertaintyOption.contains("1")){
            monitoredHumidity = uncertaintyUniformDistributionNoise(monitoredHumidity, -1., 1.);
        }
        if(uncertaintyOption.contains("2")){
            monitoredHumidity = uncertaintyMonitoringImprecision(monitoredHumidity, 5.);
        }
        if(uncertaintyOption.contains("3")){
            monitoredHumidity = uncertaintyMonitoringFrequency(monitoredHumidity, 2);
        }
        if(uncertaintyOption.contains("4")){
            monitoredHumidity = uncertaintyMonitoringFailure(monitoredHumidity, 0.05);
        }

        return monitoredHumidity;
    }

    private void sophisticatedControl(ArrayList<Double> environment, Double monitored, Double threshold){
        int controlNum = (int)((monitored - threshold) / (humidityControlPower/controlDegree)) + 1;
        if(controlNum > controlDegree){
            controlNum = controlDegree;
        }
        for(int i = 0; i < controlNum; i++){
            increaseHumidity(environment, (-1)*(humidityControlPower/controlDegree));
            /*if(monitored > threshold){

            }
            else{
                //increaseHumidity(environment, (-1)*(humidityControlPower/controlDegree));
                break;
            }*/
        }
    }
}