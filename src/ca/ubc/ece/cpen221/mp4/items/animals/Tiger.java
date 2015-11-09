package ca.ubc.ece.cpen221.mp4.items.animals;

import java.util.ArrayList;

import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.ai.AI;
import ca.ubc.ece.cpen221.mp4.items.LivingItem;

public class Tiger extends AbstractArenaAnimal {
    
    /**
     * Create a new {@link Tiger} with an {@link AI} at
     * <code> initialLocation </code>. The <code> initialLoation
     * </code> must be valid and empty.
     *
     * @param tigerAI
     *            : The AI designed for tigers
     * @param initialLocation
     *            : the location where this rabbit will be created
     */
    public Tiger(AI tigerAI, Location initialLocation) {
        super(tigerAI, initialLocation);
        setINITIAL_ENERGY(200);
        setEnergy(getINITIAL_ENERGY());
        setMAX_ENERGY(250);
        setSTRENGTH(220);
        setMIN_BREEDING_ENERGY(125);
        setVIEW_RANGE(10);
        setCOOLDOWN(4);
        setImage("tiger.gif");
    }

    @Override
    public ArrayList<String> getFood() {
        ArrayList<String> food = new ArrayList<String>();
        food.add("Rabbit");
        food.add("Bear");
        food.add("Fox");
        food.add("Hyena");
        return food;
    }

    @Override
    public ArrayList<String> getPredators() {
        ArrayList<String> predators = new ArrayList<String>();
        return predators;
    }

    @Override
    public LivingItem breed() {
        Tiger child = new Tiger(ai, location);
        child.energy = energy / 2;
        this.energy = energy / 2;
        return child;
    }

    @Override
    public String getName() {
        return "Tiger";
    }

}
