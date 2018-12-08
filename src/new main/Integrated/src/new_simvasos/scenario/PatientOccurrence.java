package new_simvasos.scenario;

import java.util.ArrayList;
import java.util.Random;

public class PatientOccurrence extends Action {

  ArrayList<Integer> environment;

  /**
   * Instantiates a new Patient occurrence.
   * PatientOccurence is an user-defined event.
   *
   * @param name        the name
   * @param environment the environment
   */
  public PatientOccurrence(String name, ArrayList environment){  // Constructor
    super(name);
    this.environment = environment;

  }
  
  // Change "do" function in the prototype into "bahave"
  // Because there is already default "do" function in Java
  public String behave(){
    Random random = new Random();
    
    int i = random.nextInt(this.environment.size());
    this.environment.set(i,this.environment.get(i) + 1);
    
    // for changing the environment(Arraylist of integerr) into String for return
    String ret = "";
    for (int j: environment) {
      ret += String.valueOf(j) + " ";
    }

    return super.behave() + "at" + String.valueOf(i) + " " + ret;
  }
}
