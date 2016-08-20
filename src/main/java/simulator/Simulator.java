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
//        boolean verdict = false;
        boolean endCondition = false;
        ArrayList<Action> immediateActions = new ArrayList<Action>();
        ArrayList<Action> actions = new ArrayList<Action>();
        while(!endCondition){
            immediateActions.clear();
            actions.clear();
            for(Constituent cs : this.csList){
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
            // shuffle actions
            Collections.shuffle(immediateActions);
            this.progress(immediateActions);
            endCondition = this.generateExogenousActions();
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

    private boolean generateExogenousActions(){
        ArrayList<Constituent> tempList = new ArrayList<Constituent>(this.csList.size());
        tempList.addAll(this.csList);
        for(Constituent cs: this.csList){
            if(cs.getRemainBudget() <= 0){
                tempList.remove(cs);
            }
        }
        return tempList.isEmpty();
    }

    private void progress(ArrayList<Action> actionList){
        if(!actionList.isEmpty()){
            for(Action a : actionList){
                int cost = a.getPerformer().getCost(a);
                if(cost == 0 && a.getBenefit() == 0){
                    continue;
                }
                Constituent cs = a.getPerformer();
                cs.updateCostBenefit(cost, a.getBenefit());
                // Update SoS benefit
                System.out.println(Integer.toString(tick) + " " + a.getPerformer() + " "
                        + a + " " + cs.getAccumulatedBenefit());
            }
        }
    }
}