package new_simvasos.scenario;

public class Action {

  String name;

  /**
   * Instantiates a new Action.
   *
   * @param name the name
   */
  public Action(String name) {  // Constructor
    this.name = name;
  }

  /**
   * A method to occur an action.
   * This method should be inherited and re-defined by user.
   *
   * @return the string
   */
// Change "do" function in the prototype into "bahave"
  // Because there is already default "do" function in Java
  public String behave() {
    return "do action: " + this.name;
  }
}
