package edu.uaskl.cpp.visualization;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.importer.OsmImporter;

public class ControlBox extends JPanel {

    private static final long serialVersionUID = -626996308310236812L;

    private final VisualizationViewer<Integer, Integer> visViewer;

    public ControlBox(final VisualizationViewer<Integer, Integer> viewer) {
        super((LayoutManager) new FlowLayout(FlowLayout.LEFT));
        
        visViewer = viewer;

        add(createMouseInteractionComboBox());
        add(createEdgeTypeComboBox());
        add(createEdgeLabelAlignmentCheckBox());

        drawGraph();
    }

    private void drawGraph() {
        final Graph<Integer, Integer> graph = createOSMGraph();

        visViewer.setGraphLayout(new ISOMLayout<>(graph));
    }

    private SparseMultigraph<Integer, Integer> createOSMGraph() {
        final SparseMultigraph<Integer, Integer> graph = new SparseMultigraph<>();

        GraphUndirected<NodeCppOSM, EdgeCppOSM> osmGraph;
        osmGraph = OsmImporter.importOsmUndirected("src/test/resources/edu/uaskl/cpp/fh_way_no_meta.osm");

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

        return graph;
    }

    /**
     * Radio buttons for choosing an edge interpolation.
     */
    private JPanel createEdgeTypeComboBox() {
        final String[] edgeTypes = { "Line", "Quad Curve" };
        final JComboBox<String> edgeTypeComboBox = new JComboBox<>();
        edgeTypeComboBox.setPreferredSize(new Dimension(150, 28));

        edgeTypeComboBox.addItem(edgeTypes[0]);
        edgeTypeComboBox.addItem(edgeTypes[1]);
        
        edgeTypeComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if ((String)e.getItem() == edgeTypes[0]) {
                        visViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Integer, Integer>());
                        visViewer.repaint();
                    }
                    else if ((String)e.getItem() == edgeTypes[1]) {
                        visViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Integer, Integer>());
                        visViewer.repaint();
                    }
                }
            }
        });

        edgeTypeComboBox.setSelectedIndex(1);
        
        final JPanel edgeTypePanel = new JPanel();
        edgeTypePanel.setPreferredSize(new Dimension(180, 65));
        edgeTypePanel.setBorder(BorderFactory.createTitledBorder("Edge Type"));

        edgeTypePanel.add(edgeTypeComboBox);
        
        return edgeTypePanel;
    }

    /**
     * Checkbox for choosing normalized or parallel edge label alignment.
     */
    private JPanel createEdgeLabelAlignmentCheckBox() {
        final JCheckBox edgeLabelAlignmentCheckBox = new JCheckBox("Parallel to Edge");

        edgeLabelAlignmentCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                final AbstractButton button = (AbstractButton) e.getSource();
                visViewer.getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(button.isSelected());
                visViewer.repaint();
            }
        });

        edgeLabelAlignmentCheckBox.setSelected(true);

        final JPanel edgeLabelAlignmentPanel = new JPanel();
        edgeLabelAlignmentPanel.setPreferredSize(new Dimension(180, 65));
        edgeLabelAlignmentPanel.setBorder(BorderFactory.createTitledBorder("Egde Label Alignment"));
        edgeLabelAlignmentPanel.add(edgeLabelAlignmentCheckBox);

        return edgeLabelAlignmentPanel;
    }

    /**
     * Combobox for choosing a mouse interaction mode.
     */
    @SuppressWarnings("unchecked")
    private JPanel createMouseInteractionComboBox() {
        final JPanel mouseInteractionModePanel = new JPanel();
        mouseInteractionModePanel.setPreferredSize(new Dimension(180, 65));
        mouseInteractionModePanel.setBorder(BorderFactory.createTitledBorder("Mouse Interaction Mode"));
        JComboBox<String> graphMouseComboBox = ((DefaultModalGraphMouse<Integer, Integer>) visViewer.getGraphMouse()).getModeComboBox();
        graphMouseComboBox.setPreferredSize(new Dimension(150, 28));
        
        mouseInteractionModePanel.add(graphMouseComboBox);
        
        return mouseInteractionModePanel;
    }

}