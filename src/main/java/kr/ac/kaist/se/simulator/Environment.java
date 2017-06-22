package kr.ac.kaist.se.simulator;

import kr.ac.kaist.se.simulator.method.DummyAction;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Simulator for System of Systems
 * World Class
 * Raise actions that are handled by CSs
 * Send a message to CSs that the action is raised
 */
public final class Environment {

    /**
     * 필요한 자료구조
     * 1. 각 CS의 capability list
     * 2. 현재 각 Action 의 진행 상황
     */
    private ArrayList<BaseConstituent> csList = null; // 모든 CS list
    private ArrayList<BaseAction> actionList = null; // 모든 Action List

    private boolean isAlreadyGenerated; // action이 매번 발생하는가? random하게 생성되는가?
    private ArrayList<BaseAction> actionTemplate; // 랜덤하게 생성할 action 템플릿

    public Environment(BaseConstituent[] CSs, BaseAction[] actions){
        this.csList = new ArrayList<>();
        Collections.addAll(this.csList, CSs);
        this.actionList = new ArrayList<>();
        Collections.addAll(this.actionList, actions);
        this.isAlreadyGenerated = false;
    }

    /**
     * 랜덤하게 Action 생성하는 메소드
     * 발생된 메소드를 각 CS에게 메시지로 전달하는 메소드
     * CS에 의해 처리된 메소드를 제거하는 메소드
     */

    /**
     * Randomly generate actions that are not raised
     * Raise the action whose status is exactly not_raised
     * If the number of actions to be raised is 0, then no action is raised.
     */
    public void generateAction(){
        /*
         * 1. Count the number of possible actions to be raised
         * 2. Generate a random number that how many actions will be raised
         * 3. Shuffle the possible action list and pick actions
         */
        if(!this.isAlreadyGenerated) {
            // Step 1
            ArrayList<String> possibleActionList = new ArrayList<>();
            for (BaseAction a : this.actionList) {
                if (a.getStatus() == BaseAction.Status.NOT_RAISED)
                    possibleActionList.add(a.getName());
            }

            // Step 2 Randomly generate option

//        int numRaisingActions = 0;
//        if(possibleActionList.size() > 0){
//            Random randomGenerator = new Random();
//            numRaisingActions = randomGenerator.nextInt(possibleActionList.size()+1);
//                if(numRaisingActions > 0){
//                    ArrayList<BaseAction> selectedActionList = new ArrayList<BaseAction>();
//                    Collections.shuffle(possibleActionList);
//
//                    for(int i = 0; i < numRaisingActions; i++){
//                        String actionName = possibleActionList.get(i);
//                        for(BaseAction a : this.actionList){
//                            if(a.getName().equalsIgnoreCase(actionName)){
//                                a.setStatus(BaseAction.Status.RAISED);
//                                selectedActionList.add(a);
//                                break;
//                            }
//                        }
//                    }
//
//                updateActionStatus(selectedActionList);
//            }
//        }

            ArrayList<BaseAction> selectedActionList = new ArrayList<>();
            for (String targetName : possibleActionList) {
                for (BaseAction a : this.actionList) {
                    if (a.getName().equalsIgnoreCase(targetName)) {
                        a.setStatus(BaseAction.Status.RAISED);
                        selectedActionList.add(a);
                        break;
                    }
                }
            }
            updateActionStatus(selectedActionList);

        }else{// Randomly generating the actions
            // 언제 발생할지는 알려져 있지만 어떻게 발생시킬지는 랜덤하게.
            BaseAction nA = this.actionTemplate.remove(0); // Always same as the number of to be raised actions and the size of action template
            nA.randomGenerate();
            nA.setStatus(BaseAction.Status.RAISED);
            updateActionStatus(nA);
        }
    }

    /**
     * Notify randomly generated actions to CSs
     * Send a message that the actions are raised
     * The CS which got the message will modify their available action list
     */
    public void notifyCS(){
        for(BaseConstituent cs : this.csList){
            cs.updateCapability(this.actionList);
        }
    }

    public void notifyManager(BaseConstituent manager){
        manager.updateCapability(this.actionList);
    }


    /**
     * Update the status of actions executed by CSs
     * @param actionList the list of actions that are changed its status
     */
    public void updateActionStatus(ArrayList<BaseAction> actionList){
        for(BaseAction a : actionList){
            if( a instanceof DummyAction )
                continue;
            String targetName = a.getName();
            for(int i=0; i<this.actionList.size() ;i++){
                if(this.actionList.get(i).getName().equalsIgnoreCase(targetName)){
                    this.actionList.set(i, a);
                    break;
                }
            }
        }
    }

    /**
     * Update the status of actions executed by CSs
     * @param action a single action that are changed its status
     */
    public void updateActionStatus(BaseAction action){
        for(int i = 0; i<this.actionList.size(); i++){
            if(this.actionList.get(i).getName().equalsIgnoreCase(action.getName())){
                if(action instanceof DummyAction)
                    break;
                this.actionList.set(i, action);
                break;
            }
        }
    }

    public void setPlannedGeneration(){
        this.isAlreadyGenerated = true;
        this.actionTemplate = new ArrayList<>();
        for(int i=0; i<this.actionList.size(); i++){
            this.actionTemplate.add(this.actionList.get(i).clone());
        }
    }

    public void reset(){
        if(!this.isAlreadyGenerated) {
            ;
        }else{
            for(BaseAction a: this.actionList)
                a.reset();
            this.actionTemplate.clear();
        }
    }

    public void setActionList(ArrayList<BaseAction> actions){
        this.actionList = actions;
    }
}
