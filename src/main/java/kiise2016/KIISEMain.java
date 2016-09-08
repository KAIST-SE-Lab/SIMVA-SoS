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

        int[] boundArr = {100, 110, 120, 130, 140, 150, 160, 170, 180, 190, 200};
        for(int bound: boundArr){
            System.out.println("----------------------------------------------------");
            System.out.println("SoS-level benefit is greater than "+bound + ".");
            BLTLChecker checker = new BLTLChecker(10000, bound, BLTLChecker.comparisonType.GREATER_THAN_AND_EQUAL_TO);
            SPRTMethod sprt = new SPRTMethod(0.01, 0.01, 0.005);

            double[] thetaArr = {0.8, 0.85, 0.9, 0.95, 0.99};
            Instant start = Instant.now();
            for(double theta: thetaArr){
                sprt.setExpression(theta);

                while(!sprt.checkStopCondition())
                {
                    sim.execute();
                    SIMResult res = sim.getResult();
                    int checkResult = checker.evaluateSample(res);
                    sprt.updateResult(checkResult);
                }


                boolean h0 = sprt.getResult();
                System.out.print("SMC decides that your hypothesis is ");
                if(h0)
                {
                    System.out.println("accepted at " + theta + " / number of samples: " + sprt.getNumSamples());
                }
                else
                {
                    System.out.println("not accepted at " + theta + " / number of samples: " + sprt.getNumSamples());
                }

                sprt.reset();


            }
            Instant end = Instant.now();
            System.out.println("TOTAL EXECUTION TIME : " + Duration.between(start, end));
        }


    }
}
