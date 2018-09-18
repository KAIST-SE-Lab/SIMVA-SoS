package new_simvasos.scenario;

public class Action {

  String name;
  public Action(String name) {  // Constructor
    this.name = name;
  }

  // Change "do" function in the prototype into "bahave"
  // Because there is already default "do" function in Java
  public String behave() {
    return "do action: " + this.name;
  }
  
}
