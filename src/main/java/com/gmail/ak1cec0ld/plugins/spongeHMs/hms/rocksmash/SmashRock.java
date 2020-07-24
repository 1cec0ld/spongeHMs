package com.gmail.ak1cec0ld.plugins.spongeHMs.hms.rocksmash;

import com.gmail.ak1cec0ld.plugins.spongeHMs.SpongeHMs;
import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.IBreakable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.PortionType;
import org.spongepowered.api.data.type.PortionTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class SmashRock implements IBreakable {

    private final Location<World> root;
    private static final List<Direction> directions = Arrays.asList(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);
    private static final List<PortionType> portions =   Arrays.asList(PortionTypes.BOTTOM, PortionTypes.TOP);

    public SmashRock(Location<World> initial){
        int locsearchX = 0;
        int locsearchZ = 0;
        while(isTriggerBlock(initial.add(locsearchX+1, 0, 0).getBlock())){
            locsearchX++;
        }
        while(isTriggerBlock(initial.add(0, 0, locsearchZ+1).getBlock())){
            locsearchZ++;
        }
        int rootYOffset = -1;
        while(isTriggerBlock(initial.add(0,rootYOffset,0).getBlock())){
            rootYOffset--;
        }
        root = initial.add(locsearchX%2-1,rootYOffset+1,locsearchZ%2-1);
    }

    @Override
    public void vanishAndRebuild() {
        setLayer(root, BlockTypes.AIR);
        setLayer(root.add(0,1,0), BlockTypes.AIR);

        Sponge.getScheduler().createTaskBuilder()
                .execute(this::rebuild)
                .delay(5, TimeUnit.SECONDS)
                .submit(SpongeHMs.instance());
    }

    private void rebuild() {
        setLayer(root, BlockTypes.STONE_STAIRS);
        setLayer(root.add(0,1,0), BlockTypes.STONE_STAIRS);
    }

    private void setLayer(Location<World> block, BlockType newmat){
        block.setBlockType(newmat);
        block.add(0,0,1).setBlockType(newmat);
        block.add(1,0,0).setBlockType(newmat);
        block.add(1,0,1).setBlockType(newmat);
        if(!newmat.getDefaultState().supports(Keys.PORTION_TYPE))return;
        setFacingRandom(block);
        setFacingRandom(block.add(0,0,1));
        setFacingRandom(block.add(1,0,0));
        setFacingRandom(block.add(1,0,1));
    }
    private void setFacingRandom(Location<World> target){
        Random r = new Random();
        int randomFace = (int)(4 * r.nextDouble()); //truncate, 0,1,2,3
        int randomPortion = (int)(2 * r.nextDouble()); // 0,1
        Direction dir = directions.get(randomFace);
        PortionType portion = portions.get(randomPortion);
        target.setBlock(target.getBlock().with(Keys.DIRECTION, dir).get(), BlockChangeFlags.NONE);
        target.setBlock(target.getBlock().with(Keys.PORTION_TYPE, portion).get(), BlockChangeFlags.NONE);
    }
    @Override
    public boolean allowBreaking(Player player) {
        return player.hasPermission("hms.rocksmash.use");
    }

    @Override
    public Text successMessage() {
        return Text.of(TextColors.DARK_GREEN, "You used ROCK SMASH!");
    }

    @Override
    public Text failMessage() {
        return Text.of(TextColors.RED, "This rock looks breakable.");
    }

    public static boolean exists(Location<World> location){
        return isTriggerBlock(location.getBlock()) && hasIdentifier(location) ;
    }

    private static boolean isTriggerBlock(BlockState block){
        return block.getType().equals(BlockTypes.STONE_STAIRS);
    }


    private static boolean hasIdentifier(Location<World> hitBlock){
        final int MAX_DEPTH = 8;
        for(int i = 0; i < MAX_DEPTH; i++){
            if(isIdentifier(hitBlock.add(0, -i,0).getBlock())){
                return true;
            }
        }
        return false;
    }

    private static boolean isIdentifier(BlockState block){
        if(!block.getType().equals(BlockTypes.END_PORTAL_FRAME)) return false;
        if(!block.get(Keys.DIRECTION).isPresent()) return false;
        return block.get(Keys.DIRECTION).get().equals(Direction.SOUTH);
    }
}
