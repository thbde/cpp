package edu.uaskl.cpp.visualization;

import java.awt.Dimension;
import java.awt.Color;
import java.awt.BorderLayout;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.visualization.GraphZoomScrollPane;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.KKLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.control.ModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;
import edu.uci.ics.jung.visualization.decorators.ConstantDirectionalEdgeValueTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableEdgePaintTransformer;
import edu.uci.ics.jung.visualization.decorators.PickableVertexPaintTransformer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;

public class UserInterface extends JApplet {

    private static final long serialVersionUID = -8412559010664936507L;

    VisualizationViewer<Integer, Integer> visViewer;

    private UserInterface() {
    }

    public static void main(final String[] args) {
        final UserInterface ui = new UserInterface();
        ui.initUserInterface();
    }

    private void initUserInterface() {
        createGUI();
    }

    private void createGUI() {
        final JFrame frame = new JFrame();
        frame.setTitle("CPP Visualization");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JSplitPane graphSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createEmptyGraph(), createEmptyGraph());
        graphSplitPane.setOneTouchExpandable(true);
        graphSplitPane.setDividerLocation(600);

        Dimension minimumSize = new Dimension(150, 150);
        graphSplitPane.getComponent(0).setMinimumSize(minimumSize);
        graphSplitPane.getComponent(1).setMinimumSize(minimumSize);
        
        frame.add(graphSplitPane);
        frame.add(new ControlBox(visViewer), BorderLayout.NORTH);
        
        frame.pack();
        frame.setVisible(true);
    }

    private GraphZoomScrollPane createEmptyGraph() {
        final Graph<Integer, Integer> graph = new SparseMultigraph<>();

        final Layout<Integer, Integer> layout = new KKLayout<>(graph);
        visViewer = new VisualizationViewer<>(layout);

        // Mouse Interaction
        final DefaultModalGraphMouse<Integer, Integer> graphMouse = new DefaultModalGraphMouse<>();
        graphMouse.setMode(ModalGraphMouse.Mode.TRANSFORMING);
        visViewer.setGraphMouse(graphMouse);

        // Edge labels
        visViewer.getRenderContext().setEdgeLabelTransformer(new Transformer<Integer, String>() {
            @Override
            public String transform(final Integer e) {
                return "Stub";
            }
        });

        // Centered edge labels
        final ConstantDirectionalEdgeValueTransformer<Integer, Integer> edgeValueTransformer = 
                new ConstantDirectionalEdgeValueTransformer<>(.5, .5);
        visViewer.getRenderContext().setEdgeLabelClosenessTransformer(edgeValueTransformer);

        // Colors
        visViewer.setBackground(Color.white);
        visViewer.getRenderContext().setEdgeDrawPaintTransformer(
                new PickableEdgePaintTransformer<>(visViewer.getPickedEdgeState(), Color.black, Color.cyan));
        visViewer.getRenderContext().setVertexFillPaintTransformer(
                new PickableVertexPaintTransformer<>(visViewer.getPickedVertexState(), Color.red, Color.yellow));

        // Tool tip displaying node number
        visViewer.setVertexToolTipTransformer(new ToStringLabeller<Integer>());

        return new GraphZoomScrollPane(visViewer);
    }

}
