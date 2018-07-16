package main.tal.metrics;

public class TimeCalculator {

    private long startTime = 0;
    private long endTime = 0;

    public TimeCalculator() {
        startTime = 0;
        endTime = 0;
    }

    public void start() {
        startTime = System.nanoTime();
    }

    //return time interval
    public long stop() {
        if (startTime == 0)
            return 0;
        return ((System.nanoTime() - startTime) / 1000000);
    }
}
