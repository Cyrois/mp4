package ca.ubc.ece.cpen221.mp4.ai;

import ca.ubc.ece.cpen221.mp4.ArenaWorld;
import ca.ubc.ece.cpen221.mp4.commands.BreedCommand;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
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
		if(shouldFoxBreed(animal)) {
			return new BreedCommand(animal, getRandomEmptyAdjacentLocation(world, animal));
		} else if(isFoodNearby(world, animal)){
			return moveToFood(world, animal);
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
}
