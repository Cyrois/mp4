package ca.ubc.ece.cpen221.mp4.ai;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.commands.BreedCommand;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.EatCommand;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
import ca.ubc.ece.cpen221.mp4.items.*;
import ca.ubc.ece.cpen221.mp4.items.animals.*;

public class ArenaAnimalAI extends AbstractAI {

	public ArenaAnimalAI() {
	}

    @Override
    public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
        if(shouldAnimalBreed(animal)) {
            return new BreedCommand(animal, getRandomEmptyAdjacentLocation(world, animal));
        } else if(isFoodNearby(world, animal)){
            return moveToFood(world, animal);
        } else {
            return new WaitCommand();
        }
    }
    
    private boolean shouldAnimalBreed(ArenaAnimal animal) {
        int energy = animal.getEnergy();
        int maxEnergy = animal.getMaxEnergy();
        int breedEnergy = animal.getMinimumBreedingEnergy();

        //TODO optimize this
        int shouldBreedEnergy = maxEnergy / breedEnergy;
        shouldBreedEnergy = maxEnergy / 2;
        if(energy > shouldBreedEnergy) {
            return true;
        } else {
            return false;
        }
    }
	
	
	/* Old AI
	@Override
	public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
		Direction dir = Util.getRandomDirection();
		Location targetLocation = new Location(animal.getLocation(), dir);
		Set<Item> possibleEats = world.searchSurroundings(animal);
		Location current = animal.getLocation();
		Iterator<Item> it = possibleEats.iterator();
		while (it.hasNext()) {
			Item item = it.next();
			if ((item.getName().equals("Gnat") || item.getName().equals("Rabbit"))
					&& (current.getDistance(item.getLocation()) == 1)) {
				return new EatCommand(animal, item); // arena animals eat gnats
														// and rabbits
			}
		}
		if (Util.isValidLocation(world, targetLocation) && this.isLocationEmpty(world, animal, targetLocation)) {
			return new MoveCommand(animal, targetLocation);
		}
		return new WaitCommand();
	}
    */
}