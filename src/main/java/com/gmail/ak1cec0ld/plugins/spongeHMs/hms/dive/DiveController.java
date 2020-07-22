package com.gmail.ak1cec0ld.plugins.spongeHMs.hms.dive;

import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class DiveController{


    private static boolean isIdentifier(BlockState block){
        if(!block.getType().equals(BlockTypes.END_PORTAL_FRAME)) return false;
        if(!block.get(Keys.DIRECTION).isPresent()) return false;
        return block.get(Keys.DIRECTION).get().equals(Direction.EAST);
    }
}
