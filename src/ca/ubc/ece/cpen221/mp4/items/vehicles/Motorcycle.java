package ca.ubc.ece.cpen221.mp4.items.vehicles;

import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.commands.Command;
import ca.ubc.ece.cpen221.mp4.commands.MoveCommand;
import ca.ubc.ece.cpen221.mp4.commands.WaitCommand;
import ca.ubc.ece.cpen221.mp4.commands.CollisionCommand;

public class Motorcycle extends AbstractArenaVehicle {

    private Direction dir;
    
    public Motorcycle(Location initialLocation) {
        super(initialLocation);
        setSTRENGTH(125); 
        setINITIAL_SPEED(3);
        setMAXIMUM_SPEED(8);
        setACCELERATION(3);
        setSpeed(getINITIAL_SPEED());
        setImage("motorcycles.gif");
        this.dir = Util.getRandomDirection(); // random initial direction
    }

    @Override
    public String getName() {
        return "Motorcycle";
    }

    // Motorcycle moves in random directions at a moderate speed with fast acceleration.
    // The direction changes randomly at a 10% chance per step.
    // If the next location is empty, simply move to that location. If the next location 
    // is not empty, the motorcycle will destroy the other item or crash based on strength. 
    @Override
    public Command getNextAction(World world) {
        if (Util.RAND.nextInt(10)==0){
           setSpeed(getINITIAL_SPEED());
           dir = Util.getRandomDirection(); //20% chance to change direction
        }
        Location targetLocation = new Location(this.getLocation(), dir);
        
        if (Util.isValidLocation(world, targetLocation) && Util.isLocationEmpty(world, targetLocation)) { // target location is empty
            setSpeed(Math.min(getMAXIMUM_SPEED(), getSpeed() + getACCELERATION())); // accelerate
            return new MoveCommand(this, targetLocation);
        }
        else if (Util.isValidLocation(world, targetLocation) && !Util.isLocationEmpty(world, targetLocation)){ // if there is a collision
            return new CollisionCommand(world, this, targetLocation);
        }
        else {
            setSpeed(getINITIAL_SPEED());
            return new WaitCommand();   
        }
    }

}
