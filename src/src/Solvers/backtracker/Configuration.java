package Solvers.backtracker;
import java.util.Collection;

/**
 * @author Quan Quy
 */
public interface Configuration {
    /**
     * get successors for Solvers.backtracker
     * @return a collection configurations
     */
    Collection<Configuration> getSuccessors();

    /**
     * check if this configuration is valid or not
     * @return true if valid, false otherwise
     */
    boolean isValid();

    /**
     * check if this configuration is goal
     * @return true is is goal
     */
    boolean isGoal();
}