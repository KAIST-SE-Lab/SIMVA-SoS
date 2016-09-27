package kr.ac.kaist.se.simulator;

import simulator.Action;
import simulator.Constituent;

import java.util.*;

/**
 * Simulator for System of Systems
 * Environment Class
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

    public Environment(BaseConstituent[] CSs, BaseAction[] actions){
        this.csList = new ArrayList<BaseConstituent>();
        Collections.addAll(this.csList, CSs);
        this.actionList = new ArrayList<BaseAction>();
        Collections.addAll(this.actionList, actions);
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
    public int generateAction(){
        /*
         * 1. Count the number of possible actions to be raised
         * 2. Generate a random number that how many actions will be raised
         * 3. Shuffle the possible action list and pick actions
         */

        // Step 1
        ArrayList<String> possibleActionList = new ArrayList<String>();
        for(BaseAction a : this.actionList){
            if(a.getStatus() == BaseAction.Status.NOT_RAISED)
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

        ArrayList<BaseAction> selectedActionList = new ArrayList<BaseAction>();
        for(String targetName : possibleActionList){
            for(BaseAction a : this.actionList){
                if(a.getName().equalsIgnoreCase(targetName)){
                    a.setStatus(BaseAction.Status.RAISED);
                    selectedActionList.add(a);
                    break;
                }
            }
        }
        updateActionStatus(selectedActionList);

        return selectedActionList.size();
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

    /**
     * Update the status of actions executed by CSs
     * @param actionList the list of actions that are changed its status
     */
    public void updateActionStatus(ArrayList<BaseAction> actionList){
        for(BaseAction a : actionList){
            String targetName = a.getName();
            for(int i=0; i<this.actionList.size() ;i++){
                if(this.actionList.get(i).getName().equalsIgnoreCase(targetName)){
                    this.actionList.set(i, a);
                    break;
                }
            }
        }
    }
}