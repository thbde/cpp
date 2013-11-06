package edu.uaskl.cpp.visualization;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JApplet;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.EdgeShape;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class UserInterface extends JApplet {
    private static final long serialVersionUID = -8412559010664936507L;

    private Graph<Integer, Number> graph;
    private VisualizationViewer<Integer, Number> visViewer;
    private GraphZoomScrollPane graphPane;

    private final DefaultModalGraphMouse<Integer, Number> graphMouse;

    private UserInterface() {
        graphMouse = new DefaultModalGraphMouse<>();
    }

    private void initUserInterface() {
        createGraph();
        createGUI();
    }

    /**
     * TEMP: Creates the jung graph.
     */
    private void createGraph() {

        // Creation of the actual graph.
        graph = new SparseMultigraph<>();
        createVertices();

        // Layout and viewer
        final Layout<Integer, Number> layout = new KKLayout<>(graph);
        visViewer = new VisualizationViewer<>(layout, new Dimension(1000, 700));
        visViewer.setBackground(Color.white);

        // Mouse interaction
        visViewer.setGraphMouse(graphMouse);
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);

        // Edge labels
        visViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<Number, String>() {
            @Override
            public String transform(final Number e) {
                return graph.getEndpoints(e).toString();
            }
        });

        // Centered edge labels
        final ConstantDirectionalEdgeValueTransformer<Integer, Number> edgeValueTransformer = new ConstantDirectionalEdgeValueTransformer<>(.5, .5);
        visViewer.getRenderContext().setEdgeLabelClosenessTransformer(edgeValueTransformer);

        // Colors
        visViewer.getRenderContext().setEdgeDrawPaintTransformer(new PickableEdgePaintTransformer<>(visViewer.getPickedEdgeState(), Color.black, Color.cyan));
        visViewer.getRenderContext().setVertexFillPaintTransformer(
                new PickableVertexPaintTransformer<>(visViewer.getPickedVertexState(), Color.red, Color.yellow));

        // Tool tip displaying node number
        visViewer.setVertexToolTipTransformer(new ToStringLabeller<Integer>());

        graphPane = new GraphZoomScrollPane(visViewer);
    }

    /**
     * TEMP: Dummy graph
     */
    private void createVertices() {
        for (int i = 0; i < 10; i++)
            graph.addVertex(i);

        graph.addEdge(new Double(Math.random()), 0, 1, EdgeType.DIRECTED);
        graph.addEdge(new Double(Math.random()), 2, 4, EdgeType.DIRECTED);
        graph.addEdge(new Double(Math.random()), 5, 3, EdgeType.DIRECTED);
        graph.addEdge(new Double(Math.random()), 8, 5, EdgeType.DIRECTED);
        graph.addEdge(new Double(Math.random()), 9, 2, EdgeType.DIRECTED);
        graph.addEdge(new Double(Math.random()), 2, 7, EdgeType.DIRECTED);
        graph.addEdge(new Double(Math.random()), 6, 9, EdgeType.DIRECTED);
        graph.addEdge(new Double(Math.random()), 6, 3, EdgeType.DIRECTED);
        graph.addEdge(new Double(Math.random()), 3, 0, EdgeType.DIRECTED);
        graph.addEdge(new Double(Math.random()), 8, 0, EdgeType.DIRECTED);
    }

    /**
     * Handles creation and layout of gui elements.
     */
    private void createGUI() {
        final Box controlBox = Box.createVerticalBox();

        controlBox.add(createEdgeTypeRadioButtons());
        controlBox.add(createEdgeLabelAlignmentCheckBox());
        controlBox.add(createMouseInteractionComboBox());

        final JFrame frame = new JFrame();
        frame.setTitle("Jung Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        final Container contentContainer = frame.getContentPane();

        contentContainer.add(graphPane);
        contentContainer.add(controlBox, BorderLayout.EAST);

        frame.pack();
        frame.setVisible(true);
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
                    visViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.Line<Integer, Number>());
                    visViewer.repaint();
                }
            }
        });

        final JRadioButton edgeTypeQuadRadio = new JRadioButton("Quad Curve");

        edgeTypeQuadRadio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    visViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.QuadCurve<Integer, Number>());
                    visViewer.repaint();
                }
            }
        });

        final JRadioButton edgeTypeCubicRadio = new JRadioButton("Cubic Curve");

        edgeTypeCubicRadio.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(final ItemEvent e) {
                if (e.getStateChange() == ItemEvent.SELECTED) {
                    visViewer.getRenderContext().setEdgeShapeTransformer(new EdgeShape.CubicCurve<Integer, Number>());
                    visViewer.repaint();
                }
            }
        });

        edgeTypeQuadRadio.setSelected(true);

        edgeTypeGroup.add(edgeTypeLineRadio);
        edgeTypeGroup.add(edgeTypeQuadRadio);
        edgeTypeGroup.add(edgeTypeCubicRadio);

        final JPanel edgeTypePanel = new JPanel(new GridLayout(0, 1));
        edgeTypePanel.setBorder(BorderFactory.createTitledBorder("Edge Type"));

        edgeTypePanel.add(edgeTypeLineRadio);
        edgeTypePanel.add(edgeTypeQuadRadio);
        edgeTypePanel.add(edgeTypeCubicRadio);

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
        edgeLabelAlignmentPanel.setBorder(BorderFactory.createTitledBorder("Egde Label Alignment"));
        edgeLabelAlignmentPanel.add(edgeLabelAlignmentCheckBox);

        return edgeLabelAlignmentPanel;
    }

    /**
     * Combobox for choosing a mouse interaction mode.
     */
    private JPanel createMouseInteractionComboBox() {
        final JPanel mouseInteractionModePanel = new JPanel();
        mouseInteractionModePanel.setBorder(BorderFactory.createTitledBorder("Mouse Interaction Mode"));
        mouseInteractionModePanel.add(graphMouse.getModeComboBox());

        return mouseInteractionModePanel;
    }

    public static void main(final String[] args) {
        final UserInterface ui = new UserInterface();
        ui.initUserInterface();
    }

}
