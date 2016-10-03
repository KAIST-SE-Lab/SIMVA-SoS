package mci;

import kr.ac.kaist.se.simulator.BaseAction;

/**
 * RescueAction.java
 * Rescue patient action class

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
public class RescueAction extends BaseAction {

    public enum PatientStatus { Dangerous, Very_Dangerous }

    private PatientStatus pStat;
    private int raisedLoc;

    public RescueAction(int raisedLoc){
        this.raisedLoc = raisedLoc;
    }

    @Override
    public void addBenefit(int additionalBenefit) {

    }

    @Override
    public BaseAction clone() {
        return null;
    }

    @Override
    public void reset() {

    }

    public int getRaisedLoc(){
        return this.raisedLoc;
    }

    public PatientStatus getPatientStatus(){
        return this.pStat;
    }
}
