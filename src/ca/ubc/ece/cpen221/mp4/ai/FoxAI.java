package ca.ubc.ece.cpen221.mp4.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.commands.BreedCommand;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.EatCommand;
import ca.ubc.ece.cpen221.mp4.commands.InvalidCommandException;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
import ca.ubc.ece.cpen221.mp4.items.Item;
import ca.ubc.ece.cpen221.mp4.items.animals.ArenaAnimal;

/**
 * Your Fox AI.
 */
public class FoxAI extends AbstractAI {
	private int closest = 2; // max number; greater than fox's view range

	public FoxAI() {

	}

	@Override
	public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
		//TODO implement breeding
		if(shouldFoxBreed(animal)) {
			return new BreedCommand(animal, Util.getRandomEmptyAdjacentLocation((World) world, animal.getLocation()));
		} else if(isFoodNearby(world, animal)){
			Item food = getClosestFood(world, animal);
			if(isFoodEdible(animal, food)) {
				return new EatCommand(animal, food);
			} else {
				return getBestMove(world, animal, food);
			}
		} else {
			return new WaitCommand();
		}
	}
	
	private boolean shouldFoxBreed(ArenaAnimal animal) {
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

	//check if there is food within sight
	private boolean isFoodNearby(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		
		//assuming foxes will eat rabbits
		//TODO add more food cases
		for(Item item : nearbyItems) {
			if(item.getName().equalsIgnoreCase("Rabbit")) {
				return true;
			}
		}

		return false;
	}
	
	//check if food is a distance of one block, thus edible
	private boolean isFoodEdible(ArenaAnimal animal, Item food) {
		if(1 == food.getLocation().getDistance(animal.getLocation())) {
			return true;
		} 
		return false;
		
	}

	//find the closest food item
	private Item getClosestFood(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		int closestFoodDistance = animal.getViewRange();
		Item closestFood = null;
		
		for(Item item : nearbyItems) {
			if(item.getName().equalsIgnoreCase("Rabbit")) {
				int foodDistance = item.getLocation().getDistance(animal.getLocation());
				if(foodDistance <= closestFoodDistance) {
					closestFoodDistance = foodDistance;
					closestFood = item;
				}
			}
		}
		
		return closestFood;
	}
	
	//move towards the food
	private MoveCommand getBestMove(ArenaWorld world, ArenaAnimal animal, Item food) {
		Direction moveDirection = Util.getDirectionTowards(animal.getLocation(), food.getLocation());
		Location newLocation = new Location(animal.getLocation(), moveDirection);
		if(Util.isLocationEmpty((World) world, newLocation)) {
			return new MoveCommand(animal, newLocation);
		} else {
			newLocation = Util.getRandomEmptyAdjacentLocation((World) world, animal.getLocation());
			return new MoveCommand(animal, newLocation);
		}
	}

}
