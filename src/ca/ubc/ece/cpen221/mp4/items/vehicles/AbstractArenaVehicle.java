package ca.ubc.ece.cpen221.mp4.items.vehicles;

import javax.swing.ImageIcon;

import ca.ubc.ece.cpen221.mp4.Direction;
import ca.ubc.ece.cpen221.mp4.Location;
import ca.ubc.ece.cpen221.mp4.Util;
import ca.ubc.ece.cpen221.mp4.World;
import ca.ubc.ece.cpen221.mp4.ai.AbstractAI;
import ca.ubc.ece.cpen221.mp4.commands.Command;

public abstract class AbstractArenaVehicle implements ArenaVehicle {

    private int STRENGTH;
    private int INITIAL_SPEED;
    private int MAXIMUM_SPEED;
    private int ACCELERATION;
    private ImageIcon image;
    
    // Since our cooldown decreases as current speed increases, we need a 
    // reference speed to determine the minimum cooldown. 
    // For speed == REFERENCE_SPEED, cooldown is 1
    // For speed == 1, cooldown is REFERENCE_SPEED
    protected int REFERENCE_SPEED = 10;
    
    protected int speed;
    protected Location location;
    protected int energy;
    
    public AbstractArenaVehicle(Location initialLocation){
        this.location = initialLocation;
        this.energy = 1;
    }
    
    public void setSpeed(int i) {
        this.speed = i;
    }

    protected void setEnergy(int i) {
        this.energy = i;
    }

    protected void setSTRENGTH(int i) {
        this.STRENGTH = i;
    }

    protected void setINITIAL_SPEED(int i) {
        this.INITIAL_SPEED = i;
    }
    
    protected void setMAXIMUM_SPEED(int i) {
        this.MAXIMUM_SPEED = i;
    }
    
    protected void setACCELERATION(int i) {
        this.ACCELERATION = i;
    }

    protected void setLocation(Location l) {
        this.location = l;
    }

    protected void setImage(String imgFile) {
        this.image = Util.loadImage(imgFile);
    }
    
    @Override
    public int getSpeed() {
        return speed;   
    }
    
    @Override
    public int getINITIAL_SPEED(){
        return INITIAL_SPEED;
    }
    
    @Override
    public int getMAXIMUM_SPEED(){
        return MAXIMUM_SPEED;
    }
    
    @Override
    public int getACCELERATION(){
        return ACCELERATION;
    }
    
    @Override
    public int getStrength(){
        return STRENGTH;
    }
    
    @Override
    public Location getLocation(){
        return location;
    }
    
    @Override
    public ImageIcon getImage(){
        return image;
    }
    
    @Override
    public abstract String getName();
    
    @Override
    public int getCoolDownPeriod() {
     // cooldown is determined by current speed
        return REFERENCE_SPEED - speed + 1;   
    }
    
    @Override
    public int getPlantCalories(){
        return 0; // not a plant
    }
    
    @Override
    public int getMeatCalories(){
        return 0; // not meat
    }
    
    @Override
    public void loseEnergy(int energyLoss) {
        this.energy = this.energy - energyLoss;
    }
    
    @Override
    public boolean isDead() {
        return this.energy <= 0;
    }
    
    @Override
    public abstract Command getNextAction(World world);
    
    @Override
    public int getMovingRange(){
        return 1;
    }
    
    public Direction turnDirection (boolean turnRight, Direction dir){
        if (turnRight){
            return AbstractAI.rightPerpendicularDir(dir);
        }
        else{
            return AbstractAI.leftPerpendicularDir(dir);
        }
    }
    
    @Override
    public void moveTo(Location targetLocation) {
        location = targetLocation;
    }
    
}