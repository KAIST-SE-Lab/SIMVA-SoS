package simvasos.scenario.robot;

import simvasos.simulation.component.Action;
import simvasos.simulation.component.Agent;
import simvasos.simulation.component.World;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Random;

/**
 * Created by mgjin on 2017-06-21.
 */
public class Robot extends Agent{

    private String name;

    public int xpos;
    public boolean token;
    private boolean immediateStep;
    private Action move;

    public Robot(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Override
    public Action step() {
        Action next = this.move;

        if (this.immediateStep) {
            if (xpos == 10) { // Immediate action 1
                next = new Action(0) {

                    @Override
                    public void execute() {
                        token = true;
                    }

                    @Override
                    public String getName() {
                        return "Grab";
                    }
                };
                this.immediateStep = false;
            } else if (xpos > 10) { // Immediate action 2
                int drop = new Random().nextInt(100);
                if (drop == 0) {
                    next = new Action(0) {

                        @Override
                        public void execute() {
                            token = false;
                        }

                        @Override
                        public String getName() {
                            return "Drop";
                        }
                    };
                    this.immediateStep = false;
                } else {
                    // Normal Step
                }
            }
        } else {
            this.immediateStep = true;
        }

        return next;
    }

    @Override
    public void reset() {
        this.xpos = 10;
        this.token = true;
        this.immediateStep = true;
        this.move = new Action(1) {

            @Override
            public void execute() {
                xpos++;
            }

            @Override
            public String getName() {
                return "Move Right";
            }
        };
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getSymbol() {
        return "R";
    }

    @Override
    public HashMap<String, Object> getProperties() {
        LinkedHashMap<String, Object> agentProperties = new LinkedHashMap<String, Object>();
        agentProperties.put("xpos", xpos);
        agentProperties.put("token", token);

        return agentProperties;
    }
}
