package ca.ubc.ece.cpen221.mp4.items.humans;

import ca.ubc.ece.cpen221.mp4.items.MoveableItem;

/**
 * Abstractions for foxes and rabbits that provide additional information that
 * might be of use to the AI:
 * <ol>
 * <li>Maximum energy, given by {@link #getMaxEnergy()}</li>
 * <li>View range, given by {@link #getViewRange()}</li>
 * <li>Minimum breeding energy, given by {@link #getMinimumBreedingEnergy()}
 * </li>
 * </ol>
 *
 */
public interface ArenaVehicle extends MoveableItem {

	
}
