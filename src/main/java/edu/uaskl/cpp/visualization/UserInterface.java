package edu.uaskl.cpp.visualization;

import javax.swing.JApplet;

public class UserInterface extends JApplet {

    private static final long serialVersionUID = -8412559010664936507L;
    private static GraphPanel graphPanel;

    private UserInterface() {
        graphPanel = new GraphPanel();
    }

    public static void main(final String[] args) {
        final UserInterface ui = new UserInterface();
        ui.runApp();
    }
    
    private void runApp() {
        graphPanel.showGraphPanel();
    }

}
