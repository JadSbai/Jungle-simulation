// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines Cobra objects and represents their specific
 * characteristics within the simulation. It is a subclass of the animal class.
 * 
 * @author Jad Sbai, Saif Latifi
 * @version 22/2/21
 */
public class Cobra extends Animal {
    private static final ArrayList<Class> foodSources = new ArrayList<>() {
        {
            add(Monkey.class);
        }
    };
    private static final int MAX_AGE = 330;
    private static final int MAX_LITTER_SIZE = 10;
    private static final double BREEDING_PROBABILITY = 0.3;
    private static final int FOOD_VALUE = 25;

    /**
     * This constructor sets a Cobra animal. It allows their position to be set in
     * the field and allows them to be setup with a random age if needed. They are
     * nocturnal animals.
     * 
     * @param randomAge Boolean value of whether Cobras should start with a random
     *                  age
     * @param field     The field they are located in
     * @param location  The location within the field
     */
    public Cobra(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location, foodSources);
        this.isNocturnal = true;
    }

    /**
     * Check whether or not this cobra is to give birth at this step. New births
     * will be made into free adjacent locations.
     * 
     * @param newCobras A list to return newly born cobras.
     */
    protected void giveBirth(List<Animal> newCobras) {
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Cobra young = new Cobra(false, field, loc);
            newCobras.add(young);
        }
    }

    /**
     * This method gets the maximum age of a cobra
     * 
     * @return The maximum age as an integer
     */
    protected int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * This method gets the maximum litter size that a cobra can have
     * 
     * @return Maximum litter size as an integer
     */
    protected int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }

    /**
     * This method gets the breeding probabilty of a cobra
     * 
     * @return Breeding probability as a double
     */
    protected double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * This method gets the food value of the cobra if its eaten
     * 
     * @return The food value as an integer
     */
    protected int getFoodValue() {
        return FOOD_VALUE;
    }
}
