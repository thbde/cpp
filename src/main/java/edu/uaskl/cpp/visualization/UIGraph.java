package edu.uaskl.cpp.visualization;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.collections15.Transformer;

import edu.uaskl.cpp.importer.OsmImporter;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
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

    private VisualizationViewer<Integer, Integer> leftVisViewer;
    private VisualizationViewer<Integer, Integer> rightVisViewer;
    
    public UIGraph() {
        // Empty Graphs
        leftVisViewer = new VisualizationViewer<>(new KKLayout<>(new SparseMultigraph<Integer, Integer>()));
        rightVisViewer = new VisualizationViewer<>(new KKLayout<>(new SparseMultigraph<Integer, Integer>()));
        
        // Mouse Interaction
        final DefaultModalGraphMouse<Integer, Integer> graphMouse = new DefaultModalGraphMouse<>();
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        leftVisViewer.setGraphMouse(graphMouse);
        rightVisViewer.setGraphMouse(graphMouse);

        // Edge labels
        leftVisViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<Integer, String>() {
            @Override
            public String transform(final Integer e) {
                return "Stub";
            }
        });
        rightVisViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<Integer, String>() {
            @Override
            public String transform(final Integer e) {
                return "Stub";
            }
        });

        // Centered edge labels
        final ConstantDirectionalEdgeValueTransformer<Integer, Integer> edgeValueTransformer = 
                new ConstantDirectionalEdgeValueTransformer<>(.5, .5);
        leftVisViewer.getRenderContext().setEdgeLabelClosenessTransformer(edgeValueTransformer);
        rightVisViewer.getRenderContext().setEdgeLabelClosenessTransformer(edgeValueTransformer);

        // Colors
        leftVisViewer.setBackground(Color.white);
        leftVisViewer.getRenderContext().setEdgeDrawPaintTransformer(
                new PickableEdgePaintTransformer<>(leftVisViewer.getPickedEdgeState(), Color.black, Color.cyan));
        leftVisViewer.getRenderContext().setVertexFillPaintTransformer(
                new PickableVertexPaintTransformer<>(leftVisViewer.getPickedVertexState(), Color.red, Color.yellow));
        rightVisViewer.setBackground(Color.white);
        rightVisViewer.getRenderContext().setEdgeDrawPaintTransformer(
                new PickableEdgePaintTransformer<>(rightVisViewer.getPickedEdgeState(), Color.black, Color.cyan));
        rightVisViewer.getRenderContext().setVertexFillPaintTransformer(
                new PickableVertexPaintTransformer<>(rightVisViewer.getPickedVertexState(), Color.red, Color.yellow));

        // Tool tip displaying node number
        leftVisViewer.setVertexToolTipTransformer(new ToStringLabeller<Integer>());
        rightVisViewer.setVertexToolTipTransformer(new ToStringLabeller<Integer>());
    }
    
    public VisualizationViewer<Integer, Integer> getLeftVisualizationViewer() {
        return leftVisViewer;
    }
    
    public VisualizationViewer<Integer, Integer> getRightVisualizationViewer() {
        return rightVisViewer;
    }
    
    public GraphZoomScrollPane createLeftGraphPane() {
        return new GraphZoomScrollPane(leftVisViewer);
    }
    
    public GraphZoomScrollPane createRightGraphPane() {
        return new GraphZoomScrollPane(rightVisViewer);
    }
    
    public void createGraphFromOSMFile(String file) {
        final SparseMultigraph<Integer, Integer> graph = new SparseMultigraph<>();
    
        GraphUndirected<NodeCppOSM, EdgeCppOSM> osmGraph;
        osmGraph = OsmImporter.importOsmUndirected(file);
    
        final Collection<NodeCppOSM> nodes = osmGraph.getNodes();
        final Iterator<NodeCppOSM> iterator = nodes.iterator();
    
        int edgeNumber = 0;
        final List<Integer> processedNodes = new ArrayList<>(); // you should use a hash* here, probably a hashset -tbach
    
        while (iterator.hasNext()) { // TODO you could use a foreach loop here -tbach
            final NodeCppOSM node = iterator.next();
            graph.addVertex(node.hashCode()); // TODO the id could be more interesting? -tbach
    
            final List<EdgeCppOSM> edges = node.getEdges();
    
            for (final EdgeCppOSM edge : edges)
                // TODO the contains is a linear search with the arraylist -tbach
                if (!processedNodes.contains(edge.getNode1().hashCode()) && !processedNodes.contains(edge.getNode2().hashCode())) {
                    graph.addEdge(edgeNumber, edge.getNode1().hashCode(), edge.getNode2().hashCode());
    
                    edgeNumber++;
                }
            processedNodes.add(node.hashCode());
        }
        
        leftVisViewer.setGraphLayout(new ISOMLayout<>(graph));
    }

}
