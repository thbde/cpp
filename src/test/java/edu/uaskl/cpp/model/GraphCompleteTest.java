package edu.uaskl.cpp.model;

import static org.fest.assertions.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Test;

import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.graph.GraphComplete;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCpp;

public class GraphCompleteTest { // TODO this is not the right class for all of this tests. was initially my fault :/ -tbach

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
        final GraphUndirected<NodeCpp, EdgeCpp> graph = new GraphUndirected<>();
        final NodeCpp node1 = new NodeCpp();
        final NodeCpp node2 = new NodeCpp();
        final NodeCpp node3 = new NodeCpp();
        final NodeCpp node4 = new NodeCpp();
        final NodeCpp node5 = new NodeCpp();
        final NodeCpp node6 = new NodeCpp();
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
        final GraphUndirected<NodeCpp, EdgeCpp> graph = new GraphUndirected<>();
        final NodeCpp node1 = new NodeCpp();
        final NodeCpp node2 = new NodeCpp();
        final NodeCpp node3 = new NodeCpp();
        final NodeCpp node4 = new NodeCpp();
        final NodeCpp node5 = new NodeCpp();
        final NodeCpp node6 = new NodeCpp();

        node1.connectWithNode(node2);
        node2.connectWithNode(node3);
        node2.connectWithNode(node4);
        node2.connectWithNode(node5);
        node3.connectWithNode(node5);
        node3.connectWithNode(node6);
        node4.connectWithNode(node5);
        node5.connectWithNode(node6);

        graph.addNode(node1).addNode(node2).addNode(node3).addNode(node4).addNode(node5).addNode(node6);

        graph.getAlgorithms().matchGraph();

        boolean odd = false;

        for (final Object nodeItem : graph.getNodes()) {
            final NodeCpp node = (NodeCpp) nodeItem;
            if (node.isDegreeOdd()) {
                odd = true;
                break;
            }
        }
        assertThat(odd).isFalse();
    }

    @Test
    public void getPathBetweenTest() {
        final GraphUndirected<NodeCpp, EdgeCpp> graph = new GraphUndirected<>();

        final NodeCpp node0 = new NodeCpp();
        final NodeCpp node1 = new NodeCpp();
        final NodeCpp node2 = new NodeCpp();
        final NodeCpp node3 = new NodeCpp();
        final NodeCpp node4 = new NodeCpp();
        final NodeCpp node5 = new NodeCpp();
        final NodeCpp node6 = new NodeCpp();
        final NodeCpp node7 = new NodeCpp();
        final NodeCpp node8 = new NodeCpp();

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

        final ArrayList<NodeCpp> pathList = graph.getAlgorithms().getPathBetween(node0, node8);

        assertThat(pathList).startsWith(node0);
        assertThat(pathList).endsWith(node8);
        assertThat(pathList.size()<=graph.getGetNumberOfEdges());
    }

    @Test
    public void connectCirclesTest() {
        final GraphUndirected<NodeCpp, EdgeCpp> graph = new GraphUndirected<>();

        final NodeCpp node0 = new NodeCpp();
        final NodeCpp node1 = new NodeCpp();
        final NodeCpp node2 = new NodeCpp();
        final NodeCpp node3 = new NodeCpp();
        final NodeCpp node4 = new NodeCpp();

        node0.connectWithNode(node1);
        node1.connectWithNode(node2);
        node2.connectWithNode(node0);
        node1.connectWithNode(node2);
        node1.connectWithNode(node3);
        node3.connectWithNode(node4);
        node4.connectWithNode(node2);

        graph.addNode(node0).addNode(node1).addNode(node2).addNode(node3).addNode(node4);

        final ArrayList<NodeCpp> little = new ArrayList<>();
        final ArrayList<NodeCpp> big = new ArrayList<>();

        big.add(node0);
        big.add(node1);
        big.add(node2);
        big.add(node0);

        little.add(node1);
        little.add(node3);
        little.add(node4);
        little.add(node2);
        little.add(node1);

        final ArrayList<NodeCpp> list = new ArrayList<>(graph.getAlgorithms().connectCircles(big, little));

        assertThat(list).startsWith(big.get(0));
        assertThat(list).endsWith(big.get(0));
        assertThat(list.size()-1 == big.size() + little.size());	//-1 because (121=3 and 232=3)=6 should be 12321=5
    }

    @Test
    public void getEulerianCircleTEST() throws Exception {
        final GraphUndirected<NodeCpp, EdgeCpp> graph = new GraphUndirected<>();

        final NodeCpp node0 = new NodeCpp();
        final NodeCpp node1 = new NodeCpp();
        final NodeCpp node2 = new NodeCpp();
        final NodeCpp node3 = new NodeCpp();
        final NodeCpp node4 = new NodeCpp();

        node0.connectWithNode(node1);
        node1.connectWithNode(node2);
        node2.connectWithNode(node0);
        node2.connectWithNode(node1);
        node1.connectWithNode(node3);
        node3.connectWithNode(node4);
        node4.connectWithNode(node2);

        graph.addNode(node0).addNode(node1).addNode(node2).addNode(node3).addNode(node4);

        final ArrayList<NodeCpp> eulerianList = new ArrayList<>(graph.getAlgorithms().getEulerianCircle(node0).getNodes());

        boolean odd = false;

        for (final Object nodeItem : graph.getNodes()) {
            final NodeCpp node = (NodeCpp) nodeItem;
            if (node.isDegreeOdd()) {
                odd = true;
                break;
            }
        }
        assertThat(odd).isFalse();
        assertThat(eulerianList.get(0).equals(eulerianList.get(eulerianList.size() - 1))).isTrue();
        assertThat(eulerianList).endsWith(eulerianList.get(0));
        assertThat(eulerianList.size()==graph.getGetNumberOfEdges());
    }

    @Test
    public void getPathBetween() {

        final GraphUndirected<NodeCpp, EdgeCpp> graph = new GraphUndirected<>();

        final NodeCpp node0 = new NodeCpp();
        final NodeCpp node1 = new NodeCpp();
        final NodeCpp node2 = new NodeCpp();
        final NodeCpp node3 = new NodeCpp();
        final NodeCpp node4 = new NodeCpp();
        final NodeCpp node5 = new NodeCpp();
        final NodeCpp node6 = new NodeCpp();
        final NodeCpp node7 = new NodeCpp();
        final NodeCpp node8 = new NodeCpp();

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

        final ArrayList<NodeCpp> pathList = graph.getAlgorithms().getPathBetween(node0, node8);

        assertThat(pathList).startsWith(node0);
        assertThat(pathList).endsWith(node8);
        assertThat(pathList.size()<=graph.getGetNumberOfEdges());
    }

    // TODO should add negative tests -tbach
    @Test
    public void ShortetstPathTEST() {

        final GraphUndirected<NodeCpp, EdgeCpp> graph = new GraphUndirected<>();

        final NodeCpp node0 = new NodeCpp();
        final NodeCpp node1 = new NodeCpp();
        final NodeCpp node2 = new NodeCpp();
        final NodeCpp node3 = new NodeCpp();
        final NodeCpp node4 = new NodeCpp();
        final NodeCpp node5 = new NodeCpp();
        final NodeCpp node6 = new NodeCpp();
        final NodeCpp node7 = new NodeCpp();
        final NodeCpp node8 = new NodeCpp();

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

        final ArrayList<NodeCpp> pathList = graph.getAlgorithms().shortestPath(node0, node8);
        
        /*
        for (NodeCpp nodeCpp : pathList) {
		 	System.out.println(nodeCpp);
        }
       */
        assertThat(pathList).startsWith(node0);
        assertThat(pathList).endsWith(node8);
        assertThat(pathList.size()<=graph.getGetNumberOfEdges());

    	
    }
    
}
