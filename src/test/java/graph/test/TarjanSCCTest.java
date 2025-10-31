package graph.scc;
import graph.common.Edge;
import org.junit.Assert;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class TarjanSCCTest {
    @Test
    public void simpleCycle() {
        List<List<Edge>> adj = new ArrayList<List<Edge>>();
        int n = 3;
        int i = 0;
        while (i < n) { adj.add(new ArrayList<Edge>()); i++; }

        adj.get(0).add(new Edge(1,1));
        adj.get(1).add(new Edge(2,1));
        adj.get(2).add(new Edge(0,1));

        TarjanSCC tj = new TarjanSCC(adj);
        tj.run();
        Assert.assertEquals(1, tj.getCompCount());
        Assert.assertEquals(1, tj.getComponents().size());
        Assert.assertEquals(3, tj.getComponents().get(0).size());
    }
}
