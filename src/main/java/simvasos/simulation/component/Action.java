package simvasos.simulation.component;

import simvasos.simulation.analysis.HasName;

/**
 * Created by mgjin on 2017-06-21.
 */
public abstract class Action implements HasName {
    protected boolean immediate = false;
    protected int duration = 0;
    protected String name = "";

    public Action(int duration) {
        this.duration = duration;
        if (duration == 0)
            immediate = true;
    }

    public boolean isImmediate() {
        return immediate;
    }

    public abstract void execute();
    public abstract String getName();

    @Override
    public String getSymbol() {
        return "a";
    };

    public static Action getNullAction(int duration, String nullActionName) {
        return new Action(duration) {

            @Override
            public void execute() {

            }

            @Override
            public String getName() {
                return nullActionName;
            }
        };
    }
}
