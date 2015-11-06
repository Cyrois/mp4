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

	/**
	 * Return a direction that is pseudo-perpendicular to the given direction
	 * @param dir the direction to find a perpendicular direction to
	 * @return a direction that is 90 degress clockwise from the given direction
	 */
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

	public static boolean isLocationEmpty(ArenaWorld world, ArenaAnimal animal,
			Location location) { // returns
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

	/**
	 * returns a random empty space that is adjacent to the animal
	 * 
	 * @param world
	 *            the ArenaWorld of the game
	 * @param animal
	 *            the animal to find a space around
	 * @return a Location of the empty space
	 */
	public static Location getRandomEmptyAdjacentLocation(ArenaWorld world,
			ArenaAnimal animal) {
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

	/**
	 * check if there is food within sight of the animal
	 * @param world the ArenaWorld of the world
	 * @param animal the animal that is looking for food
	 * @return true if there is food, false otherwise
	 */
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

	/**
	 * check if food is a distance of one block, thus edible
	 * @param animal the animal that is needs to eat
	 * @param food the food item to eat
	 * @return true if the manhattan distance of the food from the animal is one
	 */
	private boolean isFoodEdible(ArenaAnimal animal, Item food) {
		if (1 == food.getLocation().getDistance(animal.getLocation())
				&& (animal.getStrength() > food.getStrength())) {
			return true;
		}
		return false;
	}

	/**
	 * @param world The ArenaWorld of the world
	 * @param animal the animal that wants to eat
	 * @return returns the closest food item 
	 */
	private Item getClosestFood(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		int closestFoodDistance = animal.getViewRange();
		Item closestFood = null;
		ArrayList<String> food = animal.getFood();

		for (Item item : nearbyItems) {
			if (doesListContainItem(food, item)) {
				int foodDistance = item.getLocation().getDistance(
						animal.getLocation());
				if (foodDistance <= closestFoodDistance) {
					closestFoodDistance = foodDistance;
					closestFood = item;
				}
			}
		}

		return closestFood;
	}

	/**
	 * This method will get the best move for the animal when a piece of food is
	 * nearby. It will move towards the food if possible or else the animal will
	 * stay still.
	 * 
	 * @param world
	 *            the ArenaWorld of the world
	 * @param animal
	 *            The animal to move
	 * @return A command of the best move for the animal
	 */
	public Command moveToFood(ArenaWorld world, ArenaAnimal animal) {
		Item food = getClosestFood(world, animal);
		if (isFoodEdible(animal, food)) {
			return new EatCommand(animal, food);
		} else {
			Direction moveDirection = Util.getDirectionTowards(
					animal.getLocation(), food.getLocation());
			Location newLocation = new Location(animal.getLocation(),
					moveDirection);
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

	/**
	 * Some animals will have predators, this method will return a command to
	 * have the animal run away from a predator.
	 * 
	 * @param world The ArenaWorld of the world
	 * @param animal the animal to move
	 * @return a command for the animal to move.
	 */
	public Command runFromPredators(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		Location animalLoc = animal.getLocation();
		ArrayList<String> predators = animal.getPredators();
		Direction moveDirection = null;

		for (Item item : nearbyItems) {
			if (doesListContainItem(predators, item)) {
				Direction nextDirection = Util.getDirectionTowards(animalLoc,
						item.getLocation());
				nextDirection = oppositeDir(nextDirection);
				
				if (nextDirection.equals(moveDirection)) {
					nextDirection = perpendicularDirection(nextDirection);
					Location newLocation = new Location(animal.getLocation(), nextDirection);
					if(isLocationEmpty(world, animal, newLocation)) {
						moveDirection = nextDirection;
					}
				} else {
					nextDirection = oppositeDir(nextDirection);
					Location newLocation = new Location(animal.getLocation(), nextDirection);
					if(isLocationEmpty(world, animal, newLocation)) {
						moveDirection = nextDirection;
					}
				}
			}
		}
		if(moveDirection == null) {
			return new WaitCommand();
		} else {
			Location newLocation = new Location(animalLoc, moveDirection);
			return new MoveCommand(animal, newLocation);
		}
	}

	/**
	 * checks if a predator is nearby the animal
	 * @param world the ArenaAnimal of the world
	 * @param animal the animal that is worried about nearby predators
	 * @return true if a predator is nearby, false otherwise
	 */
	public boolean isPredatorNearby(ArenaWorld world, ArenaAnimal animal) {
		Set<Item> nearbyItems = world.searchSurroundings(animal);
		ArrayList<String> predators = animal.getPredators();
		for (Item item : nearbyItems) {
			if (doesListContainItem(predators, item)) {
				if (animal.getLocation().getDistance(item.getLocation()) < 2) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Checks if an item is contain in the list
	 * @param list The list of strings to search in
	 * @param item The item that will be searched for in the list
	 * @return true if the list contains the string, false otherwise
	 */
	private boolean doesListContainItem(ArrayList<String> list, Item item) {
		String itemName = item.getName();
		Iterator<String> it = list.iterator();
		while (it.hasNext()) {
			String itr = it.next();
			if (itemName.equalsIgnoreCase(itr)) {
				return true;
			}
		}
		return false;
	}
}
