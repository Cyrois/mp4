package ca.ubc.ece.cpen221.mp4.items.animals;

import java.util.ArrayList;

import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.ai.AI;
import ca.ubc.ece.cpen221.mp4.items.LivingItem;

public class Bear extends AbstractArenaAnimal {
    
    private AI ai;

    private Location location;
    private int energy;
    
    /**
     * Create a new {@link Bear} with an {@link AI} at
     * <code> initialLocation </code>. The <code> initialLoation
     * </code> must be valid and empty.
     *
     * @param bearAI
     *            : The AI designed for bears
     * @param initialLocation
     *            : the location where this rabbit will be created
     */
    public Bear(AI bearAI, Location initialLocation) {
        super(bearAI, initialLocation);
        setINITIAL_ENERGY(450);
        setEnergy(getINITIAL_ENERGY());
        setMAX_ENERGY(600);
        setSTRENGTH(200);
        setMIN_BREEDING_ENERGY(100);
        setVIEW_RANGE(4);
        setCOOLDOWN(5);
        setImage("bear.gif");
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
        Bear child = new Bear(ai, location);
        child.energy = energy / 2;
        this.energy = energy / 2;
        return child;
    }

    @Override
    public String getName() {
        return "Bear";
    }

}
