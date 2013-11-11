package edu.uaskl.cpp.visualization;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

//import edu.uci.ics.jung.algorithms.layout.KKLayout;
//import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
//import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uaskl.cpp.model.edge.EdgeCppOSM;
import edu.uaskl.cpp.model.node.NodeCppOSM;
import edu.uaskl.cpp.model.graph.GraphUndirected;
import edu.uaskl.cpp.importer.OsmImporter;

public class ControlBox extends Box {

    private static final long serialVersionUID = -626996308310236812L;

    private VisualizationViewer<Integer, Integer> visViewer;

    public ControlBox(VisualizationViewer<Integer, Integer> viewer) {
        super(BoxLayout.Y_AXIS);

        visViewer = viewer;

        add(createEdgeTypeRadioButtons());
        add(createEdgeLabelAlignmentCheckBox());
        add(createMouseInteractionComboBox());

        drawGraph();
    }

    private void drawGraph() {
        Graph<Integer, Integer> graph = createOSMGraph();

        visViewer.setGraphLayout(new ISOMLayout<>(graph));
    }

    private SparseMultigraph<Integer, Integer> createOSMGraph() {
        SparseMultigraph<Integer, Integer> graph = new SparseMultigraph<>();

        GraphUndirected<NodeCppOSM, EdgeCppOSM> osmGraph;
        osmGraph = OsmImporter
                .importOsmUndirected("src/test/resources/edu/uaskl/cpp/fh_way_no_meta.osm");

        Collection<NodeCppOSM> nodes = osmGraph.getNodes();
        Iterator<NodeCppOSM> iterator = nodes.iterator();

        int edgeNumber = 0;
        List<Integer> processedNodes = new ArrayList<>();

        while (iterator.hasNext()) {
            NodeCppOSM node = iterator.next();
            graph.addVertex(node.hashCode());

            List<EdgeCppOSM> edges = node.getEdges();

            for (EdgeCppOSM edge : edges) {
                if (!processedNodes.contains(edge.getNode1().hashCode())
                        && !processedNodes.contains(edge.getNode2().hashCode())) {
                    graph.addEdge(edgeNumber, edge.getNode1().hashCode(), edge
                            .getNode2().hashCode());

                    edgeNumber++;
                }
            }
            processedNodes.add(node.hashCode());
        }

        return graph;
    }

    /**
     * Radio buttons for choosing an edge interpolation.
     */
    private JPanel createEdgeTypeRadioButtons() {
        final ButtonGroup edgeTypeGroup = new ButtonGroup();

        final JRadioButton edgeTypeLineRadio = new JRadioButton("Line");

        edgeTypeLineRadio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    visViewer.getRenderContext().setEdgeShapeTransformer(
                            new EdgeShape.Line<Integer, Integer>());
                    visViewer.repaint();
                }
            }
        });

        final JRadioButton edgeTypeQuadRadio = new JRadioButton("Quad Curve");

        edgeTypeQuadRadio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    visViewer.getRenderContext().setEdgeShapeTransformer(
                            new EdgeShape.QuadCurve<Integer, Integer>());
                    visViewer.repaint();
                }
            }
        });

        edgeTypeQuadRadio.setSelected(true);

        edgeTypeGroup.add(edgeTypeLineRadio);
        edgeTypeGroup.add(edgeTypeQuadRadio);

        final JPanel edgeTypePanel = new JPanel(new GridLayout(0, 1));
        edgeTypePanel.setBorder(BorderFactory.createTitledBorder("Edge Type"));

        edgeTypePanel.add(edgeTypeLineRadio);
        edgeTypePanel.add(edgeTypeQuadRadio);

        return edgeTypePanel;
    }

    /**
     * Checkbox for choosing normalized or parallel edge label alignment.
     */
    private JPanel createEdgeLabelAlignmentCheckBox() {
        final JCheckBox edgeLabelAlignmentCheckBox = new JCheckBox(
                "Parallel to Edge");

        edgeLabelAlignmentCheckBox.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                final AbstractButton button = (AbstractButton) e.getSource();
                visViewer.getRenderContext().getEdgeLabelRenderer()
                        .setRotateEdgeLabels(button.isSelected());
                visViewer.repaint();
            }
        });

        edgeLabelAlignmentCheckBox.setSelected(true);

        final JPanel edgeLabelAlignmentPanel = new JPanel();
        edgeLabelAlignmentPanel.setBorder(BorderFactory
                .createTitledBorder("Egde Label Alignment"));
        edgeLabelAlignmentPanel.add(edgeLabelAlignmentCheckBox);

        return edgeLabelAlignmentPanel;
    }

    /**
     * Combobox for choosing a mouse interaction mode.
     */
    @SuppressWarnings("unchecked")
    private JPanel createMouseInteractionComboBox() {
        final JPanel mouseInteractionModePanel = new JPanel();
        mouseInteractionModePanel.setBorder(BorderFactory
                .createTitledBorder("Mouse Interaction Mode"));
        mouseInteractionModePanel
                .add(((DefaultModalGraphMouse<Integer, Integer>) visViewer
                        .getGraphMouse()).getModeComboBox());

        return mouseInteractionModePanel;
    }

}