package mci.checker;

import kr.ac.kaist.se.mc.CheckerInterface;
import kr.ac.kaist.se.simulator.SIMResult;

/**
 * Existence.java

 * Author: Junho Kim <jhim@se.kaist.ac.kr>

 * The MIT License (MIT)

 * Copyright (c) 2016 Junho Kim

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: TBD
 */

public class RobotChecker implements CheckerInterface{

    /*
     * BLTL Model Checker for SIMVASoS
     * 1. Getting a sample sequence from the simulator
     * 2. Check whether the sample sequence satisfies or not the condition (temporal logic)
     * 3. Return Bernoulli random variable value (0, not satisfied or 1, satisfied)
     */

    public enum comparisonType {LESS_THAN, GREATER_THAN, EQUAL_TO, LESS_THAN_AND_EQUAL_TO, GREATER_THAN_AND_EQUAL_TO}

    private int baseTick;
    private int baseSoSBenefit;
    private comparisonType type;
    private int minTick;
    private int maxTick;
    private boolean negation = false;

    /**
     * Initialize params of BLTL model Checker
     * @param baseTick baseline of the time tick, BLTL Checker will evaluate the sample sequence based on this tick
     * @param baseSoSBenefit baseline of SoS benefit, BLTL Checker will evaluate the sample sequence based on this SoS benefit
     */
    public void init(int baseTick, int baseSoSBenefit, comparisonType type){
        this.baseTick = baseTick;
        this.baseSoSBenefit = baseSoSBenefit;
        this.type = type;
        this.minTick = Integer.MAX_VALUE;
        this.maxTick = Integer.MIN_VALUE;
        this.negation = false;
    }

    @Override
    public void init(String[] params) {
        // params[0]: checker name
        // params[1]: probability
        init(Integer.parseInt(params[3]), Integer.parseInt(params[4]), comparisonType.EQUAL_TO);
    }

    /**
     * Return the name
     */
    public String getName() { return "Existence Checker (Robot)"; }

    /**
     * Return the description
     */
    public String getDescription() {
        // "SoS-level benefit is greater than and equal to " + this,baseSoSBenefit
        return "Globally, \"The total benefit is equal to " + this.baseSoSBenefit + "\" holds eventually by " + this.baseTick + " ticks";
    }

    /**
     * Evaluate the sample sequence based on the base tick and base SoS benefit
     * @param res Simulation result (sample sequence)
     * @return 1 - satisfied, 0 - not satisfied
     */
    public int evaluateSample(SIMResult res){
        int sampleTick = res.getNumTicks();
        int sampleBenefit = res.getSoSBenefit();
        if(sampleTick <= baseTick){
            switch(this.type){
                case LESS_THAN:
                    if(sampleBenefit < this.baseSoSBenefit)
                        return 0;
                    break;
                case GREATER_THAN:
                    if(sampleBenefit > this.baseSoSBenefit)
                        return 0;
                    break;
                case EQUAL_TO:
                    if(sampleBenefit == this.baseSoSBenefit)
                        return 0;
                    break;
                case LESS_THAN_AND_EQUAL_TO:
                    if(sampleBenefit <= this.baseSoSBenefit)
                        return 0;
                    break;
                case GREATER_THAN_AND_EQUAL_TO:
                    if(sampleBenefit >= this.baseSoSBenefit)
                        return 0;
                    break;
            }
            if(this.minTick >= res.getNumTicks())
                this.minTick = res.getNumTicks();
            else if(this.maxTick <= res.getNumTicks())
                this.maxTick = res.getNumTicks();
        }
        return 1;
    }

    public void setNegation(){
        this.negation = true;
    }

    public void setNotNegation(){
        this.negation = false;
    }

    public int getMinTick(){
        return this.minTick;
    }

    public int getMaxTick(){
        return this.maxTick;
    }
}
