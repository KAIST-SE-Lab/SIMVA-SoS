package new_simvasos.property;

public class MCIProperty extends Property {

  double rescueRate;
  double thresholdPatient;
  public MCIProperty(String name, String specification, String propertyType, double etc) {
    super(name, specification, propertyType);
    this.rescueRate = etc;
  }

  public void setThresholdPatient(double thresholdPatient) {
    this.thresholdPatient = thresholdPatient;
  }
  
  public double getValue() {
    return this.rescueRate;
  }
  
  public double getThresholdPatient() {return this.thresholdPatient;}
}
