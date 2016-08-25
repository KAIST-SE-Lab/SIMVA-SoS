package simulator;

import java.util.ArrayList;
import java.util.Collections;

public class SoSManager extends Constituent {

    private int SoSLevelBenefit;
    private ArrayList<Constituent> csList;
    private ArrayList<Action> actionList;
    private Action pickedAction;

    public SoSManager(String name, Constituent[] csList, Action[] actions) {
        super(name);
        this.csList = new ArrayList<Constituent>();
        this.actionList = new ArrayList<Action>();
        Collections.addAll(this.csList, csList);
        Collections.addAll(this.actionList, actions);
        this.SoSLevelBenefit = 0;
        this.pickedAction = null;
    }

    public void normalAction(int elapsedTime){
        /*
         * Acknowledge to the CSs that SoS manager raises the benefit of one action
         * Applying additional benefit action and notify CSs that the action is changed
         * If the action that is to be acknowledged is already chosen by a CS,
         * then skip the ack.
         */
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
        }
    }

    public int getSoSLevelBenefit(){
        return this.SoSLevelBenefit;
    }
}
