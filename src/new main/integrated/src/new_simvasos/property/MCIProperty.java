package new_simvasos.property;

public class MCIProperty extends Property {

  double rescueRate;
  public MCIProperty(String name, String specification, String propertyType, double etc) {
    super(name, specification, propertyType);
    this.rescueRate = etc;
  }

  public double getValue() {
    return this.rescueRate;
  }
}
