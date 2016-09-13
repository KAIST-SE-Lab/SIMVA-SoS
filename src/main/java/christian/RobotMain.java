package christian;

import kr.ac.kaist.se.simulator.Environment;
import kr.ac.kaist.se.simulator.Simulator;

/**
 * RobotMain.java
 * This class example is came from "Simulation and Statistical Model Checking of Logic-Based Multi-Agent System Models", Christian Kroib

 * Author: Junho Kim <jhkim@se.kaist.ac.kr>

 * The MIT License (MIT)

 * Copyright (c) 2016 Junho Kim

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: TBD
 */

public class RobotMain {
    public static void main(String[] args){

        Movement right_move = new Movement();

        Robot r1 = new Robot(0, right_move);
        Robot r2 = new Robot(1, right_move);
        Robot r3 = new Robot(2, right_move);

        r1.addCapability(right_move, 1);
        r2.addCapability(right_move, 1);
        r3.addCapability(right_move, 1);

        Robot[] robots = new Robot[] {r1, r2, r3};

        Movement[] moves = new Movement[] {right_move};

        Environment env = new Environment(robots, moves);
        Simulator sim = new Simulator(robots, null, env);
    }
}
