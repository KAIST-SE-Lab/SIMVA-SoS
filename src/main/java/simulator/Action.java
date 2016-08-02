package simulator;

public class Action {

    private String name;
    private int cost;
    private int benefit;
    private int SoSBenefit;
    private boolean isImmediate;
    private Constituent performer;

    public Action(String name, int cost, int benefit, int SoSBenefit){
        this.name = name;
        this.cost = cost;
        this.benefit = benefit;
        this.SoSBenefit = SoSBenefit;
        this.isImmediate = true;
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

    public boolean isImmediate(){
        return this.isImmediate;
    }

    public String toString(){
        return this.name;
    }
}
