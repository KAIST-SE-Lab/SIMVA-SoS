package simulator;

public class Action {

    private int cost;
    private int benefit;
    private int SoSBenefit;
    private boolean isImmediate;
    private Constituent performer;

    public Action(int cost, int benefit, int SoSBenefit, Constituent performer){
        this.cost = cost;
        this.benefit = benefit;
        this.SoSBenefit = SoSBenefit;
        this.performer = performer;
        this.isImmediate = true;
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

    public boolean isImmediate(){
        return this.isImmediate;
    }
}
