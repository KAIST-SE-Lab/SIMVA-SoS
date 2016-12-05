package mci.model;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.ConstituentInterface;
import kr.ac.kaist.se.simulator.DebugProperty;

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

    public static int num;

    private int curPos;
    private String name;
    private int PTS_STATUS; // 0: IDLE, 1: Go to Patient, 2: Return to Hospital

    public BasePTS(){
        this.name = "PTS" + String.valueOf(BasePTS.num++);
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
                    if(this.curPos == currentAction.getRaisedLoc())
                        this.PTS_STATUS = 2;
                }
            }else{ // CurrentAction raisedLoc >= 50
                int distance = currentAction.getRaisedLoc() - this.curPos;
                if(elapsedTime > distance){
                    movePTS(distance);
                    int remainTime = elapsedTime - distance;
                    this.PTS_STATUS = 2;
                    movePTS((-1) * remainTime);
                }else {
                    movePTS(elapsedTime);
                    if(this.curPos == currentAction.getRaisedLoc())
                        this.PTS_STATUS = 2;
                }
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
//                    System.out.println(this + " PTS saved a patient!");
                    this.updateCostBenefit(0, 1, 1);
                    readyForPatient();
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
        int before = this.curPos;
        this.curPos += moveVal;
        if(before < 50 && this.curPos > 50){ // Defend code
            this.curPos = 50;
        }else if(before > 50 && this.curPos < 50){
            this.curPos = 50;
        }// 값 보정
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
            if (action != null && action.getStatus() == BaseAction.Status.RAISED) { // 내가 먹을 수 있어!
                this.setCurrentAction(action);
                action.addPerformer(this);
//                System.out.println("Pick Patient"+action.getName() + " at" + action.getRaisedLoc());
                this.gotoPatient();
            }else{ // 이미 다른애가 가져감
//                if(action != null) System.out.println("[LOG] " + this + " takes nothing." );
                if(action != null)
                    System.out.println(action.getStatus());
                action = null;
            }
            if(action == null) { // 아무런 action을 찾지 못했으니 IDLE로 다시 돌림
                this.setStatus(Status.IDLE);
//                System.out.println("[LOG] " + this + " takes nothing." );
            }
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
//        System.out.println(this + " chooses an action");
    }

    public RescueAction choosePatient() {
        RescueAction bestAction = null;
        for(int lb = 47, ub = 53; (lb >= 0) && (ub <= 100); lb--, ub++){
            int candidateSize = Hospital.GeoMap.get(lb).getCurActions().size(); // 왼쪽 탐색
            if(candidateSize != 0) { // 왼쪽 비어있지 않으면
                bestAction = pickBest(Hospital.GeoMap.get(lb).getCurActions()); // 왼쪽 선택
            }
            if(bestAction != null)
                break;

            candidateSize = Hospital.GeoMap.get(ub).getCurActions().size(); // 오른쪽 검사
            if(candidateSize != 0){
                bestAction = pickBest(Hospital.GeoMap.get(ub).getCurActions()); // 오른쪽 선택
            }

            if(bestAction == null) // 맵에서 선택할만한거 없으면 다음으로 블락으로
                continue;

            break;
        } // 맵의 양 극단에서부터 candidateAction을 뽑아오기
        return bestAction;
    }

    RescueAction pickBest(ArrayList<RescueAction> aList){
        RescueAction candidate = null;

        if(aList.size() == 0) // 없으면 널
            return candidate;
        else if(aList.size() == 1) { // 한개인 경우
            if (aList.get(0).getStatus() == BaseAction.Status.RAISED) { // 환자 상태가 발생한 상태
                if (getUtility(aList.get(0)) > 0)
                    return aList.get(0);
            }
        }
        else{ // 맵 포인트에 환자가 2명 이상
            for(int iterator = 0; iterator< aList.size(); iterator++){
                RescueAction rA = aList.get(iterator); // Candidate
                if(rA.getStatus() == BaseAction.Status.RAISED){ // If this patient is raised.
                    if(candidate == null){ // no 비교군
                        if(rA.getPatientStatus()!= RescueAction.PatientStatus.Dead && getUtility(rA) > 0)
                            candidate = rA;
                    }else{// 비교군이 있음
                        if(rA.getPatientStatus()!= RescueAction.PatientStatus.Dead && getUtility(candidate) < getUtility(rA)){
                            candidate = rA;
                        }else if(getUtility(candidate) == getUtility(rA) && candidate.getPatientStatus() != RescueAction.PatientStatus.Very_Dangerous &&
                                rA.getPatientStatus() == RescueAction.PatientStatus.Very_Dangerous){
                            candidate = rA;
                        }
                    }
                }
            }
        }

        return candidate;
    }

    public int getPTS_STATUS(){
        return this.PTS_STATUS;
    }

    public int getCurPos(){
        return this.curPos;
    }

    private void readyForPatient(){
        this.setStatus(Status.IDLE);
        this.PTS_STATUS = 0;
        this.setCurrentAction(null);
    }

    public String getStatusString(){
        if(this.getPTS_STATUS() == 0)
            return "IDLE";
        else if(this.getPTS_STATUS() == 1)
            return "GO_PATIENT";
        else if(this.getPTS_STATUS() == 2)
            return "RETURN_HOSPITAL";
        return "";
    }


    @Override
    public void reset(){
        super.reset();
        this.curPos = 50;
        this.PTS_STATUS = 0;
        this.setCurrentAction(null);
    }

    @Override
    public DebugProperty getDebugProperty(){
        DebugProperty prop = new DebugProperty();
        prop.putProperty("status", this.getStatusString());
        prop.putProperty("position", this.curPos);
        return prop;
    }

    @Override
    public String getName(){
        return this.name;
    }
}
