package Games.RushHour.solver;

import Solvers.BFS.Configuration;
import Solvers.BFS.Observer;
import Games.RushHour.exceptions.NoCarException;
import Games.RushHour.model.JamModel;
import Games.RushHour.model.JamConfig;
import Games.RushHour.model.JamClientData;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

/**
 * implement observer in Jam but give the implementation to whoever instantiates Jam
 * This is done through the onUpdate field
 * @author Rohan Rao
 */
public class Jam implements Observer<JamModel, JamClientData> {

    private JamModel model;
    //private Configuration currentConfig;
    private Queue<JamConfig> queue;
    private Integer x, y;
    private Character carLetter;
    private Consumer<JamClientData> onUpdate;
    public Jam(char[][] initialConfig){
        model = new JamModel(new JamConfig(initialConfig));
        model.addObserver(this);
        //currentConfig = new JamConfig(initialConfig);
        queue = new LinkedList<>();
        queue.add(model.getCurrentConfig());
        x = null;
        y = null;
        carLetter = null;
    }

    /**
     *
     * @return a boolean representing if a Car has been selected
     */
    public boolean hasSelected(){
        return x != null && y != null && carLetter != null;
    }

    /**
     * The hint method generates a configuration representing the next step towards the solution
     * It does that by solving the currentConfig and tracing all the way back to the second configuration generated
     * Doesn't store the solution because if the user moves manually, it most likely will invalidate current solution
     * @return a boolean representing whether or not a hint can be generated
     */
    public boolean hint(){
       if(! solve()){
           return false;
        }
       JamConfig temp = model.getCurrentConfig();
       // Go to the second to last previousState, representing the state after the current state
       while (temp.getPreviousState().getPreviousState() != null){
           temp = temp.getPreviousState();
       }
       model.setCurrentConfig(temp);
       // Clear the queue because what's in it has been invalidated by the generation of a hint
       queue.clear();
       // Read and reset previous state to null to act as if the current state is the initial state
       queue.add(model.getCurrentConfig());
       model.getCurrentConfig().setPreviousState(null);
       return true;
    }

    /**
     * Takes two ints, representing the row and column of a car to move
     * If no car is currently selected, it selects the car
     * Checks to see if there is a car at the position and if there isn't it throws a NoCarException
     * @param x row of the car
     * @param y column of the car
     * @return a boolean representing whether or not the move is legal
     * @throws NoCarException thrown when the specified position does not contain a car
     */
    public boolean move(int x, int y) throws NoCarException{
        JamConfig temp = model.getCurrentConfig();
        if (! hasSelected()){
            if (temp.getCarLetterAt(x, y) == '.' || temp.getCarLetterAt(x, y) == null){

                throw new NoCarException("No car at (" + y + ", " + x + ")");
            }
            this.x = x;
            this.y = y;
            carLetter = temp.getCarLetterAt(x, y);
            return true;
        }
        JamConfig temp2 = temp.move(carLetter, x, y);
        if (temp2 == null){
            this.x = null;
            this.y = null;
            carLetter = null;
            return false;
        }
        model.setCurrentConfig(temp2);
        queue.clear();
        queue.add(temp2);
        temp2.setPreviousState(null);
        this.x = null;
        this.y = null;
        carLetter = null;
        return true;
    }

    /**
     * Solves the current config using a bfs which guarantees shortest path
     * @return a boolean representing whether or not the board is solvable
     */
    public boolean solve(){

        // To remember what states we have already been in to avoid infinitely moving up & down / left & right
        Set<String> seen = new HashSet<>();
        while (! queue.isEmpty()){
            // Quiet set to not alert observers
            model.quietSetCurrentConfig(queue.remove());
            if (model.getCurrentConfig().isGoal()){
                return true;
            }
            // Add all valid successors to the queue and add their toString version to the seen Set
            for (Configuration successor : model.getCurrentConfig().getSuccessors()) {
                if(successor == null || seen.contains(successor.toString())){
                    continue;
                }
                String serialized = successor.toString();
                seen.add(serialized);
                queue.add((JamConfig)successor);
            }
        }
        return false;
    }
    public boolean isGoal(){
        return model.getCurrentConfig().isGoal();
    }

    public Integer getX() {
        return x;
    }

    public Integer getY() {
        return y;
    }

    public JamModel getModel() {
        return model;
    }

    /**
     * The observed subject calls this method on each observer that has
     * previously registered with it. This version of the design pattern
     * follows the "push model" in that the subject can provide
     * ClientData to inform the observer about what exactly has happened.
     * But this convention is not required. It may still be necessary for
     * the observer to also query the subject to find out more about what has
     * happened. If this is a simple signal with no data attached,
     * or if it can safely be assumed that the observer already has a
     * reference to the subject, even the subject parameter may be null.
     * But as always this would have to be agreed to by designers of both sides.
     *
     * @param jamModel      the object that wishes to inform this object
     *                      about something that has happened.
     * @param jamClientData optional data the server.model can send to the observer
     * @see <a href="https://sourcemaking.com/design_patterns/observer">the
     * Observer design pattern</a>
     */
    @Override
    public void update(JamModel jamModel, JamClientData jamClientData) {
        if (onUpdate != null){
            onUpdate.accept(jamClientData);
        }
    }

    public void setOnUpdate(Consumer<JamClientData> onUpdate) {
        this.onUpdate = onUpdate;
    }
}