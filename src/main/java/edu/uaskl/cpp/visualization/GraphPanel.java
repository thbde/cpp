package edu.uaskl.cpp.visualization;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class GraphPanel extends JFrame {

    private static final long serialVersionUID = 4778930552624056527L;

    GraphPanel() {

        UIGraph graph = new UIGraph();
        graph.createGraphFromOSMFile("src/test/resources/edu/uaskl/cpp/fh_way_no_meta.osm");
        
        GraphSplitPane splitPane = new GraphSplitPane(graph);
        
        GraphControlBox controlBox = new GraphControlBox(graph);
        
        // Add to Frame
        add(splitPane);
        add(controlBox, BorderLayout.NORTH);
    }
    
    void showGraphPanel() {
        setTitle("CPP Visualization");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setVisible(true);
    }
    
}
