/**
 * 
 */
package edu.uaskl.cpp.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.uaskl.cpp.model.edge.EdgeCppOSMDirected;
import edu.uaskl.cpp.model.edge.EdgeExtended;
import edu.uaskl.cpp.model.graph.GraphDirected;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.graph.interfaces.Graph;
import edu.uaskl.cpp.model.node.NodeCppOSMDirected;
import edu.uaskl.cpp.model.node.NodeExtended;
import edu.uaskl.cpp.model.path.PathExtended;

/**
 * @author malte
 *
 */
public class AlgorithmsDirected<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> implements Algorithms<T, V> {

	private GraphDirected<T, V> graph;
	private double[][] dist;
    private Integer[][] next;
    private HashMap<Long,Integer> id2index;
    private HashMap<Integer,Long> index2id;
    private boolean preprocessed = false;

	public AlgorithmsDirected(final GraphDirected<T, V> graph) {
		this.graph = graph;
	}

	@Override
	public Graph<T, V> getGraph() {
		return graph;
	}

    /**
     * Checks whether the graph is connected.
     * Starts with one node and marks all connected node.
     * If a unmarked node is found the graph is not connected.
     * @return boolean whether the graph is connected
     */
    public boolean isConnected() {
    	//FIXME
        graph.resetStates();

        if (graph.getNodes().size() == 0)
            return true;

        final T start = graph.getNodes().iterator().next();
        visitAllNodesFromStartNode(start);

        return allNodesVisited();
    }
	
    /**
     * @return boolean, whether all nodes are visited
     */
    private boolean allNodesVisited() {
        for (final T node : graph.getNodes())
            if (!node.isVisited())
                return false;
        return true;
    }
    
    /**
     * Recursively marks all following nodes as visited.
     * @param node start node
     */
    public void visitAllNodesFromStartNode(final T node) {
        node.setVisited();

        for (final V edgeItem : node.getEdges())
            if (!edgeItem.getNode2().isVisited() == true)
                visitAllNodesFromStartNode(edgeItem.getNode2());
    }
    
    /**
     * @return boolean, whether the graph has an Eulerian circle
     */
    public boolean hasEulerCircle() {
    	if(!isConnected()){
    		return false;
    	}
        NodeCppOSMDirected node1;
		for (final T node : graph.getNodes()){
        	node1 = (NodeCppOSMDirected)node;
            if (node1.getDegree() != 0){
                return false;
            }
		}
        return true;
    }
    
    /**
     * @param originalCircle the circle in which to insert
     * @param circleToInsert the circle which to insert
     * @return a new circle, which is a concatenation of both
     */
    private ArrayList<T> connectCircles(final ArrayList<T> originalCircle, final ArrayList<T> circleToInsert) 
    {
    	// make a copy
    	ArrayList<T> newCircle = new ArrayList<>(originalCircle);
        // find the first occurrence of circleToInsert's start node
    	for (int i = 0; i < newCircle.size(); i++) 
        {
            if (newCircle.get(i).equals(circleToInsert.get(0))) 
            {
                newCircle.remove(i); // remove the insertion point, because it also occurs in the the circleToInsert
                newCircle.addAll(i,circleToInsert); // simply insert the circleToInser at the right position
                return newCircle;
            }
        }
        return newCircle;
    }
    
    /**
     * @return a Eulerian circle covering the whole graph
     * @throws Exception The graph is not Eulerian. 
     */
    public PathExtended<T> getEulerianCircle() throws Exception {
    	graph.resetStates();
    	//FIXME
//    	if(!(isConnected() || hasEulerCircle()))
//    	{
//    		//TODO throw better exception
//    		throw new Exception("not eulerian");
//    	} 
    	ArrayList<T> eulerList = new ArrayList<>(getCircle(graph.getNodes().iterator().next()));
    	// as long as there are nodes with unvisited edges, add new circles starting at those nodes
    	boolean modified;
    	do{
    		modified = false;
    		for (T node : eulerList){
    			for (V edge : node.getEdges()) {
    				if ( !edge.isVisited() && edge.getNode1().equals(node)) {
    					eulerList = connectCircles(eulerList,getCircle(edge.getNode1()));
    					modified = true;
    				}
    			}
    		}
    		
    	}while(modified);	
    	return new PathExtended<T>(eulerList);
    }
    
    /**
     * @param startNode start node of the circle
     * @return a circle starting at startNode
     */
    private ArrayList<T> getCircle(final T startNode) {
        T currentNode = startNode;
        int i = 0;
        final ArrayList<T> pathList = new ArrayList<>();
        for (int j = 0; j < startNode.getEdges().size(); j++)
            if (currentNode.getEdges().get(j).getNode1()==currentNode && !startNode.getEdges().get(j).isVisited()) 
            {
            	// add the startNode and the next node to the list
                currentNode.getEdges().get(j).setVisited();
                pathList.add(currentNode);
                currentNode = currentNode.getEdges().get(j).getRelatedNode(currentNode);
                pathList.add(currentNode);
                break;
            }
        do
            if (currentNode.getEdges().get(i).getNode1()==currentNode && !currentNode.getEdges().get(i).isVisited()) 	//	search connection to next node
            {
            	// mark the used edge, go to the next node and add it to the list
                currentNode.getEdges().get(i).setVisited();							
                currentNode = currentNode.getEdges().get(i).getNode2();
                pathList.add(currentNode);
                i = 0;
            } else
                i++;
        while (!currentNode.equals(startNode)); // go through the graph till you come back to the beginning (because its a circle)
        return pathList;
    }

    /**
     * Creates the translation maps NodeId <-> index in dist|next
     */
    private void createTanslation(){
    	id2index = new HashMap<>();
    	index2id = new HashMap<>();
    	// create the translation from id to index
    	Collection<T> nodes = graph.getNodes();
    	int index = 0;
    	for(T node : nodes) {
    		index2id.put(index, node.getId());
    		id2index.put(node.getId(), index);
    		++index;
    	}
    }
    
    /**
     * Initialises the distance matrix, setting the distance of 
     * a node to itself as 0, 
     * unconnected nodes to Infinity and 
     * connected nodes to the minimal weight of their connecting edges.
     * It also creates next.
     */
    private void initDist(){
    	this.dist = new double[graph.getNumberOfNodes()][graph.getNumberOfNodes()];
    	this.next = new Integer[graph.getNumberOfNodes()][graph.getNumberOfNodes()]; // no need to set it to null
    	// initialise the distances
    	for(int i = 0; i < dist.length; ++i) {
    		for(int j = 0; j < dist.length; ++j) {
    			dist[i][j] = Double.POSITIVE_INFINITY;
    		}
    		dist[i][i] = 0;
    	}
    	Collection<T> nodes = graph.getNodes();
    	for(T node : nodes) {
    		for(V edge : node.getEdges()) {
    			// use the edge with minimum weight
    			if(edge.getWeight() < dist[id2index.get(edge.getNode1().getId())][id2index.get(edge.getNode2().getId())]) {
    				dist[id2index.get(edge.getNode1().getId())][id2index.get(edge.getNode2().getId())] = edge.getWeight();
    			}
    		}
    	}
    }

    /**
     * Uses the Floyd-Warshall-Algorithm (https://en.wikipedia.org/wiki/Floyd–Warshall_algorithm).
     * If node i can reach node j better by going over node k, 
     * update the distance and remember k as the next node on the way from i to j
     */
    private void createDistNext() {
    	//based on Floyd-Warshall
    	createTanslation();
    	initDist();
    	// create the matrix
    	for(int k = 0; k < dist.length; ++k) {
    		for(int i = 0; i < dist.length; ++i) {
    			for(int j = 0; j < dist.length; ++j) {
    				if(dist[i][k] + dist[k][j] < dist[i][j]) {
    					dist[i][j] = dist[i][k] + dist[k][j];
    					next[i][j] = k;
    				}
    			}
    		}
    	}
    	preprocessed = true;
    }
    
    /**
     * @param path1 first path
     * @param path2 second path
     * @return a path which is a concatenation of the first and the second path 
     * without the duplicate node occurring at the end of the first path
     */
    private ArrayList<NodeCppOSMDirected> connectPathsDeDup(PathExtended<NodeCppOSMDirected> path1,PathExtended<NodeCppOSMDirected> path2){
		ArrayList<NodeCppOSMDirected> pathList = new ArrayList<>(path1.getNodes());
		pathList.remove(pathList.size()-1); //prevent duplicate
		pathList.addAll(path2.getNodes());
		return pathList;
    }
    
    /**
     * Uses the next matrix computed by Floyd-Warshall to recursively construct the shortest path from node1 to node2.
     * @link createDistNext
     * @param node1 start node
     * @param node2 end node
     * @return the shortest path from node1 to node2
     */
    private PathExtended<NodeCppOSMDirected> getShortestPath(NodeCppOSMDirected node1, NodeCppOSMDirected node2){
    	if(!preprocessed) {
    		createDistNext();
    	}
    	ArrayList<NodeCppOSMDirected> pathList = new ArrayList<>();
    	int id1 = id2index.get(node1.getId());
    	int id2 = id2index.get(node2.getId());
    	if(dist[id2index.get(node1.getId())][id2index.get(node2.getId())] == Double.POSITIVE_INFINITY){
    		throw new IllegalStateException("no path");
    	}
    	Integer temp = next[id2index.get(node1.getId())][id2index.get(node2.getId())];
    	if(temp == null){
    		// node1 and node 2 are directly connected via their shortes path
    		pathList.add(node1);
    		pathList.add(node2);
    		return new PathExtended<NodeCppOSMDirected>(pathList);
    	}
    	else {
    		// get the two parts of the shortes path and connect them
    		PathExtended<NodeCppOSMDirected> path1 = getShortestPath(node1,(NodeCppOSMDirected) graph.getNode(index2id.get(temp)));
    		PathExtended<NodeCppOSMDirected> path2 = getShortestPath((NodeCppOSMDirected) graph.getNode(index2id.get(temp)),node2);
    		pathList = connectPathsDeDup(path1,path2);
    	}
    	return new PathExtended<NodeCppOSMDirected>(pathList);
    }
    
    /**
     * This is a greedy version, taking a unmatched node and 
     * matching it with the nearest other unmatched node, until all nodes are matched.
     */
    private void getPairsNaiveGreedy(){
    	// naive version - not perfect but greedy
		ArrayList<NodeCppOSMDirected> negNodes = getNegativeNodes();
		ArrayList<NodeCppOSMDirected> posNodes = getPositiveNodes();
    	while(!negNodes.isEmpty()){
    		// get the first node in the list
    		NodeCppOSMDirected firstNode = negNodes.get(0);
    		// find the nearest partner
    		NodeCppOSMDirected nextNode = posNodes.get(0);
    		double minDist = Double.POSITIVE_INFINITY;
    		for(NodeCppOSMDirected node : posNodes) {
    			double distToI = dist[id2index.get(firstNode.getId())][id2index.get(node.getId())];
    			if(distToI < minDist) {
    				minDist = distToI;
    				nextNode = node;
    			}
    		}
    		// connect them and remove them from the search
    		createDupEdges(firstNode,nextNode);
    		negNodes = getNegativeNodes();
    		posNodes = getPositiveNodes();
    	}
    	return;
    }
        
    
    /**
     * This function searches the shortest path from startNode to endNode and then
	 * creates all the duplicated edges.
     * @param startNode start node
     * @param endNode end node
     */
    private void createDupEdges(NodeCppOSMDirected startNode, NodeCppOSMDirected endNode) {
    	PathExtended<NodeCppOSMDirected> path = getShortestPath(startNode,endNode);
		List<NodeCppOSMDirected> nodesInPath = path.getNodes();
		for(int i = 1; i < path.getLength(); ++i) {
			// for each link, find the correct edge
			NodeCppOSMDirected nextNode = nodesInPath.get(i);
			EdgeCppOSMDirected edgeToCopy = null;
			double weight = dist[id2index.get(startNode.getId())][id2index.get(nextNode.getId())];
			//if we have a "copy" method find the right edge
			for(EdgeCppOSMDirected edge : startNode.getEdges()) {
				if(edge.getWeight() == weight && edge.getNode2() == nextNode) {
					edgeToCopy = edge;
					break;
				}
			}
			
			// create the edge
			startNode.connectWithNodeWeigthAndMeta(nextNode, weight, edgeToCopy.getMetadata());
			// move startNode to the next node
			startNode = nextNode;
			
		}
		
	}

	/**
     * Creates a matching for the graph and inserts the needed duplicate edges.
     * Currently this uses a greedy matching.
     */
    public void matchPerfect() {
    	if(!preprocessed) {
    		createDistNext();
    	}
    	// match their shortest path
    	 getPairsNaiveGreedy();
    	 simplify();
    }
    

	/**
	 * searches for circles that have an inverse circle and removes them
	 */
	private void simplify() {
		boolean modified;
		do{
			modified = false;
			for(T n : graph.getNodes()) {
				NodeCppOSMDirected node = (NodeCppOSMDirected) n;
				if(findDuplicateCircles(node)) {
					modified = true;
					break;
				}
			}
			
		}while(modified);
	}
	
	

	/**
	 * Tries to find a circle which has a inverse and deletes it
	 * @param startNode start node
	 * @return boolean, whether a duplicate circle/invere was removed
	 */
	private boolean findDuplicateCircles(NodeCppOSMDirected startNode) {
		graph.resetStates();
		NodeCppOSMDirected currentNode = startNode;
		int i = 0;
        final ArrayList<NodeCppOSMDirected> pathList = new ArrayList<>();
        for (int j = 0; j < startNode.getEdges().size(); j++) {
            if (currentNode.getEdges().get(j).getNode1()==currentNode && !startNode.getEdges().get(j).isVisited()) 
            {
            	if(!hasConnection(currentNode.getEdges().get(j).getNode2(),currentNode.getEdges().get(j).getNode1(),currentNode.getEdges().get(j).getWeight())) {
            		currentNode.getEdges().get(j).setVisited();
            		continue;
            	}
            	// add the startNode and the next node to the list
                currentNode.getEdges().get(j).setVisited();
                pathList.add(currentNode);
                currentNode = currentNode.getEdges().get(j).getRelatedNode(currentNode);
                pathList.add(currentNode);
                break;
            }
        }
        // no edge to leave the start node
        if(pathList.size() == 0){
        	return false;
        }
        
        do {
        	//try to find a next node, otherwise go back one node
        	boolean found = false;
        	for(EdgeCppOSMDirected edge : currentNode.getEdges()) {
        		if(!edge.isVisited() && edge.getNode1().equals(currentNode)) {
    				edge.setVisited();
        			if(!hasConnection(edge.getNode2(),edge.getNode1(),edge.getWeight())) {
        				continue;
        			}
        			found = true;
        			currentNode = edge.getNode2();
        			pathList.add(currentNode);
        			break;
        		}
        	}
        	
        	if(!found) {
        		int lastIndex = pathList.size()-1;
        		currentNode = pathList.get(lastIndex-1);
        		pathList.remove(lastIndex);
        	}
        }
        while (!currentNode.equals(startNode));
        if(pathList.size() == 1){
        	return false;
        }
        
        //remove the edges
        for(int index = 1; index < pathList.size(); ++index) {
        	NodeCppOSMDirected node1 = pathList.get(index -1);
        	NodeCppOSMDirected node2 = pathList.get(index);
        	node1.removeEdgeTo(node2);
        }
        return true;
	}
	

	private boolean hasConnection(NodeCppOSMDirected node1,
			NodeCppOSMDirected node2, double weight) {
		for(EdgeCppOSMDirected edge : node1.getEdges()) {
			if(!edge.isVisited() && edge.getNode1().equals(node1) && edge.getNode2().equals(node2) && edge.getWeight() == weight) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @return all nodes with more outgoing edges than incoming ones
	 */
	private ArrayList<NodeCppOSMDirected> getPositiveNodes() {
		Collection<T> nodes = graph.getNodes();
    	ArrayList<NodeCppOSMDirected> oddNodes = new ArrayList<>();
    	for(T node : nodes) {
    		NodeCppOSMDirected node1 = (NodeCppOSMDirected)node;
    		if(node1.getDegree()>0){
    			oddNodes.add(node1);
    		}
    	}
		return oddNodes;
	}
	
	/**
	 * @return all nodes with more incoming edges than outgoing ones
	 */
	private ArrayList<NodeCppOSMDirected> getNegativeNodes() {
		Collection<T> nodes = graph.getNodes();
    	ArrayList<NodeCppOSMDirected> oddNodes = new ArrayList<>();
    	for(T node : nodes) {
    		NodeCppOSMDirected node1 = (NodeCppOSMDirected)node;
    		if(node1.getDegree()<0){
    			oddNodes.add(node1);
    		}
    	}
		return oddNodes;
	}
	
}
