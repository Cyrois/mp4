package ca.ubc.ece.cpen221.mp4.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.EatCommand;
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
		if(isFoodNearby(world, animal)){
			Item food = getClosestFood(world, animal);
			if(1 == food.getLocation().getDistance(animal.getLocation())) {
				return new EatCommand(animal, food);
			} else {
				Location newLocation = new Location(animal.getLocation(), getBestDirection(animal, food));
				return new MoveCommand(animal, newLocation);
			}
		}
		return new WaitCommand();
	}

	private boolean isFoodNearby(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);

		for(Item item : nearbyItems) {
			if(item.getName().equalsIgnoreCase("Rabbit")) {
				return true;
			}
		}

		return false;
	}

	//change to is food edible
	private Item getClosestFood(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		int closestFoodDistance = animal.getViewRange();
		Item closestFood = null;
		
		for(Item item : nearbyItems) {
			if(item.getName().equalsIgnoreCase("Rabbit")) {
				int foodDistance = item.getLocation().getDistance(animal.getLocation());
				if(foodDistance < closestFoodDistance) {
					closestFoodDistance = foodDistance;
					closestFood = item;
				}
			}
		}
		
		return closestFood;
	}
	
	//change to get best move
	private Direction getBestDirection(ArenaAnimal animal, Item food) {
		return animal.getLocation().getDirection(food.getLocation());
	}

}
