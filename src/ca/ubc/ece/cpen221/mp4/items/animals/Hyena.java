package ca.ubc.ece.cpen221.mp4.items.animals;

import java.util.ArrayList;

import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.ai.AI;
import ca.ubc.ece.cpen221.mp4.items.LivingItem;

public class Hyena extends AbstractArenaAnimal {
    
    /**
     * Create a new {@link Hyena} with an {@link AI} at
     * <code> initialLocation </code>. The <code> initialLoation
     * </code> must be valid and empty.
     *
     * @param hyenaAI
     *            : The AI designed for hyenas
     * @param initialLocation
     *            : the location where this rabbit will be created
     */
    public Hyena(AI hyenaAI, Location initialLocation) {
        super(hyenaAI, initialLocation);
        setINITIAL_ENERGY(150);
        setEnergy(getINITIAL_ENERGY());
        setMAX_ENERGY(200);
        setSTRENGTH(120);
        setMIN_BREEDING_ENERGY(15);
        setVIEW_RANGE(6);
        setCOOLDOWN(3);
        setImage("hyena.gif");
    }
    

    @Override
    public ArrayList<String> getFood() {
        ArrayList<String> food = new ArrayList<String>();
        food.add("Fox");
        food.add("Rabbit");
        food.add("Gnat");
        return food;
    }

    @Override
    public ArrayList<String> getPredators() {
        ArrayList<String> predators = new ArrayList<String>();
        predators.add("Bear");
        predators.add("Tiger");
        return predators;
    }

    @Override
    public LivingItem breed() {
        Hyena child = new Hyena(ai, location);
        child.energy = energy / 2;
        this.energy = energy / 2;
        return child;
    }

    @Override
    public String getName() {
        return "Hyena";
    }

}

