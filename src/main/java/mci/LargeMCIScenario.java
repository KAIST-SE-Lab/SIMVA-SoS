package mci;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.BaseScenario;
import kr.ac.kaist.se.simulator.Environment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * LargeMCIScenario.java
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

public class LargeMCIScenario extends BaseScenario {

    private ArrayList<BaseConstituent> csList;
    private BaseConstituent manager;
    private Environment env;
    private ArrayList<BaseAction> actionList;

    public LargeMCIScenario(){
        NearestPTS np1 = new NearestPTS();
        NearestPTS np2 = new NearestPTS();
        NearestPTS np3 = new NearestPTS();
        NearestPTS np4 = new NearestPTS();
        NearestPTS np5 = new NearestPTS();
        NearestPTS np6 = new NearestPTS();
        NearestPTS np7 = new NearestPTS();
        NearestPTS np8 = new NearestPTS();
        NearestPTS np9 = new NearestPTS();
        NearestPTS np10 = new NearestPTS();
        NearestPTS np11 = new NearestPTS();
        NearestPTS np12 = new NearestPTS();
        NearestPTS np13 = new NearestPTS();
        NearestPTS np14 = new NearestPTS();
        NearestPTS np15 = new NearestPTS();
        NearestPTS np16 = new NearestPTS();
        NearestPTS np17 = new NearestPTS();
        NearestPTS np18 = new NearestPTS();
        NearestPTS np19 = new NearestPTS();
        NearestPTS np20 = new NearestPTS();


        SeverityPTS sp1 = new SeverityPTS();
        SeverityPTS sp2 = new SeverityPTS();
        SeverityPTS sp3 = new SeverityPTS();
        SeverityPTS sp4 = new SeverityPTS();
        SeverityPTS sp5 = new SeverityPTS();
        SeverityPTS sp6 = new SeverityPTS();
        SeverityPTS sp7 = new SeverityPTS();
        SeverityPTS sp8 = new SeverityPTS();
        SeverityPTS sp9 = new SeverityPTS();
        SeverityPTS sp10 = new SeverityPTS();
        SeverityPTS sp11 = new SeverityPTS();
        SeverityPTS sp12 = new SeverityPTS();
        SeverityPTS sp13 = new SeverityPTS();
        SeverityPTS sp14 = new SeverityPTS();
        SeverityPTS sp15 = new SeverityPTS();
        SeverityPTS sp16 = new SeverityPTS();
        SeverityPTS sp17 = new SeverityPTS();
        SeverityPTS sp18 = new SeverityPTS();
        SeverityPTS sp19 = new SeverityPTS();
        SeverityPTS sp20 = new SeverityPTS();

        BaseConstituent[] CSs = new BaseConstituent[]{np1, np2, np3, np4, np5, np6, np7, np8, np9, np10,
                np11, np12, np13, np14, np15, sp11, sp12, sp13, sp14, sp15, np16, np17, np18, np19, np20,
                sp1, sp2, sp3, sp4, sp5, sp6, sp7, sp8, sp9, sp10, sp16, sp17, sp18, sp19, sp20};

        this.csList = new ArrayList<>();
        this.csList.addAll(Arrays.asList(CSs));
        this.manager = new Hospital();

        this.actionList = new ArrayList<>();
        for (int i = 0; i < 500; i++)
            this.actionList.add(new RescueAction(0, 0));

        this.env = new Environment(CSs, this.actionList.toArray(new BaseAction[this.actionList.size()]));
    }

    @Override
    public void init() {
        for (int i = 0; i <= 100; i++) {
            Hospital.GeoMap.add(new MapPoint(i));
        }
    }

    @Override
    public ArrayList<BaseConstituent> getCSList() {
        return csList;
    }

    @Override
    public BaseConstituent getManager() {
        return this.manager;
    }

    @Override
    public void setManager(BaseConstituent manager){
        this.manager = manager;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Override
    public Environment getEnvironment() {
        return env;
    }

    @Override
    public ArrayList<BaseAction> getActionList() {
        return this.actionList;
    }

    @Override
    public void setActionList(ArrayList<BaseAction> aList) {
        this.actionList = aList;
    }

    @Override
    public void setCSList(BaseConstituent[] CSs){
        if(csList != null)
            csList.clear();
        else
            this.csList = new ArrayList<>();
        this.csList.addAll(Arrays.asList(CSs));
    }
}
