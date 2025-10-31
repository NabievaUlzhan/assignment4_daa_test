package app;
import graph.common.Edge;
import graph.dagsp.DAGLongestPath;
import graph.dagsp.DAGShortestPaths;
import graph.scc.CondensationBuilder;
import graph.scc.TarjanSCC;
import graph.topo.KahnTopoSort;
import io.GraphLoader;
import model.EdgeData;
import model.GraphData;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            System.out.println("Usage: java -jar app.jar <path-to-json>");
            return;
        }

        String path = args[0];
        GraphLoader loader = new GraphLoader();
        GraphData data = loader.load(path);
        int n = data.getNodes();
        List<List<Edge>> adj = new ArrayList<List<Edge>>();
        int i = 0;
        while (i < n) {
            adj.add(new ArrayList<Edge>());
            i++;
        }

        List<EdgeData> edges = data.getEdges();
        int e = 0;
        while (e < edges.size()) {
            EdgeData ed = edges.get(e);
            adj.get(ed.getU()).add(new Edge(ed.getV(), ed.getW()));
            if (!data.isDirected()) {
                adj.get(ed.getV()).add(new Edge(ed.getU(), ed.getW()));
            }
            e++;
        }

        TarjanSCC tarjan = new TarjanSCC(adj);
        tarjan.run();
        int[] compId = tarjan.getCompId();
        List<List<Integer>> comps = tarjan.getComponents();
        int compCount = tarjan.getCompCount();

        System.out.println("SCC count: " + compCount);
        int c = 0;
        while (c < comps.size()) {
            List<Integer> list = comps.get(c);
            Collections.sort(list);
            System.out.println("Component " + c + " -> " + list);
            c++;
        }

        CondensationBuilder builder = new CondensationBuilder();
        List<List<Edge>> dag = builder.build(n, data.getEdges(), compId, compCount, data.isDirected());

        KahnTopoSort topoSort = new KahnTopoSort();
        List<Integer> topo = topoSort.order(dag);
        System.out.println("Topo order of components: " + topo);

        int srcNode = data.getSource();
        if (srcNode < 0 || srcNode >= n) {
            System.out.println("Invalid source in JSON: " + srcNode);
            return;
        }
        int sourceComp = compId[srcNode];
        System.out.println("Source node: " + srcNode + " belongs to component: " + sourceComp);

        DAGShortestPaths sp = new DAGShortestPaths();
        DAGShortestPaths.Result spr = sp.run(dag, sourceComp, topo);
        int[] sdist = spr.dist;

        System.out.println("DAG Shortest Distances (component-indexed):");
        int t = 0;
        while (t < sdist.length) {
            System.out.println("  comp " + t + " -> " + (sdist[t] >= (Integer.MAX_VALUE/8) ? "INF" : String.valueOf(sdist[t])));
            t++;
        }

        int lastComp = -1;
        if (topo.size() > 0) {
            lastComp = topo.get(topo.size() - 1);
        }
        if (lastComp != -1 && sdist[lastComp] < Integer.MAX_VALUE / 4) {
            List<Integer> spath = sp.reconstructPath(lastComp, spr.prev);
            System.out.println("One shortest path (comp indices) srcComp->...->lastTopo: " + spath);
        }

        DAGLongestPath lp = new DAGLongestPath();
        DAGLongestPath.Result lpr = lp.run(dag, sourceComp, topo);
        int[] ldist = lpr.dist;

        System.out.println("DAG Longest Distances (component-indexed, assumes non-negative weights):");
        int q = 0;
        while (q < ldist.length) {
            System.out.println("  comp " + q + " -> " + (ldist[q] <= (Integer.MIN_VALUE/8) ? "-INF" : String.valueOf(ldist[q])));
            q++;
        }

        int bestComp = sourceComp;
        int bestVal = ldist[bestComp];
        int r = 0;
        while (r < ldist.length) {
            if (ldist[r] > bestVal) {
                bestVal = ldist[r];
                bestComp = r;
            }
            r++;
        }

        if (bestVal > Integer.MIN_VALUE / 8) {
            List<Integer> lpath = new DAGLongestPath().reconstructPath(bestComp, lpr.prev);
            System.out.println("Critical (longest) path (comp indices) from source component: " + lpath + " with length " + bestVal);
        } else {
            System.out.println("No reachable nodes from source in condensation DAG.");
        }

        System.out.println();
        System.out.println("Component mapping (node -> comp):");
        int z = 0;
        while (z < compId.length) {
            System.out.println("  " + z + " -> " + compId[z]);
            z++;
        }

        List<Integer> expandedTopo = expandTopoToOriginal(topo, comps);
        System.out.println("Expanded Topo order over original nodes: " + expandedTopo);
    }

    private static List<Integer> expandTopoToOriginal(List<Integer> topo, List<List<Integer>> comps) {
        List<Integer> order = new ArrayList<Integer>();
        int i = 0;
        while (i < topo.size()) {
            int comp = topo.get(i);
            List<Integer> nodes = new ArrayList<Integer>(comps.get(comp));
            Collections.sort(nodes);
            int k = 0;
            while (k < nodes.size()) {
                order.add(nodes.get(k));
                k++;
            }
            i++;
        }
        return order;
    }
}
