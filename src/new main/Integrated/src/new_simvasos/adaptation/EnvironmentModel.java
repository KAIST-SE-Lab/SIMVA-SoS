package new_simvasos.adaptation;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class EnvironmentModel {
    private ArrayList<Double> trend;
    private int seasonalDuration;
    private Double seasonalMin;
    private Double seasonalMax;
    private Double remainderAverage;
    private Double remainderStandardDeviation;
    private Double globalMin;
    private Double globalMax;

    public EnvironmentModel(String configFile){
        ArrayList<Pair<String, String>> config = FileManager.readConfiguration(configFile);

        trend = new ArrayList<Double>();
        String trend_string = FileManager.getValueFromConfigDictionary(config, "trend");
        trend_string = trend_string.replace("[", "");
        trend_string = trend_string.replace("]", "");
        String[] trend_array = trend_string.split(",");
        for(String trend_value_string: trend_array){
            trend.add(Double.parseDouble(trend_value_string));
        }
        seasonalDuration = Integer.parseInt(FileManager.getValueFromConfigDictionary(config, "seasonal_duration"));
        seasonalMin = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "seasonal_min"));
        seasonalMax = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "seasonal_max"));
        remainderAverage = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "remainder_average"));
        remainderStandardDeviation = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "remainder_standard_deviation"));
        globalMin = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "global_min"));
        globalMax = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "global_max"));
    }

    public Double getEnvironmentalValue(int tick){
        int globalTick = tick % trend.size();

        Double trendValue = trend.get(globalTick);

        int seasonalTick = globalTick % seasonalDuration;   //우선은 위로 선 삼각형부터
        Double seasonalValue;
        if(seasonalTick < seasonalDuration/2){
            seasonalValue = seasonalMin + ((seasonalMax - seasonalMin) * (seasonalTick / (seasonalDuration/2)));
        }
        else{
            seasonalValue = seasonalMax - ((seasonalMax - seasonalMin) * ((seasonalTick-(seasonalDuration/2)) / (seasonalDuration/2)));
        }

        Random random = new Random();
        Double remainderValue = random.nextGaussian() * remainderStandardDeviation + remainderAverage;

        Double value = trendValue + seasonalValue + remainderValue;

        if(value > globalMax){
            return globalMax;
        }
        else if(value < globalMin){
            return globalMin;
        }
        else{
            return value;
        }
    }
}
