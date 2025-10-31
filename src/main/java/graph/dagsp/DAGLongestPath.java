package graph.dagsp;
import graph.common.Edge;
import java.util.ArrayList;
import java.util.List;

public class DAGLongestPath {
    public static class Result {
        public int[] dist;
        public int[] prev;
    }

    public Result run(List<List<Edge>> dag, int sourceComp, List<Integer> topo) {
        int n = dag.size();
        int[] dist = new int[n];
        int[] prev = new int[n];

        int i = 0;
        while (i < n) {
            dist[i] = Integer.MIN_VALUE / 4;
            prev[i] = -1;
            i++;
        }

        dist[sourceComp] = 0;

        int k = 0;
        while (k < topo.size()) {
            int u = topo.get(k);
            if (dist[u] > Integer.MIN_VALUE / 4) {
                List<Edge> list = dag.get(u);
                int t = 0;
                while (t < list.size()) {
                    Edge e = list.get(t);
                    int v = e.getTo();
                    int nd = dist[u] + e.getW();
                    if (nd > dist[v]) {
                        dist[v] = nd;
                        prev[v] = u;
                    }
                    t++;
                }
            }
            k++;
        }

        Result r = new Result();
        r.dist = dist;
        r.prev = prev;
        return r;
    }

    public List<Integer> reconstructPath(int targetComp, int[] prev) {
        List<Integer> path = new ArrayList<Integer>();
        int cur = targetComp;
        while (cur != -1) {
            path.add(0, cur);
            cur = prev[cur];
        }
        return path;
    }
}
