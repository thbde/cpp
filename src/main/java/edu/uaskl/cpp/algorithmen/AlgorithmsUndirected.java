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

        for (final T nodeItem : graph.getNodes())
            for (final V edgeItem : nodeItem.getEdges())
                if (edgeItem.isVisited() == false)
                    return false;

        return true;

    }

    private void visitAllEdgesFromStartNode(final T node) {

        for (final V edgeItem : node.getEdges()) {

            if (edgeItem.isVisited() == true)
                return;
            edgeItem.setVisited();
            visitAllEdgesFromStartNode(edgeItem.getNode2());
        }
    }

    public boolean hasEulerCircle() {
        for (final T nodesItem : graph.getNodes())
            if (nodesItem.isDegreeOdd())
                return false;
        return true;
    }

    public GraphUndirected<T, V> matchGraph(GraphUndirected<T, V> graph) {
        T node1 = null, node2 = null;
        ArrayList<T> pathList = new ArrayList<>();

        for (final T nodeItem : graph.getNodes()) {
            if ((node1 == null) && nodeItem.isDegreeOdd()) {
                node1 = nodeItem;
                continue;
            }
            if (nodeItem.isDegreeOdd()) {
                node2 = nodeItem;

                pathList = getPathBetween(node1, node2);
                graph = matchBetweenNodes(graph, pathList);
                node1 = null;
                node2 = null;
            }
        }

        return graph;
    }

    private GraphUndirected<T, V> matchBetweenNodes(final GraphUndirected<T, V> graph, final ArrayList<T> pathList) {
        for (int i = 0; i < (pathList.size() - 1); i++) {
            final T node1 = pathList.get(i);
            final T node2 = pathList.get(i + 1);

            node1.connectWithNode(node2);
        }

        return graph;
    }

    public ArrayList<T> getPathBetween(final T start, final T destination) {
        final ArrayList<T> pathList = new ArrayList<>();

        graph.resetStates();
        pathList.add(start);
        searchForPathBetweenNodesWithArrayList(start, destination, pathList);

        return pathList;

    }

    private void searchForPathBetweenNodesWithArrayList(final T actual, final T destination, final ArrayList<T> pathList) {

        for (int i = 0; i < actual.getEdges().size(); i++)
            for (int j = 0; j < destination.getEdges().size(); j++)
                if (!actual.getEdges().get(i).isVisited())
                    if (actual.getEdges().get(i).getRelatedNode(actual).equals(destination.getEdges().get(j).getRelatedNode(destination))) {
                        pathList.add(actual.getEdges().get(i).getRelatedNode(actual));
                        pathList.add(destination);
                        return;
                    } else if (pathList.contains(actual.getEdges().get(i).getRelatedNode(actual))) {
                        actual.getEdges().get(i).setVisited();
                        searchForPathBetweenNodesWithArrayList(actual.getEdges().get(i).getRelatedNode(actual), destination, pathList);
                        return;
                    } else {
                        pathList.add(actual.getEdges().get(i).getRelatedNode(actual));
                        actual.getEdges().get(i).setVisited();
                        searchForPathBetweenNodesWithArrayList(actual.getEdges().get(i).getRelatedNode(actual), destination, pathList);
                        return;
                    }
    }

    public ArrayList<T> connectCircles(final ArrayList<T> big, final ArrayList<T> little) {
        // kleine wird zur gro�en hinzugef�gt
        final ArrayList<T> list = new ArrayList<>();

        for (int i = 0; i < big.size(); i++) {
            list.add(big.get(i));

            if (big.get(i).equals(little.get(0))) {
                list.remove(i); // damit doppeltes(aufgeschobenes) element gel�scht wird

                for (int j = 0; j < little.size(); j++)
                    list.add(little.get(j));
            }

        }

        return list;

    }

    public ArrayList<T> getEulerianCircle() {
        final T node = graph.getNodes().iterator().next();
        T temp;

        ArrayList<T> eulerianList = new ArrayList<>(getCircle(node));

        for (int i = 0; i < eulerianList.size(); i++) // gehe jedes element der Liste durch
        {
            temp = eulerianList.get(i);

            for (int j = 0; j < temp.getEdges().size(); j++)
                if (!temp.getEdges().get(j).isVisited()) // wenn eine kante noch nicht besucht wurde...
                {
                    final ArrayList<T> underGraph = new ArrayList<>(getCircle(temp));

                    eulerianList = connectCircles(eulerianList, underGraph);

                    i = 0; // beginne nochmal von vorn zu suchen

                }

        }

        return eulerianList;
    }

    private ArrayList<T> getCircle(final T node) {
        T node1 = node;
        int i = 0;

        final ArrayList<T> pathList = new ArrayList<>();

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
