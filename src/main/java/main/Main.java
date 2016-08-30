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

        Action a1 = new Action("Action1", 2, 1, 2);
        a1.setActionType(Action.TYPE.NORMAL);
        Action a2 = new Action("Action2", 4, 2, 3);
        a2.setActionType(Action.TYPE.NORMAL);
        Action a3 = new Action("Action3", 3, 5, 4);
        a3.setActionType(Action.TYPE.NORMAL);

        cs1.addCapability(a1, 1);
        cs1.addCapability(a2, 2);
        cs1.addCapability(a3, 3);
        cs2.addCapability(a1, 1);
        cs2.addCapability(a3, 4);

        Constituent[] CSs = {cs1, cs2};
        Action[] actions = {a1, a2, a3};
        SoSManager manager = new SoSManager("SoS Manager", CSs, actions);
        Environment env = new Environment(CSs, actions);

        Simulator sim = new Simulator(CSs, manager, env);
        sim.execute();
    }
}
