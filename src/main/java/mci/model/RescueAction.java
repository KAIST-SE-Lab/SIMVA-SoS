package mci.model;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.DebugProperty;

import java.util.Random;

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

    public enum PatientStatus { Dangerous, Very_Dangerous, Dead }

    private PatientStatus pStat;
    private int raisedLoc;
    private int timeToDead; // Remaining time to dead patient
    private int curPos;

    private boolean isAcknowledged;

    public RescueAction(int raisedLoc, int timeToDead){
        this.raisedLoc = raisedLoc;
        this.timeToDead = timeToDead;
    }

    @Override
    public void addBenefit(int additionalBenefit) { // ACK..
        this.isAcknowledged = true;
    }

    @Override
    public BaseAction clone() {
        RescueAction cA = new RescueAction(this.raisedLoc, this.timeToDead);
        cA.setName(this.getName());
        return cA;
    }


    @Override
    public void reset() {
        super.resetAction();
        this.curPos = 0;
        this.timeToDead = 0;
        this.raisedLoc = 0;
        this.isAcknowledged = false;
        this.pStat = null;
        this.setActionType(null);
    }

    @Override
    public void randomGenerate() { // Location..?
        Random ranGen = new Random();
        int raisedLoc = Math.abs(ranGen.nextInt(101)); // 0 - 100
        while(raisedLoc > 47 && raisedLoc < 53){ // 48, 49, 50, 51, 52
            raisedLoc = Math.abs(ranGen.nextInt(101));
        }
//        int raisedLoc = 60; // For test

//        raisedLoc = 60;

        this.raisedLoc = raisedLoc;
        this.curPos = raisedLoc;

        int severity = ranGen.nextInt(2); // 0: dangerous, 1 : Very Dangerous
        if(severity == 0){
            this.pStat = PatientStatus.Dangerous;
        }else if(severity == 1){
            this.pStat = PatientStatus.Very_Dangerous;
        }

        int timeToDead = Math.abs(ranGen.nextInt(300)) + 200; // 750 ~ 1450
        this.timeToDead = timeToDead;

        this.setActionType(TYPE.NORMAL);
    }

    @Override
    public String getDebugTrace() {
        if(this.getStatus() == null ||this.getStatus() == Status.NOT_RAISED || this.pStat == PatientStatus.Dead){
            return "";
        }
        if(this.getPerformer().getClass() == Hospital.class) // Ignore the heal action of the hospital
            return "";
        String ret_str = "Patient#" + this.getName();
        ret_str += "/";
        ret_str += String.valueOf(this.curPos);
        ret_str += "/";
        ret_str += String.valueOf(this.timeToDead);
        if(isAcknowledged)
            ret_str += "/ack";
        else
            ret_str += "/not_ack";
        return ret_str;
    }

    @Override
    public DebugProperty getDebugProperty() {
        if(this.getPerformer().getClass() == Hospital.class) // Ignore the heal action of the hospital
            return null;
        DebugProperty prop = new DebugProperty();
        prop.putProperty("name", ("Patient#" + this.getName()));
        prop.putProperty("curPos", this.curPos);
        prop.putProperty("stat", RescueAction.getPatientStatusString(this.pStat));
        return prop;
    }

    public void treatAction(int elapsedTime){
        this.timeToDead -= Math.abs(elapsedTime);
        this.decreaseRemainingTime(Math.abs(elapsedTime));
        if(timeToDead <= 0) {
            this.pStat = PatientStatus.Dead;
            this.timeToDead = 0;
        }
    }

    @Override
    public void startHandle(){
        this.setStatus(Status.HANDLED);
        this.setRemainTime(timeToDead);
    }

    public int getRemainTime(){
        return this.timeToDead;
    }

    public boolean isAcknowledged(){
        return this.isAcknowledged;
    }

    public int getRaisedLoc(){
        return this.raisedLoc;
    }

    public void setCurPos(int curPos){
        this.curPos = curPos;
    }

    public PatientStatus getPatientStatus(){
        return this.pStat;
    }

    @Override
    public int getRemainingTime(){
        if( this.getPerformer() instanceof BasePTS) {
            int remainDistance = this.getRemainDistanceToHospital();
            return remainDistance > this.getRemainTime() ? this.getRemainTime() : remainDistance;
        }else{
            return timeToDead;
        }
    }

    private int getRemainDistanceToHospital(){
        BasePTS pts = (BasePTS) this.getPerformer();
        int mode = pts.getPTS_STATUS(); // 1: go to patient, 2: Return to Hospital

        if(mode == 2){ // 병원으로 오고 있으면 같이 오는 Action과 같이 이동..
            if(this.curPos > 50){
                return this.curPos - 50;
            }else if(this.curPos < 50){
                return 50 - this.curPos;
            }
        }else if(mode == 1){// 아직 도착도 안함.. PTS 거리만큼 빼줘야함.
            int curPerformerPos = pts.getCurPos();
            if(this.curPos > 50){
                int remainToPatient = this.curPos - curPerformerPos;
                return this.curPos - 50 + remainToPatient;
            } else if (this.curPos < 50) {
                int remainToPatient = curPerformerPos - this.curPos;
                return 50 - this.curPos + remainToPatient;
            }
        }
        return 0;

    }

    public static String getPatientStatusString(PatientStatus pStat){
        if(pStat == PatientStatus.Dangerous)
            return "Dangerous";
        else if(pStat == PatientStatus.Very_Dangerous)
            return "Very_Dangerous";
        else if(pStat == PatientStatus.Dead)
            return "DEAD";
        return "";
    }
}
