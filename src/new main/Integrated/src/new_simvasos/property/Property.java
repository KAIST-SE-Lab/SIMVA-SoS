package new_simvasos.property;

public class Property {

  String name;
  String specification;
  String type;
  
  public Property(String name, String specification, String propertyType) { //constructor
    this.name = name;
    this.specification = specification;
    this.type = propertyType;
  }

  // this method is for inherited classes' getValue call like MCI Property
  public double getValue() { return  -1; }
  public double getThresholdPatient() {return -1;}
}
