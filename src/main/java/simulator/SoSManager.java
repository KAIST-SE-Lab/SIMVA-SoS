package simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SoSManager extends Constituent {

    private int SoSLevelBenefit;
    private ArrayList<Constituent> csList;
    private ArrayList<Action> actionList;
    private Status status;
    private Action pickedAction;
    private Action currentAction;
    private Random generator;

    public SoSManager(String name, Constituent[] csList, Action[] actions) {
        super(name);
        this.csList = new ArrayList<Constituent>();
        this.actionList = new ArrayList<Action>();
        Collections.addAll(this.csList, csList);
        Collections.addAll(this.actionList, actions);
        this.SoSLevelBenefit = 0;
        this.pickedAction = null;
        this.currentAction = null;
        this.generator = new Random();
    }

    /**
     * step method of SoS Manager
     * The manager chooses the action that best SoS_level benefit action
     * @return the choice action to send an acknowledge, or the acknowledgement action
     */
    public Action step(){
        if(this.getRemainBudget() == 0){ // No acknowledgement budget left
            return null;
        }else{
            if(this.status == Status.IDLE){ // Searching
                this.status = Status.SELECTION;
                Action a = new Action("Acknowledgement", 0, 0, 0);
                a.setPerformer(this);
                return a;
            }else if(this.status == Status.OPERATING){
                int duration = 2;
                duration += this.generator.nextInt(2); // Duration is 2-3
                Action a = new Action("Search for acknowledgement", 0, 0, duration);
                a.setPerformer(this);
                a.startHandle();
                this.currentAction = a;
                return a;
            }
        }

        return null;
    }

    public void normalAction(int elapsedTime){
        /*
         * Acknowledge to the CSs that SoS manager raises the benefit of one action
         * Applying additional benefit action and notify CSs that the action is changed
         * If the action that is to be acknowledged is already chosen by a CS,
         * then skip the ack.
         */
        if(currentAction == null)
            return;
        currentAction.decreaseRemainingTime(elapsedTime);
        if(currentAction.getRemainingTime() == 0){
            this.currentAction = null;
            this.status = Status.IDLE;
        }
    }

    public void immediateAction(){
        /*
         * Check the SoS-level benefit of the raised actions
         * Pick the best SoS-level benefit action
         * Acknowledge that action to give more benefit
         * Additional benefit will be 1 or 2
         * If the number of raised action is only one, there is no need to acknowledgement
         */
        Action bestAction = new Action("Dummy", 0, 0, 0); // Best SoS-benefit action
        int numRaisedActions = 0;
        for(Action a : this.actionList){ // Pick the best SoS-level action
            if(a.getStatus() == Action.Status.RAISED){
                numRaisedActions++;
                if(a.getSoSBenefit() > bestAction.getSoSBenefit()){
                    bestAction = a;
                }
            }
        }
        if(numRaisedActions > 1){ // Only this case is worthwhile
            this.pickedAction = bestAction;
            this.acknowledge(this.generator.nextInt(1) + 1);
            this.pickedAction = null;
        }
    }

    private void acknowledge(int additionalBenefit){
        for(Constituent cs : this.csList){
            ArrayList<Action> actionList = cs.getCapability();
            String targetActionName = this.pickedAction.getName();
            for(Action a : actionList){
                if(a.getName().equalsIgnoreCase(targetActionName)){
                    a.addBenefit(additionalBenefit);
                }
            }
            cs.updateActionList(actionList);
        }
    }

    public int getSoSLevelBenefit(){
        return this.SoSLevelBenefit;
    }

    public void addSoSLevelBenefit(int SoSLevelBenefit){
        this.SoSLevelBenefit += SoSLevelBenefit;
    }
}
