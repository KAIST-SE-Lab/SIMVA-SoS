package simulator;

import java.util.ArrayList;
import java.util.HashMap;

public class Constituent {

    public enum Status {IDLE, SELECTION, OPERATING}

    private ArrayList<Action> actionList = null;
    private ArrayList<Action> capabilityList = null; // 일단 보존
    private HashMap<String, Integer> capabilityMap = null; // 각 CS의 Action 당 사용되는 cost <Action_name, cost>

    private String name;
    private int usedCost;
    private int totalBudget;
    private int accumulatedBenefit;
    private Status status;

    public Constituent(String name){
        this.name = name;
        this.actionList = new ArrayList<Action>();
        this.capabilityList = new ArrayList<Action>();
        this.capabilityMap = new HashMap<String, Integer>();
        this.usedCost = 0;
        this.totalBudget = 100;
        this.accumulatedBenefit = 0;
        this.status = Status.IDLE;
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
            /*
             * 1. If the status of CS is IDLE (currently no job), then select a job (immediate action)
             * 2. If the status of CS is SELECTION, then
             */
            if(this.status == Status.IDLE){ // Select an action
                /* How to choose an action
                 * 1. Get a list of available actions
                 * 2. Calculate the utilities to do actions
                 * 3. Choose the best action to get the most utility
                 */
                ArrayList<Action> availableActions = new ArrayList<Action>();
                for(Action a : this.actionList){
                    if(a.getStatus() == Action.Status.RAISED)
                        availableActions.add(a);
                }

                int bestIndex = -1;
                if(availableActions.size() > 1){
                    bestIndex = 0;
                    for(int i=1; i<availableActions.size(); i++){
                        if(this.getUtility(availableActions.get(bestIndex))
                                < this.getUtility(availableActions.get(i))){
                            bestIndex = i;
                        }
                    }

                }else if(availableActions.size() == 0){

                }else{
                    bestIndex = 0;
                }
            }
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

    public void updateActionList(ArrayList<Action> _list){
        ArrayList<Action> newList = new ArrayList<Action>(this.actionList.size());
        for(Action target : _list){
            newList.add(target);
        }
        this.actionList = newList;
    }

    public Status getStatus(){
        return this.status;
    }

}
