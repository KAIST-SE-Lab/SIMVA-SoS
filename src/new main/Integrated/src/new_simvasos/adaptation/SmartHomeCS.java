package new_simvasos.adaptation;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Random;

public class SmartHomeCS extends CS{
    ArrayList<Pair<String, String>> config;
    Double prevEnvironment = null;
    int monitoringCount = 0;

    public SmartHomeCS(String name, String configFile){
        super(name);
        config = FileManager.readConfiguration(configFile);
    }

    public void increaseTemperature(ArrayList<Double> environment, Double degree){
        environment.set(0, environment.get(0) + degree);
    }

    public void increaseHumidity(ArrayList<Double> environment, Double degree) {
        environment.set(1, environment.get(1) + degree);
    }

    public Double uncertaintyUniformDistributionNoise(Double realEnv, Double minError, Double maxError){
        Random random = new Random();
        Double error = (random.nextDouble() * (maxError - minError)) + minError;
        return realEnv + error;
    }

    public Double uncertaintyMonitoringFailure(Double realEnv, Double failureProb){
        Random random = new Random();
        if(random.nextDouble() < failureProb){
            return null;
        }
        else{
            return realEnv;
        }
    }

    public Double uncertaintyMonitoringImprecision(Double realEnv, Double unit){
        int quotient = (int)(realEnv / unit);
        // todo: not perfect.
        return quotient * unit;
    }

    public Double uncertaintyMonitoringFrequency(Double realEnv, int frequency){
        if(monitoringCount == 0){
            prevEnvironment = realEnv;
            monitoringCount = (monitoringCount + 1) % frequency;
            return realEnv;
        }
        else{
            monitoringCount = (monitoringCount + 1) % frequency;
            return prevEnvironment;
        }
    }
}
