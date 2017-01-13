package kiise2016;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.DebugProperty;
import kr.ac.kaist.se.simulator.ManagerInterface;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class SoS extends BaseConstituent implements ManagerInterface{
    private ArrayList<BaseConstituent> csList;
    private ArrayList<Action> actionList;
    private Action currentAction;
    private Random generator;

    private String name;
    private int SoSLevelBenefit;

    public SoS(String name, Constituent[] csList, Action[] actions) {
        this.name = name;
        this.csList = new ArrayList<BaseConstituent>();
        this.actionList = new ArrayList<Action>();

        Collections.addAll(this.csList, csList);
        Collections.addAll(this.actionList, actions);

        this.SoSLevelBenefit = 0;

        this.currentAction = null;
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

    public BaseAction immediateAction(){
        /*
         * Check the SoS-level benefit of the raised actions
         * Pick the best SoS-level benefit action
         * Acknowledge that action to give more benefit
         * Additional benefit will be 0 or 1 or 2
         * If the number of raised action is only one, there is no need to acknowledgement
         */
        // Actions are always raised
        // Randomly select the acknowledge value (additional benefit)
        // For 0.3 for no additional benefit, 0.4 for 1 additional benefit, 0.3 for 2 additional benefit

        int additionalBenefit = 0;
        int ranNum = this.generator.nextInt(100);
        if( 0 <= ranNum && ranNum <= 29){ //0.3
            additionalBenefit = 0;
        }else if( 30 <= ranNum && ranNum <= 69){ //0.4
            additionalBenefit = 1;
        }else if( 70 <= ranNum && ranNum <= 99){ // 0.3
            additionalBenefit = 2;
        }

        this.acknowledge(additionalBenefit);

        // For next normal action
//        Action a = new Action("Search for acknowledgement", 0, 0);
//        a.setDuration(0);
//        a.addPerformer(this);
//        a.setActionType(Action.TYPE.NORMAL);
//        a.startHandle();
//        this.setCurrentAction(a);

        this.setStatus(Status.IDLE);
//        System.out.print("a");
        return null;
    }

    /**
     * No matter what action always acknowledge to action2 and action3
     * @param additionalBenefit the additional benefit
     */
    private void acknowledge(int additionalBenefit){
        for(BaseConstituent cs : this.csList){
            ArrayList<BaseAction> actionList = cs.getCapability();
            for(BaseAction a : actionList){
                if(a.getName().equalsIgnoreCase("action2")
                        || a.getName().equalsIgnoreCase("action3")){
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

    public SoS clone(){
        // TO be implemented
//        SoSManager newManager = new SoSManager(this.name, csList, actionList);
        return null;
    }

    @Override
    public DebugProperty getDebugProperty() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    public void reset(){
        super.reset();
        this.setStatus(Status.IDLE);
        this.SoSLevelBenefit = 0;
        this.setCurrentAction(null);
    }

    public void setCurrentAction(Action a){
        this.currentAction = a;
    }

    public Action getCurrentAction(){
        return this.currentAction;
    }

    public Action step(){
        if(this.getRemainBudget() == 0){
            return null; // voidAction, nothing happen
        }else{ // We have money
            /*
             * 1. If the status of CS is IDLE (currently no job), then select a job (immediate action)
             * 2. If the status of CS is SELECTION, then
             */
            if(this.getStatus() == Status.IDLE){ // Select an action
                this.setStatus(Status.SELECTION);
                Action a = new Action("[SoS] Immediate action", 0, 0);
                a.addPerformer(this);
                a.setActionType(Action.TYPE.IMMEDIATE);
                return a;
            }
        }

        return null;
    }

}
