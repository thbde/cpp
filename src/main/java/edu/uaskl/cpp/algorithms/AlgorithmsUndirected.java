package edu.uaskl.cpp.algorithms;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import edu.uaskl.cpp.model.edge.EdgeExtended;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeExtended;
import edu.uaskl.cpp.model.path.PathExtended;

// Example, can be changed

public class AlgorithmsUndirected<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> implements Algorithms<T, V> {

    private final GraphUndirected<T, V> graph;
    private double[][] dist;
    private Integer[][] next;
    private HashMap<Long,Integer> id2index;
    private HashMap<Integer,Long> index2id;
    private boolean preprocessed = false;

    public AlgorithmsUndirected(final GraphUndirected<T, V> graph) {
        this.graph = graph;
    }

    @Override
    public GraphUndirected<T, V> getGraph() {
        return graph;
    }

    /**
     * Checks whether the graph is connected.
     * Starts with one node and marks all connected node.
     * If a unmarked node is found the graph is not connected.
     * @return boolean whether the graph is connected
     */
    public boolean isConnected() {

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
     * Recursively marks all connected nodes as visited.
     * @param node start node
     */
    public void visitAllNodesFromStartNode(final T node) {
        node.setVisited();

        for (final V edgeItem : node.getEdges())
            if (!edgeItem.getRelatedNode(node).isVisited() == true)
                visitAllNodesFromStartNode(edgeItem.getRelatedNode(node));
    }

    /**
     * @return boolean, whether the graph has an Eulerian circle
     */
    public boolean hasEulerCircle() {
        for (final T node : graph.getNodes())
            if (node.isDegreeOdd())
                return false;
        return true;
    }

    
    /**
     * Adds a matching to the graph.
     * Inserting the new edges along the way.
     */
    @Deprecated
    public void matchGraph() {
        T node1 = null;
        T node2 = null;
        ArrayList<T> pathList = new ArrayList<>();

        for (final T nodeItem : graph.getNodes()) {
            if (nodeItem.isDegreeOdd()) {
                if (node1 == null){
                	node1 = nodeItem;	
                }
                else {
	                node2 = nodeItem;
	                pathList = getPathBetween(node1, node2);
	                matchBetweenNodes(pathList);
	                node1 = null;
	                node2 = null;
                }
            }
        }
    }

    /**
     * Adds edges along the specified path.
     * @param pathList the path to be added
     */
    @Deprecated
    private void matchBetweenNodes(final ArrayList<T> pathList) {
        for (int i = 0; i < (pathList.size() - 1); i++) {
            final T node1 = pathList.get(i);
            final T node2 = pathList.get(i + 1);
            //TODO connect with correct weight
            node1.connectWithNode(node2);
        }
    }
    
/**
 * Search a path by following an unvisited edge.
 * If no unvisited edge exists, go back one node along the taken path.
 * @param node start node
 * @param destination destination node
 * @return a list of nodes connecting the start node and the destination node, including the two
 */
@Deprecated
    public ArrayList<T> getPathBetween(T node, final T destination) {
        int i = 0;
        graph.resetStates();
        final ArrayList<T> pathList = new ArrayList<>();	//make an arraylist to write down the track
        pathList.add(node);		// add startnode to list
        if (node.equals(destination)) {
            return pathList;			//already finished
        }
        do
        {
            if (!node.getEdges().get(i).isVisited()) 	//search the unvisited edge to the next node
            {
                node.getEdges().get(i).setVisited();
                node = node.getEdges().get(i).getRelatedNode(node);	//go to next node above edge that was not visited
                pathList.add(node);
                i = 0;	
            } 
            else 
            {
                i++;
                if (i == node.getEdges().size()) 	// if all Edges form node1 are visited you are in an DeadEnd
                {
                    //final T temp = pathList.get(pathList.size() - 2); // so go back to the last node that got unvisited edges (size-1 = actual node; size-2 = node you come from)
                    //pathList.remove(pathList.size() - 1);	// Remove the duplicated node in list
                    pathList.remove(pathList.size() - 1);	// Remove the Dead End from list
                    //node = temp;							// go back to node that got unvisited edges (like iterative backtracking)
                    node = pathList.get(pathList.size()-1);
                    i = 0;
                }
            }
        }while (!node.equals(destination));	// do it till you have reached de destination
        //pathList.add(node);		// add destination 
        return pathList;		//return the track
    }

    /**
     * @param originalCircle the circle in which to insert
     * @param circleToInsert the circle which to insert
     * @return a new circle, which is a concatenation of both
     */
    public ArrayList<T> connectCircles(final ArrayList<T> originalCircle, final ArrayList<T> circleToInsert) 
    { // TODO should be private? -tbach //needs to be public for tests. alternative: set on private and remove test (don't know what's better) -debeck
    	// make a copy
    	ArrayList<T> newCircle = new ArrayList<>(originalCircle);
        // find the first occurrence of circleToInsert's start node
    	for (int i = 0; i < newCircle.size(); i++) 
        {
            if (newCircle.get(i).equals(circleToInsert.get(0))) 
            {
                newCircle.remove(i); // remove the insertion point, because it also occurs in the the circleToInser
                newCircle.addAll(i,circleToInsert); // simply insert the circleToInser at the right position
                return newCircle;
            }
        }
        return newCircle;
    }

    /**
     * @param start start node where to start
     * @return a Eulerian circle covering the whole graph starting at the specified node
     * @throws Exception The graph is not Eulerian.
     */
    public PathExtended<T> getEulerianCircle(final T start) throws Exception {
    	graph.resetStates();
    	if(!(isConnected() || hasEulerCircle()))
    	{
    		//TODO throw better exception
    		throw new Exception("not eulerian");
    	}    	
    	T currentNode;
        ArrayList<T> eulerianList = new ArrayList<>(getCircle(start));	//get the first subgraph
        
        if(graph.getNumberOfNodes()==1)
        {
        	return new PathExtended<T>(eulerianList);
        }
        for (int i = 0; i < eulerianList.size(); i++) 
        {
            currentNode = eulerianList.get(i);						//go through every node of the first subgraph...
            for (int j = 0; j < currentNode.getEdges().size(); j++)
                if (!currentNode.getEdges().get(j).isVisited())	//... and have a look if there could be an other subgraph
                {
                    final ArrayList<T> subGraph = new ArrayList<>(getCircle(currentNode));	//so get that new subgraph...
                    eulerianList = connectCircles(eulerianList, subGraph);	//...and add it to the big list
//                    i = 0; // beginne nochmal von vorn zu suchen
                    // TODO not a good style to change the loop variable.
                    i=0;
                }
        }
        return new PathExtended<T>(eulerianList);
    }

    /**
     * @return a Eulerian circle covering the whole graph
     * @throws Exception The graph is not Eulerian. 
     */
    public PathExtended<T> getEulerianCircle() throws Exception {
    	graph.resetStates();
    	if(!(isConnected() || hasEulerCircle()))
    	{
    		//TODO throw better exception
    		throw new Exception("not eulerian");
    	} 
    	ArrayList<T> eulerList = new ArrayList<>(getCircle(graph.getNodes().iterator().next()));
    	// as long as there are nodes with unvisited edges, add new circles starting at those nodes
    	boolean modified;
    	do{
    		modified = false;
    		for (T node : eulerList){
    			for (V edge : node.getEdges()) {
    				if ( !edge.isVisited()) {
    					eulerList = connectCircles(eulerList,getCircle(node));
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
            if (!startNode.getEdges().get(j).isVisited()) 
            {
            	// add the startNode and the next node to the list
                currentNode.getEdges().get(j).setVisited();
                pathList.add(currentNode);
                currentNode = currentNode.getEdges().get(j).getRelatedNode(currentNode);
                pathList.add(currentNode);
                break;
            }
        do
            if (!currentNode.getEdges().get(i).isVisited()) 	//	search connection to next node
            {
            	// mark the used edge, go to the next node and add it to the list
                currentNode.getEdges().get(i).setVisited();							
                currentNode = currentNode.getEdges().get(i).getRelatedNode(currentNode);
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
    			// TODO fix this for directed
    			// use the edge with minimum weight
    			if(edge.getWeight() < dist[id2index.get(edge.getNode1().getId())][id2index.get(edge.getNode2().getId())]) {
    				dist[id2index.get(edge.getNode1().getId())][id2index.get(edge.getNode2().getId())] = edge.getWeight();
    				dist[id2index.get(edge.getNode2().getId())][id2index.get(edge.getNode1().getId())] = edge.getWeight();
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
    private ArrayList<T> connectPathsDeDup(PathExtended<T> path1,PathExtended<T> path2){
		ArrayList<T> pathList = new ArrayList<>(path1.getNodes());
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
    private PathExtended<T> getShortestPath(T node1, T node2){
    	if(!preprocessed) {
    		createDistNext();
    	}
    	ArrayList<T> pathList = new ArrayList<T>();
    	if(dist[id2index.get(node1.getId())][id2index.get(node2.getId())] == Double.POSITIVE_INFINITY){
    		throw new IllegalStateException("no path");
    	}
    	Integer temp = next[id2index.get(node1.getId())][id2index.get(node2.getId())];
    	if(temp == null){
    		// node1 and node 2 are directly connected via their shortes path
    		pathList.add(node1);
    		pathList.add(node2);
    		return new PathExtended<T>(pathList);
    	}
    	else {
    		// get the two parts of the shortes path and connect them
    		PathExtended<T> path1 = getShortestPath(node1,graph.getNode(index2id.get(temp)));
    		PathExtended<T> path2 = getShortestPath(graph.getNode(index2id.get(temp)),node2);
    		pathList = connectPathsDeDup(path1,path2);
    	}
    	return new PathExtended<T>(pathList);
    }

    /**
     * This is a greedy version, taking a unmatched node and 
     * matching it with the nearest other unmatched node, until all nodes are matched.
     * @param oddNodes a list of nodes with odd degree, which should be matched
     * @return a list of node pairs
     */
    private ArrayList<ArrayList<T>> getPairsNaiveGreedy(ArrayList<T>oddNodes){
    	// naive version - not perfect but greedy
    	ArrayList<ArrayList<T>> pairs = new ArrayList<>();
    	while(!oddNodes.isEmpty()){
    		// get the last node in the list
    		ArrayList<T> pair = new ArrayList<>();
    		T firstNode = oddNodes.get(oddNodes.size()-1);
    		pair.add(firstNode);
    		oddNodes.remove(oddNodes.size()-1);
    		// find the nearest partner
    		T nextNode = oddNodes.get(0);
    		double minDist = Double.POSITIVE_INFINITY;
    		for(T node : oddNodes) {
    			double distToI = dist[id2index.get(firstNode.getId())][id2index.get(node.getId())];
    			if(distToI < minDist) {
    				minDist = distToI;
    				nextNode = node;
    			}
    		}
    		// connect them and remove them from the search
    		pair.add(nextNode);
    		oddNodes.remove(nextNode);
    		pairs.add(pair);
    	}
    	return pairs;
    }
    
    /**
     * Creates a matching for the graph and inserts the needed duplicate edges.
     * Currently this uses a greedy matching.
     */
    public void matchPerfect() {
    	if(!preprocessed) {
    		createDistNext();
    	}
    	// Find list of all nodes with odd degree
    	ArrayList<T> oddNodes = getOddNodes();
    	// match their shortest path
    	ArrayList<ArrayList<T>> pairs = getPairsNaiveGreedy(oddNodes);
    	// create the paths
    	createDupEdges(pairs);
    }

	/**
	 * This function searches the shortest path for each of the pairs and then
	 * creates all the duplicated edges.
	 * @param pairs a list of node pars, for which to add their shortest paths
	 */
	private void createDupEdges(ArrayList<ArrayList<T>> pairs) {
		for(ArrayList<T> pair : pairs) {
			// for each pair, get their shortest path
    		T startNode = pair.get(0);
    		T endNode = pair.get(1);
    		PathExtended<T> path = getShortestPath(startNode,endNode);
    		List<T> nodesInPath = path.getNodes();
    		for(int i = 1; i < path.getLength(); ++i) {
    			// for each link, find the correct edge
    			T nextNode = nodesInPath.get(i);
//    			V edgeToCopy;
    			double weight = dist[id2index.get(startNode.getId())][id2index.get(nextNode.getId())];
    			//if we have a "copy" method find the right edge
//    			for(V edge : startNode.getEdges()) {
//    				if(edge.getWeight() == weight && (edge.getNode1() == nextNode || edge.getNode2() == nextNode)) {
//    					edgeToCopy = edge;
//    					break;
//    				}
//    			}
    			// create the edge
    			startNode.connectWithNodeAndWeigth(nextNode, weight);
    			// move startNode to the next node
    			startNode = nextNode;
    			
    		}
    	}
	}

	/**
	 * @return a list of all nodes in the graph with an odd degree
	 */
	private ArrayList<T> getOddNodes() {
		Collection<T> nodes = graph.getNodes();
    	ArrayList<T> oddNodes = new ArrayList<T>();
    	for(T node : nodes) {
    		if(node.isDegreeOdd()){
    			oddNodes.add(node);
    		}
    	}
		return oddNodes;
	}
    
}
