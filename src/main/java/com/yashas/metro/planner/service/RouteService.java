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
    private final Map<Long, Set<Neighbor>> graph = new HashMap<>();
    private final Map<Long, Station> stationMap = new HashMap<>();
    private final Map<String, List<Long>> codeToIdsMap = new HashMap<>();

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
            stationMap.put(s.getId(), s);
            codeToIdsMap.computeIfAbsent(s.getCode().toLowerCase(), k -> new ArrayList<>()).add(s.getId());
        }
        List<Connection> connections = connectionRepo.findAllWithStations();
        for (Connection c : connections) {
            Long from = c.getFromStation().getId();
            Long to = c.getToStation().getId();
            int weight = c.getTravelTime() != null ? c.getTravelTime() : 1;
            graph.computeIfAbsent(from, k -> new HashSet<>()).add(new Neighbor(to.toString(), weight));
            graph.computeIfAbsent(to, k -> new HashSet<>()).add(new Neighbor(from.toString(), weight));
        }
        logger.info("Graph initialization completed with {} stations and {} connections", stations.size(), connections.size());
    }

    @Override
    public RouteResult findShortestPath(String fromCode, String toCode) {
        logger.info("Calculating shortest path from {} to {}", fromCode, toCode);
        fromCode = fromCode.toLowerCase();
        toCode = toCode.toLowerCase();
        List<Long> fromIds = codeToIdsMap.get(fromCode);
        List<Long> toIds = codeToIdsMap.get(toCode);
        if (fromIds == null || toIds == null) {
            logger.warn("Invalid station code(s) provided: fromCode={}, toCode={}", fromCode, toCode);
            return new RouteResult(Collections.emptyList(), 0);
        }
        // Try all combinations of fromId and toId, return the shortest path
        List<Station> bestPath = Collections.emptyList();
        int minTime = Integer.MAX_VALUE;
        for (Long fromId : fromIds) {
            for (Long toId : toIds) {
                Map<Long, Integer> dist = new HashMap<>();
                Map<Long, Long> prev = new HashMap<>();
                PriorityQueue<Node> pq = new PriorityQueue<>(Comparator.comparingInt(Node::dist));
                dist.put(fromId, 0);
                pq.add(new Node(fromId.toString(), 0));
                while (!pq.isEmpty()) {
                    Node curr = pq.poll();
                    Long currId = Long.valueOf(curr.code());
                    if (currId.equals(toId)) break;
                    if (curr.dist() > dist.getOrDefault(currId, Integer.MAX_VALUE)) continue;
                    for (Neighbor neighbor : graph.getOrDefault(currId, Collections.emptySet())) {
                        Long neighborId = Long.valueOf(neighbor.code());
                        int newDist = curr.dist() + neighbor.weight();
                        if (newDist < dist.getOrDefault(neighborId, Integer.MAX_VALUE)) {
                            dist.put(neighborId, newDist);
                            prev.put(neighborId, currId);
                            pq.add(new Node(neighbor.code(), newDist));
                        }
                    }
                }
                List<Station> path = new LinkedList<>();
                Long currId = toId;
                while (prev.containsKey(currId)) {
                    path.add(0, stationMap.get(currId));
                    currId = prev.get(currId);
                }
                if (!fromId.equals(toId) && path.isEmpty()) continue;
                path.add(0, stationMap.get(fromId));
                int totalTime = dist.getOrDefault(toId, 0);
                if (totalTime < minTime) {
                    minTime = totalTime;
                    bestPath = path;
                }
            }
        }
        if (bestPath.isEmpty()) {
            logger.info("No path found between {} and {}", fromCode, toCode);
            return new RouteResult(Collections.emptyList(), 0);
        }
        logger.info("Shortest path found with total travel time: {} minutes", minTime);
        return new RouteResult(bestPath, minTime);
    }
}
