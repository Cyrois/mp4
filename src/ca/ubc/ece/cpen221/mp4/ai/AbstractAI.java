package ca.ubc.ece.cpen221.mp4.ai;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.EatCommand;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
import ca.ubc.ece.cpen221.mp4.items.Item;
import ca.ubc.ece.cpen221.mp4.items.animals.ArenaAnimal;

public class AbstractAI implements AI {

	public Direction oppositeDir(Direction dir) { // returns opposite direction
		// of direction dir
		if (dir == Direction.EAST) {
			return Direction.WEST;
		} else if (dir == Direction.WEST) {
			return Direction.EAST;
		} else if (dir == Direction.SOUTH) {
			return Direction.NORTH;
		} else {
			return Direction.SOUTH;
		}
	}

	public Direction perpendicularDirection(Direction dir) {
		if (dir == Direction.EAST) {
			return Direction.SOUTH;
		} else if (dir == Direction.WEST) {
			return Direction.NORTH;
		} else if (dir == Direction.SOUTH) {
			return Direction.WEST;
		} else {
			return Direction.EAST;
		}
	}

	public static boolean isLocationEmpty(ArenaWorld world, ArenaAnimal animal, Location location) { // returns
		// true
		// if
		// location
		// is
		// empty
		if (!Util.isValidLocation(world, location)) {
			return false;
		}
		Set<Item> possibleMoves = world.searchSurroundings(animal);
		Iterator<Item> it = possibleMoves.iterator();
		while (it.hasNext()) {
			Item item = it.next();
			if (item.getLocation().equals(location)) {
				return false;
			}
		}
		return true;
	}

	public static Location getRandomEmptyAdjacentLocation(ArenaWorld world, ArenaAnimal animal) {
		Location loc = animal.getLocation();
		Location[] neighbors = new Location[3 * 3]; // 3 x 3 bounding box
		int numLocs = 0;
		for (int x = loc.getX() - 1; x <= loc.getX() + 1; x++) {
			for (int y = loc.getY() - 1; y <= loc.getY() + 1; y++) {
				Location l = new Location(x, y);
				if (isLocationEmpty(world, animal, l)) {
					neighbors[numLocs] = l;
					numLocs++;
				}
			}
		}
		if (numLocs == 0)
			return null;
		Random RAND = new Random();
		return neighbors[RAND.nextInt(numLocs)];
	}

	@Override
	public Command getNextAction(ArenaWorld world, ArenaAnimal animal) {
		return new WaitCommand();
	}

	// check if there is food within sight
	public boolean isFoodNearby(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		ArrayList<String> food = animal.getFood();
		// TODO add more food cases
		for (Item item : nearbyItems) {
			if (doesListContainItem(food, item)) {
				return true;
			}
		}

		return false;
	}

	// check if food is a distance of one block, thus edible
	private boolean isFoodEdible(ArenaAnimal animal, Item food) {
		if (1 == food.getLocation().getDistance(animal.getLocation()) && (animal.getStrength() > food.getStrength())) {
			return true;
		}
		return false;
	}

	// find the closest food item
	private Item getClosestFood(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		int closestFoodDistance = animal.getViewRange();
		Item closestFood = null;
		ArrayList<String> food = animal.getFood();

		for (Item item : nearbyItems) {
			if (doesListContainItem(food, item)) {
				int foodDistance = item.getLocation().getDistance(animal.getLocation());
				if (foodDistance <= closestFoodDistance) {
					closestFoodDistance = foodDistance;
					closestFood = item;
				}
			}
		}

		return closestFood;
	}

	// move towards the food or else wait
	public Command getBestMove(ArenaWorld world, ArenaAnimal animal) {
		Item food = getClosestFood(world, animal);
		if (isFoodEdible(animal, food)) {
			return new EatCommand(animal, food);
		} else {
			Direction moveDirection = Util.getDirectionTowards(animal.getLocation(), food.getLocation());
			Location newLocation = new Location(animal.getLocation(), moveDirection);
			if (isLocationEmpty(world, animal, newLocation)) {
				return new MoveCommand(animal, newLocation);
			} else {
				newLocation = getRandomEmptyAdjacentLocation(world, animal);
				if (newLocation == null) {
					return new WaitCommand();
				} else {
					return new MoveCommand(animal, newLocation);
				}
			}
		}
	}

	public Command runFromPredators(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		Location animalLoc = animal.getLocation();
		Direction moveDirection = null;

		for (Item item : nearbyItems) {
			if (item.getName().equalsIgnoreCase("Fox")) { // more predators?
				Direction nextDirection = Util.getDirectionTowards(animalLoc, item.getLocation());
				if (nextDirection.equals(moveDirection)) {
					moveDirection = perpendicularDirection(moveDirection);
				} else {
					moveDirection = oppositeDir(moveDirection);
				}
			}
		}
		Location newLocation = new Location(animalLoc, moveDirection);
		return new MoveCommand(animal, newLocation);
	}

	public boolean isPredatorNearby(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		ArrayList<String> predators = animal.getPredators();
		for (Item item : nearbyItems) {
			if (doesListContainItem(predators, item)) {
				if(animal.getLocation().getDistance(item.getLocation()) < 2) {
					return true;
				}
			}
		}

		return false;
	}
	
	private boolean doesListContainItem(ArrayList<String> food, Item item) {
		String itemName = item.getName();
		Iterator<String> it = food.iterator();
		while(it.hasNext()) {
			String itr = it.next();
			if(itemName.equalsIgnoreCase(itr)) {
				return true;
			}
		}
		return false;
	}
}
