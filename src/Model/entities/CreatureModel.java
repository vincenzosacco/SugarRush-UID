package Model.entities;

/**
 * Model for Creature entity
 */
public class CreatureModel {
    private int sugarCount = 0;


    public void addSugar(){
        sugarCount++;
    }

    public int getSugarCount(){
        return sugarCount;
    }


}
