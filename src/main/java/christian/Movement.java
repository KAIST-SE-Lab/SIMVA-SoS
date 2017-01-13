package christian;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.DebugProperty;

/**
 * Movement.java
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

public class Movement extends BaseAction {

    public Movement(){
//        this.setName("Right move");
        super.setDuration(1);
        super.setActionType(TYPE.NORMAL);
    }

    @Override
    public void addBenefit(int additionalBenefit) {

    }

    @Override
    public Movement clone() {

        Movement _copy = new Movement();
        _copy.setDuration(1);
        super.setActionType(TYPE.NORMAL);

        return _copy;
    }

    @Override
    public void reset() {

    }

    @Override
    public void randomGenerate() {
        // Not-used
    }

    @Override
    public String getDebugTrace() {
        return null;
    }

    @Override
    public DebugProperty getDebugProperty() {
        return null;
    }
}
