package kr.ac.kaist.se;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.BaseConstituent;

import java.util.ArrayList;

/**
 * Executor.java
 * Author: Junho Kim <jhkim@se.kaist.ac.kr>
 *
 * Simulation Executor class
 *
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 Junho Kim
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: TBD
 */

public class Executor {
    private ArrayList<BaseConstituent> constituentList;
    private ArrayList<BaseAction> actionList;

    public Executor(){
        this.constituentList = new ArrayList<>();
        this.actionList = new ArrayList<>();
    }

    public void addCSs(ArrayList<BaseConstituent> list){
        for(BaseConstituent CS : list){
            this.constituentList.add(CS);
        }
    }

    public void addActions(ArrayList<BaseAction> aList){
        for(BaseAction a : aList){
            this.actionList.add(a);
        }
    }
}
