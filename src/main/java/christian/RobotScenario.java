package christian;

import kr.ac.kaist.se.simulator.BaseAction;
import kr.ac.kaist.se.simulator.BaseConstituent;
import kr.ac.kaist.se.simulator.BaseScenario;
import kr.ac.kaist.se.simulator.Environment;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by mgjin on 2016-12-29.
 */
public class RobotScenario  extends BaseScenario {
    private int endTick;
    private ArrayList<BaseConstituent> csList;
    private BaseConstituent manager;
    private Environment env;
    private ArrayList<BaseAction> actionList;

    public RobotScenario() {
        Movement right_move = new Movement();

        Robot r1 = new Robot(0, right_move);
        Robot r2 = new Robot(1, right_move);
        Robot r3 = new Robot(2, right_move);

        r1.addCapability(right_move, 1);
        r2.addCapability(right_move, 1);
        r3.addCapability(right_move, 1);

        BaseConstituent[] CSs = new BaseConstituent[] {r1, r2, r3};
        BaseAction[] moves = new BaseAction[] {right_move};

        this.csList = new ArrayList<>();
        this.csList.addAll(Arrays.asList(CSs));
        this.manager = null;

        this.actionList = new ArrayList<>();
        this.actionList.addAll(Arrays.asList(moves));

        this.env = new Environment(CSs, this.actionList.toArray(new BaseAction[this.actionList.size()]));

        this.endTick = 12;
    }

    @Override
    public void init() {
    }

    @Override
    public String getDescription() {
        return "Robot";
    }

    @Override
    public ArrayList<BaseConstituent> getCSList() {
        return this.csList;
    }

    @Override
    public BaseConstituent getManager() {
        return this.manager;
    }

    @Override
    public void setCSList(BaseConstituent[] CSs) {
        if(this.csList != null)
            this.csList.clear();
        else
            this.csList = new ArrayList<>();
        this.csList.addAll(Arrays.asList(CSs));
    }

    @Override
    public void setManager(BaseConstituent manager) {
        this.manager = manager;
    }

    @Override
    public void setEnvironment(Environment env) {
        this.env = env;
    }

    @Override
    public Environment getEnvironment() {
        return env;
    }

    @Override
    public ArrayList<BaseAction> getActionList() {
        return this.actionList;
    }

    @Override
    public void setActionList(ArrayList<BaseAction> aList) {
        this.actionList = aList;
    }

    @Override
    public int getEndTick() {
        return endTick;
    }

    @Override
    public void setEndTick(int endTick) {
        this.endTick = endTick;
    }
}