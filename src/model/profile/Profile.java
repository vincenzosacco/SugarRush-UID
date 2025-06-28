package model.profile;
import java.io.Serializable;

public class Profile implements Serializable {
    private static final long serialVersionUID = 1L;
    String name;
    private static int coins;

    public Profile(){
        this.name = "Player";
        this.coins = 0;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public static int getCoins() {
        return coins;
    }
    public static void setCoins(int c) {
        coins = c;
    }


}
