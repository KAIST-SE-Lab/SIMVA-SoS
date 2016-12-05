package mci.model;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.DebugProperty;
import kr.ac.kaist.se.simulator.ManagerInterface;

import java.util.ArrayList;

/**
 * Hospital.java
 * Hospital class, this is an SoS manager class

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
public class Hospital extends BaseConstituent implements ManagerInterface {

    public static ArrayList<MapPoint> GeoMap = new ArrayList<>();

    /**
     * Normal action of Hospital
     * Heal the patient.
     * This action takes 10 ticks
     * @param elapsedTime
     */
    @Override
    public void normalAction(int elapsedTime) {
        if(this.getStatus() == Status.OPERATING){
            RescueAction rA = (RescueAction) this.getCurrentAction();
            rA.treatAction(elapsedTime);
            if(rA.getRemainTime() <= 0){ // Hospital treats all the patient
                this.setStatus(Status.IDLE);
                this.setCurrentAction(null);
            }
        }
    }

    /**
     * Immediate action of Hospital
     * Hospital find the patients who has limited death time. (< 50)
     * Update the geoMap
     * @return
     */
    @Override
    public BaseAction immediateAction() {
        if(this.getStatus() == Status.SELECTION) {

            updatePatientInfo(); // Handle 되고 있지 않은 환자 전체 10씩 생존 시간 감소

            for (MapPoint eachMap : Hospital.GeoMap) {
                ArrayList<RescueAction> aList = eachMap.getCurActions();
                for(RescueAction rA : aList){
                   if(rA.getRemainTime() < 300 && rA.getRemainTime() > 0){
                       // 공식적으로 HP가 50% 이하로 떨어진 것.
                       rA.addBenefit(10);
//                       System.out.println("ACK!");
                   }
                }
            }
            RescueAction healAction = new RescueAction(50, 10);
            healAction.setName("HealAction");
            healAction.addPerformer(this);
            healAction.setStatus(BaseAction.Status.HANDLED);
            this.setCurrentAction(healAction);
            this.setStatus(Status.OPERATING);
            return healAction;
        }else{
            return null;
        }
    }

    @Override
    public BaseConstituent clone() {
        return null;
    }

    /**
     * This method is working on map update
     * @param updatedActions
     */
    @Override
    public void updateCapability(ArrayList<BaseAction> updatedActions){
        for(BaseAction _a : updatedActions){
            try {
                RescueAction rA = (RescueAction) _a;
                if(rA.getPatientStatus() == null)
                    continue;
                int raisedLoc = rA.getRaisedLoc();
                MapPoint m = Hospital.GeoMap.get(raisedLoc);
                m.addCurAction(rA);
            }catch(ClassCastException e){
                continue;
            }
        }
    }

    @Deprecated
    @Override
    public void addSoSLevelBenefit(int SoSLevelBenefit) {
        // Not used
    }

    private void updatePatientInfo(){
        for(MapPoint map : Hospital.GeoMap){
            for(RescueAction rA : map.getCurActions()){
                if(rA.getStatus() == BaseAction.Status.RAISED && rA.getPatientStatus() != RescueAction.PatientStatus.Dead){
                    rA.treatAction(10);
//                    if(rA.getRemainTime() == 0)
//                        System.out.println("One patient is dead");
                }
            }
        }
    }

    @Override
    public void reset(){
        super.reset();
        Hospital.clearMap();
    }

    public static void clearMap(){
        Hospital.GeoMap.clear();
    }
    @Override
    public DebugProperty getDebugProperty(){
        return null;
    }

    @Override
    public String getName() {
        return "HOSPITAL";
    }
}
