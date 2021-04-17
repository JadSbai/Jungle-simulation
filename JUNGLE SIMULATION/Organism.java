// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * This abstract class specifies the base behaviours for animals and plants
 * within the simulation
 * 
 * It includes a base constructor and common protected methods
 * 
 * @author Saif Latifi, Jad Sbai, David J. Barnes and Michael Kölling
 * @version 22/2/21
 */
public abstract class Organism {
    private static final int MAX_DAYS_INFECTED = 5;
    private static final double DISEASE_SPREAD_PROBABILITY = 0.2;
    private static final Random rand = Randomizer.getRandom();

    // Whether the organism is alive or not.
    private boolean alive;
    // The organism's field.
    private Field field;
    // The organism's position in the field.
    private Location location;
    // The age of the organism
    private int age;
    // The number of days the organism is infected
    private int daysInfected;

    /**
     * This constructor initiates organism object and sets their location within the
     * field. This also creates organisms without diseases from birth (i.e., age =
     * 0)
     * 
     * @param field    The field they are placed in
     * @param location The location within the field that they are in
     */
    public Organism(Field field, Location location) {
        alive = true;
        this.field = field;
        setLocation(location);
        age = 0;
        daysInfected = 0;
    }

    /**
     * Check whether the organism is alive or not.
     * 
     * @return true if the organism is still alive.
     */
    protected boolean isAlive() {
        return alive;
    }

    /**
     * Indicate that the organism is no longer alive. It is removed from the field.
     */
    protected void setDead() {
        alive = false;
        if (location != null) {
            field.clear(location);
            location = null;
            field = null;
        }
    }

    /**
     * Return the organism's location.
     * 
     * @return The organism's location.
     */
    protected Location getLocation() {
        return location;
    }

    /**
     * Place the organism at the new location in the given field.
     * 
     * @param newLocation The organism's new location.
     */
    protected void setLocation(Location newLocation) {
        if (location != null) {
            field.clear(location);
        }
        location = newLocation;
        field.place(this, newLocation);
    }

    /**
     * Return the organism's field.
     * 
     * @return The organism's field.
     */
    protected Field getField() {
        return field;
    }

    /**
     * This abstract method gets the food value of individual organisms which is
     * defined within subclasses
     * 
     * @return Food value as integer
     */
    protected abstract int getFoodValue();

    /**
     * This abstract method gets the maximum age of a organism from within the
     * subclass
     * 
     * @return The maximum age as integer.
     */

    protected abstract int getMaxAge();

    /**
     * Increase the age. This could result in the organism's death.
     */
    protected void incrementAge() {
        age++;
        if (age > getMaxAge()) {
            setDead();
        }
    }

    /**
     * This method return the current age of the organism
     *
     * @return The current age of the organism as an integer
     */
    protected int getAge() {
        return age;
    }

    /**
     * This method allows subclasses to set the age of the organism - used with
     * objects with random age
     * 
     * @param age The age of the organism.
     */
    protected void setAge(int age) {
        this.age = age;
    }

    /**
     * This method allows organismsto spread a disease if they are already infected.
     */
    private void spreadDisease() {
        if (alive) {
            Field field = getField();
            List<Location> adjacent = field.adjacentLocations(getLocation());
            Iterator<Location> it = adjacent.iterator();
            while (it.hasNext()) {
                Location where = it.next();
                Object organism = field.getObjectAt(where);
                // In our simulation, we allow only for animals to be infected (and not plants)
                if (organism != null && organism instanceof Animal && rand.nextDouble() <= DISEASE_SPREAD_PROBABILITY) {
                    ((Organism) organism).setInfected();
                }
            }
        }
    }

    /**
     * This method increments the number of days the organism has been infected and
     * spread to others (if they are infected and alive). It also sets the organism
     * to dead if they have been infected for too long.
     */
    protected void incrementDaysInfected() {
        if (daysInfected > 0) {
            daysInfected++;
        }
        if (daysInfected > MAX_DAYS_INFECTED) {
            setDead();
        } else if (daysInfected > 0) {
            spreadDisease();
        }
    }

    /**
     * The method allows for organisms to be set as infected if they currently are
     * not infected
     */
    public void setInfected() {
        if (daysInfected == 0) {
            daysInfected = 1;
        }
    }

    /**
     * Returns the boolean value if the organism has a disease or not
     * 
     * @return If the organism is infected
     */
    public boolean isInfected() {
        return daysInfected > 0;
    }
}
