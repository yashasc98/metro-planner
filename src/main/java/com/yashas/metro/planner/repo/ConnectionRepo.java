package com.yashas.metro.planner.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.yashas.metro.planner.entity.Connection;

@Repository
public interface ConnectionRepo extends JpaRepository<Connection, Long> {
    @Query("SELECT c FROM Connection c JOIN FETCH c.fromStation JOIN FETCH c.toStation")
    List<Connection> findAllWithStations();
}
