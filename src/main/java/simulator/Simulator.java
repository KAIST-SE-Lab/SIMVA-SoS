package simulator;

/**
 * Created by Junho on 2016-08-01.
 */
public class Simulator {

    public void execute(){
        this.procedure();
    }

    /*
    Simulation Procedure function from "Simulation and SMC of Logic-Based MAS Models"
     */
    private void procedure(){
        boolean verdict = false;
        boolean endCondition = false;
        while(!endCondition){
            System.out.println("Hello Simulator :)");
            endCondition = true;
        }
    }
}