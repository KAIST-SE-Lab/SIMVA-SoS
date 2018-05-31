import javafx.util.Pair;

import java.util.ArrayList;

public class MCIPropertyChecker extends PropertyChecker {

  public MCIPropertyChecker() {

    super();
  }

  // In Simulation Log, this property function check the simulation log
  // (rescued people number, patient number)
  public boolean check(SimulationLog simuLog, Property verificationProperty) {

    Pair<ArrayList<Integer>, ArrayList<Integer>> tmp = simuLog.getPropertyLog();
    
    ArrayList<Integer> rescued = tmp.getKey();
    ArrayList<Integer> environment = tmp.getValue();
    
    double sumRescued = 0;
    double sumPatients = 0;
    
    for (int i : rescued ) {
      sumRescued += i;
    }
    
    for (int i : environment) {
      sumPatients += i;
    }
    
    sumPatients += sumRescued;
    //System.out.println(sumRescued + " , " + sumPatients + " ," + verificationProperty.getValue());
    if((sumRescued / sumPatients) > verificationProperty.getValue()) {
      //System.out.println("return true");
      return true;
    }
    else {
      //System.out.println("return false");
      return false;
    }
  }
}
