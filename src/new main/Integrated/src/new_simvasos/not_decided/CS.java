package new_simvasos.not_decided;

import java.util.ArrayList;

/**
 * A constituent system
 */
public abstract class CS {

  String name;

  /**
   * Instantiates a new CS.
   *
   * @param name the name
   */
  public CS(String name) { // constructor
    this.name = name;
  }
/*
  public String act() {
    String ret = "CS Operated: ";
    
    return ret + this.name;
  }*/

  public String act(int tick, ArrayList<Integer> environment) {
    String ret = "CS Operated: ";

    return ret + this.name;
  }

  // this method is for inherited classes' reset call in SoS.java
  // for details of reset implementation, go to FireFighter.java, which is the
  // child class of  CS
  public void reset() {  }

  // this method is for inherited classes' getRescued call in simulationLog.java
  public int getRescued() { return -1;}
}
