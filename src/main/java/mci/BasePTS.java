package mci;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.ConstituentInterface;

/**
 * BasePTS.java
 * Basic PTS class

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

public abstract class BasePTS extends BaseConstituent implements ConstituentInterface{

    /**
     * normal Action method
     * @param elapsedTime
     */
    @Override
    public void normalAction(int elapsedTime) {

    }

    /**
     * immediate Action method
     * choose the patient to save
     * this method will get best patient to save using choosePatient method.
     * @return the Base Action that which patient will be rescued by this TS.
     */
    @Override
    public BaseAction immediateAction() {
        if(this.getStatus() != Status.IDLE)
            return null;
        RescueAction action = choosePatient();
        return action;
    }

    @Override
    public BaseConstituent clone() {
        return null;
    }

    @Override
    public BaseAction getCurrentAction() {
        return null;
    }

    public abstract RescueAction choosePatient();
}
