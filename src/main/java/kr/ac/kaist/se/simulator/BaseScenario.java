package kr.ac.kaist.se.simulator;

import java.util.ArrayList;

/**
 * BaseScenario.java
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

public abstract class BaseScenario {

    public abstract void init();
    public abstract String getDescription();
    public abstract ArrayList<BaseConstituent> getCSList();
    public abstract BaseConstituent getManager();
    public abstract void setCSList(BaseConstituent[] csList);
    public abstract void setManager(BaseConstituent manager);
    public abstract void setEnvironment(Environment env);
    public abstract Environment getEnvironment();
    public abstract ArrayList<BaseAction> getActionList();
    public abstract void setActionList(ArrayList<BaseAction> aList);
    public abstract int getEndTick();
    public abstract void setEndTick(int endTick);
}
