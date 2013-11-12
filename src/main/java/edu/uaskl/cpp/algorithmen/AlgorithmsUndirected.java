package edu.uaskl.cpp.algorithmen;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

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

    public boolean isConnected() {

        graph.resetStates();

        if (graph.getNodes().size() == 0)
            return true;

        final T start = graph.getNodes().iterator().next();
        visitAllEdgesFromStartNode(start);

        return allNodesVisited();
    }

    private boolean allNodesVisited() {
        for (final T node : graph.getNodes())
            if (!node.isVisited())
                return false;

        return true;
    }

    public void visitAllEdgesFromStartNode(final T node) {
        node.setVisited();

        for (final V edgeItem : node.getEdges())
            if (!edgeItem.getRelatedNode(node).isVisited() == true)
                visitAllEdgesFromStartNode(edgeItem.getRelatedNode(node));
    }

    public boolean hasEulerCircle() {
        for (final T node : graph.getNodes())
            if (node.isDegreeOdd())
                return false;
        return true;
    }

    /** This method changes the current graph. (I guess) it adds a matching */
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

    private void matchBetweenNodes(final ArrayList<T> pathList) {
        for (int i = 0; i < (pathList.size() - 1); i++) {
            final T node1 = pathList.get(i);
            final T node2 = pathList.get(i + 1);
            //TODO connect with correct weight
            node1.connectWithNode(node2);
        }
    }

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

 
    // TODO add javadoc with example and/or choose a better name -tbach
    public ArrayList<T> connectCircles(final ArrayList<T> big, final ArrayList<T> little) 
    { // TODO should be private? -tbach //needs to be public for tests. alternative: set on private and remove test (dont know whats better) -debeck
    	//insert list "little" into list "big"
        final ArrayList<T> list = new ArrayList<>();

        for (int i = 0; i < big.size(); i++) 
        {
            list.add(big.get(i));	//add all elements from big list...

            if (big.get(i).equals(little.get(0))) //...until little list could be inserted
            {
                list.remove(i); //remove doubled(shifted) element from big list because it will be added from little list again	

                // TODO List provides a "addAll" method -tbach
                list.addAll(little);
                ++i;
                for (; i < big.size(); i++)
                    list.add(big.get(i));
                return list;
            }

        }
        return list;

    }

    public ArrayList<T> getEulerianCircle(final T start) { // TODO should return a path -tbach
    	graph.resetStates();
    	if(!(isConnected() || hasEulerCircle()))
    	{
    		//throw exception?
    		return new ArrayList<>();
    	}    	
    	T temp;
        ArrayList<T> eulerianList = new ArrayList<>(getCircle(start));	//get the first subgraph
        
        if(graph.getNumberOfNodes()==1)
        {
        	return eulerianList;
        }
        for (int i = 0; i < eulerianList.size(); i++) 
        {
            temp = eulerianList.get(i);						//go through every node of the first subgraph...
            for (int j = 0; j < temp.getEdges().size(); j++)
                if (!temp.getEdges().get(j).isVisited())	//... and have a look if there could be an other subgraph
                {
                    final ArrayList<T> subGraph = new ArrayList<>(getCircle(temp));	//so get that new subgraph...
                    eulerianList = connectCircles(eulerianList, subGraph);	//...and add it to the big list
//                    i = 0; // beginne nochmal von vorn zu suchen
                    // TODO not a good style to change the loop variable.
                }
        }
        return eulerianList;
    }

    private ArrayList<T> getCircle(final T node) {
        T node1 = node;
        int i = 0;

        final ArrayList<T> pathList = new ArrayList<>();
 
        //add the startnode to the list and go to the next (connected) node. TODO Will be changed at optimizationstage
        for (int j = 0; j < node.getEdges().size(); j++)
            if (!node.getEdges().get(j).isVisited()) 
            {
                node1.getEdges().get(j).setVisited();
                pathList.add(node1);
                node1 = node1.getEdges().get(j).getRelatedNode(node1);

                break;
            }
        
        
        do
            if (!node1.getEdges().get(i).isVisited()) 	//	search connection to next node
            {
                node1.getEdges().get(i).setVisited();
                pathList.add(node1);							//add node to list...
                node1 = node1.getEdges().get(i).getRelatedNode(node1);	//... and go to next/connected node
                i = 0;
            } else
                i++;
        while (!node1.equals(node)); // go through the graph till you come back to the beginning (becaus its a circle)

        pathList.add(node1);	//add last node

        return pathList;

    }

    private void createDistNext() {
    	//based on Floyd-Warshall
    	id2index = new HashMap<>();
    	dist = new double[graph.getNumberOfNodes()][graph.getNumberOfNodes()];
    	next = new Integer[graph.getNumberOfNodes()][graph.getNumberOfNodes()];
    	// create the translation from id to index
    	Collection<T> nodes = graph.getNodes();
    	int index = 0;
    	for(T node : nodes) {
    		index2id.put(index, node.getId());
    		id2index.put(node.getId(), index);
    		++index;
    	}
    	// initialize the distances
    	for(int i = 0; i < dist.length; ++i) {
    		for(int j = 0; j < dist.length; ++j) {
    			dist[i][j] = Double.POSITIVE_INFINITY;
    		}
    		dist[i][i] = 0;
    	}
    	for(T node : nodes) {
    		for(V edge : node.getEdges()) {
    			dist[id2index.get(edge.getNode1().getId())][id2index.get(edge.getNode2().getId())] = edge.getWeight();
    		}
    	}
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
    		pathList.add(node1);
    		pathList.add(node2);
    		return new PathExtended<T>(pathList);
    	}
    	else {
    		PathExtended<T> path1 = getShortestPath(node1,graph.getNode(index2id.get(temp)));
    		PathExtended<T> path2 = getShortestPath(graph.getNode(index2id.get(temp)),node2);
    		pathList.addAll(path1.getNodes());
    		pathList.remove(pathList.size()-1); //prevent duplicate
    		pathList.addAll(path2.getNodes());
    	}
    	
    	return new PathExtended<T>(pathList);
    }
    
    public void matchPerfect() {
    	if(!preprocessed) {
    		createDistNext();
    	}
    	// Find list of all nodes with uneven degree
    	
    	// match their shortest path
    	
    	// get the paths
    	
    	// and create them
    }
    
}
