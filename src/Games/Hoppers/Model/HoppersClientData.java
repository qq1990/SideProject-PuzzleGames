package Games.Hoppers.Model;

/**
 * Hopper Client Data, contains a string carry message
 * @author Quan Quy
 */
public class HoppersClientData {
    String data;

    /**
     * construct hopper client data
     * @param data string in
     */
    public HoppersClientData(String data){
        this.data = data;
    }

    /**
     * override toString
     * @return this data
     */
    @Override
    public String toString() {
        return data;
    }
}
