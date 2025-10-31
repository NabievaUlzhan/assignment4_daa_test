package metrics;
import java.util.HashMap;
import java.util.Map;

public class SimpleMetrics implements Metrics {
    private long start;
    private long end;
    private Map<String, Long> counters;

    public SimpleMetrics() {
        counters = new HashMap<String, Long>();
    }

    public void start() {
        start = System.nanoTime();
    }
    public void stop() {
        end = System.nanoTime();
    }
    public long getElapsedNanos() {
        return end - start;
    }

    public void inc(String name) {
        Long cur = counters.get(name);
        if (cur == null) { cur = 0L; }
        counters.put(name, cur + 1L);
    }

    public long get(String name) {
        Long cur = counters.get(name);
        if (cur == null) {
            return 0L;
        }
        return cur;
    }
}

