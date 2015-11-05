package ca.ubc.ece.cpen221.mp4.ai;

import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.commands.Command;
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
	
//	public static Location getRandomEmptyAdjacentLocation(ArenaWorld world, ArenaAnimal animal) {
//		animal.getLocation();
//		Set<Item> possibleMoves = world.searchSurroundings(animal); //only return items, not empty spaces
//		Iterator<Item> it = possibleMoves.iterator();
//		while (it.hasNext()) {
//			Item item = it.next();
//			Location moveLocation = item.getLocation();
//			if(item.isDead() && (moveLocation.getDistance(animal.getLocation()) == 1)) {
//				return moveLocation;
//			}
//		}
//		return null;
//	}
	
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
}
