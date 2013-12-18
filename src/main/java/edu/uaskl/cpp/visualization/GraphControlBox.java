package edu.uaskl.cpp.visualization;

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

import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;

public class GraphControlBox extends JPanel {

    private static final long serialVersionUID = -626996308310236812L;

    UIGraph graph;

    public GraphControlBox(UIGraph graph) {
        super((LayoutManager) new FlowLayout(FlowLayout.LEFT));

        this.graph = graph;

        add(createMouseInteractionComboBox());
        add(createNodeLayoutComboBox());
        add(createEdgeTypeComboBox());
        add(createEdgeLabelAlignmentCheckBox());
    }

    /**
     * Combo box for choosing a node layout.
     */
    private JPanel createNodeLayoutComboBox() {
        final String[] nodeLayouts = { "ISOM", "KK" };
        final JComboBox<String> nodeLayoutComboBox = new JComboBox<>();
        nodeLayoutComboBox.setPreferredSize(new Dimension(150, 28));

        nodeLayoutComboBox.addItem(nodeLayouts[0]);
        nodeLayoutComboBox.addItem(nodeLayouts[1]);

        nodeLayoutComboBox.addItemListener(new ItemListener() {
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    if ((String)e.getItem() == nodeLayouts[0]) {
                        graph.createLayout(UIGraph.NodeLayout.ISOMLayout);
                    }
                    else if ((String)e.getItem() == nodeLayouts[1]) {
                        graph.createLayout(UIGraph.NodeLayout.KKLayout);
                    }
                }
            }
        });

        nodeLayoutComboBox.setSelectedIndex(0);

        final JPanel nodeLayoutPanel = new JPanel();
        nodeLayoutPanel.setPreferredSize(new Dimension(180, 65));
        nodeLayoutPanel.setBorder(BorderFactory.createTitledBorder("Node Layout"));

        nodeLayoutPanel.add(nodeLayoutComboBox);

        return nodeLayoutPanel;
    }

    /**
     * Combo box for choosing an edge interpolation.
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
                        graph.getLeftVisualizationViewer().getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Long, Long>());
                        graph.getLeftVisualizationViewer().repaint();
                        graph.getRightVisualizationViewer().getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Long, Long>());
                        graph.getRightVisualizationViewer().repaint();
                    }
                    else if ((String)e.getItem() == edgeTypes[1]) {
                        graph.getLeftVisualizationViewer().getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Long, Long>());
                        graph.getLeftVisualizationViewer().repaint();
                        graph.getRightVisualizationViewer().getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Long, Long>());
                        graph.getRightVisualizationViewer().repaint();
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
                graph.getLeftVisualizationViewer().getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(button.isSelected());
                graph.getLeftVisualizationViewer().repaint();
                graph.getRightVisualizationViewer().getRenderContext().getEdgeLabelRenderer().setRotateEdgeLabels(button.isSelected());
                graph.getRightVisualizationViewer().repaint();
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
        JComboBox<String> graphMouseComboBox = ((DefaultModalGraphMouse<Integer, Integer>) graph.getLeftVisualizationViewer().getGraphMouse()).getModeComboBox();
        graphMouseComboBox.setPreferredSize(new Dimension(150, 28));
        // TODO create ordinary combobox!!
        mouseInteractionModePanel.add(graphMouseComboBox);

        return mouseInteractionModePanel;
    }

}
