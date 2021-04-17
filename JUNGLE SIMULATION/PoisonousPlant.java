// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class models poisonous plants. Unlike normal plants, if an animal eats
 * them, they die. This class is a subclass of the Plant class.
 * 
 * @author Saif Latifi, Jad Sbai
 * @version 22/2/21
 */
public class PoisonousPlant extends Plant {
    private static final Random rand = Randomizer.getRandom();
    private static final double SPREAD_PROBABILITY = 0.25;

    /**
     * This constructs poisonous plants by setting their location and field they are
     * situated in.
     * 
     * @param field    The field the plant is in
     * @param location The location the plant is situated
     */
    public PoisonousPlant(Field field, Location location) {
        super(field, location);
    }

    /**
     * This method allows poisonous plants to reproduce creating offspring at random
     * locations, taking into account their specific probability of having a child
     * (different from other plants).
     * 
     * @return A list of new plants
     */
    protected List<Plant> reproduce() {
        incrementAge();

        if (isAlive()) {
            Location newLocation = getField().findFreeRandomLocation();
            List<Plant> newPlants = new ArrayList<>();
            if (rand.nextDouble() <= SPREAD_PROBABILITY && newLocation != null) {
                PoisonousPlant newPlant = new PoisonousPlant(getField(), newLocation);
                newPlants.add(newPlant);
            }
            return newPlants;
        }
        return null;
    }
}
