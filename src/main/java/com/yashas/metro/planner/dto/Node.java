package com.yashas.metro.planner.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Node implements Comparable<Node> {
    private final String code;
    private final int dist;

    @Override
    public int compareTo(Node other) {
        return Integer.compare(this.dist, other.dist);
    }
}
