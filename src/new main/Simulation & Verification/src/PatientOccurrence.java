import java.util.ArrayList;
public class PatientOccurrence extends Action {

  ArrayList environment;
  public PatientOccurrence(String name, ArrayList environment){  // Constructor
    super(name);
    this.environment = environment;

  }

  public String behavior(){

    return "todo";
  }
}
