package com.gmail.ak1cec0ld.plugins.spongeHMs.hms.dive;

import com.gmail.ak1cec0ld.plugins.spongeHMs.SpongeHMs;
import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.IBreakable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.concurrent.TimeUnit;

public class DiveSpot implements IBreakable {

    private final Location<World> root;
    private static final int AREA_OF_EFFECT = 2;

    public DiveSpot(Location<World> target){
        root = target;
    }

    @Override
    public void vanishAndRebuild(){
        for(int xDiff = -AREA_OF_EFFECT; xDiff <= AREA_OF_EFFECT; xDiff++){
            for(int zDiff = -AREA_OF_EFFECT; zDiff <= AREA_OF_EFFECT; zDiff++){
                if(hasIdentifier(root.add(xDiff,0,zDiff))){
                    vanishAndRebuildBlock(root.add(xDiff,0,zDiff));
                }
            }
        }
    }
    private void vanishAndRebuildBlock(Location<World> block){
        block.setBlockType(BlockTypes.AIR);

        Sponge.getScheduler().createTaskBuilder()
                .execute(() -> rebuildBlock(block))
                .delay(5, TimeUnit.SECONDS)
                .submit(SpongeHMs.instance());
    }
    private void rebuildBlock(Location<World> block){
        block.setBlockType(BlockTypes.BARRIER, BlockChangeFlags.NONE);
    }

    @Override
    public boolean allowBreaking(Player player) {
        return player.hasPermission("hms.dive.use");
    }

    @Override
    public Text successMessage() {
        return Text.of(TextColors.AQUA, "You used DIVE!");
    }

    @Override
    public Text failMessage() {
        return Text.of(TextColors.RED, "The water looks deep here.");
    }

    public static boolean exists(Location<World> where) {
        boolean isType = (where.getBlock().getType().equals(BlockTypes.BARRIER));
        return isType && hasIdentifier(where);
    }
    private static boolean isIdentifier(BlockState block){
        if(!block.getType().equals(BlockTypes.END_PORTAL_FRAME)) return false;
        if(!block.get(Keys.DIRECTION).isPresent()) return false;
        return block.get(Keys.DIRECTION).get().equals(Direction.EAST);
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
}
