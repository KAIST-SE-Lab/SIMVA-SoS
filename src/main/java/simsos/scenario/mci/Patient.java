package simsos.scenario.mci;

import simsos.sa.StatisticalAnalyzer;
import simsos.simulation.component.Action;
import simsos.simulation.component.Agent;
import simsos.simulation.component.World;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by mgjin on 2017-06-28.
 */
public class Patient extends Agent {
    private enum Status {Initial, Waiting, OnPTS, Healing}
    private enum Severity {Delayed, Immediate}

    private String name;
    private Status status;
    private Severity severity;
    private int lifePoint;
    private Location location;

    private Action bleed;

    public Patient(World world, String name) {
        super(world);

        this.name = name;
        this.reset();
    }

    @Deprecated
    public Location getLocation() {
        return this.location;
    }

    @Override
    public Action step() {
        if (this.status == Status.Initial)
            return new Action(1) {

                @Override
                public void execute() {
                    status = Status.Waiting;
                }

                @Override
                public String getName() {
                    return "Call for Rescue";
                }
            };
        else if (this.status == Status.Healing)
            return Action.getNullAction(1, "Stable");
        else
            return this.bleed;
    }

    @Override
    public void reset() {
        Random rd = new Random();

        this.status = Status.Initial;
        if (rd.nextInt(10) < 7)
            this.severity = Severity.Delayed;
        else
            this.severity = Severity.Immediate;
        this.lifePoint = 150 + (rd.nextInt(30) - 15); // 150 +- 15
        this.location = new Location(rd.nextInt(9), rd.nextInt(9));

        this.bleed = new Action(1) {

            @Override
            public void execute() {
                if (lifePoint > 0)
                    lifePoint--;
            }

            @Override
            public String getName() {
                return null;
            }
        };
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
