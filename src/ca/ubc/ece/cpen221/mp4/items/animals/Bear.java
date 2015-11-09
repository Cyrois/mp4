package ca.ubc.ece.cpen221.mp4.items.animals;

import java.util.ArrayList;

import ca.ubc.ece.cpen221.mp4.Food;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.ai.AI;
import ca.ubc.ece.cpen221.mp4.items.LivingItem;

public class Bear extends AbstractArenaAnimal {
    
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
        setINITIAL_ENERGY(250);
        setEnergy(getINITIAL_ENERGY());
        setMAX_ENERGY(300);
        setSTRENGTH(200);
        setMIN_BREEDING_ENERGY(125);
        setVIEW_RANGE(7);
        setCOOLDOWN(5);
        setImage("bear.gif");
    }

    @Override
    public ArrayList<String> getFood() {
        ArrayList<String> food = new ArrayList<String>();
        food.add("Fox");
        food.add("Hyena");
        food.add("Grass");
        food.add("Gnat");
        return food;
    }

    @Override
    public ArrayList<String> getPredators() {
        ArrayList<String> predators = new ArrayList<String>();
        predators.add("Tiger");
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
    public void eat(Food food) {
        // Bear can eat meat and plants
        energy = Math.min(getMaxEnergy(), energy + 
                Math.max(food.getMeatCalories(), food.getPlantCalories()));
    }
    
    @Override
    public String getName() {
        return "Bear";
    }

}
