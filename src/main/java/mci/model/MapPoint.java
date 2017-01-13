package mci.model;

import java.util.ArrayList;

/**
 * MapPoint.java
 * Each Map point

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
public class MapPoint {

    private int xPos;
    private ArrayList<RescueAction> aList;

    public MapPoint(int xPos){
        this.aList = new ArrayList<>();
        this.xPos = xPos;
    }

    public void addCurAction(RescueAction a){
        // 이름이 제대로 걸러지지 않음..
        for(RescueAction rA : this.aList){
            if(rA.getName().equalsIgnoreCase(a.getName())){
                return;
            }
        }
        this.aList.add(a);
    }

    public ArrayList<RescueAction> getCurActions(){
        return this.aList;
    }
}
