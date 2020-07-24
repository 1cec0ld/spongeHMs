package com.gmail.ak1cec0ld.plugins.spongeHMs.hms.cut;

import com.gmail.ak1cec0ld.plugins.spongeHMs.SpongeHMs;
import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.IBreakable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.BlockTypes;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.TreeTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.util.Direction;
import org.spongepowered.api.world.BlockChangeFlags;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.concurrent.TimeUnit;

public class CutTree implements IBreakable {
    private final Location<World> root;

    public CutTree(Location<World> initial){
        int locsearchX = 0;
        int locsearchZ = 0;
        while(isIdentifier(initial.add(locsearchX+1, 0, 0).getBlock())){
            locsearchX++;
        }
        while(isIdentifier(initial.add(0, 0, locsearchZ+1).getBlock())){
            locsearchZ++;
        }
        int rootYOffset = -1;
        while(!isIdentifier(initial.add(0,rootYOffset,0).getBlock())){
            rootYOffset--;
        }
        root = initial.add(locsearchX%2-1,rootYOffset,locsearchZ%2-1);
    }

    @Override
    public void vanishAndRebuild(){
        setLayer(root, BlockTypes.GRASS);
        for(int y = 1; y <= 3; y++){
            setLayer(root.add(0, y,0),BlockTypes.AIR);
        }

        Sponge.getScheduler().createTaskBuilder()
                .execute(this::rebuild)
                .delay(5, TimeUnit.SECONDS)
                .submit(SpongeHMs.instance());
    }

    @Override
    public boolean allowBreaking(Player player) {
        return player.hasPermission("hms.cut.use");
    }

    @Override
    public Text successMessage() {
        return Text.of(TextColors.DARK_GREEN, "You CUT the tree down!");
    }

    @Override
    public Text failMessage() {
        return Text.of(TextColors.RED,"This tree looks weak...");
    }

    public static boolean exists(Location<World> where) {
        boolean isType = (where.getBlock().getType().equals(BlockTypes.LOG2) || where.getBlock().getType().equals(BlockTypes.LEAVES2));
        boolean isDark = (where.getBlock().get(Keys.TREE_TYPE).isPresent() && where.getBlock().get(Keys.TREE_TYPE).get().equals(TreeTypes.DARK_OAK));
        boolean identifierFound = false;
        final int MAX_DEPTH = 8;
        for(int i = 0; i < MAX_DEPTH; i++){
            if(isIdentifier(where.add(0, -i,0).getBlock())){
                identifierFound = true;
            }
        }
        return isType && isDark && identifierFound;
    }
    private static boolean isIdentifier(BlockState block){
        if(!block.getType().equals(BlockTypes.END_PORTAL_FRAME)) return false;
        if(!block.get(Keys.DIRECTION).isPresent()) return false;
        return block.get(Keys.DIRECTION).get().equals(Direction.EAST);
    }
    private void rebuild(){
        setLayer(root, BlockTypes.END_PORTAL_FRAME);
        setLayer(root.add(0,1,0), BlockTypes.LOG2);
        setLayer(root.add(0,2,0), BlockTypes.LOG2);
        setLayer(root.add(0,3,0), BlockTypes.LEAVES2);
    }
    private void setLayer(Location<World> loc, BlockType newmat){
        setTreeBlock(loc, newmat);
        setTreeBlock(loc.add(0, 0, 1), newmat);
        setTreeBlock(loc.add(1, 0, 0), newmat);
        setTreeBlock(loc.add(1, 0, 1), newmat);
    }
    private void setTreeBlock(Location<World> block, BlockType newmat){
        block.setBlockType(newmat, BlockChangeFlags.NONE);
        if(newmat.equals(BlockTypes.END_PORTAL_FRAME)){
            setFacingEast(block);
        } else if(newmat.equals(BlockTypes.LOG2)){
            block.setBlock(block.getBlock().with(Keys.TREE_TYPE, TreeTypes.DARK_OAK).get());
        }
    }
    private void setFacingEast(Location<World> target){
        target.setBlock(target.getBlock().with(Keys.DIRECTION, Direction.EAST).get());
    }
}