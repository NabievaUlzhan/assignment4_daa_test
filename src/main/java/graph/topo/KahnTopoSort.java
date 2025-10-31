package graph.topo;
import graph.common.Edge;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class KahnTopoSort {
    public List<Integer> order(List<List<Edge>> adj) {
        int n = adj.size();
        int[] indeg = new int[n];

        int i = 0;
        while (i < n) {
            List<Edge> list = adj.get(i);
            int k = 0;
            while (k < list.size()) {
                int v = list.get(k).getTo();
                indeg[v] = indeg[v] + 1;
                k++;
            }
            i++;
        }

        Deque<Integer> dq = new ArrayDeque<Integer>();
        int t = 0;
        while (t < n) {
            if (indeg[t] == 0) {
                dq.add(t);
            }
            t++;
        }

        List<Integer> topo = new ArrayList<Integer>();
        while (!dq.isEmpty()) {
            int u = dq.removeFirst();
            topo.add(u);
            List<Edge> list = adj.get(u);
            int k = 0;
            while (k < list.size()) {
                int v = list.get(k).getTo();
                indeg[v] = indeg[v] - 1;
                if (indeg[v] == 0) {
                    dq.add(v);
                }
                k++;
            }
        }
        return topo;
    }
}
