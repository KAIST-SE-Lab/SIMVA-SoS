package new_simvasos.property;

public class Property {

  String name;
  String specification;
  String type;

  /**
   * Instantiates a new Property.
   *
   * @param name          the name
   * @param specification the specification
   * @param propertyType  the property type
   */
  public Property(String name, String specification, String propertyType) { //constructor
    this.name = name;
    this.specification = specification;
    this.type = propertyType;
  }

  // this method is for inherited classes' getValue call like MCI Property
  public double getValue() { return  -1; }
  public double getThresholdPatient() {return -1;}
}
