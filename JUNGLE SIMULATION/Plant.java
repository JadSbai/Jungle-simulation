// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class defines a plant and its properites. Prey eat plants to survive.
 * This class is a subclass of the Organism class.
 * 
 * @author Jad Sbai, Saif Latifi
 * @version 22/2/21
 */
public class Plant extends Organism {
    private static final double SPREAD_PROBABILITY = 0.4;
    private static final int FOOD_VALUE = 30;
    private static final int MAX_AGE = 200;
    // Max number of seeds a plant can produce per step
    private static final int MAX_SEEDS = 3;
    private static final Random rand = Randomizer.getRandom();

    // Represents the current amount of water the plant has
    private int currentWater;

    /**
     * This initializes the plant objects by setting their location on the field,
     * initalizing their age and their water level.
     * 
     * @param field    The field its located it
     * @param location The location of the field that the plant is in
     */
    public Plant(Field field, Location location) {
        super(field, location);
        currentWater = 4;
    }

    /**
     * This method gets the maximum age of the plant
     * 
     * @return The maximum age an integer
     */
    protected int getMaxAge() {
        return MAX_AGE;
    }

    /**
     * This method gets the food value of the plant if it's eaten
     * 
     * @return The food value as an integer
     */
    protected int getFoodValue() {
        return FOOD_VALUE;
    }

    /**
     * Makes the plants take action during one step of the simulation. It involves
     * reproducing and updating their health based on weather (if it is warm
     * enough).
     * 
     * @return The list of new plants reproduced.
     */
    public List<Plant> act() {
        updateHealth();
        if (isAlive()) {
            Weather currentWeather = getField().getWeather();
            if (currentWeather.getCurrentTemperature() > Weather.getMinimumTemperature()) {
                return reproduce();
            }
        }
        return null;
    }

    /**
     * This method allows plants to reproduce creating offspring at random
     * locations, taking into account the probability of having a child.
     * 
     * @return A list of new plants
     */
    protected List<Plant> reproduce() {
        incrementAge();

        if (isAlive()) {
            List<Plant> newPlants = new ArrayList<>();
            for (int i = 0; i < MAX_SEEDS; i++) {
                Location newLocation = getField().findFreeRandomLocation();
                if (rand.nextDouble() <= SPREAD_PROBABILITY && newLocation != null) {
                    Plant newPlant = new Plant(getField(), newLocation);
                    newPlants.add(newPlant);
                }
            }
            return newPlants;
        }
        return null;
    }

    /**
     * This method updates the overall health of the plant based on the weather
     * parameters (i.e., temperature and rain level)
     */
    private void updateHealth() {
        if (isAlive()) {
            Weather currentWeather = getField().getWeather();

            if (currentWeather.getCurrentRainLevel() > 0) {
                currentWater++;
            }
            if (currentWeather.getCurrentTemperature() > 35) {
                currentWater -= 2;
            }
            if (currentWater < 0) {
                setDead();
            }
        }
    }
}