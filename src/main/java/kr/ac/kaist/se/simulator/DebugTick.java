package kr.ac.kaist.se.simulator;

import java.util.HashMap;

/**
 * DebugTick.java

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

public class DebugTick {

    private HashMap<String, DebugProperty> debugInfoMap = new HashMap<>(); // E.g.,
    private int timeTick;

    public DebugTick(int timeTick){
        this.timeTick = timeTick;
    }

    public void putDebugTrace(String name, DebugProperty property){
        this.debugInfoMap.put(name, property);
    }

    public DebugProperty getDebugTrace(String name){
        return this.debugInfoMap.get(name);
    }

    public HashMap<String, DebugProperty> getDebugInfoMap(){
        return this.debugInfoMap;
    }

    public int getTimeTick(){
        return this.timeTick;
    }

}
