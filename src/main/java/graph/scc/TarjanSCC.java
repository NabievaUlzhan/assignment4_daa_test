package graph.scc;
import graph.common.Edge;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class TarjanSCC {
    private List<List<Edge>> adj;
    private int n;

    private int time;
    private int[] disc;
    private int[] low;
    private boolean[] inStack;
    private Stack<Integer> st;

    private int compCount;
    private int[] compId;
    private List<List<Integer>> components;

    public TarjanSCC(List<List<Edge>> adj) {
        this.adj = adj;
        this.n = adj.size();
        disc = new int[n];
        low = new int[n];
        inStack = new boolean[n];
        st = new Stack<Integer>();
        compId = new int[n];
        components = new ArrayList<List<Integer>>();
        int i = 0;
        while (i < n) {
            disc[i] = -1;
            low[i] = -1;
            compId[i] = -1;
            i++;
        }
    }

    public void run() {
        int i = 0;
        while (i < n) {
            if (disc[i] == -1) {
                dfs(i);
            }
            i++;
        }
    }

    private void dfs(int u) {
        disc[u] = time;
        low[u] = time;
        time = time + 1;
        st.push(u);
        inStack[u] = true;

        List<Edge> list = adj.get(u);
        int k = 0;
        while (k < list.size()) {
            Edge e = list.get(k);
            int v = e.getTo();
            if (disc[v] == -1) {
                dfs(v);
                low[u] = Math.min(low[u], low[v]);
            } else if (inStack[v]) {
                low[u] = Math.min(low[u], disc[v]);
            }
            k++;
        }

        if (low[u] == disc[u]) {
            List<Integer> comp = new ArrayList<Integer>();
            while (true) {
                int v = st.pop();
                inStack[v] = false;
                compId[v] = compCount;
                comp.add(v);
                if (v == u) { break; }
            }
            components.add(comp);
            compCount = compCount + 1;
        }
    }

    public int[] getCompId() {
        return compId;
    }
    public int getCompCount() {
        return compCount;
    }
    public List<List<Integer>> getComponents() {
        return components;
    }
}
