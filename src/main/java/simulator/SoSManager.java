package simulator;

import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.ManagerInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SoSManager extends BaseConstituent implements ManagerInterface{

    private ArrayList<BaseConstituent> csList;
    private ArrayList<Action> actionList;
    private Action pickedAction;
    private Random generator;

    private String name;
    private int SoSLevelBenefit;

    public SoSManager(String name, Constituent[] csList, Action[] actions) {
        this.name = name;
        this.csList = new ArrayList<BaseConstituent>();
        this.actionList = new ArrayList<Action>();

        Collections.addAll(this.csList, csList);
        Collections.addAll(this.actionList, actions);

        this.SoSLevelBenefit = 0;

        this.pickedAction = null;
        this.generator = new Random();

        this.setType(Type.SoSManager);
        this.setStatus(Status.IDLE);
    }

    public void normalAction(int elapsedTime){
        /*
         * Acknowledge to the CSs that SoS manager raises the benefit of one action
         * Applying additional benefit action and notify CSs that the action is changed
         * If the action that is to be acknowledged is already chosen by a CS,
         * then skip the ack.
         */
        Action currentAction = this.getCurrentAction();
        if(currentAction == null)
            return;
        currentAction.decreaseRemainingTime(elapsedTime);
        if(currentAction.getRemainingTime() == 0){
            this.resetCurrentAction();
            this.setStatus(Status.IDLE);
//            System.out.print(this + " finished Acknowledgement at ");
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
        Action bestAction = new Action("Dummy", 0, 0); // Best SoS-benefit action
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

        // For next normal action
        int duration = 2;
        duration += this.generator.nextInt(2); // Duration is 2-3
        Action a = new Action("Search for acknowledgement", 0, 0);
        a.setDuration(duration);
        a.setPerformer(this);
        a.setActionType(Action.TYPE.NORMAL);
        a.startHandle();
        this.setCurrentAction(a);

        this.setStatus(Status.OPERATING);
    }

    private void acknowledge(int additionalBenefit){
        for(BaseConstituent cs : this.csList){
            ArrayList<Action> actionList = cs.getCapability();
            String targetActionName = this.pickedAction.getName();
            for(Action a : actionList){
                if(a.getName().equalsIgnoreCase(targetActionName)){
                    a.addBenefit(additionalBenefit);
                }
            }
            cs.updateCapability(actionList);
        }
    }

    public int getSoSLevelBenefit(){
        return this.SoSLevelBenefit;
    }

    public void addSoSLevelBenefit(int SoSLevelBenefit){
        this.SoSLevelBenefit += SoSLevelBenefit;
    }

    public String toString(){
        return this.name;
    }

    public SoSManager clone(){
        // TO be implemented
//        SoSManager newManager = new SoSManager(this.name, csList, actionList);
        return null;
    }

    public void reset(){

    }
}
