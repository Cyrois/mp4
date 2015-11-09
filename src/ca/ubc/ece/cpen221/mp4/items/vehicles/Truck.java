package ca.ubc.ece.cpen221.mp4.items.vehicles;

import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
import ca.ubc.ece.cpen221.mp4.commands.CollisionCommand;

public class Truck extends AbstractArenaVehicle {

    private Direction dir;
    private boolean isTurning; // true while truck is turning
    private boolean turnRight; // determines the turn direction 
    
    public Truck(Location initialLocation) {
        super(initialLocation);
        setSTRENGTH(500);
        setINITIAL_SPEED(2);
        setMAXIMUM_SPEED(7);
        setACCELERATION(1);
        setSpeed(getINITIAL_SPEED());
        setImage("trucks.gif");
        this.dir = Util.getRandomDirection(); // random initial direction
        isTurning = false;
        turnRight = (Util.RAND.nextInt(1)==1); //random initial turn
    }

    @Override
    public String getName() {
        return "Truck";
    }

    // Truck moves in straight lines at a slow speed and acceleration.
    // If the truck reaches the end of a world, it will brake and do a U-turn.
    // If the next location is not empty, the truck will destroy the other
    // item or crash based on strength.
    @Override
    public Command getNextAction(World world) {
        Location targetLocation = new Location(this.getLocation(), dir);
        
        if (isTurning){
            dir = turnDirection(turnRight, dir);
            isTurning = false; //complete the turn
            turnRight = !turnRight; //next turn will be in other direction
        }
        
        if (Util.isValidLocation(world, targetLocation) && Util.isLocationEmpty(world, targetLocation)) { // target location is empty
            setSpeed(Math.min(getMAXIMUM_SPEED(), getSpeed() + getACCELERATION())); // accelerate
            return new MoveCommand(this, targetLocation);
        }
        else if (Util.isValidLocation(world, targetLocation) && !Util.isLocationEmpty(world, targetLocation)){ // if there is a collision
            return new CollisionCommand(world, this, targetLocation);
        }
        else { // if we have reached the edge of the world, turn
            setSpeed(getINITIAL_SPEED());
            dir = turnDirection(turnRight, dir);
            if (!Util.isValidLocation(world, new Location(this.getLocation(), dir))){
                dir = turnDirection(turnRight, dir); // do an additional turn if we are in a corner
            }
            isTurning = true; // Change direction again on the next action
            return new WaitCommand();
        }
    }

}
