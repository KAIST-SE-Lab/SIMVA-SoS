package kiise2016;


import kr.ac.kaist.se.mc.BLTLChecker;
import kr.ac.kaist.se.simulator.Environment;
import kr.ac.kaist.se.simulator.SIMResult;
import kr.ac.kaist.se.simulator.Simulator;
import kr.ac.kaist.se.simulator.method.SPRTMethod;

import java.time.Duration;
import java.time.Instant;

public class KIISEMain {
    public static void main(String[] args){
        Constituent cs1 = new Constituent("CS1", 100);
        Constituent cs2 = new Constituent("CS2", 100);
        Constituent cs3 = new Constituent("CS3", 100);

        Action a1 = new Action("Action1", 2, 1);
        a1.setActionType(Action.TYPE.NORMAL);
        Action a2 = new Action("Action2", 2, 2);
        a2.setActionType(Action.TYPE.NORMAL);
        Action a3 = new Action("Action3", 3, 3);
        a3.setActionType(Action.TYPE.NORMAL);

        cs1.addCapability(a1, 1);
        cs1.addCapability(a2, 2);

        cs2.addCapability(a2, 2);
        cs2.addCapability(a3, 3);

        cs3.addCapability(a1, 1);
        cs3.addCapability(a3, 3);

        Constituent[] CSs = {cs1, cs2, cs3};
        Action[] actions = {a1, a2, a3};
        SoS sos = new SoS("SoS Manager", CSs, actions);
        Environment env = new Environment(CSs, actions);

        Simulator sim = new Simulator(CSs, sos, env);
        BLTLChecker checker = new BLTLChecker(500, 450, BLTLChecker.comparisonType.GREATER_THAN_AND_EQUAL_TO);
        SPRTMethod sprt = new SPRTMethod(0.01, 0.01, 0.001);

        double[] thetaArr = {0.5, 0.55, 0.6, 0.65, 0.7, 0.75, 0.8, 0.85, 0.9, 0.95, 0.99};

        for(double theta: thetaArr){
            // Time measure
            Instant start = Instant.now();

            sprt.setExpression(theta);

            while(!sprt.checkStopCondition()){
                sim.execute();
                SIMResult res = sim.getResult();
                int checkResult = checker.evaluateSample(res); // 1: satisfy, 0: not-satisfy
                sprt.updateResult(checkResult);
            }


            boolean h0 = sprt.getResult();
            System.out.print("SMC decides that your hypothesis is ");
            if(h0)
                System.out.println("accepted at " + theta);
            else
                System.out.println("not accepted at " + theta);

            sprt.reset();


            Instant end = Instant.now();
            System.out.println(Duration.between(start, end));
        }
    }
}
