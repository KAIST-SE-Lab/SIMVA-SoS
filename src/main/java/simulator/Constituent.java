package simulator;

import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.ConstituentInterface;

import java.util.ArrayList;
import java.util.HashMap;

public class Constituent extends BaseConstituent implements ConstituentInterface{

    private String name;
    private int currentPosition;

    public Constituent(String name, int totalBudget){
        this.name = name;
        this.setType(Type.Constituent);
        this.initBudget(totalBudget);
        // Randomly positioned
        this.currentPosition = (int) (( Math.random() * 12) % 6);
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

        this.updateDurationMap(capabilityList);
        getAvailableActionList(availableActions, capabilityList);

        Action candidateAction = null;
        int bestIndex = chooseBestAction(availableActions);
        if(bestIndex != -1)
            candidateAction = availableActions.get(bestIndex);

        if(bestIndex != -1 && candidateAction != null){
            // Selected action exists
            if(candidateAction.getStatus() == Action.Status.RAISED){ // Lucky!
                candidateAction.setDuration(this.getDurationMap().get(candidateAction.getName()));
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

    private void getAvailableActionList(ArrayList<Action> availableActions, ArrayList<Action> capabilityList){
        for(Action a : capabilityList){
            if(a.getStatus() == Action.Status.RAISED)
                availableActions.add(a);
        }
    }

    private int chooseBestAction(ArrayList<Action> availableActions){
        int bestIndex = -1;
        if(availableActions.size() > 1){
            bestIndex = 0;
            for(int i=1; i<availableActions.size(); i++){
                if(this.getUtility(availableActions.get(bestIndex))
                        < this.getUtility(availableActions.get(i))){
                    bestIndex = i;
                }
            }
        }else if(availableActions.size() == 1){ // No more job left
            bestIndex = 0;
        }
        return bestIndex;
    }

    private void updateDurationMap(ArrayList<Action> capabilityList){
        // Current Position 위치를 결정할 때 duration 이 다시 update 됨..
        // Action 의 raisedLocation 정보를 기초로 duration 산출
        HashMap<String, Integer> newDurationMap = new HashMap<String, Integer>();
        for(Action a : capabilityList){
            if(a.getStatus() == Action.Status.RAISED && a.getActionType() == Action.TYPE.NORMAL){
                String actionName = a.getName();
                int requiredDuration = Math.abs(a.getRaisedLocation() - this.currentPosition) + 1;
                newDurationMap.put(actionName, requiredDuration);
            }
        }
        this.updateDurationMap(newDurationMap);
    }
}
