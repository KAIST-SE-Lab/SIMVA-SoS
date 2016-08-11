package simulator;

public class Action {

    private String name;
    private int cost;
    private int benefit;
    private int duration;
    private int SoSBenefit;
    private Constituent performer;

    public Action(String name, int cost, int benefit, int SoSBenefit){
        this.name = name;
        this.cost = cost;
        this.benefit = benefit;
        this.SoSBenefit = SoSBenefit;
        this.duration = 2;
    }

    public void updatePerformer(Constituent performer){
        this.performer = performer;
    }

    public int getCost(){
        return this.cost;
    }

    public int getBenefit() {
        return this.benefit;
    }

    public int getSoSBenefit() {
        return this.SoSBenefit;
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
}
