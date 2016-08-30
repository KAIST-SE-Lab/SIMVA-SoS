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
public class Environment {

    /**
     * 필요한 자료구조
     * 1. 각 CS의 capability list
     * 2. 현재 각 Action 의 진행 상황
     */
    private ArrayList<Constituent> csList = null; // 모든 CS list
    private ArrayList<Action> actionList = null; // 모든 Action List
    private HashMap<String, Action.Status> statusHashMap = null;

    public Environment(Constituent[] CSs, Action[] actions){
        this.csList = new ArrayList<Constituent>();
        Collections.addAll(this.csList, CSs);
        this.actionList = new ArrayList<Action>();
        Collections.addAll(this.actionList, actions);
        this.statusHashMap = new HashMap<String, Action.Status>();

        for(Action a : this.actionList){
            this.statusHashMap.put(a.getName(), a.getStatus());
        }

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
        ArrayList<String> possibleActionList = new ArrayList<String>();
        for(Map.Entry<String, Action.Status> entry: this.statusHashMap.entrySet()){
            if(entry.getValue() == Action.Status.NOT_RAISED){
                possibleActionList.add(entry.getKey());
            }
        }
        int numRaisingActions = 0;
        if(possibleActionList.size() > 0){
            Random randomGenerator = new Random();
            numRaisingActions = randomGenerator.nextInt(possibleActionList.size()+1);
            if(numRaisingActions > 0){
                ArrayList<Action> selectedActionList = new ArrayList<Action>();
                Collections.shuffle(possibleActionList);

                for(int i = 0; i < numRaisingActions; i++){
                    String actionName = possibleActionList.get(i);
                    for(Action a : this.actionList){
                        if(a.getName().equalsIgnoreCase(actionName)){
                            a.setStatus(Action.Status.RAISED);
                            selectedActionList.add(a);
                            break;
                        }
                    }
                }

                updateActionStatus(selectedActionList);
            }
        }
        return numRaisingActions;
    }

    /**
     * Notify randomly generated actions to CSs
     * Send a message that the actions are raised
     * The CS which got the message will modify their available action list
     */
    public void notifyCS(){
        for(Constituent cs : this.csList){
            cs.updateCapability(this.actionList);
        }
    }

    /**
     * Update the status of actions executed by CSs
     * @param actionList the list of actions that are changed its status
     */
    public void updateActionStatus(ArrayList<Action> actionList){
        for(Action a : actionList){
            Action.Status newStatus = a.getStatus();
            this.statusHashMap.remove(a.getName());
            this.statusHashMap.put(a.getName(), newStatus);
        }
    }
}
