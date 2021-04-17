// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.ArrayList;
import java.util.List;

/**
 * This class defines Elephant objects and represents their specific
 * characteristics within the simulation. This class is a subclass of the Animal
 * class.
 * 
 * @author Jad Sbai, Saif Latifi
 * @version 22/2/21
 */
public class Elephant extends Animal {
    private static final ArrayList<Class> foodSources = new ArrayList<>() {
        {
            add(Plant.class);
            add(PoisonousPlant.class);
        }
    };
    private static final int MAX_AGE = 150;
    private static final int MAX_LITTER_SIZE = 2;
    private static final double BREEDING_PROBABILITY = 0.1;
    private static final int FOOD_VALUE = 50;

    /**
     * This constructor sets a Elephant. It allows their position to be set in the
     * field and allows them to be setup with a random age if needed
     * 
     * @param randomAge Boolean value of whether Elephants should start with a
     *                  random age
     * @param field     The field they are located in
     * @param location  The location within the field
     */
    public Elephant(boolean randomAge, Field field, Location location) {
        super(randomAge, field, location, foodSources);

    }

    /**
     * Check whether or not this elephant is to give birth at this step. New births
     * will be made into free adjacent locations.
     * 
     * @param newElephants A list to return newly born elephants.
     */
    protected void giveBirth(List<Animal> newElephants) {
        // Get a list of adjacent free locations.
        Field field = getField();
        List<Location> free = field.getFreeAdjacentLocations(getLocation());
        int births = breed();
        for (int b = 0; b < births && free.size() > 0; b++) {
            Location loc = free.remove(0);
            Elephant young = new Elephant(false, field, loc);
            newElephants.add(young);
        }
    }

    /**
     * This method gets the maximum age of a Elephant
     * 
     * @return The maximum age as an integer
     */
    protected int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * This method gets the maximum litter size that a Elephant can have
     * 
     * @return Maximum litter size as an integer
     */
    protected int getMaxLitterSize() {
        return MAX_LITTER_SIZE;
    }

    /**
     * This method gets the breeding probabilty of a Elephant
     * 
     * @return Breeding probability as a double
     */
    protected double getBreedingProbability() {
        return BREEDING_PROBABILITY;
    }

    /**
     * This method gets the food value of the Elephant if its eaten
     * 
     * @return The food value as an integer
     */
    protected int getFoodValue() {
        return FOOD_VALUE;
    }
}
