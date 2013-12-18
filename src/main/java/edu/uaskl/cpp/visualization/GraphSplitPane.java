package edu.uaskl.cpp.visualization;

import java.awt.Dimension;

import javax.swing.JSplitPane;

public class GraphSplitPane extends JSplitPane {

    private static final long serialVersionUID = 3932355170071356172L;

    private static final Dimension MINIMUM_SIZE = new Dimension(150, 150);
    private static final int DIVIDER_LOCATION = 600;

    GraphSplitPane(UIGraph graph) {
        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        setOneTouchExpandable(true);
        setDividerLocation(DIVIDER_LOCATION);

        getLeftComponent().setMinimumSize(MINIMUM_SIZE);
        getRightComponent().setMinimumSize(MINIMUM_SIZE);

        setLeftComponent(graph.createLeftGraphPane());
        setRightComponent(graph.createRightGraphPane());
    }
}
