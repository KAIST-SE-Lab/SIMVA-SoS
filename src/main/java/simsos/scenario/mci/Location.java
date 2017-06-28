package simsos.scenario.mci;

/**
 * Created by mgjin on 2017-06-28.
 */
public class Location {
    private int x;
    private int y;

    public Location(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int distanceTo(Location loc) {
        return Math.abs(this.getX() - loc.getX()) + Math.abs(this.getY() - loc.getY());
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
