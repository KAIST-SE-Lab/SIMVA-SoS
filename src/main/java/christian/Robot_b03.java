package christian;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.DebugProperty;
import kr.ac.kaist.se.simulator.method.DummyAction;

import java.util.Random;

/**
 * Robot.java
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

public class Robot_b03 extends Robot{

    private int xpos;
    private int ypos;
    private boolean token; // False -> drop, True -> no drop
    private Movement move;
    private Random ranNumGenerator;

    public Robot_b03(int ypos, Movement move){
        super(ypos, move);

        this.xpos = 10;
        this.ypos = ypos;
        this.token = false;
        this.move = move;
        this.ranNumGenerator = new Random();
        this.setStatus(Status.IDLE);
    }

    @Override
    public void normalAction(int elapsedTime) {
        if(xpos == 10) {
            int ranNum = this.ranNumGenerator.nextInt(100);
            if (ranNum == 0) {
                token = false; // loose its token;
            }
        }

        if(token) {
            xpos++;
            this.setStatus(Status.IDLE);
        }
        else
            this.setStatus(Status.END);
        if(this.xpos > 20){
            this.updateCostBenefit(0, 0, 1); // SoS-benefit is complete the task
            this.setStatus(Status.END);
        }
    }

    @Override
    public BaseAction immediateAction() {
        if(this.xpos == 10)
        {
            token = true; // token 줍기
            Movement do_movement = this.move.clone();
//            do_movement.setStatus();
            this.setStatus(Status.OPERATING);
            do_movement.startHandle();
            do_movement.addPerformer(this);
            return do_movement;
        }else if(token){
            Movement do_movement = this.move.clone();
            this.setStatus(Status.OPERATING);
            do_movement.startHandle();
            do_movement.addPerformer(this);
            return do_movement;
        }
        return null;
    }

    public BaseAction step(){
        if(this.getRemainBudget() == 0){
            return null; // voidAction, nothing happen
        }else{
            if(this.getStatus() == Status.IDLE){ // Select an action
                BaseAction a = new DummyAction("Immediate action", 0, 0);
                a.addPerformer(this);
                a.setActionType(BaseAction.TYPE.IMMEDIATE);
                return a;
            }else if(this.getStatus() == Status.OPERATING){ // Operation step
                return this.move;
            }
        }
        return null;
    }

    @Override
    public BaseConstituent clone() {
        return null;
    }

    @Override
    public BaseAction getCurrentAction() {
        return move;
    }

    @Override
    public void reset(){
        this.xpos = 10;
        this.token = false;
        super.reset();
    }

    @Override
    public DebugProperty getDebugProperty(){
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
