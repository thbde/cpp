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
        // TODO add at least the idea of what is happening here
        // a good alternative would be a backtracking algorithm -tbach

        T node1 = node;
        int i = 0;
        graph.resetStates();
        final ArrayList<T> pathList = new ArrayList<>();

        for (int j = 0; j < node.getEdges().size(); j++)
            if (!node.getEdges().get(j).isVisited()) {
                node1.getEdges().get(j).setVisited();
                pathList.add(node1);
                node1 = node1.getEdges().get(j).getRelatedNode(node1);
                j = node.getEdges().size(); // TODO hu? do not modify the loop variable if you want to break. someone invented the break statement for this -tbach
                // break;
            }

        if (node1.equals(destination))
            return pathList;

        do
            if (!node1.getEdges().get(i).isVisited()) {
                node1.getEdges().get(i).setVisited();
                pathList.add(node1);
                node1 = node1.getEdges().get(i).getRelatedNode(node1);
                i = 0;
            } else {
                i++;
                if (i == node1.getEdges().size()) {
                    final T temp = pathList.get(pathList.size() - 2); // TODO can not follow what is happening here -tbach
                    pathList.remove(pathList.size() - 1);
                    pathList.remove(pathList.size() - 1);
                    node1 = temp;

                    i = 0;
                }

            }
        while (!node1.equals(destination));

        pathList.add(node1);

        return pathList;

    }

    /*
    	public ArrayList<NodeOSM> getPathBetween(final NodeOSM start, final NodeOSM destination) {
    		final ArrayList<NodeOSM> pathList = new ArrayList<>();

    		graph.resetStates();
    		searchForPathBetweenNodesWithArrayList(start, destination, pathList);


    		return pathList;

    	}

    	private void searchForPathBetweenNodesWithArrayList(final NodeOSM actual, final NodeOSM destination, final ArrayList<NodeOSM> pathList)
    	{
    			if (actual.equals(destination))
    			{		
    				pathList.add(actual);
    				return;
    			}
    				
    			pathList.add(actual);
    			
    			for (int i = 0; i < actual.getEdges().size(); i++)
    			{
    				if (!actual.getEdges().get(i).isVisited())
    				{
    						
    						actual.getEdges().get(i).setVisited();
    						searchForPathBetweenNodesWithArrayList(actual.getEdges().get(i).getRelatedNode(actual),destination, pathList);
    						return;
    				}
    				else if( i == actual.getEdges().size() - 1 )			
    				{
    					
    					pathList.remove(pathList.size() - 1);
    					NodeOSM temp = pathList.get(pathList.size() - 1);
    					pathList.remove(pathList.size() - 1);
    					searchForPathBetweenNodesWithArrayList(temp, destination, pathList);
    					return;
    				}
    			}
    		
    		
    	}
    */

    // TODO add javadoc with example and/or choose a better name -tbach
    public ArrayList<T> connectCircles(final ArrayList<T> big, final ArrayList<T> little) { // TODO should be private? -tbach
        // TODO comments are good, but we agreed to use english ;) -tbach
        // kleine wird zur großen hinzugefügt
        final ArrayList<T> list = new ArrayList<>();

        for (int i = 0; i < big.size(); i++) {
            list.add(big.get(i));

            if (big.get(i).equals(little.get(0))) {
                list.remove(i); // damit doppeltes(aufgeschobenes) element
                                // gelöscht wird

                // TODO List provides a "addAll" method -tbach
                for (int j = 0; j < little.size(); j++)
                    list.add(little.get(j));
            }

        }
        return list;

    }

    public ArrayList<T> getEulerianCircle(final T start) { // TODO should return a path -tbach
        // TODO comments language -tbach
        // TODO check if one exists, before you search one. or write in the javadoc that you expect a "good" input -tbach
        T temp;

        ArrayList<T> eulerianList = new ArrayList<>(getCircle(start));
        // TODO you could check if this is already the whole graph -tbach

        for (int i = 0; i < eulerianList.size(); i++) // gehe jedes element der
                                                      // Liste durch
        // TODO this comment is obvious.. -tbach
        {
            temp = eulerianList.get(i);

            for (int j = 0; j < temp.getEdges().size(); j++)
                if (!temp.getEdges().get(j).isVisited()) // wenn eine kante noch
                                                         // nicht besucht
                                                         // wurde...
                // TODO this comment is obvious, too. Do not write the code again in comments -tbach
                {
                    final ArrayList<T> underGraph = new ArrayList<>(getCircle(temp));
                    // TODO subGraph would be a better name -tbach

                    eulerianList = connectCircles(eulerianList, underGraph);

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
        // TODO this can be simplified (and some comments with the idea could be added -tbach
        for (int j = 0; j < node.getEdges().size(); j++)
            if (!node.getEdges().get(j).isVisited()) {
                node1.getEdges().get(j).setVisited();
                pathList.add(node1);
                node1 = node1.getEdges().get(j).getRelatedNode(node1);

                break;
            }

        do
            if (!node1.getEdges().get(i).isVisited()) {
                node1.getEdges().get(i).setVisited();
                pathList.add(node1);
                node1 = node1.getEdges().get(i).getRelatedNode(node1);
                i = 0;
            } else
                i++;
        while (!node1.equals(node));

        pathList.add(node1);

        return pathList;

    }

}
