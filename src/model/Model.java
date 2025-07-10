package model;

import model.game.Game;

/**
 * Singleton Interface to the Model.
 */
public class Model {
    // SINGLETON //
    private static Model instance = null;

    private Model(){
    }

    public static Model getInstance(){
        if(instance == null){
            instance = new Model();
        }
        return instance;
    }

    public Game getGame(){
        return Game.getInstance();
    }


}
