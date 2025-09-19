package com.yashas.metro.planner.dto;

public record Node(String code, int dist) implements Comparable<Node> {

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.dist, other.dist);
    }

}
