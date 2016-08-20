package simulator;

import java.util.ArrayList;
import java.util.HashMap;

public class Constituent {

    private ArrayList<Action> actionList = null;
    private ArrayList<Action> capabilityList = null; // 일단 보존
    private HashMap<String, Integer> capabilityMap = null; // 각 CS의 Action 당 사용되는 cost

    private String name;
    private int usedCost;
    private int totalBudget;
    private int accumulatedBenefit;

    public Constituent(String name){
        this.name = name;
        this.actionList = new ArrayList<Action>();
        this.capabilityList = new ArrayList<Action>();
        this.capabilityMap = new HashMap<String, Integer>();
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
            return null; // voidAction, nothing happen
        }else{ // We have money
            ArrayList<Action> tempList = new ArrayList<Action>(this.actionList.size());
            tempList.addAll(this.actionList);
            for(Action a : this.actionList){
                if(this.getCost(a) > this.getRemainBudget()){
                    tempList.remove(a);
                }
            }
            if(tempList.size() > 0)
                return selectAction(tempList);
            else{ // We don't have enough money to execute an action.
                return null;
            }
        }
    }

    private Action selectAction(ArrayList<Action> actions){
        // TODO: 2016-08-02 Select action based on probability distribution of utility
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

    private int getUtility(Action a){
        return a.getBenefit() - this.getCost(a);
    }

    public String toString(){
        return this.name;
    }

    public int getAccumulatedBenefit(){
        return this.accumulatedBenefit;
    }

    public void addCapability(Action a, int cost){
        this.capabilityList.add(a);
        this.capabilityMap.put(a.getName(), cost);
    }

    public ArrayList<Action> getCapability(){
        return this.capabilityList;
    }

    public ArrayList<Action> getActionList(){
        return this.actionList;
    }

    public int getCost(Action a){
        Integer _cost = this.capabilityMap.get(a.getName());
        if(_cost != null)
            return _cost;
        else
            return 0;
    }

}
