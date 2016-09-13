package kr.ac.kaist.se.simulator.method;

import java.math.BigDecimal;
import java.math.MathContext;

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
//    private double alpha; // 0.01
    private BigDecimal alpha; // 0.01
    private BigDecimal beta; // 0.01
    private BigDecimal delta; // 0.005
    private BigDecimal theta; // (0,1)

    private BigDecimal p0; // theta + delta
    private BigDecimal p1; // theta - delta

    private BigDecimal p0m;
    private BigDecimal p1m;

    private boolean h0decision;

    private int numSamples; // Total number of samples to calculate the decision
    private int dm; // Total number of samples that satisfies the condition

    public SPRTMethod(double alpha, double beta, double delta){
        this.alpha = new BigDecimal(String.valueOf(alpha));
        this.beta = new BigDecimal(String.valueOf(beta));
        this.delta = new BigDecimal(String.valueOf(delta));
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

        BigDecimal pRatioA = new BigDecimal(String.valueOf(1));
        pRatioA = pRatioA.subtract(this.beta);
        pRatioA = pRatioA.divide(this.alpha, MathContext.DECIMAL128);

//        double pRatioA = (1-this.beta) / this.alpha;

        BigDecimal pRatioB = this.beta.divide(new BigDecimal(String.valueOf(1)).subtract(this.alpha), MathContext.DECIMAL128);
//        double pRatioB = this.beta / (1 - this.alpha);

        if( this.p1m.divide(this.p0m, MathContext.DECIMAL128).compareTo(pRatioB) <= 0){
            h0decision = true;
            return true;
        }
        else if( this.p1m.divide(this.p0m, MathContext.DECIMAL128).compareTo(pRatioA) >= 0){
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
        this.p1 = this.theta.subtract(this.delta);
        this.p0 = this.theta.add(this.delta);
    }

    public void updateResult(int res){
        if( !(res == 1 || res == 0) )
            return;
//        if(this.numSamples > 1000000){ // Large number of samples, re-sampling
//            System.out.println("\n[WARN] Num of Sample has been breached 1,000,000, Re-sampled");
//            this.numSamples = 0;
//            this.dm = 0;
//        }else if(this.numSamples == 100000){
//            System.out.println("\n[WARN] Num of Sample has been breached 100,000");
//        }else if(this.numSamples == 300000){
//            System.out.println("\n[WARN] Num of Sample has been breached 300,000");
//        }else if(this.numSamples == 500000){
//            System.out.println("\n[WARN] Num of Sample has been breached 500,000");
//        }else if(this.numSamples == 700000){
//            System.out.println("\n[WARN] Num of Sample has been breached 700,000");
//        }
        this.numSamples++;
        if(res == 1)
            this.dm++;
        if(numSamples > 1){
            BigDecimal p1m_before = new BigDecimal(String.valueOf(1));
            p1m_before = p1m_before.subtract(this.p1);
            p1m_before = p1m_before.pow(this.numSamples-this.dm);
            this.p1m = this.p1.pow(this.dm);
            this.p1m = this.p1m.multiply(p1m_before);
//            this.p1m = Math.pow(this.p1,this.dm) * Math.pow((1 - this.p1), (this.numSamples - this.dm));

            BigDecimal p0m_before = new BigDecimal(String.valueOf(1));
            p0m_before = p0m_before.subtract(this.p0);
            p0m_before = p0m_before.pow(this.numSamples - this.dm);

            this.p0m = this.p0.pow(this.dm);
            this.p0m = this.p0m.multiply(p0m_before);

//            this.p0m = Math.pow(this.p0,this.dm) * Math.pow((1 - this.p0), (this.numSamples - this.dm));
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
