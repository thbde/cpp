package edu.uaskl.cpp.algorithmen;

import java.util.ArrayList;

import edu.uaskl.cpp.model.edge.EdgeExtended;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeExtended;

// Example, can be changed

public class AlgorithmsUndirected<T extends NodeExtended<T, V>, V extends EdgeExtended<T, V>> implements Algorithms<T, V> {

    private final GraphUndirected<T, V> graph;

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

    private void visitAllEdgesFromStartNode(final T node) {
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

        for (final T nodeItem : graph.getNodes()) { // the if/if could be simplified -tbach
            if ((node1 == null) && nodeItem.isDegreeOdd()) {
                node1 = nodeItem;
                continue;
            }
            if (nodeItem.isDegreeOdd()) {
                node2 = nodeItem;

                pathList = getPathBetween(node1, node2);
                matchBetweenNodes(pathList);
                node1 = null;
                node2 = null;
            }
        }
    }

    private void matchBetweenNodes(final ArrayList<T> pathList) {
        for (int i = 0; i < (pathList.size() - 1); i++) {
            final T node1 = pathList.get(i);
            final T node2 = pathList.get(i + 1);

            node1.connectWithNode(node2);
        }
    }

    public ArrayList<T> getPathBetween(final T node, final T destination) {

        T node1 = node;
        int i = 0;
        graph.resetStates();
        
        final ArrayList<T> pathList = new ArrayList<>();	//make an arraylist to write down the track

        pathList.add(node1);		// add startnode to list
        
        if (node1.equals(destination))
            return pathList;			//already finished

        do
        {
            if (!node1.getEdges().get(i).isVisited()) 	//search the unvisited edge to the next node
            {
                node1.getEdges().get(i).setVisited();
                pathList.add(node1);
                node1 = node1.getEdges().get(i).getRelatedNode(node1);	//go to next node above edge that was not visited
                i = 0;	
            } 
            else 
            {
                i++;
                if (i == node1.getEdges().size()) 	// if all Edges form node1 are visited you are in an DeadEnd
                {
                    final T temp = pathList.get(pathList.size() - 2); // so go back to the last node that got unvisited edges (size-1 = actual node; size-2 = node you come from)
                    pathList.remove(pathList.size() - 1);	// Remove the duplicated node in list
                    pathList.remove(pathList.size() - 1);	// Remove the Dead End from list
                    node1 = temp;							// go back to node that got unvisited edges (like iterative backtracking)

                    i = 0;
                }
            }
        }while (!node1.equals(destination));	// do it till you have reached de destination

        pathList.add(node1);		// add destination 
        
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
                for (int j = 0; j < little.size(); j++)
                    list.add(little.get(j));
            }

        }
        return list;

    }

    public ArrayList<T> getEulerianCircle(final T start) { // TODO should return a path -tbach

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

                    i = 0; // beginne nochmal von vorn zu suchen
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

}
