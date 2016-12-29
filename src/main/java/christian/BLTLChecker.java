package christian;

import kr.ac.kaist.se.mc.BaseChecker;

/**
 * BaseChecker.java
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

public class BLTLChecker extends BaseChecker{
    /**
     * Constructor for BLTL model Checker
     *
     * @param baseTick       baseline of the time tick, BLTL Checker will evaluate the sample sequence based on this tick
     * @param baseSoSBenefit baseline of SoS benefit, BLTL Checeker will evaluate the sample seuqence based on this SoS benefit
     * @param type
     */
    public BLTLChecker(int baseTick, int baseSoSBenefit, comparisonType type) {
        super();
        super.init(baseTick, baseSoSBenefit, type);
    }
}
