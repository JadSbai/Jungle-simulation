// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.Random;

/**
 * This class simulates weather within the field. It simulates rain and
 * temperate randomly, which directly affects plants.
 * 
 * @author Jad Sbai, Saif Latifi
 * @version 22/2/21
 */
public class Weather {
    // The maximum temperature reachable within the simulation
    private static final int MAX_TEMP = 40;
    // The minimum temperature reachable within the simulation
    private static final int MIN_TEMP = 10;
    // Max rain level for the simulation
    private static final int MAX_RAIN_LEVEL = 15;
    private static final int INITIAL_RAIN_LEVEL = 2;
    private static final Random rand = Randomizer.getRandom();

    // Field stores the current temperature of the simulation
    private int currentTemperature;
    // Stores the mm of rain currently available at each step within the simulation
    private int currentRainLevel;

    /**
     * Initialise the weather objects with a random temperature and sets the current
     * rain level to 2.
     */
    public Weather() {
        // Sets a random temperate greater than minimum temperature initially
        currentTemperature = rand.nextInt(MAX_TEMP - MIN_TEMP) + MIN_TEMP;
        // Initial rain level to prevent plants dying immediatetly
        currentRainLevel = INITIAL_RAIN_LEVEL;
    }

    /**
     * This method gets the minimum temperature for the simulation
     * 
     * @return The minimum temperature
     */
    public static int getMinimumTemperature() {
        return MIN_TEMP;
    }

    /**
     * Simulate the weather (temperature, rain...) for every step within the
     * simulation. It randomly changes the rain and temperature.
     */
    public void simulate() {
        increaseRainLevel();
        currentTemperature = rand.nextInt(MAX_TEMP - MIN_TEMP) + MIN_TEMP;
        decreaseRainLevel();
    }

    /**
     * Returns the current rain level on the field
     * 
     * @return the current rain level
     */
    public int getCurrentRainLevel() {
        return currentRainLevel;
    }

    /**
     * Returns the current temperature on the field
     * 
     * @return the current temperature
     */
    public int getCurrentTemperature() {
        return currentTemperature;
    }

    /**
     * Decreases the current rain level depending on the current temperature.
     */
    private void decreaseRainLevel() {
        if (currentTemperature > 30) {
            currentRainLevel -= 2;
        } else {
            currentRainLevel--;
        }

        // Preventing negitive rain level
        if (currentRainLevel < 0) {
            currentRainLevel = 0;
        }
    }

    /**
     * Increases the current rain level. It cannot exceed a certain limit.
     */
    private void increaseRainLevel() {
        // Decides if it is raining
        int newRain = rand.nextInt(2);
        if (newRain == 0) {
            currentRainLevel += 3;
        }

        // Setting a maximum rain level
        if (currentRainLevel > MAX_RAIN_LEVEL) {
            currentRainLevel = MAX_RAIN_LEVEL;
        }
    }
}
