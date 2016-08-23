package simulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

/**
 * System of Systems Simulator
 * Created by Junho on 2016-08-01.
 */
public class Simulator {

    private ArrayList<Constituent> csList = null;
    private SoSManager manager = null;
    private Environment env = null;
    private int tick;

    public Simulator(Constituent[] CSs, SoSManager manager, Environment env){
        this.csList = new ArrayList<Constituent>();
        this.csList.addAll(Arrays.asList(CSs));
        this.manager = manager;
        this.env = env;
        this.tick = 0;
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
                this.progress(immediateActions); // Choose

                verdict = false;
                for(Constituent _cs: this.csList){
                    if(_cs.getStatus() == Constituent.Status.IDLE)
                        verdict = true;
                }
            }

            this.generateExogenousActions(); // Environment action
            Collections.shuffle(actions);
            this.progress(actions);
//            endCondition = this.evaluateProperties();
            incTick();
        }
    }

    private void incTick(){
        this.tick++;
    }

    private boolean evaluateProperties(){
        return true;
    }

    private void generateExogenousActions(){
        /* Currently not used
        ArrayList<Constituent> tempList = new ArrayList<Constituent>(this.csList.size());
        tempList.addAll(this.csList);
        for(Constituent cs: this.csList){
            if(cs.getRemainBudget() <= 0){
                tempList.remove(cs);
            }
        }
        */
        /*
         * Generate randomly out action
         * Notify to CS that environment generate the actions
         */
        env.generateAction();
        env.notifyCS();

    }

    private void progress(ArrayList<Action> actionList){
        if(!actionList.isEmpty()){
            for(Action a : actionList){
                if(a.getName().equalsIgnoreCase("Action select")) {
                    a.getPerformer().immediateAction(); // Select action
                }else{
                    a.getPerformer().normalAction();
                }
                /*
                System.out.println(Integer.toString(tick) + " " + a.getPerformer() + " "
                        + a + " " + cs.getAccumulatedBenefit());
                */
            }
        }
    }
}