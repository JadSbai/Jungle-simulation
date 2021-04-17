// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

/**
 * Represent a rectangular grid of field positions. Each position is able to
 * store a single organism.
 * 
 * @author Jad Sbai, Saif Latifi, David J. Barnes and Michael Kölling
 * @version 22/2/20
 */
public class Field {
    // A random number generator for providing random locations.
    private static final Random rand = Randomizer.getRandom();

    // The depth and width of the field.
    private int depth, width;
    // Storage for the animals.
    private Object[][] field;
    // The current hour of the simulated time
    private int currentHour;
    // The weather simulated in the field
    private Weather weather;

    /**
     * Represent a field of the given dimensions. It also intializes the current
     * time of the simulation. It creates a weather object for simulating the
     * weather on the field.
     * 
     * @param depth The depth of the field.
     * @param width The width of the field.
     */
    public Field(int depth, int width) {
        this.depth = depth;
        this.width = width;
        field = new Object[depth][width];
        currentHour = 0;
        weather = new Weather();
    }

    /**
     * Empty the field.
     */
    public void clear() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                field[row][col] = null;
            }
        }
    }

    /**
     * Clear the given location.
     * 
     * @param location The location to clear.
     */
    public void clear(Location location) {
        field[location.getRow()][location.getCol()] = null;
    }

    /**
     * Place an organism at the given location. If there is already an organism at
     * the location it will be lost.
     * 
     * @param organism The organism to be placed.
     * @param row      Row coordinate of the location.
     * @param col      Column coordinate of the location.
     */
    public void place(Object organism, int row, int col) {
        place(organism, new Location(row, col));
    }

    /**
     * Place an organism at the given location. If there is already an organism at
     * the location it will be lost.
     * 
     * @param organism The organism to be placed.
     * @param location Where to place the animal.
     */
    public void place(Object organism, Location location) {
        field[location.getRow()][location.getCol()] = organism;
    }

    /**
     * Return the organism at the given location, if any.
     * 
     * @param location Where in the field.
     * @return The animal at the given location, or null if there is none.
     */
    public Object getObjectAt(Location location) {
        return getObjectAt(location.getRow(), location.getCol());
    }

    /**
     * Return the organism at the given location, if any.
     * 
     * @param row The desired row.
     * @param col The desired column.
     * @return The animal at the given location, or null if there is none.
     */
    public Object getObjectAt(int row, int col) {
        return field[row][col];
    }

    /**
     * Generate a random location that is adjacent to the given location, or is the
     * same location. The returned location will be within the valid bounds of the
     * field.
     * 
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location randomAdjacentLocation(Location location) {
        List<Location> adjacent = adjacentLocations(location);
        return adjacent.get(0);
    }

    /**
     * Get a shuffled list of the free adjacent locations.
     * 
     * @param location Get locations adjacent to this.
     * @return A list of free adjacent locations.
     */
    public List<Location> getFreeAdjacentLocations(Location location) {
        List<Location> free = new LinkedList<>();
        List<Location> adjacent = adjacentLocations(location);
        for (Location next : adjacent) {
            if (getObjectAt(next) == null) {
                free.add(next);
            }
        }
        return free;
    }

    /**
     * Try to find a free location that is adjacent to the given location. If there
     * is none, return null. The returned location will be within the valid bounds
     * of the field.
     * 
     * @param location The location from which to generate an adjacency.
     * @return A valid location within the grid area.
     */
    public Location freeAdjacentLocation(Location location) {
        // The available free ones.
        List<Location> free = getFreeAdjacentLocations(location);
        if (free.size() > 0) {
            return free.get(0);
        } else {
            return null;
        }
    }

    /**
     * Return a shuffled list of locations adjacent to the given one. The list will
     * not include the location itself. All locations will lie within the grid.
     * 
     * @param location The location from which to generate adjacencies.
     * @return A list of locations adjacent to that given.
     */
    public List<Location> adjacentLocations(Location location) {
        assert location != null : "Null location passed to adjacentLocations";
        // The list of locations to be returned.
        List<Location> locations = new LinkedList<>();
        if (location != null) {
            int row = location.getRow();
            int col = location.getCol();
            for (int roffset = -1; roffset <= 1; roffset++) {
                int nextRow = row + roffset;
                if (nextRow >= 0 && nextRow < depth) {
                    for (int coffset = -1; coffset <= 1; coffset++) {
                        int nextCol = col + coffset;
                        // Exclude invalid locations and the original location.
                        if (nextCol >= 0 && nextCol < width && (roffset != 0 || coffset != 0)) {
                            locations.add(new Location(nextRow, nextCol));
                        }
                    }
                }
            }

            // Shuffle the list. Several other methods rely on the list
            // being in a random order.
            Collections.shuffle(locations, rand);
        }
        return locations;
    }

    /**
     * Return the depth of the field.
     * 
     * @return The depth of the field as an integer
     */
    public int getDepth() {
        return depth;
    }

    /**
     * Return the width of the field.
     * 
     * @return The width of the field as an integer
     */
    public int getWidth() {
        return width;
    }

    /**
     * This method increments the current time of the simulation in the field.
     */
    public void incrementTime() {
        if (currentHour < 24) {
            currentHour++;
        } else {
            currentHour = 0;
        }
    }

    /**
     * This method returns the current hour of the day.
     * 
     * @return The current hour as an integer
     */
    public int getHour() {
        return currentHour;
    }

    /**
     * This method return the boolean value of whether it is currently night
     * 
     * @return Whether it is night or not
     */
    public boolean isNight() {
        return (currentHour >= 19 && currentHour < 24) || (currentHour >= 0 && currentHour <= 6);
    }

    /**
     * This method gets a random un-occupied location within the field
     * 
     * @return A free random location
     */
    public Location findFreeRandomLocation() {
        boolean found = false;
        while (!found && isFreeLocation()) {
            int randomDepth = rand.nextInt(depth);
            int randomWidth = rand.nextInt(width);
            if (getObjectAt(randomDepth, randomWidth) == null) {
                return new Location(randomDepth, randomWidth);
            }
        }
        return null;
    }

    /**
     * Checks whether there is a free location on the field currently
     * 
     * @return If there is a free location
     */
    private boolean isFreeLocation() {
        for (int row = 0; row < depth; row++) {
            for (int col = 0; col < width; col++) {
                if (field[row][col] == null) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This methods allows for the weather object to be returned, so that other
     * classes (e.g. the simulator) can perform actions on the weather
     * 
     * @return The weather object
     */
    public Weather getWeather() {
        return weather;
    }

}
