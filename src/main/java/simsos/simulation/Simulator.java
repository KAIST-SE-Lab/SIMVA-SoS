package simsos.simulation;

import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.Snapshot;
import simsos.simulation.component.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * Created by mgjin on 2017-06-21.
 */
public class Simulator {
    public static ArrayList<Snapshot> execute(World world, int endOfTime) {
        ArrayList<Snapshot> simulationLog = new ArrayList<Snapshot>();

        boolean stoppingCondition = false;
        ArrayList<Action> actions = new ArrayList();
        ArrayList<Action> immediateActions = new ArrayList();

        world.reset();
        simulationLog.add(world.getCurrentSnapshot()); // Initial snapshot

        while (!stoppingCondition) {
            System.out.println("World Time: " + world.getTime());

            actions.clear();
            do {
                immediateActions.clear();
                for (Agent agent : world.getAgents()) {
                    Action action = agent.step();

                    if (action.isImmediate()) {
                        immediateActions.add(action);
                    } else {
                        actions.add(action);
                    }
                }

                Collections.shuffle(immediateActions);
                progress(immediateActions);
            } while (immediateActions.size() > 0);

            ArrayList<Action> exoActions = world.generateExogenousActions();
            actions.addAll(exoActions);
            actions = new ArrayList<Action>(new LinkedHashSet<Action>(actions)); // Remove duplicates

            Collections.shuffle(actions);
            progress(actions);
            world.progress(1);
            simulationLog.add(world.getCurrentSnapshot());
            // Verdict - evaluateProperties();
            if (world.getTime() >= endOfTime)
                stoppingCondition = true;
        }

        return simulationLog;
    }

    private static void progress(ArrayList<Action> actions) {
        for (Action action : actions) {
            action.execute();
        }
    }
}
