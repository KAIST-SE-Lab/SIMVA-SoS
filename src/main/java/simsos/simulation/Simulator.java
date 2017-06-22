package simsos.simulation;

import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;

/**
 * Created by mgjin on 2017-06-21.
 */
public class Simulator {
    public static void execute(World world, int endOfTime) {
        boolean stoppingCondition = false;
        ArrayList<Action> actions = new ArrayList();
        ArrayList<Action> immediateActions = new ArrayList();

        world.reset();

        while (!stoppingCondition) {
//            System.out.println("World Time: " + world.getTime());

            actions.clear();
            do {
                immediateActions.clear();
                for (Agent agent : world.getAgents()) {
                    Action action = agent.step();
//                    System.out.println(agent.getName() + ", " + action.getName());

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
            // Verdict - evaluateProperties();
            if (world.getTime() >= endOfTime)
                stoppingCondition = true;
        }
    }

    private static void progress(ArrayList<Action> actions) {
        for (Action action : actions) {
            action.execute();
        }
    }
}
