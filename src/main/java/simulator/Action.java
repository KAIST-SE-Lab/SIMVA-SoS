package simulator;

import java.util.ArrayList;

public class Action {

    public enum Status {NOT_RAISED, RAISED, HANDLED}
    public enum TYPE {IMMEDIATE, NORMAL}

    private String name;

    private int benefit;
    private int SoSBenefit;
    private int duration;
    private int remainTime;
    private Status status;

    private Constituent performer = null; // Current performer

    public Action(String name, int benefit, int SoSBenefit, int duration){
        this.name = name;
        this.benefit = benefit;
        this.SoSBenefit = SoSBenefit;
        this.duration = duration;
        this.remainTime = -1; // not_raised
        this.status = Status.NOT_RAISED;
    }

    public int getBenefit() {
        return this.benefit;
    }

    public int getSoSBenefit() {
        return this.SoSBenefit;
    }

    public void setPerformer(Constituent performer){
        if(this.performer == null){
            this.performer = performer;
            // Need of checking deleting performer
            // performer 가 항상 중복이 없다라고 가정할 수 있어야함.
        }
    }

    public void startHandle(){
        this.setStatus(Status.HANDLED);
        this.remainTime = duration;
    }

    public int getRemainingTime(){
        return this.remainTime;
    }

    public void decreaseRemainingTime(int elapsedTime){
        if(elapsedTime > this.remainTime)
            return;
        this.remainTime -= elapsedTime;
    }

    public void resetAction(){
        this.status = Status.NOT_RAISED;
        this.remainTime = -1;
        this.performer= null;
    }

    public Constituent getPerformer(){
        return this.performer;
    }

    public String toString(){
        return this.name;
    }

    public int getDuration(){
        return this.duration;
    }

    public String getName(){
        return this.name;
    }

    public Status getStatus(){
        return this.status;
    }

    public void setStatus(Status status){
        this.status = status;
    }
}