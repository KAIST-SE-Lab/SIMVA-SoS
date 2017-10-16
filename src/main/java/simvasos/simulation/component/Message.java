package simvasos.simulation.component;

import simvasos.simulation.util.Location;

import java.util.HashMap;
import java.util.Map;

public class Message {
    public enum Purpose {ReqInfo, ReqAction, Response, Delivery, Order}

    public String name = null; // description

    public String sender = null;
    public String receiver = null;
    public Location location = null;
    public Purpose purpose = null;
    public Map<String, Object> data = new HashMap<String, Object>();
    public int additionalBenefit = 0;
    public int reducedCost = 0;
    public int timestamp = 0;

    public String context = null;
    public int trust = 0;

    public String getName() {
        return this.name;
    }
}
