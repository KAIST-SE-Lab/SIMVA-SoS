package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * System of Systems Simulator
 * Created by Junho on 2016-08-01.
 * Output model : System.out.println(Integer.toString(tick) + " "
 * + a.getPerformer() + " " + a + " " + cs.getAccumulatedBenefit());
 */
public class Simulator {

    private ArrayList<Constituent> csList = null;
    private SoSManager manager = null;
    private Environment env = null;
    private int tick;
    private int minimumActionCost;

    public Simulator(Constituent[] CSs, SoSManager manager, Environment env){
        this.csList = new ArrayList<Constituent>();
        this.csList.addAll(Arrays.asList(CSs));
        this.manager = manager;
        this.env = env;
        this.tick = 0;

        this.minimumActionCost = Integer.MAX_VALUE - 100000;
        for(Constituent CS: this.csList){
            for(Action a : CS.getCapability()){
                if(CS.getCost(a) < minimumActionCost)
                    minimumActionCost = CS.getCost(a);
            }
        }
    }

    public void execute(){
        this.procedure();
    }

    /**
     * Simulation Procedure function from "Simulation and SMC of Logic-Based MAS Models"
     */
    private void procedure(){
        boolean endCondition = false;
        ArrayList<Action> immediateActions = new ArrayList<Action>();
        ArrayList<Action> actions = new ArrayList<Action>();

        while(!endCondition){
            actions.clear();
            // Check whether all CS has a job
            boolean verdict = true;
            while(verdict){
                immediateActions.clear();

                for(Constituent cs : this.csList){ // Get immediate candidate action
                    Action a = cs.step();
                    if(a == null)
                        continue;
                    if(a.getDuration() == 0){ // This action is immediate
                        // Immediate action: making a decision
                        // insert to immediateAction set
                        immediateActions.add(a);
                    }else {
                        // insert to normal action set
                        // Normal actions
                        actions.add(a);
                    }
                }

                Collections.shuffle(immediateActions);
                this.progress(immediateActions, Action.TYPE.IMMEDIATE); // Choose

                verdict = false;
                for(Constituent _cs: this.csList){
                    if(_cs.getStatus() == Constituent.Status.IDLE)
                        verdict = true;
                }
            }

            this.generateExogenousActions(); // Environment action
            Collections.shuffle(actions);
            this.progress(actions, Action.TYPE.NORMAL);

            endCondition = this.evaluateProperties();
        }
        System.out.println("Done Tick" + this.tick);
    }

    private void increaseTick(int minimumElapsedTime){
        if(minimumElapsedTime > 0)
            this.tick += minimumElapsedTime;
    }

    private boolean evaluateProperties(){
        /*
         * 1. Check whether is world ended?
         * 1-1. If all CS has the cost which is less than the minimum cost of actions to execute
         *
         */
        boolean verdict = true;
        for(Constituent CS: this.csList){
            if(CS.getRemainBudget() >= this.minimumActionCost)
                verdict = false;
        }
        return verdict;
    }

    private void generateExogenousActions(){
        /*
         * Generate randomly out action
         * Notify to CS that environment generate the actions
         */
        env.generateAction();
        env.notifyCS();

    }

    private void progress(ArrayList<Action> actionList, Action.TYPE type){
        if(!actionList.isEmpty()){
            if(type == Action.TYPE.IMMEDIATE){
                for(Action a : actionList){
                    if(a.getName().equalsIgnoreCase("Action select")) {
                        a.getPerformer().immediateAction(); // Select action
                    }
                }
            }else if(type == Action.TYPE.NORMAL){
                /*
                 * 1. Calculate the minimum time to elapse among action list
                 * 2. Elapse the time and execute
                 * 3. If the remaining time is 0, then update Cost & Benefit
                 */
                int minimumElapsedTime = -1;
                for(Action a : actionList){
                    if(minimumElapsedTime < a.getRemainingTime())
                        minimumElapsedTime = a.getRemainingTime();
                }
                for(Action a : actionList){
                    a.getPerformer().normalAction(minimumElapsedTime);
                }
                increaseTick(minimumElapsedTime);
                System.out.println("Minimum time: " + minimumElapsedTime);
            }
        }
        actionList.clear();
    }
}