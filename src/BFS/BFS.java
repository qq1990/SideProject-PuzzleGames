package BFS;

import java.util.*;
import BFS.Configuration;

/**
 * Solver, get configuration in and find the shortest path using bfs
 *
 * @author Quan Quy
 */

public class BFS {
    private Configuration goalConfig;
    private long numConfigs;
    private long uniqueConfigs;

    /**
     * Initialize number of configs and unique configs
     *
     */
    public BFS() {
        this.numConfigs = 0;
        this.uniqueConfigs = 0;
    }

    /**
     * get number of total configs
     * @return number of configs
     */
    public long getNumConfigs() {
        return numConfigs;
    }

    /**
     * get number of unique configs
     * @return number of unique configs
     */
    public long getUniqueConfigs() {
        return uniqueConfigs;
    }

    /**
     * get the shortest path, given config
     * @param config get the config in
     * @return shortest path
     */
    public Collection<Configuration> getShortestPath(Configuration config) {
        List<Configuration> queue = new LinkedList<>();
        queue.add(config);

        Map<Configuration, Configuration> predecessors = new HashMap<>();
        predecessors.put(config, config);

        while (!queue.isEmpty()) {
            Configuration current = queue.remove(0);
            if (current.isGoal()) {
                this.goalConfig = current;
                break;
            }
            for (Configuration nbr : current.getSuccessors()) {
                numConfigs += 1;
                if(!predecessors.containsKey(nbr)) {
                    predecessors.put(nbr, current);
                    queue.add(nbr);
                }
            }
        }
        this.uniqueConfigs = predecessors.size();
        return constructPath(predecessors, config, goalConfig);
    }

    /**
     * build a path
     * @param predecessors configs already visited
     * @param startConfig start config
     * @param goalConfig end config
     * @return path from start config to end config
     */
    private List<Configuration> constructPath(Map<Configuration, Configuration> predecessors,
                                              Configuration startConfig, Configuration goalConfig) {
        List<Configuration> path = new LinkedList<>();

        if(predecessors.containsKey(goalConfig)) {
            Configuration currConfig = goalConfig;
            while (currConfig != startConfig) {
                path.add(0, currConfig);
                currConfig = predecessors.get(currConfig);
            }
            path.add(0, startConfig);
        }
        return path;
    }

}
