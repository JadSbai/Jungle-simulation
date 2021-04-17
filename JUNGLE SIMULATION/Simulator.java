// Students: Saif Latifi (20008164) and Jad Sbai (20020900)

import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.awt.Color;

/**
 * A simple predator-prey simulator, based on a rectangular field simulating a
 * jungle environment containing living organisms: tigers, jaguars, elephants,
 * cobras, monkeys, plants and among them poisonous plants. Weather and disease
 * are both simulated and impact the overall simulation.
 * 
 * @author Jad Sbai, Saif Latifi, David J. Barnes and Michael Kölling
 * @version 22/2/21
 */
public class Simulator {
    // Constants representing configuration information for the simulation.

    // The default width for the grid.
    private static final int DEFAULT_WIDTH = 300;
    // The default depth of the grid.
    private static final int DEFAULT_DEPTH = 300;

    private static final Random rand = Randomizer.getRandom();

    // The probability that a tiger will be created in any given grid position.
    private static final double TIGER_CREATION_PROBABILITY = 0.01;
    // The probability that a jaguar will be created in any given grid position.
    private static final double JAGUAR_CREATION_PROBABILITY = 0.011;
    // The probability that a elephant will be created in any given grid position.
    private static final double ELEPHANT_CREATION_PROBABILITY = 0.02;
    // The probability that a cobra will be created in any given grid position.
    private static final double COBRA_CREATION_PROBABILITY = 0.04;
    // The probability that a monkey will be created in any given grid position.
    private static final double MONKEY_CREATION_PROBABILITY = 0.2;
    // The probability that a plant will be created in any given grid position.
    private static final double PLANT_CREATION_PROBABILITY = 0.1;
    // The probability that a poisonous plant will be created in any given grid
    // position.
    private static final double POISONOUS_PLANT_CREATION_PROBABILITY = 0.005;
    // The probability that an animal gets infected
    private static final double INFECTION_PROBABILITY = 0.5;

    // List of animals in the field.
    private List<Animal> animals;
    // List of plants in the field.
    private List<Plant> plants;
    // The current state of the field.
    private Field field;
    // The current step of the simulation.
    private int step;
    // A graphical view of the simulation.
    private SimulatorView view;

    /**
     * Construct a simulation field with default size.
     */
    public Simulator() {
        this(DEFAULT_DEPTH, DEFAULT_WIDTH);
    }

    /**
     * Create a simulation field with the given size.
     * 
     * @param depth Depth of the field. Must be greater than zero.
     * @param width Width of the field. Must be greater than zero.
     */
    public Simulator(int depth, int width) {
        if (width <= 0 || depth <= 0) {
            System.out.println("The dimensions must be greater than zero.");
            System.out.println("Using default values.");
            depth = DEFAULT_DEPTH;
            width = DEFAULT_WIDTH;
        }

        animals = new ArrayList<>();
        plants = new ArrayList<>();
        field = new Field(depth, width);

        // Create a view of the state of each location in the field.
        view = new SimulatorView(depth, width);
        view.setColor(Monkey.class, Color.BLACK);
        view.setColor(Tiger.class, Color.RED);
        view.setColor(Jaguar.class, Color.ORANGE);
        view.setColor(Cobra.class, Color.MAGENTA);
        view.setColor(Elephant.class, Color.GRAY);
        view.setColor(Plant.class, Color.GREEN);
        view.setColor(PoisonousPlant.class, Color.YELLOW);

        // Setup a valid starting point.
        reset();
    }

    /**
     * Run the simulation from its current state for a reasonably long period, (4000
     * steps).
     */
    public void runLongSimulation() {
        simulate(4000);
    }

    /**
     * Run the simulation from its current state for the given number of steps. Stop
     * before the given number of steps if it ceases to be viable.
     * 
     * @param numSteps The number of steps to run for.
     */
    public void simulate(int numSteps) {
        for (int step = 1; step <= numSteps && view.isViable(field); step++) {
            simulateOneStep();
            delay(200);
        }
    }

    /**
     * Run the simulation from its current state for a single step. Iterate over the
     * whole field updating the state of each organism. It also increments the time
     * of the simulation.
     */
    public void simulateOneStep() {
        step++;
        field.incrementTime();

        simulateDisease();
        simulateAnimals();

        simulateWeather();
        simulatePlants();

        view.showStatus(step, field);
    }

    /**
     * Reset the simulation to a starting position.
     */
    public void reset() {
        step = 0;
        animals.clear();
        populate();

        // Show the starting state in the view.
        view.showStatus(step, field);
    }

    /**
     * This method will simulate weather as well as update the view to show current
     * rain level and temperature
     */
    private void simulateWeather() {
        Weather currentWeather = field.getWeather();
        currentWeather.simulate();
        view.setInfoText("Temperature: " + currentWeather.getCurrentTemperature() + "°C and Rain level: "
                + currentWeather.getCurrentRainLevel() + "mm");
    }

    /**
     * This method will randomly give an animal a disease (if the probability is
     * high enough) once per step
     */
    private void simulateDisease() {
        if (rand.nextDouble() <= INFECTION_PROBABILITY) {
            if (animals.size() > 1) {
                Animal randomAnimal = animals.get(rand.nextInt(animals.size() - 1));
                randomAnimal.setInfected();
            }
        }
    }

    /**
     * This method simulates behaviour for all the different animals within the
     * simulation
     */
    private void simulateAnimals() {
        // Provide space for newborn animals.
        List<Animal> newAnimals = new ArrayList<>();
        // Let all animals act.
        for (Iterator<Animal> it = animals.iterator(); it.hasNext();) {
            Animal animal = it.next();
            animal.act(newAnimals);
            if (!animal.isAlive()) {
                it.remove();
            }
        }

        // Add the newly born animals to the main lists.
        animals.addAll(newAnimals);
    }

    /**
     * Randomly populate the field with tigers, jaguars, monkeys, cobras, elephants,
     * plants and poisonous plants.
     */
    private void populate() {
        field.clear();
        for (int row = 0; row < field.getDepth(); row++) {
            for (int col = 0; col < field.getWidth(); col++) {
                if (rand.nextDouble() <= TIGER_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Tiger tiger = new Tiger(true, field, location);
                    animals.add(tiger);
                } else if (rand.nextDouble() <= JAGUAR_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Jaguar jaguar = new Jaguar(true, field, location);
                    animals.add(jaguar);
                } else if (rand.nextDouble() <= ELEPHANT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Elephant elephant = new Elephant(true, field, location);
                    animals.add(elephant);
                } else if (rand.nextDouble() <= COBRA_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Cobra cobra = new Cobra(true, field, location);
                    animals.add(cobra);
                } else if (rand.nextDouble() <= PLANT_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Plant plant;
                    if (rand.nextDouble() <= POISONOUS_PLANT_CREATION_PROBABILITY) {
                        plant = new PoisonousPlant(field, location);
                    } else {
                        plant = new Plant(field, location);
                    }
                    plants.add(plant);
                } else if (rand.nextDouble() <= MONKEY_CREATION_PROBABILITY) {
                    Location location = new Location(row, col);
                    Monkey monkey = new Monkey(true, field, location);
                    animals.add(monkey);
                }

                // else leave the location empty.
            }
        }
    }

    /**
     * This method simulates activities for plants: reproducing and reacting to the
     * weather (i.e., rain and temperature).
     * 
     */
    private void simulatePlants() {
        List<Plant> newPlants = new ArrayList<>();
        for (Iterator<Plant> it = plants.iterator(); it.hasNext();) {
            Plant plant = it.next();
            List<Plant> temp = plant.act();
            if (temp != null) {
                newPlants.addAll(temp);
            }
            if (!plant.isAlive()) {
                it.remove();
            }
        }
    }

    /**
     * Pause for a given time.
     * 
     * @param millisec The time to pause for, in milliseconds
     */
    private void delay(int millisec) {
        try {
            Thread.sleep(millisec);
        } catch (InterruptedException ie) {
            // wake up
        }
    }

    /**
     * This method is the main method for the simulator. It creates a simulator
     * object and runs a long simulation.
     * 
     * @param args Additional arguments to be passed into the main method
     */
    public static void main(String[] args) {
        Simulator simulator = new Simulator();
        simulator.runLongSimulation();
    }
}
