package simvasos.simulation.util;

public class Location implements Cloneable {
    private int x;
    private int y;

    public Location(Location location) {
        this.x = location.x;
        this.y = location.y;
    }

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

    public void setLocation(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void setLocation(Location location) {
        this.setLocation(location.getX(), location.getY());
    }

    public Location move(int transX, int transY) {
        moveX(transX);
        moveY(transY);

        return this;
    }

    public int moveX(int transX) {
        return this.x += transX ;
    }

    public int moveY(int transY) {
        return this.y += transY;
    }

    public Location add(int transX, int transY) {
        return new Location(this.x + transX, this.y + transY);
    }

    public int xDistanceTo(Location loc) {
        return Math.abs(this.getX() - loc.getX());
    }

    public int yDistanceTo(Location loc) {
        return Math.abs(this.getY() - loc.getY());
    }

    public int distanceTo(Location loc) {
        return xDistanceTo(loc) + yDistanceTo(loc);
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
