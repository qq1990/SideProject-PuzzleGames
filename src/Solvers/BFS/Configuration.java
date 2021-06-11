package Solvers.BFS;

import java.util.Collection;

/**
 * The representation of a single configuration for a puzzle.
 *
 *
 * @author Quan Quy
 */
public interface Configuration {
    /**
     * Get the collection of successors from the current one.
     *
     * @return All successors, valid and invalid
     */
    Collection<Configuration> getSuccessors();

    /**
     * Is the current configuration a goal?
     * @return true if goal; false otherwise
     */
    boolean isGoal();
}
