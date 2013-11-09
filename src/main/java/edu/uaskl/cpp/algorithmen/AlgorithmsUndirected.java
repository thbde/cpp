package edu.uaskl.cpp.algorithmen;

import java.util.ArrayList;

import edu.uaskl.cpp.model.edge.EdgeCpp;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCpp;

// Example, can be changed

public class AlgorithmsUndirected implements Algorithms {

	private final GraphUndirected graph;

	public AlgorithmsUndirected(final GraphUndirected graph) {
		this.graph = graph;
	}

	@Override
	public GraphUndirected getGraph() {
		return graph;
	}

	public boolean isConnected() {

		graph.resetStates();

		if (graph.getNodes().size() == 0)
			return true;

		final NodeCpp start = graph.getNodes().iterator().next();
		visitAllEdgesFromStartNode(start);

		return allNodesVisited();
	}

	private boolean allNodesVisited() {
		for (final NodeCpp nodeItem : graph.getNodes()) {
			if (!nodeItem.isVisited())
				return false;
		}

		return true;
	}

	private void visitAllEdgesFromStartNode(final NodeCpp node) {
		node.setVisited();

		for (final EdgeCpp edgeItem : node.getEdges()) {
			if (!edgeItem.getRelatedNode(node).isVisited() == true) {
				visitAllEdgesFromStartNode(edgeItem.getRelatedNode(node));
			}
		}
	}

	public boolean hasEulerCircle() {
		for (final NodeCpp nodesItem : graph.getNodes())
			if (nodesItem.isDegreeOdd())
				return false;
		return true;
	}

	public GraphUndirected matchGraph(GraphUndirected graph) {
		NodeCpp node1 = null, node2 = null;
		ArrayList<NodeCpp> pathList = new ArrayList<>();

		for (final NodeCpp nodeItem : graph.getNodes()) {
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

	private GraphUndirected matchBetweenNodes(final GraphUndirected graph,
			final ArrayList<NodeCpp> pathList) {
		for (int i = 0; i < (pathList.size() - 1); i++) {
			final NodeCpp node1 = pathList.get(i);
			final NodeCpp node2 = pathList.get(i + 1);

			node1.connectWithNode(node2);
		}

		return graph;
	}

	public ArrayList<NodeCpp> getPathBetween(final NodeCpp start,
			final NodeCpp destination) {
		final ArrayList<NodeCpp> pathList = new ArrayList<>();

		graph.resetStates();
		searchForPathBetweenNodesWithArrayList(start, destination, pathList);


		return pathList;

	}

	private void searchForPathBetweenNodesWithArrayList(final NodeCpp actual, final NodeCpp destination, final ArrayList<NodeCpp> pathList)
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
					NodeCpp temp = pathList.get(pathList.size() - 1);
					pathList.remove(pathList.size() - 1);
					searchForPathBetweenNodesWithArrayList(temp, destination, pathList);
					return;
				}
			}
		
		
	}

	public ArrayList<NodeCpp> connectCircles(final ArrayList<NodeCpp> big,
			final ArrayList<NodeCpp> little) {
		// kleine wird zur großen hinzugefügt
		final ArrayList<NodeCpp> list = new ArrayList<>();

		for (int i = 0; i < big.size(); i++) {
			list.add(big.get(i));

			if (big.get(i).equals(little.get(0))) {
				list.remove(i); // damit doppeltes(aufgeschobenes) element
								// gelöscht wird

				for (int j = 0; j < little.size(); j++)
					list.add(little.get(j));
			}

		}

		return list;

	}

	public ArrayList<NodeCpp> getEulerianCircle(NodeCpp start) {
		final NodeCpp node = start;
		NodeCpp temp;

		ArrayList<NodeCpp> eulerianList = new ArrayList<>(getCircle(node));

		for (int i = 0; i < eulerianList.size(); i++) // gehe jedes element der
														// Liste durch
		{
			temp = eulerianList.get(i);

			for (int j = 0; j < temp.getEdges().size(); j++)
				if (!temp.getEdges().get(j).isVisited()) // wenn eine kante noch
															// nicht besucht
															// wurde...
				{
					final ArrayList<NodeCpp> underGraph = new ArrayList<>(
							getCircle(temp));

					eulerianList = connectCircles(eulerianList, underGraph);

					i = 0; // beginne nochmal von vorn zu suchen

				}

		}

		return eulerianList;
	}

	private ArrayList<NodeCpp> getCircle(final NodeCpp node) {
		NodeCpp node1 = node;
		int i = 0;

		final ArrayList<NodeCpp> pathList = new ArrayList<>();

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
