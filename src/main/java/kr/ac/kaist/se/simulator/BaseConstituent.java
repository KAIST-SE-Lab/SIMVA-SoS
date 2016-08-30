package kr.ac.kaist.se.simulator;

import simulator.Action;
import sun.util.resources.cldr.dua.CalendarData_dua_CM;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * BaseConstituent.java

 * Author: Junho Kim <jhim@se.kaist.ac.kr>

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
    private int accumulatedBenefit = 0;
    private int totalBudget = 100;
    private int requiredMinimumBudget = 0;
    private Status status;
    private Type type;

    private Action currentAction;
    private ArrayList<Action> capabilityList = null;
    private HashMap<String, Integer> capabilityMap = null; // 각 CS의 Action 당 사용되는 cost <Action_name, cost>
    private HashMap<String, Integer> durationMap = null; // 각 CS의 Action 당 사용되는 duration

    protected BaseConstituent(){
        this.status = Status.IDLE;
        this.currentAction = null;
        this.capabilityList = new ArrayList<Action>();
        this.capabilityMap = new HashMap<String, Integer>();
        this.durationMap = new HashMap<String, Integer>();
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
    public void updateCostBenefit(int cost, int benefit){
        this.usedCost += cost;
        this.accumulatedBenefit += benefit;
    }

    public void initBudget(int totalBudget){
        if(totalBudget < 0)
            this.totalBudget = 100;
        else
            this.totalBudget = totalBudget;
    }

    public int getCost(Action a){
        Integer _cost = this.capabilityMap.get(a.getName());
        if(_cost != null)
            return _cost;
        else
            return 0;
    }

    public void updateCapability(ArrayList<Action> _list){
        ArrayList<Action> newList = new ArrayList<Action>(this.capabilityList.size());
        for(Action target : _list){
            newList.add(target);
        }
        this.capabilityList = newList;
    }

    public void addCapability(Action a, int cost){
        this.capabilityList.add(a);
        this.capabilityMap.put(a.getName(), cost);
        if(this.requiredMinimumBudget < cost)
            this.requiredMinimumBudget = cost;
    }

    public HashMap<String, Integer> getDurationMap(){
        return this.durationMap;
    }
    public void updateDurationMap(HashMap<String, Integer> _druationMap){
        this.durationMap = _druationMap;
    }

    public ArrayList<Action> getCapability(){
        return this.capabilityList;
    }
    public Status getStatus(){
        return this.status;
    }
    public void setStatus(Status _status){
        this.status = _status;
    }

    public void setCurrentAction(Action a){
        this.currentAction = a;
    }
    public Action getCurrentAction(){
        return this.currentAction;
    }
    public void resetCurrentAction(){
        this.currentAction = null;
    }

    /**
     * step method
     * CS chooses an action according to probability distribution.
     * Before selecting, the CS checks the acknowledgement from SoS manager.
     * @return chosen Action instance
     */
    public Action step(){
        if(this.getRemainBudget() == 0){
            return null; // voidAction, nothing happen
        }else{ // We have money
            /*
             * 1. If the status of CS is IDLE (currently no job), then select a job (immediate action)
             * 2. If the status of CS is SELECTION, then
             */
            if(this.getStatus() == Status.IDLE){ // Select an action
                this.setStatus(Status.SELECTION);
                Action a = new Action("Immediate action", 0, 0);
                a.setPerformer(this);
                a.setActionType(Action.TYPE.IMMEDIATE);
                return a;
            }else if(this.getStatus() == Status.OPERATING){ // Operation step
                return this.currentAction;
            }
        }

        return null;
    }

    // Need to be implemented methods
    public abstract void normalAction(int elapsedTime);
    public abstract void immediateAction();
}
