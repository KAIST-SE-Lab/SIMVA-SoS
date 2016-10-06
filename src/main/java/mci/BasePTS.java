package mci;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.ConstituentInterface;

import java.util.ArrayList;

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

    private int PTS_STATUS; // 0: IDLE, 1: Go to Patient, 2: Return to Hospital

    public BasePTS(){
        this.curPos = 50;
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
        RescueAction currentAction = (RescueAction) this.getCurrentAction();
        if(this.PTS_STATUS == 1){ // Go to Patient
            if(currentAction.getRaisedLoc() < 50){
                int distance = this.curPos - currentAction.getRaisedLoc();
                if(elapsedTime > distance){
                    movePTS( (-1) * distance );
                    int remainTime = elapsedTime - distance;
                    this.PTS_STATUS = 2;
                    movePTS(remainTime);
                }else{
                    movePTS((-1) * elapsedTime);
                }
            }else{
                int distance = currentAction.getRaisedLoc() - this.curPos;
                if(elapsedTime > distance){
                    movePTS(distance);
                    int remainTime = elapsedTime - distance;
                    this.PTS_STATUS = 2;
                    movePTS((-1) * remainTime);
                }else
                    movePTS(elapsedTime);
            }
        }else if(this.PTS_STATUS == 2){ // 병원으로 돌아옴
            if(this.curPos < 50){
                int distance = 50 - this.curPos;
                if(elapsedTime > distance){
                    movePTS(distance);
                }else{
                    movePTS(elapsedTime);
                }
            }else if(this.curPos > 50){
                int distance = this.curPos - 50;
                if(elapsedTime > distance){
                    movePTS((-1) * distance);
                }else{
                    movePTS((-1) * elapsedTime);
                }
            }
            if(this.curPos == 50){// 병원 도착
                RescueAction.PatientStatus pStat = currentAction.getPatientStatus();
                if(pStat != RescueAction.PatientStatus.Dead){
                    this.updateCostBenefit(0, 1, 1);
                }
                this.PTS_STATUS = 0;
            }
        }
    }

    /**
     * Move the PTS moveVal value in GeoMap
     * @param moveVal
     */
    private void movePTS(int moveVal){
        RescueAction currentAction = (RescueAction) this.getCurrentAction();
        this.curPos += moveVal;
        if(this.PTS_STATUS == 2) //병원으로 갈 때는 같이 이동
            currentAction.setCurPos(curPos);
        currentAction.treatAction(moveVal);
    }

    /**
     * immediate Action method
     * choose the patient to save
     * this method will get best patient to save using choosePatient method.
     * @return the Base Action that which patient will be rescued by this TS.
     */
    @Override
    public BaseAction immediateAction() {
        if(this.getStatus() != Status.IDLE && this.getStatus() != Status.SELECTION)
            return null;
        if(this.getStatus() == Status.SELECTION) {
            RescueAction action = choosePatient();
            if (action != null && action.getStatus() == BaseAction.Status.RAISED) {
                this.setCurrentAction(action);
                action.addPerformer(this);
                this.gotoPatient();
            }
            if(action == null) // 아무런 action을 찾지 못했으니 IDLE로 다시 돌림
                this.setStatus(Status.IDLE);
            return action;
        }
        this.setStatus(Status.IDLE);
        return null;
    }

    @Override
    public BaseConstituent clone() {
        return null;
    }

    private void gotoPatient(){
        this.PTS_STATUS = 1;
        this.setStatus(Status.OPERATING);

        RescueAction currentAction = (RescueAction) this.getCurrentAction();

        currentAction.startHandle();
    }

    public RescueAction choosePatient() {
        RescueAction bestAction = null;
        for(MapPoint map : Hospital.GeoMap){
            int candidateSize = map.getCurActions().size();
            if(candidateSize == 0)
                continue;
            bestAction = pickBest(map.getCurActions());
        }
        return bestAction;
    }

    private RescueAction pickBest(ArrayList<RescueAction> aList){
        if(aList.size() == 1 && aList.get(0).getStatus() == BaseAction.Status.RAISED)
            return aList.get(0);
        else if(aList.size() > 1){
            RescueAction candidate = aList.get(0);
            for(RescueAction a : aList){
                if(a.getStatus() != BaseAction.Status.RAISED)
                    continue;
                if(getUtility(candidate) < getUtility(a))
                    candidate = a;
            }
            return candidate;
        }else{
            return null;
        }
    }

    public int getPTS_STATUS(){
        return this.PTS_STATUS;
    }

    public int getCurPos(){
        return this.curPos;
    }
}
