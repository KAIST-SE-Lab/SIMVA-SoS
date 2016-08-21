package simulator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

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

    private ArrayList<Constituent> csList = null;
    private ArrayList<Action> actionList = null;
    private HashMap<String, Action.Status> statusHashMap = null;

    public Environment(Constituent[] CSs, Action[] actions){
        this.csList = new ArrayList<Constituent>();
        Collections.addAll(this.csList, CSs);
        this.actionList = new ArrayList<Action>();
        Collections.addAll(this.actionList, actions);
        this.statusHashMap = new HashMap<String, Action.Status>();
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
        int numPossibleActions = 0;
    }

    /**
     * Notify randomly generated actions to CSs
     */
    public void notifyCS(){

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
