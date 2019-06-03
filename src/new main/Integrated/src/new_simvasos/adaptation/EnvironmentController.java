package new_simvasos.adaptation;

import java.util.ArrayList;


public class EnvironmentController extends SmartHomeCS {    //outdoor environment controller
    Double outdoorTemperature;
    Double outdoorHumidity;
    Double degreeOfOpeningOfWindow;
    EnvironmentModel temperatureModel;
    EnvironmentModel humidityModel;

    public EnvironmentController(String name, String configFile) {
        super(name, configFile);
        outdoorTemperature = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_outdoor_temperature"));
        outdoorHumidity = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_outdoor_humidity"));
        degreeOfOpeningOfWindow = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "degree_of_opening_of_window"));

        String modelPath = FileManager.getValueFromConfigDictionary(super.config, "model_path");
        String temperatureModelFile = FileManager.getValueFromConfigDictionary(super.config, "temperature_model");
        String humidityModelFile = FileManager.getValueFromConfigDictionary(super.config, "humidity_model");

        temperatureModel = new EnvironmentModel(modelPath + temperatureModelFile);
        humidityModel = new EnvironmentModel(modelPath + humidityModelFile);
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = super.name + ":";

        Double newTemperature = temperatureModel.getEnvironmentalValue(tick);
        Double newHumidity = humidityModel.getEnvironmentalValue(tick);

        if(newTemperature >= outdoorTemperature){
            ret = ret + "INC_T/";
            increaseOutdoorTemperature(newTemperature - outdoorTemperature, environment);
        }
        else{
            ret = ret + "DEC_T/";
            increaseOutdoorTemperature(newTemperature - outdoorTemperature, environment);
        }
        if(newHumidity >= outdoorHumidity){
            ret = ret + "INC_H";
            increaseOutdoorHumidity(newHumidity - outdoorHumidity, environment);
        }
        else{
            ret = ret + "DEC_H";
            increaseOutdoorHumidity(newHumidity - outdoorHumidity, environment);
        }

        ret += " ";
        ret += "outdoorTemperature:" + Math.round(outdoorTemperature*100)/100.0;
        ret += " ";
        ret += "outdoorHumidity:" + Math.round(outdoorHumidity*100)/100.0;
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
