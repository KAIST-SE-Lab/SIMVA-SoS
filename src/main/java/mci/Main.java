package mci;

import kr.ac.kaist.se.simulator.NormalDistributor;

import java.util.ArrayList;

/**
 * Main.java
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
public class Main {

    public static void main(String[] args){

        Hospital hos = new Hospital();
        // Initialize Patient map
        for(int i=0; i<=100; i++){
            Hospital.GeoMap.add(new MapPoint(i));
        }

        NormalDistributor distributor = new NormalDistributor();
        distributor.setNormalDistParams(150, 50);
        ArrayList<Integer> list = distributor.getDistributionArray(100);
        for(Integer i : list){
            System.out.println(i);
        }
    }



}
