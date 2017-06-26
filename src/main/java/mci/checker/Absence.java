package mci.checker;

import kr.ac.kaist.se.mc.CheckerInterface;
import kr.ac.kaist.se.simulator.DebugProperty;
import kr.ac.kaist.se.simulator.DebugTick;
import kr.ac.kaist.se.simulator.SIMResult;

import java.util.HashMap;
import java.util.Map;

/**
 * Absence.java

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

public class Absence implements CheckerInterface{

    /**
     * Return the name
     */
    public String getName() { return "Absence Checker"; }

    /**
     * Return the description
     */
    public String getDescription() {
        return "Globally, it is never the case that \"Dead patients become alive again\" holds";
    }

    @Override
    public void init(String[] params) {
        // params[0]: checker name
        // params[1]: probability
    }

    /**
     * evaluateSample Method
     * Evaluate a given property satisfies absence property
     * Check all time ticks whether there is a patient whose status changes
     * from DEAD to other status (Dangerous or Very_Dangerous)
     * @param res Simulation result class which contains debugTick Map
     * @return 1, there is an absence, otherwise 0
     */
    @Override
    public int evaluateSample(SIMResult res) {
        HashMap<Integer, DebugTick> traceMap = res.getDebugTraces();
        HashMap<String, String> patientStatusMap = new HashMap<>(); // Additional map

        for(Map.Entry <Integer,DebugTick> t: traceMap.entrySet()){
            for(Map.Entry<String, DebugProperty> debugTick: t.getValue().getDebugInfoMap().entrySet()){
                String name = debugTick.getKey();
                if(name.contains("Patient")){
                    String stat = (String) debugTick.getValue().getProperty("stat");
                    if(patientStatusMap.containsKey(name)){
                        String beforeStat = patientStatusMap.get(name);
                        if(beforeStat.equalsIgnoreCase("DEAD") && !stat.equalsIgnoreCase("DEAD"))
                            return 0;
                    }else{
                        patientStatusMap.put(name, stat);
                    }
                }
            }
        }
        return 1;
    }

    @Override
    public int getMinTick() {
        return 0;
    }

    @Override
    public int getMaxTick() {
        return 0;
    }
}
