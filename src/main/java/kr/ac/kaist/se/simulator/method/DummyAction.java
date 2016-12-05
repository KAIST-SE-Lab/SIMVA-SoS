package kr.ac.kaist.se.simulator.method;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.DebugProperty;

/**
 * DummyAction.java

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

public class DummyAction extends BaseAction {

    private int additionalBenefit;

    public DummyAction(String name, int benefit, int SoSBenefit){
        this.additionalBenefit = 0;
        this.setBenefit(benefit);
        this.setSoSBenefit(SoSBenefit);
        this.setDuration(0);
        this.setRemainTime(-1); // not_raised
        this.setStatus(BaseAction.Status.NOT_RAISED);
    }

    public String toString(){
        return this.getName();
    }

    public void addBenefit(int additionalBenefit){
        this.additionalBenefit = additionalBenefit;
//        this.setBenefit(this.getBenefit() + additionalBenefit);
    }
    public void resetAction(){
        super.resetAction();
        this.additionalBenefit = 0;
    }

    public int getBenefit(){
        return super.getBenefit() + this.additionalBenefit;
    }

    public DummyAction clone(){
        DummyAction copyAction = new DummyAction(this.getName(), this.getBenefit(), this.getSoSBenefit());
        return copyAction;
    }

    public void reset(){
        this.resetAction();
    }

    @Override
    public void randomGenerate() {
        // Do nothing
    }

    @Override
    public String getDebugTrace() {
        return "";
    }

    @Override
    public DebugProperty getDebugProperty() {
        return null;
    }


}
