package new_simvasos.adaptation;

import java.util.ArrayList;


public class EnvironmentController extends SmartHomeCS {    //outdoor environment controller
    Double outdoorTemperature;
    Double outdoorHumidity;
    Double degreeOfOpeningOfWindow;

    int peak;

    public EnvironmentController(String name, String configFile) {
        super(name, configFile);
        peak = Integer.parseInt(FileManager.getValueFromConfigDictionary(super.config, "peakPoint"));
        outdoorTemperature = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "initial_outdoor_temperature"));
        outdoorHumidity = Double.parseDouble(FileManager.getValueFromConfigDictionary(super.config, "initial_outdoor_humidity"));
        degreeOfOpeningOfWindow = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "degree_of_opening_of_window"));
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";

        if(tick < peak){
            ret = ret + "INC";
            increaseOutdoorTemperature(1., environment);
            increaseOutdoorHumidity(1., environment);
        }
        else{
            ret = ret + "DEC";
            increaseOutdoorTemperature(-1., environment);
            increaseOutdoorHumidity(-1., environment);
        }

        return ret;
    }

    private void increaseOutdoorTemperature(Double degree, ArrayList<Double> indoorEnvironment){
        this.outdoorTemperature = this.outdoorTemperature + degree;

        Double heatTransfer = (outdoorTemperature - indoorEnvironment.get(0)) * degreeOfOpeningOfWindow;
        increaseTemperature(indoorEnvironment, Math.round(heatTransfer*100)/100.0);
    }

    private void increaseOutdoorHumidity(Double degree, ArrayList<Double> indoorEnvironment){
        outdoorHumidity = outdoorHumidity + degree;

        Double humidityTransfer = (outdoorHumidity - indoorEnvironment.get(1)) * degreeOfOpeningOfWindow;
        increaseHumidity(indoorEnvironment, Math.round(humidityTransfer*100)/100.0);
    }


}
