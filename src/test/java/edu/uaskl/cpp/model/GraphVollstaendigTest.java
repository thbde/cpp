package edu.uaskl.cpp.model;

import static org.fest.assertions.api.Assertions.*;

import org.junit.Test;

import edu.uaskl.cpp.model.graph.GraphComplete;

public class GraphVollstaendigTest {

    @Test
    public void hasEulercircleTrue() {
        assertThat(new GraphComplete(19).getAlgorithms().hasEulerCircle()).isTrue();
    }

    @Test
    public void hasEulerCircleFalse() {
        assertThat(new GraphComplete(20).getAlgorithms().hasEulerCircle()).isTrue();
    }

    @Test
    public void isConnected20True() {
        assertThat(new GraphComplete(20).getAlgorithms().isConnected()).isTrue();
    }

    @Test
    public void isConnected1000True() {
        assertThat(new GraphComplete(1000).getAlgorithms().isConnected()).isTrue();
    }

    @Test
    public void isConnectedFalse() {
        fail("Not yet implemented");
    }
}
