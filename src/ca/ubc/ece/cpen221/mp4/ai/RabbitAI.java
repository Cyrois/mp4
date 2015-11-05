package ca.ubc.ece.cpen221.mp4.ai;

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
			return getBestMove(world, animal);
		} else {
			return new MoveCommand(animal, getRandomEmptyAdjacentLocation(world, animal));
		}
		
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
}
