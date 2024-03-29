package edu.uaskl.cpp.algorithms;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeMap;

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
	private ArrayList<T> unvisitedKnownNodes = new ArrayList<T>();
	
	
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

    public PathExtended<T> getShortestPathAStar(T startNode, T endNode) {
    	NodeExtendedComparator<T, V> comp = new NodeExtendedComparator<T, V>(startNode);
    	Set<T> processedNodes = new HashSet<T>();
    	TreeMap<Double,T> knownUnprocessedNodes = new TreeMap<Double,T>();
    	Map<T,T> predecessor = new HashMap<T,T>();
    	Map<T,Double> costFromStart = new HashMap<T,Double>();
    	Map<T,Double> costEstimateTotal = new HashMap<T,Double>();
    	
    	knownUnprocessedNodes.put(0.,startNode);
    	costFromStart.put(startNode, 0.);
    	costEstimateTotal.put(startNode, comp.getDistance(startNode, endNode));
    	
    	while ( !knownUnprocessedNodes.isEmpty() ) {
    		T currentNode = knownUnprocessedNodes.pollFirstEntry().getValue();
    		if( currentNode == endNode){
    			List<T> path = new ArrayList<>();
    			while(currentNode != startNode) {
    				path.add(0,currentNode);
    				currentNode = predecessor.get(currentNode);
    			}
    			path.add(0,startNode);
    			return new PathExtended<>(path,costFromStart.get(endNode).intValue());
    		}
    		processedNodes.add(currentNode);
    		//get the neighbours
    		Set<T> neighbours = new HashSet<T>();
    		for( V edge : currentNode.getEdges()) {
    			neighbours.add(edge.getRelatedNode(currentNode));
    		}
    		for(T neighbour : neighbours) {
    			//TODO does not bother with different edges
    			double alternativeCostFromStart = costFromStart.get(currentNode) + currentNode.getShortestConnection(neighbour).getWeight();
    			double alternativeCostEstimateTotal = alternativeCostFromStart + comp.getDistance(neighbour, endNode);
    			if (processedNodes.contains(neighbour) && alternativeCostEstimateTotal >= costEstimateTotal.get(neighbour)) {
    				continue;
    			}
    			if (!knownUnprocessedNodes.containsValue(neighbour) || alternativeCostEstimateTotal < costEstimateTotal.get(neighbour)) {
    				predecessor.put(neighbour, currentNode);
    				costEstimateTotal.put(neighbour, alternativeCostEstimateTotal);
    				costFromStart.put(neighbour, alternativeCostFromStart);
    				if (!knownUnprocessedNodes.containsValue(neighbour)) {
    					knownUnprocessedNodes.put(alternativeCostEstimateTotal,neighbour);
    				}
    			}
    		}
    		
    	}
    	throw new IllegalStateException();
//    	return null;
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
//    	ArrayList<ArrayList<T>> pairs = getPairsNaiveGreedy(oddNodes);
    	ArrayList<ArrayList<T>> pairs = getPairsBlossomExtern(oddNodes);
    	// create the paths
    	createDupEdges(pairs);
//    	removeRedundantEdges(); //not needed for Blossom
    }

	private ArrayList<ArrayList<T>> getPairsBlossomExtern(ArrayList<T> oddNodes) {
		if(!preprocessed) {
			createDistNext();
		}
		int numberOfOddNodes = oddNodes.size();
		int numberOfEdges = numberOfOddNodes/2 * (numberOfOddNodes-1);
		Writer fw = null;
		try
		{
		  fw = new FileWriter( "graph.txt" );
		  fw.write( numberOfOddNodes +" "+numberOfEdges+"\n" );
		  for(int i=0; i < numberOfOddNodes-1; ++i) {
				for(int j=i+1; j< numberOfOddNodes;++j) {
					fw.write(i+" "+j+" "+((int)(dist[id2index.get(oddNodes.get(i).getId())][id2index.get(oddNodes.get(j).getId())]*100))+"\n");
				}
			}
		  fw.append( System.getProperty("line.separator") ); // e.g. "\n"
		}
		catch ( IOException e ) {
		  System.err.println( "Konnte Datei nicht erstellen" );
		}
		finally {
		  if ( fw != null )
		    try { fw.close(); } catch ( IOException e ) { e.printStackTrace(); }
		}
		System.out.println("run blossom");
		Scanner scan = new Scanner(System.in);
		scan.nextLine().isEmpty();
		scan.close();
		String outputFile = "output.txt";
		BufferedReader br = null;
		String line = "";
		boolean first = true;

    	ArrayList<ArrayList<T>> pairs = new ArrayList<>();
		try {
	 
			br = new BufferedReader(new FileReader(outputFile));
			while ((line = br.readLine()) != null) {

	    		ArrayList<T> pair = new ArrayList<>();
				if(first) {
					//skip the first line
					first = false;
					continue;
				}
				String[] values = line.split("\\s+");
				pair.add(oddNodes.get(Integer.parseInt(values[0])));
				pair.add(oddNodes.get(Integer.parseInt(values[1])));
				pairs.add(pair);
				
	 
			}
	 
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return pairs;
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
	
	/**
	 * Shortest path may have added duplicate edges.
	 * So if you have 2 edges in the one direction and 2 matching ones in the other, delete one for each direction.
	 */
	private void removeRedundantEdges() {
		for(T node : graph.getNodes()) {
			boolean modified;
			do {
				modified = false;
				graph.resetStates(); //TODO could be done more locally
				//NodeCppOSMDirected node = (NodeCppOSMDirected) n;
				//TODO find edges that are at least twice each direction
				for(V edge : node.getEdges()) {
					edge.setVisited();
					V inverseEdge = getUnvisitedConnection(edge.getNode2(),edge.getNode1(),edge.getWeight());
					if(inverseEdge != null) {
						//we have at least one edge in each direction
						inverseEdge.setVisited();
						V dupEdge = getUnvisitedConnection(edge.getNode1(),edge.getNode2(),edge.getWeight());
						edge.resetState();
						if(dupEdge != null ) {
							edge.getNode1().removeEdge(dupEdge);
							edge.getNode1().removeEdge(inverseEdge);
							modified = true;
							break;
						}
						inverseEdge.resetState();
					}
				}
			}while(modified);
			
		}
		
	}
	
	private V getUnvisitedConnection(T node1,
			T node2, double weight) {
		for(V edge : node1.getEdges()) {
			if(!edge.isVisited() && ((edge.getNode1().equals(node1) && edge.getNode2().equals(node2))||(edge.getNode1().equals(node2) && edge.getNode2().equals(node1))) && edge.getWeight() == weight) {
				return edge;
			}
		}
		return null;
	}
	
	
	
	public void Dijkstra (T start)
	{
		initialize(start);
		
		while(!unvisitedKnownNodes.isEmpty())
		{
			T smallestNode = getNodeWithSmallestDistance();
			unvisitedKnownNodes.remove(smallestNode);
			smallestNode.setVisited();
			T neighbour= null;
			for (int i = 0; i<smallestNode.getEdges().size(); i++)
			{
				neighbour = smallestNode.getEdges().get(i).getRelatedNode(smallestNode);

				distanceUpdate(smallestNode,neighbour);
			}
//			start = neighbour;
		}
	}
	
	private void initialize(T start)
	{
		for(T node : graph.getNodes()) {
			node.resetStates();
			node.setPrevious(null);
			node.setDistance(Double.POSITIVE_INFINITY);
		}
		start.setDistance(0);
//		unvisitedNodes = new ArrayList<T>(graph.getNodes());
		unvisitedKnownNodes = new ArrayList<T>();
		unvisitedKnownNodes.add(start);
	}
	
	private void distanceUpdate(T start, T neighbour)
	{
		double alternative = start.getDistance() + start.getEdgeToNode(neighbour).getWeight();

		if(alternative < neighbour.getDistance())
		{
			neighbour.setDistance(alternative);
			neighbour.setPrevious(start);
			if(!neighbour.isVisited() && !unvisitedKnownNodes.contains(neighbour)) {
				unvisitedKnownNodes.add(neighbour);
			}
		}
	}
	
	public ArrayList<T> shortestPath(T start, T destination)
	{
		Dijkstra(start);
		
		ArrayList<T> path = new ArrayList<T>();
		T current = destination;
		
		while(current.getPrevious() != null)
		{
			current= current.getPrevious();
			path.add(0,current);
		}
		path.add(destination);
		return path;
	}
	
	private T getNodeWithSmallestDistance()
	{
		double smallestDist = unvisitedKnownNodes.get(0).getDistance();
		T smallestNode = unvisitedKnownNodes.get(0);
		for(int i=0; i < unvisitedKnownNodes.size(); i++)
		{
			if(unvisitedKnownNodes.get(i).getDistance() < smallestDist)
			{
				smallestDist = unvisitedKnownNodes.get(i).getDistance();
				smallestNode = unvisitedKnownNodes.get(i);
			}
		}
		return smallestNode;
	}
	
	//################################################################ alternative dijkstra
	//																   getshortestPath(start, destination) to use it
	
	
	
	private ArrayList<T> Q = new ArrayList<T>();
	
	public void dijkstra(T start)
	{
		graph.resetStates();
		
		for (T node : graph.getNodes()) 
		{
			node.setDistance(Double.POSITIVE_INFINITY);
			node.setPrevious(null);
		}
		
		start.setDistance(0.0);
		
		Q.add(start);
		
		while(!Q.isEmpty())
		{
			T u = smalestDist(Q);
			Q.remove(u);
			u.setVisited();
			double alt;
			for (V edge : u.getEdges()) 
			{
				alt = u.getDistance() + edge.getWeight();
				if(alt < edge.getRelatedNode(u).getDistance())
				{
					edge.getRelatedNode(u).setDistance(alt);
					edge.getRelatedNode(u).setPrevious(u);
					if(!edge.getRelatedNode(u).isVisited())
					{
						Q.add(edge.getRelatedNode(u));
					}
				}
			}
			
		}
		
	}
	
	public ArrayList<T> getshortestPath(T start, T destination)
	{
		dijkstra(start);
		ArrayList<T> path = new ArrayList<T>();
		T u = destination;
		
		while(u.getPrevious() != null)
		{
			path.add(0, u);
			u = u.getPrevious();
		}
		
		path.add(0, u);

		return path;
	}
	
	public T smalestDist(ArrayList<T> Q)
	{
		double smallestDist = Q.get(0).getDistance();
		T smallestNode = Q.get(0);
		for(int i=0; i < Q.size(); i++)
		{
			if(Q.get(i).getDistance() < smallestDist)
			{
				smallestDist = Q.get(i).getDistance();
				smallestNode = Q.get(i);
			}
		}
		return smallestNode;
	}
	
}
	
