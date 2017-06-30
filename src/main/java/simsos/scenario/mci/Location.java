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

    public int moveX(int transX) {
        return this.x += transX ;
    }

    public int moveY(int transY) {
        return this.y += transY;
    }

    public int distanceTo(Location loc) {
        return Math.abs(this.getX() - loc.getX()) + Math.abs(this.getY() - loc.getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Location) {
            Location loc = (Location) obj;
            if (this.x == loc.getX() && this.y == loc.getY())
                return true;
            else
                return false;
        } else
            return false;
    }

    public String toString() {
        return "(" + this.x + ", " + this.y + ")";
    }
}
