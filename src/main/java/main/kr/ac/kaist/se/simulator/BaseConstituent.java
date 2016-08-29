package main.kr.ac.kaist.se.simulator;

import simulator.Action;

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

    private int usedCost = 0;
    private int accumulatedBenefit = 0;
    private int totalBudget = 100;
    private int requiredMinimumBudget = 0;
    private Status status;

    private ArrayList<Action> capabilityList = null;
    private HashMap<String, Integer> capabilityMap = null; // 각 CS의 Action 당 사용되는 cost <Action_name, cost>

    protected BaseConstituent(){
        this.status = Status.IDLE;
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

    public void updateActionList(ArrayList<Action> _list){
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

    public ArrayList<Action> getCapability(){
        return this.capabilityList;
    }
    public Status getStatus(){
        return this.status;
    }

    public void setStatus(Status _status){
        this.status = _status;
    }

    // Need to be implemented methods
    public abstract int getUtility(Action a);
    public abstract void normalAction(int elapsedTime);
    public abstract void immediateAction();
}
