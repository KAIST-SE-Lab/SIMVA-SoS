package simulator;

import java.util.ArrayList;

public class Constituent {

    private ArrayList<Action> actionList = null;
    private String name;
    private int usedCost;
    private int totalBudget;
    private int accumulatedBenefit;

    public Constituent(String name){
        this.name = name;
        this.actionList = new ArrayList<Action>();
        this.usedCost = 0;
        this.totalBudget = 100;
        this.accumulatedBenefit = 0;
    }

    /**
     * step method
     * CS chooses an action according to probability distribution.
     * Before selecting, the CS checks the acknowledgement from SoS manager.
     * @return chosen Action instance
     */
    public Action step(){
        if(this.getRemainBudget() == 0){
            Action a = new Action("void", 0, 0, 0);
            a.updatePerformer(this);
            return a; // voidAction, nothing happen
        }else{
            // Need to updated, currently select best utility action
            ArrayList<Action> tempList = new ArrayList<Action>(this.actionList.size());
            tempList.addAll(this.actionList);
            for(Action a : this.actionList){
                if(a.getCost() > this.getRemainBudget()){
                    tempList.remove(a);
                }
            }
            if(tempList.size() > 0)
                return selectAction(tempList);
            else{
                Action a = new Action("void", 0, 0, 0);
                a.updatePerformer(this);
                return a;
            }
        }
    }

    public Action selectAction(ArrayList<Action> actions){
        Action retAction = actions.get(0);
        for(Action a : actions){
            if(getUtility(a) > getUtility(retAction)){
                retAction = a;
            }
        }
        return retAction;
    }

    public int getRemainBudget(){
        return this.totalBudget - this.usedCost;
    }

    public void updateCostBenefit(int cost, int benefit){
        this.usedCost += cost;
        this.accumulatedBenefit += benefit;
    }

    public int getUtility(Action a){
        return a.getBenefit() - a.getCost();
    }

    public String toString(){
        return this.name;
    }

    public int getAccumulatedBenefit(){
        return this.accumulatedBenefit;
    }

    public void addAction(Action a){
        this.actionList.add(a);
        a.updatePerformer(this);
    }

}
