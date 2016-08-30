package simulator;

import kr.ac.kaist.se.simulator.BaseAction;

public class Action extends BaseAction{

    private String name;

    public Action(String name, int benefit, int SoSBenefit, int duration){
        this.name = name;
        this.setBenefit(benefit);
        this.setSoSBenefit(SoSBenefit);
        this.setDuration(duration);
        this.setRemainTime(-1); // not_raised
        this.setStatus(BaseAction.Status.NOT_RAISED);
    }

    public String toString(){
        return this.name;
    }

    public String getName(){
        return this.name;
    }

    public void addBenefit(int additionalBenefit){
        this.setBenefit(this.getBenefit() + additionalBenefit);
    }
}
