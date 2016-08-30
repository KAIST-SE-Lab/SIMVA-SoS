package simulator;

import main.kr.ac.kaist.se.simulator.BaseConstituent;
import main.kr.ac.kaist.se.simulator.ConstituentInterface;

import java.util.ArrayList;

public class Constituent extends BaseConstituent implements ConstituentInterface{

    private String name;

    public Constituent(String name, int totalBudget){
        this.name = name;
        this.setType(Type.Constituent);
        this.initBudget(totalBudget);
    }

    /**
     * How to choose an action
     * 1. Get a list of available actions
     * 2. Calculate the utilities to do actions
     * 3. Choose the best action to get the most utility
     * 4. If other CS choose the action first, then this CS return to IDLE
     * 5. If no action remain, then do nothing
     */
    public void immediateAction(){
        if(this.getStatus() == Status.OPERATING) // Defend code
            return;

        ArrayList<Action> availableActions = new ArrayList<Action>();
        ArrayList<Action> capabilityList = this.getCapability();
        for(Action a : capabilityList){
            if(a.getStatus() == Action.Status.RAISED)
                availableActions.add(a);
        }

        int bestIndex = -1;
        Action candidateAction = null;
        if(availableActions.size() > 1){
            bestIndex = 0;
            for(int i=1; i<availableActions.size(); i++){
                if(this.getUtility(availableActions.get(bestIndex))
                        < this.getUtility(availableActions.get(i))){
                    bestIndex = i;
                }
            }
            candidateAction = availableActions.get(bestIndex);
        }else if(availableActions.size() == 1){ // No more job left
            candidateAction = availableActions.get(0);
            bestIndex = 0;
        }

        if(bestIndex != -1 && candidateAction != null){
            // Selected action exists
            if(candidateAction.getStatus() == Action.Status.RAISED){ // Lucky!
                candidateAction.startHandle();
                candidateAction.setPerformer(this);
                this.setStatus(Status.OPERATING);
                this.setCurrentAction(candidateAction);
            }
        }else{ // To select an action again
            this.setStatus(Status.IDLE);
        }
    }

    public void normalAction(int elapsedTime){ // TODO: Need of renaming!
        Action currentAction = this.getCurrentAction();

        if(currentAction == null)
            return;
        currentAction.decreaseRemainingTime(elapsedTime);
        if(currentAction.getRemainingTime() == 0){
            // All actions are always more than and equal to 0
            System.out.print(this.name + " finished " + currentAction.getName() + " at ");
            int cost = this.getCost(currentAction);
            this.updateCostBenefit(cost, currentAction.getBenefit());
            // TODO: 2016-08-23 Add SoS benefit update
            currentAction.resetAction();
            this.resetCurrentAction();
            this.setStatus(Status.IDLE);
            if(this.getRemainBudget() < this.getRequiredMinimumBudget())
                this.setStatus(Status.END);
        }
    }

    public int getUtility(Action a){
        return a.getBenefit() - this.getCost(a);
    }

    public String toString(){
        return this.name;
    }

}
