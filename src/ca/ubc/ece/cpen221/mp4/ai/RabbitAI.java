package ca.ubc.ece.cpen221.mp4.ai;

import java.util.Iterator;
import java.util.Set;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.commands.BreedCommand;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.EatCommand;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
import ca.ubc.ece.cpen221.mp4.items.Item;
import ca.ubc.ece.cpen221.mp4.items.animals.ArenaAnimal;
import ca.ubc.ece.cpen221.mp4.items.animals.Fox;
import ca.ubc.ece.cpen221.mp4.items.animals.Rabbit;

/**
 * Your Rabbit AI.
 */
public class RabbitAI extends AbstractAI {

	private int closest = 10; // max number; greater than rabbit's view range
	private int temp;
	private boolean foxFound;

	public RabbitAI() {
	}

	@Override
	public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
		if(shouldRabbitBreed(animal)) {
			return new BreedCommand(animal, getRandomEmptyAdjacentLocation(world, animal));
		} if(isPredatorNearby(world, animal)) {
			return runFromPredators(world, animal);
		} else if(isFoodNearby(world, animal)){
			Item food = getClosestFood(world, animal);
			if(isFoodEdible(animal, food)) {
				return new EatCommand(animal, food);
			} else {
				return getBestMove(world, animal, food);
			}
		}
		return new WaitCommand();
	}
	
	private Command runFromPredators(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		Location animalLoc = animal.getLocation();
		Direction moveDirection = null;
		
		for(Item item : nearbyItems) {
			if(item.getName().equalsIgnoreCase("Fox")) { //more predators?
				Direction nextDirection = Util.getDirectionTowards(animalLoc, item.getLocation());
				if(nextDirection.equals(moveDirection)) {
					moveDirection = perpendicularDirection(moveDirection);
				} else {
					moveDirection = oppositeDir(moveDirection);
				}
			}
		}
		Location newLocation = new Location(animalLoc, moveDirection);
		return new MoveCommand(animal, newLocation);
	}

	private boolean isPredatorNearby(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		
		//TODO add more predator cases
		for(Item item : nearbyItems) {
			if(item.getName().equalsIgnoreCase("Fox")) {
				return true;
			}
		}

		return false;
	}
	
	private boolean shouldRabbitBreed(ArenaAnimal animal) {
		int energy = animal.getEnergy();
		int maxEnergy = animal.getMaxEnergy();
		int breedEnergy = animal.getMinimumBreedingEnergy();

		//TODO optimize this
		int shouldBreedEnergy = (int) ((double) maxEnergy * 0.8);
		if(energy > shouldBreedEnergy) {
			return true;
		} else {
			return false;
		}
	}

	//check if there is food within sight
	private boolean isFoodNearby(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		
		//TODO add more food cases
		for(Item item : nearbyItems) {
			if(item.getName().equalsIgnoreCase("Grass")) {
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
			if(item.getName().equalsIgnoreCase("Grass")) {
				int foodDistance = item.getLocation().getDistance(animal.getLocation());
				if(foodDistance <= closestFoodDistance) {
					closestFoodDistance = foodDistance;
					closestFood = item;
				}
			}
		}
		
		return closestFood;
	}
	
	//move towards the food or else wait
	private Command getBestMove(ArenaWorld world, ArenaAnimal animal, Item food) {
		Direction moveDirection = Util.getDirectionTowards(animal.getLocation(), food.getLocation());
		Location newLocation = new Location(animal.getLocation(), moveDirection);
		if(isLocationEmpty(world, animal, newLocation)) {
			return new MoveCommand(animal, newLocation);
		} else {
			newLocation = getRandomEmptyAdjacentLocation(world, animal);
			if(newLocation == null) {
				return new WaitCommand();
			}
			return new MoveCommand(animal, newLocation);
		}
	}
}
