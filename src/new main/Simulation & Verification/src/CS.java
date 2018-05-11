public class CS {

  String name;
  public CS(String name) { // constructor
    this.name = name;
  }

  public String act() {

    return "todo";
  }

  // this method for inherited classes' reset call from SoS.java
  // for details of reset implementation, go to FireFighter.java, which is the
  // child class of  CS
  public void reset() {  }
}
