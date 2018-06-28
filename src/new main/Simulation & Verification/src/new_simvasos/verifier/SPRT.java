package new_simvasos.verifier;

import new_simvasos.log.Log;
import new_simvasos.log.SimulationLog;
import new_simvasos.property.Property;
import new_simvasos.property.PropertyChecker;
import new_simvasos.simulator.Simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SPRT extends Verifier {
  double alpha;
  double beta;
  double delta;
  int minimumSample;
  
  public SPRT(PropertyChecker checker) {
    super(checker);
    this.alpha = 0.05;
    this.beta = 0.05;
    this.delta = 0.01;
    this.minimumSample = 2;
  }
  
  private void verifyExistedLogs(ArrayList<SimulationLog> simuLogs, Property verificationProperty) {
    boolean ret;
    boolean totalRet = true;
    double probability = 0;
    double theta = 0;
    int logLen = simuLogs.size();
    
    int numSamples;
    int numTrue;
    
    for(int i = 1; i < 100; i++) {
      theta = i * 0.01;
      
      numSamples = 0;
      numTrue = 0;

      //randomSeed = System.nanoTime();
      Collections.shuffle(simuLogs, new Random());
      
      while(isSampleNeeded(numSamples, numTrue, theta)) {

        if (numSamples >=logLen) {
          System.out.println("Lack of simulation  Logs: " + logLen);
          break;
        }
        
        if (this.propertychecker.check(simuLogs.get(numSamples), verificationProperty)) {
          numTrue += 1;
        }
        numSamples += 1;
      }
  
      ret = this.isSatisfied(numSamples, numTrue, theta);
      System.out.println("theta: " + Double.parseDouble(String.format("%.2f",theta)) +
          " numSamples: " + numSamples + " numTrue: " + numTrue + " result: " + ret);
      
      if(totalRet) {
        if (!ret) {
          totalRet = false;
          probability = theta;
        }
      }
    }
    System.out.println("Probability: about " + probability * 100 + "%");
  }
  
  public void verifyWithSimulator(Simulator simulator, Property verificationProperty, int maxRepeat) {
    int maxNumSamples = maxRepeat;
    
    boolean totalRet = true;
    boolean ret = true;
    double probability = 0;
    double theta;
    int numSamples;
    int numTrue;
    
    for (int i = 1; i < 100; i++) {
      theta = i * 0.01;
      numSamples = 0;
      numTrue = 0;
      
      while(this.isSampleNeeded(numSamples, numTrue, theta)) {
        if (!(numSamples < maxNumSamples)) {
          System.out.println("Over maximum repeat: " + maxNumSamples);
          break;
        }
        
        Log log = simulator.run();
        
        if(this.propertychecker.check(log.getSimuLog(), verificationProperty)) {
          numTrue += 1;
        }
        numSamples += 1;
      }

      ret = this.isSatisfied(numSamples, numTrue, theta);
      System.out.println("theta: " + Double.parseDouble(String.format("%.2f",theta)) +
          " numSamples: " + numSamples + " numTrue: " + numTrue + " result: " + ret);
      
      if(totalRet) {
        if (!ret) {
          totalRet = false;
          probability = theta;
        }
      }
    }
    
    System.out.println("Probability: about " + probability * 100 + "%");
  }
  
  
  private boolean isSampleNeeded(int numSample, int numTrue, double theta) {
    if (numSample < this.minimumSample) return true;
    
    double h0Threshold = this.beta / (1-this.alpha);
    double h1Threshold = (1-this.beta) / this.alpha;
    
    double v = this.getV(numSample, numTrue, theta);
    
    if (v <= h0Threshold) {
      return false;
    }
    else if (v >= h1Threshold) {
      return false;
    }
    else {
      return true;
    }
  }
  
  private boolean isSatisfied(int numSamples, int numTrue, double theta) {
    double h0Threshold = this.beta/(1-this.alpha);
    
    double v = this.getV(numSamples, numTrue, theta);
    
    if (v <= h0Threshold) {
      return true;
    } else  {
      return false;
    }
  }
  
  private double getV(int numSample, int numTrue, double theta) {
    double p0 = theta + this.delta;
    double p1 = theta - this.delta;
    
    int numFalse = numSample - numTrue;
    
    double p1m = Math.pow(p1, numTrue) * Math.pow((1-p1), numFalse);
    double p0m = Math.pow(p0, numTrue) * Math.pow((1-p0), numFalse);
    
    if (p0m == 0) {
      p1m = p1m + Double.MIN_VALUE;
      p0m = p0m + Double.MIN_VALUE;
    }
    
    return p1m / p0m;
  }
  
}
