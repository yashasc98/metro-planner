package com.yashas.metro.planner.service;

import com.yashas.metro.planner.dto.Neighbor;
import com.yashas.metro.planner.dto.Node;
import com.yashas.metro.planner.dto.RouteResult;
import com.yashas.metro.planner.entity.Connection;
import com.yashas.metro.planner.entity.Station;
import com.yashas.metro.planner.repo.ConnectionRepo;
import com.yashas.metro.planner.repo.StationRepo;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Service for route planning and shortest path calculation between stations.
 */
@Service
public class RouteService implements RouteServiceInterface {
    private static final Logger logger = LoggerFactory.getLogger(RouteService.class);
    private final StationRepo stationRepo;
    private final ConnectionRepo connectionRepo;
    private final Map<String, List<Neighbor>> graph = new HashMap<>();
    private final Map<String, Station> stationMap = new HashMap<>();

    public RouteService(StationRepo stationRepo, ConnectionRepo connectionRepo) {
        this.stationRepo = stationRepo;
        this.connectionRepo = connectionRepo;
    }

    /**
     * Initializes the in-memory graph of stations and connections after bean construction.
     */
    @PostConstruct
    public void initGraph() {
        logger.info("Initializing in-memory graph for route planning");
        List<Station> stations = stationRepo.findAll();
        for (Station s : stations) {
            stationMap.put(s.getCode().toLowerCase(), s);
        }
        List<Connection> connections = connectionRepo.findAllWithStations();
        for (Connection c : connections) {
            String from = c.getFromStation().getCode().toLowerCase();
            String to = c.getToStation().getCode().toLowerCase();
            int weight = c.getTravelTime() != null ? c.getTravelTime() : 1;
            graph.computeIfAbsent(from, k -> new ArrayList<>()).add(new Neighbor(to, weight));
            graph.computeIfAbsent(to, k -> new ArrayList<>()).add(new Neighbor(from, weight));
        }
        logger.info("Graph initialization completed with {} stations and {} connections", stations.size(), connections.size());
    }

    @Override
    public RouteResult findShortestPath(String fromCode, String toCode) {
        logger.info("Calculating shortest path from {} to {}", fromCode, toCode);
        fromCode = fromCode.toLowerCase();
        toCode = toCode.toLowerCase();
        if (!graph.containsKey(fromCode) || !graph.containsKey(toCode)) {
            logger.warn("Invalid station code(s) provided: fromCode={}, toCode={}", fromCode, toCode);
            return new RouteResult(Collections.emptyList(), 0);
        }
        Map<String, Integer> dist = new HashMap<>();
        Map<String, String> prev = new HashMap<>();
        PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::getDist));
        dist.put(fromCode, 0);
        pq.add(new Node(fromCode, 0));
        while (!pq.isEmpty()) {
            Node curr = pq.poll();
            if (curr.getCode().equals(toCode)) break;
            if (curr.getDist() > dist.getOrDefault(curr.getCode(), Integer.MAX_VALUE)) continue;
            for (Neighbor neighbor : graph.getOrDefault(curr.getCode(), Collections.emptyList())) {
                int newDist = curr.getDist() + neighbor.getWeight();
                if (newDist < dist.getOrDefault(neighbor.getCode(), Integer.MAX_VALUE)) {
                    dist.put(neighbor.getCode(), newDist);
                    prev.put(neighbor.getCode(), curr.getCode());
                    pq.add(new Node(neighbor.getCode(), newDist));
                }
            }
        }
        List<Station> path = new LinkedList<>();
        String curr = toCode;
        while (prev.containsKey(curr)) {
            path.add(0, stationMap.get(curr));
            curr = prev.get(curr);
        }
        if (!fromCode.equals(toCode) && path.isEmpty()) {
            logger.info("No path found between {} and {}", fromCode, toCode);
            return new RouteResult(Collections.emptyList(), 0);
        }
        path.add(0, stationMap.get(fromCode));
        int totalTime = dist.getOrDefault(toCode, 0);
        logger.info("Shortest path found with total travel time: {} minutes", totalTime);
        return new RouteResult(path, totalTime);
    }
}
