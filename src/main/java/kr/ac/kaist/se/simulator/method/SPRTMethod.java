package kr.ac.kaist.se.simulator.method;

import kr.ac.kaist.se.mc.BLTLChecker;
import kr.ac.kaist.se.simulator.SIMResult;

/**
 * SPRTMethod.java

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

public class SPRTMethod {
    // Sequential Probability Ratio Test Method for Statistical Model Checking

    // Default alpha: 0.1, 0.01, 0.001
    // Default beta: same as above
    // Default delta: 0.01, 0.03, 0.05 -> 99%, 97%, 95% we choose 99%
    private double alpha; // 0.01
    private double beta; // 0.01
    private double delta; // 0.005
    private double theta;

    private double p0; // theta + delta
    private double p1; // theta - delta

    private double p0m;
    private double p1m;

    private boolean h0decision;

    private int numSamples; // Total number of samples to calculate the decision
    private int dm; // Total number of samples that satisfies the condition


    public SPRTMethod(double alpha, double beta, double delta){
        this.alpha = alpha;
        this.beta = beta;
        this.delta = delta/2;
        this.numSamples = 0;
        this.dm = 0;
    }

    /**
     *
     * @return true - we can decide smc is done.
     */
    public boolean checkStopCondition(){
        if(this.numSamples < 2)
            return false;
        double pRatioA = (1-this.beta) / this.alpha;
        double pRatioB = this.beta / (1 - this.alpha);

        if( (this.p1m / this.p0m) <= pRatioB){
            h0decision = true;
            return true;
        }
        else if( (this.p1m / this.p0m) >= pRatioA){
            h0decision = false;
            return true;
        }else{
            return false;
        }
    }

    /**
     * Set the theta value from verification property
     * @param theta - probability that requires property
     */
    public void setExpression(double theta){
        this.theta = theta;
        this.p1 = this.theta - this.delta;
        this.p0 = this.theta + this.delta;
    }

    public void updateResult(int res){
        if( !(res == 1 || res == 0) )
            return;

        this.numSamples++;
        if(res == 1)
            this.dm++;
        if(numSamples > 1){
            this.p1m = Math.pow(this.p1,this.dm) * Math.pow((1 - this.p1), (this.numSamples - this.dm));
            this.p0m = Math.pow(this.p0,this.dm) * Math.pow((1 - this.p0), (this.numSamples - this.dm));
        }
    }

    /**
     *
     * @return decision result - true: accept h0, false: accept h1
     */
    public boolean getResult(){
        return this.h0decision;
    }

    public void reset(){
        this.numSamples = 0;
        this.theta = 0.0;
        this.dm = 0;
    }

}
