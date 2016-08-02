package main;

import simulator.Action;
import simulator.Constituent;
import simulator.Simulator;
import simulator.SoSManager;

/**
 * Simulator for System of Systems
 * Created by Junho on 2016-08-01.
 */
public class Main {
    public static void main(String []args){
        Constituent cs1 = new Constituent("CS1");
        Constituent cs2 = new Constituent("CS2");
        cs1.addAction(new Action("Action1", 1, 2, 1));
        cs1.addAction(new Action("Action2", 2, 4, 2));
        cs2.addAction(new Action("Action1", 1, 2, 1));
        SoSManager manager = new SoSManager();
        Constituent[] CSs = {cs1, cs2};

        Simulator sim = new Simulator(CSs, manager);
        sim.execute();
    }
}
