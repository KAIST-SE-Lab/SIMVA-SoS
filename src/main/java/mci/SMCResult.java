package mci;

/**
 * SMCResult.java
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
public class SMCResult {

    private double prob;
    private int numOfSamples;
    private long executionTime;
    private int minTick;
    private int maxTick;
    private boolean result;

    public SMCResult(double prob, int numOfSamples, long executionTime, int minTick, int maxTick, boolean result){
        this.prob = prob;
        this.numOfSamples = numOfSamples;
        this.executionTime = executionTime;
        this.minTick = minTick;
        this.maxTick = maxTick;
        this.result = result;
    }

    public String toString(){
        String retStr = "";
        retStr += String.format("%.2f", this.prob);
        retStr += "," + numOfSamples;
        retStr += String.format("%.3f", (this.executionTime/1000.0));
        retStr += "," + this.minTick + "," + this.maxTick;
        retStr += "," + this.result;
        return retStr;
    }

    public String[] getArr(){
        return new String[] {String.format("%.2f", this.prob), Integer.toString(numOfSamples),
                String.format("%.3f", (this.executionTime/1000.0)), Boolean.toString(this.result)};
    }
}
