package edu.uaskl.cpp.model;

import static org.fest.assertions.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Test;

import edu.uaskl.cpp.model.graph.GraphComplete;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeOSM;
import edu.uaskl.cpp.model.node.NodeOSM;

public class GraphVollstaendigTest {

    @Test
    public void hasEulerCircleTrue() {
        assertThat(new GraphComplete(19).getAlgorithms().hasEulerCircle()).isTrue();
    }

    @Test
    public void hasEulerCircleFalse() {
        assertThat(new GraphComplete(20).getAlgorithms().hasEulerCircle()).isFalse();
    }

    @Test
    public void isConnected4True() {
        assertThat(new GraphComplete(4).getAlgorithms().isConnected()).isTrue();
    }
    
    @Test
    public void isConnected7True() {
        assertThat(new GraphComplete(7).getAlgorithms().isConnected()).isTrue();
    }
    
    @Test
    public void isConnected10True() {
        assertThat(new GraphComplete(10).getAlgorithms().isConnected()).isTrue();
    }
    
    @Test
    public void isConnected15True() {
        assertThat(new GraphComplete(15).getAlgorithms().isConnected()).isTrue();
    }

    @Test
    public void isConnected20True() {
        assertThat(new GraphComplete(20).getAlgorithms().isConnected()).isTrue();
    }

    @Test
    public void isConnected1000True() {
        assertThat(new GraphComplete(1000).getAlgorithms().isConnected()).isTrue();
    }
  

    @Test
    public void isConnectedFalse() {
        final GraphUndirected graph = new GraphUndirected();
        final NodeOSM node1 = new NodeOSM();
        final NodeOSM node2 = new NodeOSM();
        final NodeOSM node3 = new NodeOSM();
        final NodeOSM node4 = new NodeOSM();
        final NodeOSM node5 = new NodeOSM();
        final NodeOSM node6 = new NodeOSM();
        node1.connectWithNode(node2);
        node3.connectWithNode(node3);
        node4.connectWithNode(node5);
        node5.connectWithNode(node4);
        node1.connectWithNode(node3);
        node4.connectWithNode(node6);
        graph.addNode(node1).addNode(node2).addNode(node3).addNode(node4).addNode(node5).addNode(node6);
        assertThat(graph.getAlgorithms().isConnected()).isFalse();
    }

    @Test
    public void matchingTest() {
        final GraphUndirected graph = new GraphUndirected();
        final NodeOSM node1 = new NodeOSM();
        final NodeOSM node2 = new NodeOSM();
        final NodeOSM node3 = new NodeOSM();
        final NodeOSM node4 = new NodeOSM();
        final NodeOSM node5 = new NodeOSM();
        final NodeOSM node6 = new NodeOSM();

        node1.connectWithNode(node2);
        node2.connectWithNode(node3);
        node2.connectWithNode(node4);
        node2.connectWithNode(node5);
        node3.connectWithNode(node5);
        node3.connectWithNode(node6);
        node4.connectWithNode(node5);
        node5.connectWithNode(node6);

        graph.addNode(node1).addNode(node2).addNode(node3).addNode(node4).addNode(node5).addNode(node6);

        graph.getAlgorithms().matchGraph(graph);

        boolean odd = false;

        for (final Object nodeItem : graph.getNodes())
        {
        	NodeOSM node = (NodeOSM)nodeItem;
            if (node.isDegreeOdd()) {
                odd = true;
                break;
            }
        }
        assertThat(odd).isFalse();
    }

    @Test
    public void getPathBetweenTest() {
        final GraphUndirected graph = new GraphUndirected();

        final NodeOSM node0 = new NodeOSM();
        final NodeOSM node1 = new NodeOSM();
        final NodeOSM node2 = new NodeOSM();
        final NodeOSM node3 = new NodeOSM();
        final NodeOSM node4 = new NodeOSM();
        final NodeOSM node5 = new NodeOSM();
        final NodeOSM node6 = new NodeOSM();
        final NodeOSM node7 = new NodeOSM();
        final NodeOSM node8 = new NodeOSM();

        node0.connectWithNode(node1);
        node1.connectWithNode(node2);
        node1.connectWithNode(node5);
        node2.connectWithNode(node3);
        node2.connectWithNode(node5);
        node2.connectWithNode(node6);
        node3.connectWithNode(node6);
        node3.connectWithNode(node7);
        node3.connectWithNode(node4);
        node4.connectWithNode(node7);
        node4.connectWithNode(node8);
        node5.connectWithNode(node6);
        node6.connectWithNode(node7);
        node7.connectWithNode(node8);

        graph.addNode(node0).addNode(node1).addNode(node2).addNode(node3).addNode(node4).addNode(node5).addNode(node6).addNode(node7).addNode(node8);
        
        final ArrayList pathList = graph.getAlgorithms().getPathBetween(node0, node8);

        assertThat(pathList).startsWith(node0);
        assertThat(pathList).endsWith(node8);
    }

    @Test
    public void connectCirclesTest() {
        final GraphUndirected graph = new GraphUndirected();

        final NodeOSM node0 = new NodeOSM();
        final NodeOSM node1 = new NodeOSM();
        final NodeOSM node2 = new NodeOSM();
        final NodeOSM node3 = new NodeOSM();
        final NodeOSM node4 = new NodeOSM();

        node0.connectWithNode(node1);
        node1.connectWithNode(node2);
        node2.connectWithNode(node0);
        node1.connectWithNode(node2);
        node1.connectWithNode(node3);
        node3.connectWithNode(node4);
        node4.connectWithNode(node2);

        graph.addNode(node0).addNode(node1).addNode(node2).addNode(node3).addNode(node4);

        final ArrayList little = new ArrayList();
        final ArrayList big = new ArrayList();

        big.add(node0);
        big.add(node1);
        big.add(node2);
        big.add(node0);

        little.add(node1);
        little.add(node3);
        little.add(node4);
        little.add(node2);
        little.add(node1);

        final ArrayList list = new ArrayList(graph.getAlgorithms().connectCircles(big, little));

        assertThat(list).startsWith(big.get(0));
        assertThat(list).endsWith(big.get(0));
    }

    @Test
    public void getEulerianCircleTEST() {
        final GraphUndirected graph = new GraphUndirected();

        final NodeOSM node0 = new NodeOSM();
        final NodeOSM node1 = new NodeOSM();
        final NodeOSM node2 = new NodeOSM();
        final NodeOSM node3 = new NodeOSM();
        final NodeOSM node4 = new NodeOSM();

        node0.connectWithNode(node1);
        node1.connectWithNode(node2);
        node2.connectWithNode(node0);
        node2.connectWithNode(node1);
        node1.connectWithNode(node3);
        node3.connectWithNode(node4);
        node4.connectWithNode(node2);

        graph.addNode(node0).addNode(node1).addNode(node2).addNode(node3).addNode(node4);

        final ArrayList eulerianList = new ArrayList(graph.getAlgorithms().getEulerianCircle(node0));

        boolean odd = false;

        for (final Object nodeItem : graph.getNodes()){
        	NodeOSM node = (NodeOSM)nodeItem;
            if (node.isDegreeOdd()) 
            {
                odd = true;
                break;
            }
        }
        assertThat(odd).isFalse();
        assertThat(eulerianList.get(0).equals(eulerianList.get(eulerianList.size() - 1))).isTrue();
    }
 
    @Test
    public void getPathBetweenIterativBig() {

        final GraphUndirected graph = new GraphUndirected();

        final NodeOSM node0 = new NodeOSM();
        final NodeOSM node1 = new NodeOSM();
        final NodeOSM node2 = new NodeOSM();
        final NodeOSM node3 = new NodeOSM();
        final NodeOSM node4 = new NodeOSM();
        final NodeOSM node5 = new NodeOSM();
        final NodeOSM node6 = new NodeOSM();
        final NodeOSM node7 = new NodeOSM();
        final NodeOSM node8 = new NodeOSM();

        node0.connectWithNode(node1);
        node1.connectWithNode(node2);
        node1.connectWithNode(node5);
        node2.connectWithNode(node3);
        node2.connectWithNode(node5);
        node2.connectWithNode(node6);
        node3.connectWithNode(node6);
        node3.connectWithNode(node7);
        node3.connectWithNode(node4);
        node4.connectWithNode(node7);
        node4.connectWithNode(node8);
        node5.connectWithNode(node6);
        node6.connectWithNode(node7);
        node7.connectWithNode(node8);

        graph.addNode(node0).addNode(node1).addNode(node2).addNode(node3).addNode(node4).addNode(node5).addNode(node6).addNode(node7).addNode(node8);
        
        
        final ArrayList<NodeOSM> pathList = graph.getAlgorithms().getPathBetween(node0, node8);

        assertThat(pathList).startsWith(node0);
        assertThat(pathList).endsWith(node8);

    }
    
}
