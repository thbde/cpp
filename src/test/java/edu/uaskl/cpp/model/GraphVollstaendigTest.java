package edu.uaskl.cpp.model;

import static org.fest.assertions.api.Assertions.*;

import java.util.ArrayList;

import org.junit.Test;

import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.graph.GraphComplete;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCpp;

public class GraphVollstaendigTest {

    @Test
    public void hasEulercircleTrue() {
        assertThat(new GraphComplete(19).getAlgorithms().hasEulerCircle()).isTrue();
    }

    @Test
    public void hasEulerCircleFalse() {
        assertThat(new GraphComplete(20).getAlgorithms().hasEulerCircle()).isTrue();
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
        GraphUndirected graph = new GraphUndirected();
        NodeCpp node1 = new NodeCpp();
        NodeCpp node2 = new NodeCpp();
        NodeCpp node3 = new NodeCpp();
        NodeCpp node4 = new NodeCpp();
        NodeCpp node5 = new NodeCpp();
        NodeCpp node6 = new NodeCpp();
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
    public void matchingTest()
    {
        GraphUndirected graph = new GraphUndirected();
        NodeCpp node1 = new NodeCpp();
        NodeCpp node2 = new NodeCpp();
        NodeCpp node3 = new NodeCpp();
        NodeCpp node4 = new NodeCpp();
        NodeCpp node5 = new NodeCpp();
        NodeCpp node6 = new NodeCpp();
        
        
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
        
		for (NodeCpp nodeItem : graph.getNodes()) 
		{
			if(nodeItem.isDegreeOdd())
				{
					odd = true;
					break;
				}
			
		}
	
    	assertThat(odd).isFalse();
    }
    
    @Test
    public void getPathBetweenTest()
    {
    	GraphUndirected graph = new GraphUndirected();
    
        NodeCpp node0 = new NodeCpp();
        NodeCpp node1 = new NodeCpp();
        NodeCpp node2 = new NodeCpp();
        NodeCpp node3 = new NodeCpp();
        NodeCpp node4 = new NodeCpp();
        NodeCpp node5 = new NodeCpp();
        NodeCpp node6 = new NodeCpp();
        NodeCpp node7 = new NodeCpp();
        NodeCpp node8 = new NodeCpp();
        
        
        
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
   
        ArrayList pathList = graph.getAlgorithms().getPathBetween(node0, node8);

        
        assertThat(pathList.get(0).equals(node0)&& pathList.get(pathList.size()-1).equals(node8)).isFalse();
    }
    
	
	@Test
	public void connectCirclesTest()
	{
		GraphUndirected graph = new GraphUndirected();
		
		NodeCpp node0 = new NodeCpp();
	    NodeCpp node1 = new NodeCpp();
	    NodeCpp node2 = new NodeCpp();
	    NodeCpp node3 = new NodeCpp();
	    NodeCpp node4 = new NodeCpp();
	    
	    
	    node0.connectWithNode(node1);
	    node1.connectWithNode(node2);
	    node2.connectWithNode(node0);
	    node1.connectWithNode(node2);
	    node1.connectWithNode(node3);
	    node3.connectWithNode(node4);
	    node4.connectWithNode(node2);
	    
	    graph.addNode(node0).addNode(node1).addNode(node2).addNode(node3).addNode(node4);
	    
	    ArrayList little = new ArrayList();
	    ArrayList big = new ArrayList();
	    
	    big.add(node0);
	    big.add(node1);
	    big.add(node2);
	    big.add(node0);
	    
	    little.add(node1);
	    little.add(node3);
	    little.add(node4);
	    little.add(node2);
	    little.add(node1);
	    
	   
	    ArrayList list = new ArrayList (graph.getAlgorithms().connectCircles(big, little));
	    
	    assertThat(list.get(0).equals(list.get(list.size()-1))).isTrue();
	}
	
	@Test
	public void getEulerianCircleTEST()
	{
		GraphUndirected graph = new GraphUndirected();

		NodeCpp node0 = new NodeCpp();
	    NodeCpp node1 = new NodeCpp();
	    NodeCpp node2 = new NodeCpp();
	    NodeCpp node3 = new NodeCpp();
	    NodeCpp node4 = new NodeCpp();
	    
	    
	    node0.connectWithNode(node1);
	    node1.connectWithNode(node2);
	    node2.connectWithNode(node0);
	    node2.connectWithNode(node1);
	    node1.connectWithNode(node3);
	    node3.connectWithNode(node4);
	    node4.connectWithNode(node2);
	    
	    graph.addNode(node0).addNode(node1).addNode(node2).addNode(node3).addNode(node4);

		ArrayList eulerianList = new ArrayList(graph.getAlgorithms().getEulerianCircle());
		
		
		
		boolean odd = false;
        
		for (NodeCpp nodeItem : graph.getNodes()) 
		{
			if(nodeItem.isDegreeOdd())
				{
					odd = true;
					break;
				}
		}
	    
		assertThat(odd).isFalse();
		assertThat(eulerianList.get(0).equals(eulerianList.get(eulerianList.size()-1))).isTrue();
	}

}
