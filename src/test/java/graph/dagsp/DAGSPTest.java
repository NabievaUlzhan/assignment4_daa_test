package graph.dagsp;
import graph.common.Edge;
import graph.topo.KahnTopoSort;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class DAGSPTest {
    @Test
    public void spChain() {
        List<List<Edge>> dag = new ArrayList<List<Edge>>();
        int comps = 4;
        int i = 0;
        while (i < comps) { dag.add(new ArrayList<Edge>()); i++; }

        dag.get(0).add(new Edge(1,2));
        dag.get(1).add(new Edge(2,5));
        dag.get(2).add(new Edge(3,1));

        KahnTopoSort topo = new KahnTopoSort();
        List<Integer> order = topo.order(dag);

        DAGShortestPaths dsp = new DAGShortestPaths();
        DAGShortestPaths.Result res = dsp.run(dag, 0, order);

        Assert.assertEquals(0, res.dist[0]);
        Assert.assertEquals(2, res.dist[1]);
        Assert.assertEquals(7, res.dist[2]);
        Assert.assertEquals(8, res.dist[3]);
    }
}
