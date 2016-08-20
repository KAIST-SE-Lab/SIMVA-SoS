package simulator;

import java.util.ArrayList;
import java.util.Collections;

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

    public Environment(Constituent[] CSs){
        this.csList = new ArrayList<Constituent>();
        Collections.addAll(this.csList, CSs);
    }

    /**
     * 랜덤하게 Action 생성하는 메소드
     * 발생된 메소드를 각 CS에게 메시지로 전달하는 메소드
     * CS에 의해 처리된 메소드를 제거하는 메소드
     */

    /**
     * Randomly generate actions that are not raised
     */
    public void generateAction(){

    }

    /**
     * Notify randomly generated actions to CSs
     */
    public void notifyCS(){

    }
}
