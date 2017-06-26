package kr.ac.kaist.se.simulator;

import java.util.*;

/**
 * System of Systems Simulator
 * Created by Junho on 2016-08-01.
 * Output model : System.out.println(Integer.toString(tick) + " "
 * + a.getPerformer() + " " + a + " " + cs.getAccumulatedSoSBenefit());
 */
public final class Simulator {

    private ArrayList<BaseConstituent> csList = null;
    private ArrayList<BaseAction> immediateActions = new ArrayList<>();
    private ArrayList<BaseAction> actions = new ArrayList<>();

    private SIMResult result = null;
    private BaseConstituent manager = null;
    private Environment env = null;

    private int tick;
    private int endTick;

    private ArrayList<Integer> plannedActionTicks; // Raising actions following specific random distribution
    private boolean isPlanned; // Checking the is Planned actions?
    private boolean DEBUG; // DEBUG mode

    private HashMap<Integer, List<String>> debugTraces;
    private HashMap<Integer, DebugTick> debugTraceMap;
    private BaseScenario scenario;

    @Deprecated
    public Simulator(BaseConstituent[] CSs, BaseConstituent manager, Environment env) {
        this.csList = new ArrayList<>();
        this.csList.addAll(Arrays.asList(CSs));
        this.manager = manager;
        this.env = env;

        this.tick = 0;
        this.endTick = 0;
        this.isPlanned = false;
    }

    @Deprecated
    public void setEndTick(int endTick) {
        this.endTick = endTick;
    }

    public Simulator(BaseScenario sc) {
        this.csList = sc.getCSList();
        this.manager = sc.getManager();
        this.env = sc.getEnvironment();

        this.tick = 0;
        this.endTick = sc.getEndTick();
        this.isPlanned = false;
        this.scenario = sc;
    }

    /**
     * simulate the model, reset the environment and states
     */
    public void execute() {
        this.procedure();
    }

    public void reset() {
        this.result = null;
        this.env.reset();
        this.csList.forEach((CS) -> CS.reset());
        if (manager != null)
            manager.reset();
        if (this.DEBUG)
            this.debugTraces.clear();
    }

    public SIMResult getResult() {
        return this.result;
    }

    /**
     * Simulation Procedure function from "Simulation and SMC of Logic-Based MAS Models"
     */
    private void procedure() {
        this.tick = 0;
        boolean endCondition = false;

        while (!endCondition) {
//            System.out.println("World Time: " + this.tick);
            actions.clear();

            immediateActions.clear();

            // World actions first
            this.generateExogenousActions(); // World action

            for (BaseConstituent cs : this.csList) { // Get immediate candidate action
                BaseAction a = cs.step();
                if (a == null)
                    continue;
                if (a.getActionType() == BaseAction.TYPE.IMMEDIATE) { // This action is immediate
                    // Immediate action: making a decision
                    // insert to immediateAction set
//                    System.out.println(cs.getName() + ", Immediate");
                    immediateActions.add(a);
                } else {
                    // insert to normal action set
                    // Normal actions
//                    System.out.println(cs.getName() + ", Normal (Procedure)");
                    actions.add(a);
                }
            }

            if (this.manager != null) {
                BaseAction a = this.manager.step();
                if (a != null) {
                    if (a.getActionType() == BaseAction.TYPE.IMMEDIATE) {
                        immediateActions.add(a);
                    } else
                        actions.add(a);
                }
            }

            Collections.shuffle(immediateActions);
            this.progress(BaseAction.TYPE.IMMEDIATE); // Choose


            Collections.shuffle(actions);
            this.progress(BaseAction.TYPE.NORMAL);

            if (this.DEBUG) {
                DebugTick debugTick = new DebugTick(this.tick);
                for(BaseConstituent cs: this.csList){
                    debugTick.putDebugTrace(cs.getName(), cs.getDebugProperty());
                }
                for (BaseAction a : this.actions) {
                    debugTick.putDebugTrace(a.getName(), a.getDebugProperty());
                }

                DebugProperty prop = new DebugProperty();
                int SoS_benefit = 0;
                for(BaseConstituent cs: this.csList)
                    SoS_benefit += cs.getAccumulatedSoSBenefit();
                prop.putProperty("SoS_level_benefit", SoS_benefit);
                debugTick.putDebugTrace("SoS_level_benefit", prop);

                this.debugTraceMap.put(this.tick, debugTick);
            }

            actions.clear();
            immediateActions.clear();
            endCondition = this.evaluateProperties();
        }
        int SoSBenefit = 0;
        for (BaseConstituent CS : this.csList) {
            SoSBenefit += CS.getAccumulatedSoSBenefit();
        }
        this.result = new SIMResult(this.tick, SoSBenefit);
        if(this.DEBUG) this.result.setDebugTraces(this.debugTraceMap);
    }

    private void increaseTick(int minimumElapsedTime) {
        if (minimumElapsedTime > 0)
            this.tick += minimumElapsedTime;
        if(this.tick > this.endTick)
            this.tick = this.endTick;
    }

    private boolean evaluateProperties() {
        /*
         * 1. Check whether is world ended?
         * 1-1. If all CS has the cost which is less than the minimum cost of actions to execute
         *
         */
        boolean verdict = true;
        for (BaseConstituent CS : this.csList) {
            if (CS.getStatus() != BaseConstituent.Status.END)
                verdict = false;
        }
        if (this.tick >= endTick) {
            verdict = true;
        }
        return verdict;
    }

    private void generateExogenousActions() {
        /*
         * Generate randomly out action
         * Notify to CS that environment generate the actions
         */
        if (this.isPlanned) {
            while (this.plannedActionTicks.size() > 0 && this.tick >= this.plannedActionTicks.get(0)) {
                env.generateAction();
                env.notifyCS();
                if (this.manager != null)
                    env.notifyManager(this.manager);
                this.plannedActionTicks.remove(0);
            }
        } else {
            env.generateAction();
            env.notifyCS();
        }
    }

    private void progress(BaseAction.TYPE type) {
        if (type == BaseAction.TYPE.IMMEDIATE) {
            ArrayList<BaseAction> actionList = this.immediateActions;
            for (BaseAction a : actionList) {
                if (a.getActionType() == BaseAction.TYPE.IMMEDIATE) {
                    BaseAction selectedAction = a.getPerformer().immediateAction(); // Select action
                    if (selectedAction != null)
//                        System.out.println(a.getPerformer().getName() + ", Normal (Progress)");
                        actions.add(selectedAction);
                }
            }
        } else if (type == BaseAction.TYPE.NORMAL) {
            /*
             * 1. Calculate the minimum time to elapse among action list
             * 2. Elapse the time and execute
             * 3. If the remaining time is 0, then update Cost & Benefit
             */
            ArrayList<BaseAction> actionList = this.actions;
            int minimumElapsedTime = -1;
            if (!actionList.isEmpty()) { // If the action list is not empty
                boolean discreteCondition = true;
                minimumElapsedTime = 1;
                for (BaseConstituent CS : this.csList) { // To get all CS are in operation.
                    if (CS.getStatus() != BaseConstituent.Status.OPERATING) {
                        discreteCondition = false;
                        break;
                    }
                }
                if (!this.DEBUG && discreteCondition) { // To jump the tick
                    for (BaseAction a : actionList) {// Calculate the minimum jump tick
                        int rTime = a.getRemainingTime();
                        if (minimumElapsedTime < rTime)
                            minimumElapsedTime = rTime;
                    }
                }
                for (BaseAction a : actionList) { // List로 수정하면 이부분 수정해야함..
//                    a.getPerformer().normalAction(minimumElapsedTime);
                    BaseConstituent[] tmpArr = a.getPerformerList().toArray(new BaseConstituent[a.getPerformerList().size()]);
                    for (int i = 0; i < tmpArr.length; i++) {
                        // Real normal action happens.
                        tmpArr[i].normalAction(minimumElapsedTime);
                    }
                }
            } else
                minimumElapsedTime = 1;

            increaseTick(minimumElapsedTime);
        }
        if (type == BaseAction.TYPE.IMMEDIATE) {
            env.updateActionStatus(immediateActions);
        } else {
            env.updateActionStatus(actions);
        }
    }

    public void setActionPlan(ArrayList<Integer> randomDistribution) {
        this.isPlanned = true;
        this.plannedActionTicks = randomDistribution;
        this.env.setPlannedGeneration();
    }

    public BaseScenario getScenario() {
        return this.scenario;
    }

    public void setDEBUG() {
        this.DEBUG = true;
        this.debugTraces = new HashMap<>();
        this.debugTraceMap = new HashMap<>();
    }

    public HashMap<Integer, DebugTick> getDebugTraces() {
        return this.debugTraceMap;
    }
}