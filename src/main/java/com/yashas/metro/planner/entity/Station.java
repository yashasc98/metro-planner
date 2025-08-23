package com.yashas.metro.planner.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Entity
public class Station {

	@Id
	private Long id;
	private String code;
	private String city;
	private String name;
	private String line;
	private Character active;

}
