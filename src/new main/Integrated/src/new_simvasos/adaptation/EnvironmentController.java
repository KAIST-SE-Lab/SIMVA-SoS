package new_simvasos.adaptation;

import java.util.ArrayList;
import java.util.Random;


public class EnvironmentController extends SmartHomeCS {    //outdoor environment controller
    Double outdoorTemperature;
    Double outdoorHumidity;
    Double degreeOfOpeningOfWindow;
    Double degreeOfOpeningOfWindowMin;
    Double degreeOfOpeningOfWindowMax;
    EnvironmentModel temperatureModel;
    EnvironmentModel humidityModel;
    int experimentOption;


    public EnvironmentController(String name, String configFile) {
        super(name, configFile);
        outdoorTemperature = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_outdoor_temperature"));
        outdoorHumidity = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "initial_outdoor_humidity"));
        degreeOfOpeningOfWindowMin = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "degree_of_opening_of_window_min"));
        degreeOfOpeningOfWindowMax = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "degree_of_opening_of_window_max"));
        //degreeOfOpeningOfWindow = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "degree_of_opening_of_window"));
        reset();    //degreeOfOpeningOfWindow setting
        experimentOption = Integer.parseInt(FileManager.getValueFromConfigDictionary(config, "environment_model_experiment_option"));

        String modelPath = FileManager.getValueFromConfigDictionary(super.config, "model_path");
        String temperatureModelFile = FileManager.getValueFromConfigDictionary(super.config, "temperature_model");
        String humidityModelFile = FileManager.getValueFromConfigDictionary(super.config, "humidity_model");

        //todo window opening model
        temperatureModel = new EnvironmentModel(modelPath + temperatureModelFile);
        humidityModel = new EnvironmentModel(modelPath + humidityModelFile);
    }

    public String act(int tick, ArrayList<Double> environment) {
        String ret = "window:"+ Math.round(degreeOfOpeningOfWindow*100)/100.0;
        ret += " ";
        ret += super.name + ":";

        Double newTemperature = 0.;
        Double newHumidity = 0.;

        if(experimentOption == 0){ //environment model
            newTemperature = temperatureModel.getEnvironmentalValue(tick);
            newHumidity = humidityModel.getEnvironmentalValue(tick);
        }
        else if(experimentOption == 1){    //random environment
            newTemperature = temperatureModel.getRandomEnvironmentalValue(tick);
            newHumidity = humidityModel.getRandomEnvironmentalValue(tick);
        }
        else if(experimentOption == 2){    //min-max triangle
            newTemperature = temperatureModel.getMinMaxTriangleEnvironmentalValue(tick);
            newHumidity = humidityModel.getMinMaxTriangleEnvironmentalValue(tick);
        }
        else if(experimentOption == 3){    //discrete environment model
            newTemperature = temperatureModel.getDiscreteEnvironmentalValue(tick);
            newHumidity = humidityModel.getDiscreteEnvironmentalValue(tick);
        }
        else if(experimentOption == 4){    //historical data
            newTemperature = temperatureModel.getHistoricalEnvironmentValue(tick);
            newHumidity = humidityModel.getHistoricalEnvironmentValue(tick);
        }
        else if(experimentOption == 5){    //trend data
            newTemperature = temperatureModel.getTrendEnvironmentalValue(tick);
            newHumidity = humidityModel.getTrendEnvironmentalValue(tick);
        }
        else{
            System.out.println("wrong environment model option");
        }


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

    public void reset(){
        Random random = new Random();
        degreeOfOpeningOfWindow = (random.nextDouble() * (degreeOfOpeningOfWindowMax - degreeOfOpeningOfWindowMin)) + degreeOfOpeningOfWindowMin;
    }
}
