package main;

import kr.ac.kaist.se.simulator.Environment;
import kr.ac.kaist.se.simulator.Simulator;
import simulator.*;

/**
 * Simulator for System of Systems
 * Created by Junho on 2016-08-01.
 */
public class Main {
    public static void main(String []args){

        Constituent cs1 = new Constituent("CS1", 100);
        Constituent cs2 = new Constituent("CS2", 100);
        Constituent cs3 = new Constituent("CS3", 100);
        Constituent cs4 = new Constituent("CS4", 100);
        Constituent cs5 = new Constituent("CS5", 100);

        Action a1 = new Action("Action1", 2, 1);
        a1.setActionType(Action.TYPE.NORMAL);
        Action a2 = new Action("Action2", 4, 2);
        a2.setActionType(Action.TYPE.NORMAL);
        Action a3 = new Action("Action3", 3, 3);
        a3.setActionType(Action.TYPE.NORMAL);
        Action a4 = new Action("Action4", 1, 2);
        a4.setActionType(Action.TYPE.NORMAL);
        Action a5 = new Action("Action5", 3, 2);
        a5.setActionType(Action.TYPE.NORMAL);

        cs1.addCapability(a1, 1);
        cs1.addCapability(a2, 2);
        cs1.addCapability(a3, 3);
        cs1.addCapability(a4, 2);
        cs1.addCapability(a5, 3);

        cs2.addCapability(a1, 1);
        cs2.addCapability(a3, 3);
        cs2.addCapability(a4, 2);
        cs2.addCapability(a5, 3);

        cs3.addCapability(a2, 1);
        cs3.addCapability(a3, 3);
        cs3.addCapability(a4, 1);
        cs3.addCapability(a5, 2);

        cs4.addCapability(a1, 2);
        cs4.addCapability(a2, 3);
        cs4.addCapability(a3, 1);
        cs4.addCapability(a4, 3);

        cs5.addCapability(a2, 2);
        cs5.addCapability(a3, 3);
        cs5.addCapability(a4, 2);


        Constituent[] CSs = {cs1, cs2, cs3, cs4, cs5};
        Action[] actions = {a1, a2, a3, a4, a5};
        SoSManager manager = new SoSManager("SoS Manager", CSs, actions);
        Environment env = new Environment(CSs, actions);

        Simulator sim = new Simulator(CSs, manager, env);
        sim.execute(100);
    }
}
