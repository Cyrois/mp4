package ca.ubc.ece.cpen221.mp4.items.animals;

import java.util.ArrayList;

import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.ai.AI;
import ca.ubc.ece.cpen221.mp4.items.LivingItem;

public class Tiger extends AbstractArenaAnimal {
    
    private AI ai;

    private Location location;
    private int energy;
    
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
        setINITIAL_ENERGY(150);
        setEnergy(getINITIAL_ENERGY());
        setMAX_ENERGY(200);
        setSTRENGTH(150);
        setMIN_BREEDING_ENERGY(30);
        setVIEW_RANGE(6);
        setCOOLDOWN(4);
        setImage("tiger.gif");
    }

    @Override
    public ArrayList<String> getFood() {
        ArrayList<String> food = new ArrayList<String>();
        // TODO add food?
        return food;
    }

    @Override
    public ArrayList<String> getPredators() {
        ArrayList<String> predators = new ArrayList<String>();
        // TODO add predators?
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
