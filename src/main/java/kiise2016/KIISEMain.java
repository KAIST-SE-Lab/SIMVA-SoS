package kiise2016;

import com.opencsv.CSVWriter;
import kr.ac.kaist.se.mc.BLTLChecker;
import kr.ac.kaist.se.simulator.Environment;
import kr.ac.kaist.se.simulator.SIMResult;
import kr.ac.kaist.se.simulator.Simulator;
import kr.ac.kaist.se.simulator.method.SPRTMethod;

import java.io.*;
import java.util.ArrayList;


public class KIISEMain {
    public static void main(String[] args) throws IOException{
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

        int[] boundArr = {140, 145, 150, 155, 160, 165, 170};
        for(int bound: boundArr){

            String outputName = "SIM_" + bound + ".csv";
            CSVWriter cw = new CSVWriter(new OutputStreamWriter(new FileOutputStream(outputName), "UTF-8"), ',', '"');

            ArrayList<SMCResult> resList = new ArrayList<SMCResult>();

            System.out.println("----------------------------------------------------");
            System.out.println("SoS-level benefit is greater than "+bound + ".");
            BLTLChecker checker = new BLTLChecker(5000, bound, BLTLChecker.comparisonType.GREATER_THAN_AND_EQUAL_TO);
            SPRTMethod sprt = new SPRTMethod(0.01, 0.01, 0.005);

            for(int i=0; i<100; i++){
                double theta = 0.01 * i; // theta
                long start = System.currentTimeMillis();
                sprt.setExpression(theta);

                while(!sprt.checkStopCondition())
                {
                    sim.execute();
                    SIMResult res = sim.getResult();
                    int checkResult = checker.evaluateSample(res);
                    sprt.updateResult(checkResult);
                }


                boolean h0 = sprt.getResult(); // Result
                int numSamples = sprt.getNumSamples();
//                System.out.print("SMC decides that your hypothesis is ");
//                if(h0)
//                {
//                    System.out.println("accepted at " + theta + " / number of samples: " + numSamples);
//                }
//                else
//                {
//                    System.out.println("not accepted at " + theta + " / number of samples: " + numSamples);
//                }

                long exec_time = System.currentTimeMillis() - start; //exec time
                int minTick = checker.getMinTick();
                int maxTick = checker.getMaxTick();
                sprt.reset();
                resList.add(new SMCResult(theta, numSamples, exec_time, minTick, maxTick, h0));
                System.out.print(".");
            }
            System.out.println();
            System.out.print("w");
            for(SMCResult r : resList){
                System.out.print(".");
                cw.writeNext(r.getArr());
            }
            cw.close();
            resList.clear();
            System.out.println();
        }
        System.out.println("Finished");

    }
}
