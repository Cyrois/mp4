package ca.ubc.ece.cpen221.mp4.items.vehicles;

import ca.ubc.ece.cpen221.mp4.items.MoveableItem;
import ca.ubc.ece.cpen221.mp4.Actor;

public interface ArenaVehicle extends MoveableItem, Actor {
    
    /**
     * Returns the current speed of this vehicle, which is a function
     * of the maximum speed, initial speed, and acceleration. 
     *
     * @return the current speed of this ArenaVehicle
     */
    int getSpeed();
    
    /**
     * Returns the initial speed of this vehicle. 
     *
     * @return the initial speed of this ArenaVehicle
     */
    int getINITIAL_SPEED();
    
    /**
     * Returns the maximum speed of this vehicle. 
     *
     * @return the maximum speed of this ArenaVehicle
     */
    int getMAXIMUM_SPEED();
    
    /**
     * Returns the acceleration of this vehicle. 
     *
     * @return the acceleration of this ArenaVehicle
     */
    int getACCELERATION();
}
