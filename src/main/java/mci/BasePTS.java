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

    private int curPos;
    private RescueAction candidateAction;
    private RescueAction currentAction;

    private int PTS_STATUS; // 0: IDLE, 1: Go to Patient, 2: Return to Hospital

    public BasePTS(){
        this.curPos = 50;
        this.candidateAction = null;
        this.PTS_STATUS = 0;
    }

    /**
     * normal Action method
     * Rescue the patient
     * The elapsedTime will never exceed the maximum distance of this PTS.
     * @param elapsedTime
     */
    @Override
    public void normalAction(int elapsedTime) {
        if(this.PTS_STATUS == 1){ // Go to Patient
            int patientPos = this.currentAction.getRaisedLoc();
            patientPos = Math.abs(patientPos - 50); // 50 = Hospital location

            if(elapsedTime > patientPos) {
                // E.g., curPos = 59, patientPos = 70, elapsedTime = 30
                movePTS(patientPos - this.curPos); // 70-59 = 11 & Reached!
                int remainingTime = elapsedTime - (patientPos - this.curPos); // 30-11 = 19
                this.PTS_STATUS = 2; // Return to hospital
                movePTS((-1)*remainingTime);
            }else{
                // E.g., curPos = 59, patientPos = 70, elapsedTime = 10
                movePTS(elapsedTime);
            }
        }else if(this.PTS_STATUS == 2){ // Go to Hospital
            movePTS(elapsedTime);
            if(this.curPos == 50){ // PTS has breached to the hospital!
                if(this.currentAction.getPatientStatus() != RescueAction.PatientStatus.Dead) // Not dead
                    this.updateCostBenefit(0, 1, 1); // save one patient!
                this.PTS_STATUS = 0;
            }
        }
    }

    /**
     * Move the PTS moveVal value in GeoMap
     * @param moveVal
     */
    private void movePTS(int moveVal){
        this.curPos += moveVal;
        this.currentAction.treatAction(moveVal);
    }

    /**
     * immediate Action method
     * choose the patient to save
     * this method will get best patient to save using choosePatient method.
     * @return the Base Action that which patient will be rescued by this TS.
     */
    @Override
    public BaseAction immediateAction() {
        if(this.getStatus() != Status.IDLE || this.getStatus() != Status.SELECTION)
            return null;
        if(this.getStatus() == Status.IDLE){
            RescueAction action = choosePatient();
            return action;
        }else if(this.getStatus() == Status.SELECTION){
            if(this.candidateAction.getStatus() == BaseAction.Status.RAISED){
                this.currentAction = this.candidateAction;
                this.currentAction.addPerformer(this);
                this.gotoPatient();
                return this.candidateAction;
            }
        }
        return null;
    }

    @Override
    public BaseConstituent clone() {
        return null;
    }

    @Override
    public BaseAction getCurrentAction() {
        return this.currentAction;
    }

    private void gotoPatient(){
        this.PTS_STATUS = 1;
        this.setStatus(Status.OPERATING);
    }

    public RescueAction choosePatient() {
        RescueAction bestAction = null;
        for(MapPoint map : Hospital.GeoMap){
            RescueAction candidate = map.getCurAction();
            if(candidate == null)
                continue;
            if(bestAction == null){
                bestAction = candidate;
            }else{
                if(getUtility(candidate) > getUtility(bestAction))
                    bestAction = candidate;
            }
        }
        return bestAction;
    }
}
