package edu.uaskl.cpp.visualization;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections15.Transformer;

import edu.uaskl.cpp.importer.OsmImporter;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

class UIGraph {

    public enum NodeLayout {
        ISOMLayout,
        KKLayout
    }

    private GraphUndirected<NodeCppOSM, EdgeCppOSM> osmGraph;
    private VisualizationViewer<Long, Long> leftVisViewer;
    private VisualizationViewer<Long, Long> rightVisViewer;
    private Map<Long, EdgeCppOSM> leftEdgesWithID = new HashMap<>();
    private Map<Long, EdgeCppOSM> rightEdgesWithID = new HashMap<>();

    public UIGraph(String file) {

        osmGraph = OsmImporter.importFH();

        // Empty Graphs
        leftVisViewer = new VisualizationViewer<>(new KKLayout<>(new SparseMultigraph<Long, Long>()));
        rightVisViewer = new VisualizationViewer<>(new KKLayout<>(new SparseMultigraph<Long, Long>()));

        // Mouse Interaction
        final DefaultModalGraphMouse<Long, Long> graphMouse = new DefaultModalGraphMouse<>();
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        leftVisViewer.setGraphMouse(graphMouse);
        rightVisViewer.setGraphMouse(graphMouse);

        // Edge labels
        leftVisViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<Long, String>() {
            @Override
            public String transform(Long e) {
            	String output = leftEdgesWithID.get(e).getMetadata().getName();
            	if (output == "unknown")
            		return "";
            	else
            		return output;
            }
        });
        rightVisViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<Long, String>() {
            @Override
            public String transform(final Long e) {
            	String output = rightEdgesWithID.get(e).getMetadata().getName();
            	if (output == "unknown")
            		return "";
            	else
            		return output;
            }
        });

        // Centered edge labels
        final ConstantDirectionalEdgeValueTransformer<Long, Long> edgeValueTransformer =
                new ConstantDirectionalEdgeValueTransformer<>(.5, .5);
        leftVisViewer.getRenderContext().setEdgeLabelClosenessTransformer(edgeValueTransformer);
        rightVisViewer.getRenderContext().setEdgeLabelClosenessTransformer(edgeValueTransformer);

        // Colors
        leftVisViewer.setBackground(Color.white);
        leftVisViewer.getRenderContext().setEdgeDrawPaintTransformer(
                new PickableEdgePaintTransformer<>(leftVisViewer.getPickedEdgeState(), Color.gray, Color.red));
        leftVisViewer.getRenderContext().setVertexFillPaintTransformer(
                new PickableVertexPaintTransformer<>(leftVisViewer.getPickedVertexState(), Color.gray, Color.red));
        rightVisViewer.setBackground(Color.white);
        rightVisViewer.getRenderContext().setEdgeDrawPaintTransformer(
                new PickableEdgePaintTransformer<>(rightVisViewer.getPickedEdgeState(), Color.gray, Color.red));
        rightVisViewer.getRenderContext().setVertexFillPaintTransformer(
                new PickableVertexPaintTransformer<>(rightVisViewer.getPickedVertexState(), Color.gray, Color.red));

        // Tool tip displaying node number
        leftVisViewer.setVertexToolTipTransformer(new ToStringLabeller<Long>());
        rightVisViewer.setVertexToolTipTransformer(new ToStringLabeller<Long>());

        createLeftGraph(file);
        createRightGraph(file);
    }

    public VisualizationViewer<Long, Long> getLeftVisualizationViewer() {
        return leftVisViewer;
    }

    public VisualizationViewer<Long, Long> getRightVisualizationViewer() {
        return rightVisViewer;
    }

    public GraphZoomScrollPane createLeftGraphPane() {
        return new GraphZoomScrollPane(leftVisViewer);
    }

    public GraphZoomScrollPane createRightGraphPane() {
        return new GraphZoomScrollPane(rightVisViewer);
    }

    private void createLeftGraph(String file) {
        final SparseMultigraph<Long, Long> graph = new SparseMultigraph<>();

        final Collection<NodeCppOSM> nodes = osmGraph.getNodes();
        final Iterator<NodeCppOSM> iterator = nodes.iterator();

        long edgeNumber = 0;
        final List<Long> processedNodes = new ArrayList<>(); // you should use a hash* here, probably a hashset -tbach

        while (iterator.hasNext()) {
            final NodeCppOSM node = iterator.next();
            graph.addVertex(node.getId());

            final List<EdgeCppOSM> edges = node.getEdges();

            for (final EdgeCppOSM edge : edges)
                // TODO the contains is a linear search with the arraylist -tbach
                if (!processedNodes.contains(edge.getNode1().getId()) && !processedNodes.contains(edge.getNode2().getId())) {
                    graph.addEdge(edgeNumber, edge.getNode1().getId(), edge.getNode2().getId());
                    leftEdgesWithID.put(edgeNumber, edge);
                    edgeNumber++;
                }
            processedNodes.add(node.getId());
        }
        leftVisViewer.setGraphLayout(new ISOMLayout<>(graph));
    }

    private void createRightGraph(String file) {
        final SparseMultigraph<Long, Long> graph = new SparseMultigraph<>();

//        osmGraph.getAlgorithms().matchPerfect();

        final Collection<NodeCppOSM> nodes = osmGraph.getNodes();
        final Iterator<NodeCppOSM> iterator = nodes.iterator();

        long edgeNumber = 0;
        final List<Long> processedNodes = new ArrayList<>(); // you should use a hash* here, probably a hashset -tbach

        while (iterator.hasNext()) {
            final NodeCppOSM node = iterator.next();
            graph.addVertex(node.getId());

            final List<EdgeCppOSM> edges = node.getEdges();

            for (final EdgeCppOSM edge : edges)
                // TODO the contains is a linear search with the arraylist -tbach
                if (!processedNodes.contains(edge.getNode1().getId()) && !processedNodes.contains(edge.getNode2().getId())) {
                    graph.addEdge(edgeNumber, edge.getNode1().getId(), edge.getNode2().getId());
                    rightEdgesWithID.put(edgeNumber, edge);
                    edgeNumber++;
                }
            processedNodes.add(node.getId());
        }
        rightVisViewer.setGraphLayout(new ISOMLayout<>(graph));
    }

    public void createLayout(NodeLayout layout) {
        Graph<Long, Long> leftGraph = leftVisViewer.getGraphLayout().getGraph();
        Graph<Long, Long> rightGraph = rightVisViewer.getGraphLayout().getGraph();

        switch (layout) {
        case ISOMLayout: {
            leftVisViewer.setGraphLayout(new ISOMLayout<>(leftGraph));
            rightVisViewer.setGraphLayout(new ISOMLayout<>(rightGraph));
        }
        case KKLayout: {
            leftVisViewer.setGraphLayout(new KKLayout<>(leftGraph));
            rightVisViewer.setGraphLayout(new KKLayout<>(rightGraph));
        }
        }
    }

}
