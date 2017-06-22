package simsos.simulation.component;

/**
 * Created by mgjin on 2017-06-21.
 */
public abstract class Action {
    private boolean immediate = false;
    private int duration = 0;
    public String name;

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
}
