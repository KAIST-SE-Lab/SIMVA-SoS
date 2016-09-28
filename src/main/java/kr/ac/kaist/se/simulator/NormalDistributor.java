package kr.ac.kaist.se.simulator;

import java.math.BigDecimal;
import java.util.Random;

/**
 * NormalDistributor.java
 * Raise action at given tick
 * This distributor will generate when the patients will be raised.

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
public class NormalDistributor {

    private Random distGenerator;
    private int mean;
    private int stDev;

    public NormalDistributor(){
        this.distGenerator = new Random();
        this.mean = 0;
        this.stDev = 1;
    }

    public void setNormalDistParams(int mean, int stDev){
        this.mean = mean;
        this.stDev = stDev;
    }

    public int getNextVal(){
        String _val = Double.toString(this.distGenerator.nextGaussian());
        BigDecimal dec = new BigDecimal(_val);
        BigDecimal std = new BigDecimal(Integer.toString(this.stDev));
        dec = dec.add(new BigDecimal(Integer.toString(mean)));
        std = std.multiply(new BigDecimal(_val));
        int retVal = dec.add(std).toBigInteger().intValue();
        return retVal;
    }
}
