package new_simvasos.verifier;

import javafx.util.Pair;
import new_simvasos.log.Log;
import new_simvasos.property.Property;
import new_simvasos.property.PropertyChecker;
import new_simvasos.simulation.Simulation;
import new_simvasos.simulator.Simulator;

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
  
  /*private void verifyExistedLogs(ArrayList<SimulationLog> simuLogs, Property verificationProperty) {
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
  */


  public void verifyWithSimulator(Simulator simulator, Property verificationProperty, int maxRepeat) {
    int maxNumSamples = maxRepeat;
    
    boolean totalRet = true;
    boolean ret = true;
    double probability = 0;
    double theta;
    int numSamples;
    int numTrue;
    
    for (int i = 1; i <= 100; i++) {
      theta = i * 0.01;
      numSamples = 0;
      numTrue = 0;
      
      while(this.isSampleNeeded(numSamples, numTrue, theta)) {
        if (!(numSamples < maxNumSamples)) {
          System.out.println("Over maximum repeat: " + maxNumSamples);
          break;
        }
        
        Log log = simulator.run();
        
        if(this.propertychecker.check(log, verificationProperty)) {
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
        if (i == 100)
          probability = 1.0;
      }
    }
    
    System.out.println("Probability: about " + probability * 100 + "%");
  }

    // Return <pair for drawing graph<number of samples, true/false>,
    // verification result on each theta>
    public Pair<Pair<Integer,Boolean>, String> verifyWithSimulationGUI(Simulation simulation, Property verificationProperty, int maxRepeat, double theta) {
        int maxNumSamples = maxRepeat;
        boolean ret = true;
        int numSamples;
        int numTrue;

        numSamples = 0;
        numTrue = 0;

        while(this.isSampleNeeded(numSamples, numTrue, theta)) {
            if (!(numSamples < maxNumSamples)) {
                System.out.println("Over maximum repeat: " + maxNumSamples);
                break;
            }

            Log log = simulation.runSimulation();
  
          // ExistenceChecker, AbsenceChecker, UniversalityChecker
          if(this.propertychecker.check(log, verificationProperty)) {
            
            // SteadyStateProbability Checker
            //if(this.propertychecker.check(log, verificationProperty, 0.5, 6000)) {
    
            // TransientStateProbabilityChecker
            // if(this.propertychecker.check(log, verificationProperty, 0.90, 6000, 5900)) {
    
            // MinimumDurationChecker
            // if(this.propertychecker.check(log, verificationProperty, 6000, 600) {
                numTrue += 1;
            }
            numSamples += 1;
        }

        ret = this.isSatisfied(numSamples, numTrue, theta);
        String verificationResult = "theta: " + Double.parseDouble(String.format("%.2f",theta)) +
                " numSamples: " + numSamples + " numTrue: " + numTrue + " result: " + ret;


        return new Pair(new Pair(numSamples, ret), verificationResult);
    }

    public void verifyWithSimulation(Simulation simulation, Property verificationProperty, int maxRepeat) {
    int maxNumSamples = maxRepeat;

    boolean totalRet = true;
    boolean ret = true;
    double probability = 0;
    double theta;
    int numSamples;
    int numTrue;

    for (int i = 1; i <= 100; i++) {
      theta = i * 0.01;
      numSamples = 0;
      numTrue = 0;

      while(this.isSampleNeeded(numSamples, numTrue, theta)) {
        if (!(numSamples < maxNumSamples)) {
          System.out.println("Over maximum repeat: " + maxNumSamples);
          break;
        }

        Log log = simulation.runSimulation();

        // ExistenceChecker, AbsenceChecker, UniversalityChecker
        if(this.propertychecker.check(log, verificationProperty)) {
        // SteadyStateProbability Checker
        //if(this.propertychecker.check(log, verificationProperty, 0.5, 6000)) {
        
        // TransientStateProbabilityChecker
          // if(this.propertychecker.check(log, verificationProperty, 0.90, 6000, 5900)) {
          
        // MinimumDurationChecker
        // if(this.propertychecker.check(log, verificationProperty, 6000, 600) {
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
        if (i == 100)
          probability = 1.0;
      }
    }

    System.out.println("Probability: about " + probability * 100 + "%");
  }
  /*
  // return number of samples
  public int verifyWithSimulator(Simulator simulator, Property verificationProperty, int maxRepeat, double theta) {
    int maxNumSamples = maxRepeat;

    boolean totalRet = true;
    boolean ret = true;
    double probability = 0;

    int numSamples=0;
    int numTrue=0;

    while(this.isSampleNeeded(numSamples, numTrue, theta)) {
      if (!(numSamples < maxNumSamples)) {
        System.out.println("Over maximum repeat: " + maxNumSamples);
        break;
      }

      Log log = simulator.run();

      if(this.propertychecker.check(log, verificationProperty)) {
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


    System.out.println("Probability: about " + probability * 100 + "%");
    return numSamples;
  }


  // return boolean (ret)
*/

  public Pair<Integer, Boolean> verifyWithSimulator(Simulator simulator, Property verificationProperty, int maxRepeat, double theta) {
    int maxNumSamples = maxRepeat;

    boolean totalRet = true;
    boolean ret = true;
    double probability = 0;

    int numSamples=0;
    int numTrue=0;

      while(this.isSampleNeeded(numSamples, numTrue, theta)) {
        if (!(numSamples < maxNumSamples)) {
          System.out.println("Over maximum repeat: " + maxNumSamples);
          break;
        }

        Log log = simulator.run();

        if(this.propertychecker.check(log, verificationProperty)) {
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


    System.out.println("Probability: about " + probability * 100 + "%");
    return new Pair(numSamples, ret);

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
