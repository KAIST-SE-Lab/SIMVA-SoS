package simvasos.simulation.util;

public class TimedValue<T> implements Comparable {
    private long timestamp;
    private T value;

    public TimedValue() {
        this.timestamp = 0;
        this.value = (T) new Object();
    }

    public TimedValue(long timestamp, T value) {
        this();

        this.timestamp = timestamp;
        this.value = value;
    }

    public long getTimestamp() {
        return this.timestamp;
    }

    public T getValue() {
        return this.value;
    }

    public boolean updateValue(TimedValue<T> timedValue) {
        if (this.value == null || timedValue.getTimestamp() > this.timestamp) {
            this.timestamp = timedValue.getTimestamp();
            this.value = timedValue.getValue();

            return true;
        }

        return false;
    }

    @Override
    public int compareTo(Object o) {
        long timestampCompared = ((TimedValue) o).getTimestamp();

        return (int) (this.timestamp - timestampCompared);
    }

    @Override
    public String toString() {
        if (value == null)
            return "null";
        else
            return value.toString() + "(Time: " + this.timestamp + ")";
    }
}
