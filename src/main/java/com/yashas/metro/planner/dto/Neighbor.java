package com.yashas.metro.planner.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class Neighbor {
    private final String code;
    private final int weight;
}
