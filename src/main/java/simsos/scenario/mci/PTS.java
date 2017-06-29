package simsos.scenario.mci;

import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.World;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by mgjin on 2017-06-28.
 */
public class PTS extends Agent {
    private enum Status {Waiting, OnRescue}

    private String name;
    private Status status;
    private Location location;

    public PTS(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Override
    public Action step() {
        if (this.status == Status.Waiting)
            return Action.getNullAction(1, "Waiting");
        else // if (this.status == Status.OnRescue)
            return Action.getNullAction(1, "Move");
    }

    @Override
    public void reset() {
        Random rd = new Random();

        this.status = Status.Waiting;
        this.location = new Location(rd.nextInt(9), rd.nextInt(9));
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public HashMap<String, Object> getProperties() {
        return new HashMap<String, Object>();
    }
}
