package kr.ac.kaist.se.simulator;

import java.util.HashMap;

/**
 * DebugProperty.java

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

public class DebugProperty {

    private HashMap<String, Object> properties;

    public DebugProperty(){
        this.properties = new HashMap<>();
    }

    public void putProperty(String k, Object o){
        this.properties.put(k, o);
    }

    public Object getProperty(String k){
        return this.properties.get(k);
    }

    public HashMap<String, Object> getProperties(){
        return this.properties;
    }

}
