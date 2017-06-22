package kr.ac.kaist.se.simulator.method;

import java.math.BigDecimal;
import java.math.MathContext;

/**
 * SPRTMethod.java

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

public class SPRTMethod {
    // Sequential Probability Ratio Test Method for Statistical Model Checking

    // Default alpha: 0.1, 0.01, 0.001
    // Default beta: same as above
    // Default delta: 0.01, 0.03, 0.05 -> 99%, 97%, 95% we choose 99%

    private BigDecimal alpha; // 0.01
    private BigDecimal beta; // 0.01
    private BigDecimal delta; // 0.01
    private BigDecimal theta; // (0,1)

    private BigDecimal p0; // theta + delta
    private BigDecimal p1; // theta - delta

    private BigDecimal p0m;
    private BigDecimal p1m;

    private boolean h0decision;

    private int numSamples; // Total number of samples to calculate the decision
    private int dm; // Total number of samples that satisfies the condition

    private boolean condition; // True -> more than, False -> less than

    private BigDecimal pRatioA = null;
    private BigDecimal pRatioB = null;

    public SPRTMethod(double alpha, double beta, double delta){
        this.alpha = new BigDecimal(String.valueOf(alpha));
        this.beta = new BigDecimal(String.valueOf(beta));
        this.delta = new BigDecimal(String.valueOf(delta));
        this.numSamples = 0;
        this.dm = 0;
        this.condition = true;

        pRatioA = new BigDecimal(String.valueOf(1));
        pRatioA = pRatioA.subtract(this.beta);
        pRatioA = pRatioA.divide(this.alpha, MathContext.DECIMAL32);
        pRatioB = this.beta.divide(new BigDecimal(String.valueOf(1)).subtract(this.alpha), MathContext.DECIMAL32);
    }

    public void setBigCheck(){
        this.condition = true;
    }

    public void setLessCheck(){
        this.condition = false;
    }

    /**
     *
     * @return true - we can decide sa is done.
     */
    public boolean checkStopCondition(){
        if(this.numSamples < 2) // Minimum required samples
            return false;

        if( this.p1m.divide(this.p0m, MathContext.DECIMAL32).compareTo(pRatioB) <= 0){
            h0decision = true;
            return true;
        }
        else if( this.p1m.divide(this.p0m, MathContext.DECIMAL32).compareTo(pRatioA) >= 0){
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
        this.theta = new BigDecimal(String.valueOf(theta));
        if(condition)
        {
            this.p1 = this.theta.subtract(this.delta).add(new BigDecimal(Double.toString(Double.MIN_VALUE)));
            this.p0 = this.theta.add(this.delta).subtract(new BigDecimal(Double.toString(Double.MIN_VALUE)));
        }else{
            this.p0 = this.theta.subtract(this.delta).add(new BigDecimal(Double.toString(Double.MIN_VALUE)));
            this.p1 = this.theta.add(this.delta).subtract(new BigDecimal(Double.toString(Double.MIN_VALUE)));
        }

    }

    public void updateResult(int res){
        if( !(res == 1 || res == 0) )
            return;
        this.numSamples++;
        if(res == 1)
            this.dm++;
        if(numSamples > 1){
            BigDecimal p1m_before = new BigDecimal(String.valueOf(1));
            p1m_before = p1m_before.subtract(this.p1);
            p1m_before = p1m_before.pow(this.numSamples-this.dm, MathContext.DECIMAL32);
            this.p1m = this.p1.pow(this.dm, MathContext.DECIMAL32);
            this.p1m = this.p1m.multiply(p1m_before, MathContext.DECIMAL32);

            BigDecimal p0m_before = new BigDecimal(String.valueOf(1));
            p0m_before = p0m_before.subtract(this.p0);
            p0m_before = p0m_before.pow(this.numSamples - this.dm, MathContext.DECIMAL32);

            this.p0m = this.p0.pow(this.dm, MathContext.DECIMAL32);
            this.p0m = this.p0m.multiply(p0m_before, MathContext.DECIMAL32);
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
        this.theta = new BigDecimal(String.valueOf(0));
        this.dm = 0;
    }

    public int getNumSamples(){
        return this.numSamples;
    }

}
