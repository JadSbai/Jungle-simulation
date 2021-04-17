// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines Tiger objects and represents their specific
 * characteristics within the simulation. It is a subclass of the animal class.
 * 
 * @author Jad Sbai, Saif Latifi
 * @version 22/2/21
 */
public class Tiger extends Animal {

    private static final ArrayList<Class> foodSources = new ArrayList<>() {
        {
            add(Monkey.class);
            add(Elephant.class);
            add(Jaguar.class);
        }
    };
    private static final int MAX_AGE = 400;
    private static final int MAX_LITTER_SIZE = 3;
    private static final double BREEDING_PROBABILITY = 0.2;
    private static final int FOOD_VALUE = 30;

    /**
     * This constructor sets a Tiger. It allows their position to be set in the
     * field and allows them to be setup with a random age if needed
     * 
     * @param randomAge Boolean value of whether tigers should start with a random
     *                  age
     * @param field     The field they are located in
     * @param location  The location within the field
     */
    public Tiger(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location, foodSources);

    }

    /**
     * Check whether or not this tiger is to give birth at this step. New births
     * will be made into free adjacent locations.
     * 
     * @param newTigers A list to return newly born tigers.
     */
    protected void giveBirth(List<Animal> newTigers) {
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Tiger young = new Tiger(false, field, loc);
            newTigers.add(young);
        }
    }

    /**
     * This method gets the maximum age of a Tiger
     * 
     * @return The maximum age as an integer
     */
    protected int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * This method gets the maximum litter size that a tiger can have
     * 
     * @return Maximum litter size as an integer
     */
    protected int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }

    /**
     * This method gets the breeding probabilty of a tiger
     * 
     * @return Breeding probability as a double
     */
    protected double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * This method gets the food value of the tiger if its eaten
     * 
     * @return The food value as an integer
     */
    protected int getFoodValue() {
        return FOOD_VALUE;
    }
}
