package kr.ac.kaist.se.simulator;

import kr.ac.kaist.se.simulator.method.DummyAction;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * BaseConstituent.java

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

public abstract class BaseConstituent {
    public enum Status {IDLE, SELECTION, OPERATING, END}
    public enum Type {Constituent, SoSManager}

    private int usedCost = 0;
    private int accumulatedSoSBenefit = 0;
    private int accumulatedBenefit = 0;
    private int totalBudget = 100;
    private int requiredMinimumBudget = 0;
    private Status status;
    private Type type;

    private BaseAction currentAction;
    private ArrayList<BaseAction> capabilityList = null;
    private HashMap<String, Integer> capabilityMap = null; // 각 CS의 Action 당 사용되는 cost <Action_name, cost>

    protected BaseConstituent(){
        this.status = Status.IDLE;
        this.currentAction = null;
        this.capabilityList = new ArrayList<>();
        this.capabilityMap = new HashMap<>();
    }

    protected void setType(Type type){
        this.type = type;
    }

    public Type getType(){
        return this.type;
    }

    public int getRemainBudget(){
        return this.totalBudget - this.usedCost;
    }
    public int getRequiredMinimumBudget() { return this.requiredMinimumBudget; }
    public void updateCostBenefit(int cost, int CSbenefit, int SoSbenefit){
        this.usedCost += cost;
        this.accumulatedSoSBenefit += SoSbenefit;
        this.accumulatedBenefit += CSbenefit;
    }

    public void initBudget(int totalBudget){
        if(totalBudget < 0)
            this.totalBudget = 100;
        else
            this.totalBudget = totalBudget;
    }
    public int getTotalBudget(){
        return this.totalBudget;
    }

    public int getCost(BaseAction a){
        Integer _cost = this.capabilityMap.get(a.getName());
        if(_cost != null)
            return _cost;
        else
            return 0;
    }

    public void updateCapability(ArrayList<BaseAction> _list){
        ArrayList<BaseAction> newList = new ArrayList<>(this.capabilityList.size());
        for(BaseAction target : _list){
            for(BaseAction a : this.capabilityList){
                if(a.getName().equalsIgnoreCase(target.getName()))
                    newList.add(target);
            }
        }
        this.capabilityList = newList;
    }

    public void addCapability(BaseAction a, int cost){
        this.capabilityList.add(a);
        this.capabilityMap.put(a.getName(), cost);
        if(this.requiredMinimumBudget < cost)
            this.requiredMinimumBudget = cost;
    }

    public int getAccumulatedSoSBenefit(){
        return this.accumulatedSoSBenefit;
    }
    public int getAccumulatedBenefit(){
        return this.accumulatedBenefit;
    }
    public ArrayList<BaseAction> getCapability(){
        return this.capabilityList;
    }
    public Status getStatus(){
        return this.status;
    }
    public void setStatus(Status _status){
        this.status = _status;
    }

    public void resetCurrentAction(){
        this.currentAction = null;
    }

    public void reset(){
        for(BaseAction a : this.getCapability()){
            a.reset();
        }
        this.usedCost = 0;
        this.accumulatedBenefit = 0;
        this.accumulatedSoSBenefit = 0;
        this.setStatus(Status.IDLE);
        this.currentAction = null;
    }

    /**
     * step method
     * CS chooses an action according to probability distribution.
     * Before selecting, the CS checks the acknowledgement from SoS manager.
     * @return chosen Action instance
     */
    public BaseAction step(){
        if(this.getRemainBudget() == 0){
            return null; // voidAction, nothing happen
        }else{ // We have money
            /*
             * 1. If the status of CS is IDLE (currently no job), then select a job (immediate action)
             * 2. If the status of CS is SELECTION, then
             */
            if(this.getStatus() == Status.IDLE){ // Select an action
                this.setStatus(Status.SELECTION);
                BaseAction a = new DummyAction("Immediate action", 0, 0);
                a.addPerformer(this);
                a.setActionType(BaseAction.TYPE.IMMEDIATE);
                return a;
            }else if(this.getStatus() == Status.OPERATING){ // Operation step
                return this.currentAction;
            }
        }

        return null;
    }

    public void setCurrentAction(BaseAction a){
        this.currentAction = a;
    }
    public BaseAction getCurrentAction(){
        return this.currentAction;
    }

    public String getDebugTrace(){
        String debugTrace = "";
        switch(this.getStatus()){
            case IDLE: debugTrace += "IDLE"; break;
            case SELECTION: debugTrace += "SEL"; break;
            case OPERATING: debugTrace += "OP"; break;
            case END: debugTrace += "END"; break;
        }
        if(this.getCurrentAction() != null)
            debugTrace += "/" + this.getCurrentAction().getName();
        return debugTrace;
    }

    /*
     * Abstract methods
     */
    public abstract void normalAction(int elapsedTime);
    public abstract BaseAction immediateAction();
    public abstract BaseConstituent clone();
    public abstract DebugProperty getDebugProperty();
    public abstract String getName();
}
