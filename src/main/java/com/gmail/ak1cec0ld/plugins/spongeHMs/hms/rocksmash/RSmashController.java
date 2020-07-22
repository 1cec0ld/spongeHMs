package com.gmail.ak1cec0ld.plugins.spongeHMs.hms.rocksmash;


import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class RSmashController {


    public static boolean isTriggerBlock(BlockState block){
        return block.getType().equals(BlockTypes.STONE_STAIRS);
    }

    static boolean isIdentifier(BlockState block){
        if(!block.getType().equals(BlockTypes.END_PORTAL_FRAME)) return false;
        if(!block.get(Keys.DIRECTION).isPresent()) return false;
        return block.get(Keys.DIRECTION).get().equals(Direction.EAST);
    }

    static Location<World> getIdentifier(Location<World> hitBlock){
        final int MAX_DEPTH = 8;
        for(int i = 0; i < MAX_DEPTH; i++){
            if(isIdentifier(hitBlock.add(0, -i,0).getBlock())){
                return hitBlock.add(0,-i,0);
            }
        }
        return null;
    }
}