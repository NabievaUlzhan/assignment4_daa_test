package graph.scc;
import graph.common.Edge;
import model.EdgeData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CondensationBuilder {

    public List<List<Edge>> build(int n, List<EdgeData> edges, int[] compId, int compCount, boolean directed) {
        List<List<Edge>> dag = new ArrayList<List<Edge>>();
        int i = 0;
        while (i < compCount) {
            dag.add(new ArrayList<Edge>());
            i++;
        }

        Map<Long, Boolean> seen = new HashMap<Long, Boolean>();
        int j = 0;
        while (j < edges.size()) {
            EdgeData ed = edges.get(j);
            int cu = compId[ed.getU()];
            int cv = compId[ed.getV()];
            if (cu != cv) {
                dag.get(cu).add(new Edge(cv, ed.getW()));
                long key = ((long) cv << 32) ^ (long) cu;
                seen.put(key, true);
            }
            if (!directed) {
                if (cu != cv) {
                    dag.get(cv).add(new Edge(cu, ed.getW()));
                    long key2 = ((long) cu << 32) ^ (long) cv;
                    seen.put(key2, true);
                }
            }
            j++;
        }
        return dag;
    }
}
