package simvasos.modelparsing.modeling.ABCPlus;

import simvasos.simulation.component.Action;

public class ABCItem {
    public final Action action;
    public int benefit;
    public int cost;

    public ABCItem(Action action, int benefit, int cost) {
        this.action = action;
        this.benefit = benefit;
        this.cost = cost;
    }

    public int utility() {
        return this.benefit - this.cost;
    }
}
