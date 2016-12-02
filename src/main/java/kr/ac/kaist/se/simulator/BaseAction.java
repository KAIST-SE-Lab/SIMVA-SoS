package kr.ac.kaist.se.simulator;

import java.util.ArrayList;

/**
 * BaseAction.java

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

public abstract class BaseAction {

    public static int ID = 0;
    public int this_ID;
    public enum Status {NOT_RAISED, RAISED, HANDLED}
    public enum TYPE {IMMEDIATE, NORMAL}

    private String name;
    private int benefit;
    private int SoSBenefit;
    private int duration;
    private int remainTime;
    private BaseAction.Status status;
    private BaseAction.TYPE actionType;

    private BaseConstituent performer = null; // Current performer
    private ArrayList<BaseConstituent> performerList = new ArrayList<>();

    public BaseAction(){
        this.name = Integer.toString(hashCode()) + Integer.toString(ID);
        this.this_ID = ID++;
    }

    public void startHandle(){
        this.setStatus(BaseAction.Status.HANDLED);
        this.remainTime = duration;
    }

    public void resetAction(){
        this.status = BaseAction.Status.NOT_RAISED;
        this.remainTime = -1;
        this.performer= null;
    }

    public void decreaseRemainingTime(int elapsedTime){
        if(elapsedTime > this.remainTime)
            return;
        this.remainTime -= elapsedTime;
    }

    public TYPE getActionType(){
        return this.actionType;
    }
    public void setActionType(TYPE type){
        this.actionType = type;
    }

    public void setDuration(int _duration){
        this.duration = _duration;
    }
    public int getDuration(){
        return this.duration;
    }

    public Status getStatus(){
        return this.status;
    }
    public void setStatus(Status status){
        this.status = status;
    }
    public void addPerformer(BaseConstituent performer){
        // 메소드가 너무 여러개 들어감
        if(!this.performerList.contains(performer))
            this.performerList.add(performer);

        if(this.performer == null){
            this.performer = performer;
            // Need of checking deleting performer
            // performer 가 항상 중복이 없다라고 가정할 수 있어야함.
        }
    }

    public void removeFromList(BaseConstituent _cs){
        this.performerList.remove(_cs);
    }

    public BaseConstituent getPerformer(){
        return this.performer;
    }

    public ArrayList<BaseConstituent> getPerformerList(){
        return this.performerList;
    }

    public int getRemainingTime(){
        return this.remainTime;
    }

    public void setBenefit(int _benefit){
        this.benefit = _benefit;
    }
    public int getBenefit() {
        return this.benefit;
    }
    public void setSoSBenefit(int _SoSBenefit){
        this.SoSBenefit = _SoSBenefit;
    }
    public int getSoSBenefit() {
        return this.SoSBenefit;
    }

    public void setRemainTime(int remainTime){
        this.remainTime = remainTime;
    }

    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return this.name;
    }

    /*
     * Abstract methods
     */
    public abstract void addBenefit(int additionalBenefit);
    public abstract BaseAction clone();
    public abstract void reset();
    public abstract void randomGenerate();
    public abstract String getDebugTrace();
    public abstract DebugProperty getDebugProperty();
}
