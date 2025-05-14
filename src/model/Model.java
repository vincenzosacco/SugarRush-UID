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

    // MODEL //
    private final Game game = new Game();

    public Game getGame(){
        return game;
    }

}
