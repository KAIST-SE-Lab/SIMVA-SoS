package main;

import simulator.*;

/**
 * Simulator for System of Systems
 * Created by Junho on 2016-08-01.
 */
public class Main {
    public static void main(String []args){
        Constituent cs1 = new Constituent("CS1");
        Constituent cs2 = new Constituent("CS2");
        Action a1 = new Action("Action1", 2, 1);
        Action a2 = new Action("Action2", 4, 2);
        cs1.addCapability(a1, 1);
        cs1.addCapability(a2, 2);
        cs2.addCapability(a1, 1);

        SoSManager manager = new SoSManager();
        Constituent[] CSs = {cs1, cs2};
        Environment env = new Environment(CSs);

        Simulator sim = new Simulator(CSs, manager, env);
        sim.execute();
    }
}
