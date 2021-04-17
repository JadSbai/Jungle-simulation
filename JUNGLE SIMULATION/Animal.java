// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * An abstract class defining animal objects and representing shared
 * characteristics of animals within the simulation. This class is a subclass of
 * the Organism class.
 * 
 * @author Saif Latifi, Jad Sbai, David J. Barnes and Michael Kölling
 * @version 22/2/21
 */
public abstract class Animal extends Organism {
    // The minimum age for an animal to be able to breed
    private static final int BREEDING_AGE = 15;
    private static final Random rand = Randomizer.getRandom();
    // The initial food level for any newborn animal.
    private static final int INITIAL_FOOD_LEVEL = 8;

    // This list represents the animal's preys
    private ArrayList<Class> foodSources;
    // Current level of food for the animal
    private int foodLevel;
    // The gender of the animal
    private boolean female;
    // The animal's lifestyle
    protected boolean isNocturnal;

    /**
     * Create a new animal at location in field. It sets a starting food level, the
     * list of preys, their gender, age and lifestyle.
     * 
     * @param field    The field currently occupied.
     * @param location The location within the field.
     * @param food     The list of the animal's preys.
     */
    public Animal(boolean randomAge, Field field, Location location, ArrayList<Class> food) {
        super(field, location);
        foodSources = food;
        foodLevel = INITIAL_FOOD_LEVEL;

        // Randomly set the animal's gender
        int gender = rand.nextInt(2);
        female = (gender == 1);

        if (randomAge) {
            setAge(rand.nextInt(getMaxAge()));
        }

        // Default status for nocturnal animals is false. Overriden in subclasses.
        isNocturnal = false;
    }

    /**
     * Make this animal act - this includes moving, eating and breeding if the
     * animal is awake.
     * 
     * @param newAnimals A list to receive newly born animals.
     */
    public void act(List<Animal> newAnimals) {
        incrementDaysInfected();
        incrementHunger();
        incrementAge();

        // This statement catches animals who have just died
        if (isAlive() != true) {
            return;
        }

        // Checks if the animal is awake or not
        if ((getField().isNight() && !isNocturnal) || (!getField().isNight() && isNocturnal)) {
            return;
        }

        // If the animal is alive and awake, it acts.
        if (isAlive()) {
            giveBirth(newAnimals);
            // Move towards a source of food if found.
            Location newLocation = eat();

            // Catching animals that have died eating poisonous plants
            if (!isAlive()) {
                return;
            }

            if (newLocation == null) {
                // No food found - try to move to a free location.
                newLocation = getField().freeAdjacentLocation(getLocation());
            }
            // See if it was possible to move.
            if (newLocation != null) {
                setLocation(newLocation);
            } else {
                // Overcrowding.
                setDead();
            }
        }
    }

    /**
     * This method allows the animal to try and find food, and if so, eat it
     * 
     * @return The location of the organism to be eaten
     * 
     */
    private Location eat() {
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            if (organism != null) {
                if (foodSources.contains(organism.getClass())) {
                    Organism food = (Organism) organism;
                    if (food.isAlive()) {
                        eatPoisonousPlant(food);
                        food.setDead();
                        foodLevel += food.getFoodValue();
                        return where;
                    }
                }
            }
        }
        return null;
    }

    /**
     * Make this animal more hungry. This could result in the animals's death.
     */
    private void incrementHunger() {
        foodLevel--;
        if (foodLevel <= 0) {
            setDead();
        }
    }

    /**
     * This abstract method returns the maximum litter size for the animal
     * (different for each subclass)
     * 
     * @return The maximum litter size
     */
    protected abstract int getMaxLitterSize();

    /**
     * This abstract method returns the breeding proability for individual animals.
     * 
     * @return The breeding probabilty as a double
     */
    protected abstract double getBreedingProbability();

    /**
     * Check whether or not this Animal is to give birth at this step. New births
     * will be made into free adjacent locations.
     * 
     * @param newAnimals A list to return newly born animals.
     */
    protected abstract void giveBirth(List<Animal> newAnimals);

    /**
     * Generate a number representing the number of births, if it can breed.
     * 
     * @return The number of births as an Integer (may be zero).
     */
    protected int breed() {
        int births = 0;
        if (canBreed() && rand.nextDouble() <= getBreedingProbability()) {
            births = rand.nextInt(getMaxLitterSize()) + 1;
        }
        return births;
    }

    /**
     * An animal can breed if it is female, is of breeding age and also whether they
     * have a male animal of the same type next to them.
     * 
     * @return The boolean value of if they can breed
     */
    private boolean canBreed() {
        // Checks whether animal is male
        if (this.isFemale() == false) {
            return false;
        }

        // Gets animals if adjancent locations & checks if they are male
        Field field = getField();
        List<Location> adjacent = field.adjacentLocations(getLocation());
        Iterator<Location> it = adjacent.iterator();
        while (it.hasNext()) {
            Location where = it.next();
            Object organism = field.getObjectAt(where);
            // Checks to see if organism is an animal and is next to an opposite gender
            // animal of the same type.
            if (organism != null && !(organism instanceof Plant)) {
                if (organism.getClass() == this.getClass() && ((Animal) organism).isFemale() != this.isFemale()) {
                    // Checks that both animals are of breeding age
                    if (getAge() >= BREEDING_AGE && ((Animal) organism).getAge() >= BREEDING_AGE) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This method returns the boolean value of whether the animal is female or not.
     * It will return true if it is female.
     * 
     * @return The boolean value of whether the animal is female.
     */
    private boolean isFemale() {
        return female;
    }

    /**
     * This method checks whether the animal has eaten a poisonous plant, if so, it
     * sets the animal to dead
     */
    private void eatPoisonousPlant(Organism organism) {
        if (organism instanceof PoisonousPlant) {
            setDead();
        }
    }

}
