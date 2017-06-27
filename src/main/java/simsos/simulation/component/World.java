package simsos.simulation.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * Created by mgjin on 2017-06-21.
 */
public abstract class World {
    private ArrayList<Agent> agents = new ArrayList<Agent>();
    private int time = 0;

    public ArrayList<Agent> getAgents() {
        return agents;
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

    public Snapshot getCurrentSnapshot() {
        // Environment - Property - Value
        // Agent1 - Property - Value
        // Agent2 - Property - Value

        Snapshot snapshot = new Snapshot();

        LinkedHashMap<String, Object> worldProperties = new LinkedHashMap<String, Object>();
        worldProperties.put("Time", time);
        snapshot.addProperties(null, worldProperties);

        for (Agent agent : agents)
            snapshot.addProperties(agent, agent.getProperties());

        return snapshot;
    }

    public abstract ArrayList<Action> generateExogenousActions();
}
