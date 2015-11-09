package ca.ubc.ece.cpen221.mp4.commands;

import java.util.Iterator;

import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.items.Item;
import ca.ubc.ece.cpen221.mp4.items.vehicles.ArenaVehicle;

public class CollisionCommand implements Command {

    private final ArenaVehicle vehicle;
    private final Location target;
    private final Item targetItem;
    
    public CollisionCommand(World world, ArenaVehicle vehicle, Location target) {
        this.vehicle = vehicle;
        this.target = target;
        this.targetItem = getItemAtLocation(target, world.getItems());
    }

    private Item getItemAtLocation(Location loc, Iterable<Item> items){
        Iterator<Item> it = items.iterator();
        while (it.hasNext()) {
            Item item = it.next();
            if (item.getLocation().equals(loc)) {
                return item;
            }
        }
        return null;
    }
    
    @Override
    public void execute(World world) throws InvalidCommandException {
        if (!Util.isValidLocation(world, target) || Util.isLocationEmpty(world, target)) {
            throw new InvalidCommandException("Invalid CollisionCommand: Invalid/empty collision target location");
        }
        
        if (vehicle.getStrength() > targetItem.getStrength()){
            targetItem.loseEnergy(Integer.MAX_VALUE); // target item dies
            vehicle.moveTo(target);
        }
        else if (vehicle.getStrength() == targetItem.getStrength()){
            targetItem.loseEnergy(Integer.MAX_VALUE); // both items die/crash
            vehicle.loseEnergy(Integer.MAX_VALUE);
        }
        else {
            vehicle.loseEnergy(Integer.MAX_VALUE); // vehicle crashes
        }
        
    }

}
