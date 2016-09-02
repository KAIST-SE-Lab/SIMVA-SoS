package kr.ac.kaist.se.simulator.method;

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

    private double alpha;
    private double beta;
    private double delta;

    private double p0;
    private double p1;

    private boolean h0decision;

    private int numSamples; // Total number of samples to calculate the decision

    public SPRTMethod(double alpha, double beta, double delta){
        this.alpha = alpha;
        this.beta = beta;
        this.delta = delta;
        this.numSamples = 0;
    }

    public boolean checkStopCondition(){
        return false;
    }

    private double getRatio(){
        if(this.numSamples < 2)
            return 0.0;
        else
            return 0.0;
    }
}
