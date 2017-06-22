package simsos.simulation.component;

import java.util.ArrayList;

/**
 * Created by mgjin on 2017-06-21.
 */
public class World {
    private ArrayList<Agent> agents = new ArrayList<Agent>();
    private int time;

    public ArrayList<Agent> getAgents() {
        return agents;
    }

    public ArrayList<Action> generateExogenousActions() {
        return new ArrayList<Action>();
    }

    public void addAgent(Agent agent) {
        agents.add(agent);
    }

    public void reset() {
        for (Agent agent : this.agents)
            agent.reset();

        this.time = 0;
    }

    public void progress(int time) {
        this.time += time;
    }

    public int getTime() {
        return this.time;
    }
}
