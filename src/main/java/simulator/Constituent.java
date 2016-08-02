package simulator;

import java.util.ArrayList;

public class Constituent {

    private ArrayList<Action> actionList = null;
    private int usedCost;
    private int totalBudget;
    private int cumulatedBenefit;

    public Constituent(){
        this.actionList = new ArrayList<Action>();
        this.usedCost = 0;
        this.totalBudget = 0;
        this.cumulatedBenefit = 0;
    }

    public Action step(){
        return new Action(0, 0, 0, this);
    }

    public int getRemainCost(){
        return this.totalBudget - this.usedCost;
    }

    public void updateCostBenefit(int cost, int benefit){
        this.usedCost += cost;
        this.cumulatedBenefit += benefit;
    }

}
