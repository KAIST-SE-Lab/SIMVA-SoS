package new_simvasos.adaptation;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class EnvironmentModel {
    private ArrayList<Double> trend;
    private ArrayList<Double> seasonality;
    private ArrayList<Double> remainder;
    private ArrayList<Double> observation;
    private ArrayList<Double> remainderSeasonalMin;
    private ArrayList<Double> remainderSeasonalMax;

    private int seasonalDuration;
    private Double seasonalMin;
    private Double seasonalMax;
    private Double seasonalDiffMin;
    private Double seasonalDiffMax;
    private Double remainderAverage;
    private Double remainderStandardDeviation;
    private Double remainderDiffMin;
    private Double remainderDiffMax;
    private Double globalMin;
    private Double globalMax;
    private Double preRemainder; // remainder

    public EnvironmentModel(String configFile){
        ArrayList<Pair<String, String>> config = FileManager.readConfiguration(configFile);

        trend = stringToList(FileManager.getValueFromConfigDictionary(config, "trend"));
        seasonality = stringToList(FileManager.getValueFromConfigDictionary(config, "seasonality"));
        remainder = stringToList(FileManager.getValueFromConfigDictionary(config, "remainder"));
        remainderSeasonalMin = stringToList(FileManager.getValueFromConfigDictionary(config, "remainder_seasonal_min"));
        remainderSeasonalMax = stringToList(FileManager.getValueFromConfigDictionary(config, "remainder_seasonal_max"));

        observation = stringToList(FileManager.getValueFromConfigDictionary(config, "observation"));

        seasonalDuration = Integer.parseInt(FileManager.getValueFromConfigDictionary(config, "seasonal_duration"));
        seasonalMin = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "seasonal_min"));
        seasonalMax = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "seasonal_max"));
        seasonalDiffMin = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "seasonal_diff_min"));
        seasonalDiffMax = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "seasonal_diff_max"));
        remainderAverage = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "remainder_average"));
        remainderStandardDeviation = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "remainder_standard_deviation"));
        remainderDiffMin = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "remainder_diff_min"));
        remainderDiffMax = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "remainder_diff_max"));
        globalMin = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "global_min"));
        globalMax = Double.parseDouble(FileManager.getValueFromConfigDictionary(config, "global_max"));
    }

    private ArrayList<Double> stringToList(String dataListStr){
        ArrayList<Double> dataList = new ArrayList<Double>();
        dataListStr = dataListStr.replace("[", "");
        dataListStr = dataListStr.replace("]", "");
        String[] trend_array = dataListStr.split(",");
        for(String trend_value_string: trend_array){
            dataList.add(Double.parseDouble(trend_value_string));
        }
        return dataList;
    }

    public Double getEnvironmentalValue(int tick){
        Random random = new Random();
        int globalTick = tick % trend.size();

        Double trendValue = trend.get(globalTick);

        int seasonalTick = globalTick % seasonalDuration;   //우선은 위로 선 삼각형부터
        Double seasonalValue;
        /*if(seasonalTick < seasonalDuration/2){
            seasonalValue = seasonalMin + ((seasonalMax - seasonalMin) * ((double)seasonalTick / (double)(seasonalDuration/2)));
        }
        else{
            seasonalValue = seasonalMax - ((seasonalMax - seasonalMin) * ((double)(seasonalTick-(seasonalDuration/2)) / (double)(seasonalDuration/2)));
        }*/
        seasonalValue = seasonality.get(seasonalTick) + (random.nextDouble() * (seasonalDiffMax - seasonalDiffMin)) + seasonalDiffMin;
        //seasonalValue = seasonality.get(seasonalTick);

        /*Double remainderValue = random.nextGaussian() * remainderStandardDeviation + remainderAverage;
        //Double remainderValue = remainder.get(globalTick) + (random.nextDouble() * (remainderDiffMax - remainderDiffMin)) + remainderDiffMin;
        if(preRemainder == null){   //
            preRemainder = remainderValue;
        }
        else{
            if(globalTick % seasonalDuration == 0){
                preRemainder = remainderValue;
            }
            else{
                remainderValue = preRemainder;
            }
        }*/
        Double remainderValue = random.nextDouble() * (remainderSeasonalMax.get(seasonalTick) - remainderSeasonalMin.get(seasonalTick)) + remainderSeasonalMin.get(seasonalTick);

        Double value = trendValue + seasonalValue + remainderValue;
        //Double value = trendValue + seasonalValue;

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

    public Double getMinMaxTriangleEnvironmentalValue(int tick){
        int timeLength = trend.size();
        int globalTick = tick % timeLength;

        Double value;
        if(globalTick < timeLength / 2){
            value = globalMin + ((globalMax - globalMin) * ((double)globalTick / (double)(timeLength/2)));
        }
        else{
            value = globalMax - ((globalMax - globalMin) * ((double)(globalTick-(timeLength/2)) / (double)(timeLength/2)));
        }

        return value;
    }

    public Double getDiscreteEnvironmentalValue(int tick){
        int timeLength = trend.size();
        int globalTick = tick % timeLength;

        Double sectionSize = (globalMax - globalMin)/3.0;
        Double lowValue = globalMin + sectionSize * 0.5;
        Double midValue = lowValue + sectionSize;
        Double highValue = midValue + sectionSize;

        int numSeason = 5;
        int seasonLength = timeLength / numSeason;
        if(globalTick < seasonLength){
            return lowValue;
        }
        else if(globalTick < 2 * seasonLength){
            return midValue;
        }
        else if(globalTick < 3 * seasonLength){
            return highValue;
        }
        else if(globalTick < 4 * seasonLength){
            return midValue;
        }
        else{
            return lowValue;
        }
    }

    public Double getTrendEnvironmentalValue(int tick){
        int globalTick = tick % trend.size();

        return trend.get(globalTick);
    }

    public Double getRandomEnvironmentalValue(int tick){
        Random random = new Random();
        return (random.nextDouble() * (globalMax - globalMin)) + globalMin;
    }

    public Double getHistoricalEnvironmentValue(int tick){
        int globalTick = tick % observation.size();
        return observation.get(globalTick);
    }
}
