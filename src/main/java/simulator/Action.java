package simulator;

import java.util.ArrayList;

public class Action {

    public static ArrayList<Constituent> ownerList = new ArrayList<Constituent>();

    public enum Status {NOT_RAISED, RAISED, HANDLED}

    private String name;

    private int benefit;
    private int SoSBenefit;
    private int duration;
    private Status status;

    private Constituent performer = null; // Current performer

    public Action(String name, int benefit, int SoSBenefit){
        this.name = name;
        this.benefit = benefit;
        this.SoSBenefit = SoSBenefit;
        this.duration = 2;
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
            Action.ownerList.add(this.performer);
        }
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
