package com.gmail.ak1cec0ld.plugins.spongeHMs.hms.rocksmash;

import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

public class RSmashInteractListener{
    private Random r;
    private List<BlockFace> faces;
    
    public RSmashInteractListener(){
        this.r = new Random();
        faces = Arrays.asList(BlockFace.valueOf("WEST"), BlockFace.valueOf("EAST"), BlockFace.valueOf("NORTH"), BlockFace.valueOf("SOUTH"));
    }

    
    private void breakRock(Block block){
        setLayer(block,Material.AIR);
        setLayer(block.getRelative(0,1,0),Material.AIR);
        PokemonServer.instance().getServer().getScheduler().runTaskLater(PokemonServer.instance(), () -> makeRock(block), 100L);
    }

    private Block getRockLoc(Location<World> hitblockLoc, int id_y){
        int identifier = id_y;
        int idshiftx = -1;
        int idshiftz = -1;
        int locsearch = 0;
        while(isSmashRock(hitblockLoc.getWorld().getBlockAt((int)hitblockLoc.getX(), identifier, (int)hitblockLoc.getZ()-1-locsearch))){
            locsearch++;
        }
        if(locsearch%2 == 0){
            idshiftz=1;
        }
        locsearch = 0;
        while(isSmashRock(hitblockLoc.getWorld().getBlockAt((int)hitblockLoc.getX()-1-locsearch, identifier, (int)hitblockLoc.getZ()))){
            locsearch++;
        }
        if(locsearch%2 == 0){
            idshiftx = 1;
        }
        
        int minx = (int)Math.min(hitblockLoc.getX(), idshiftx+hitblockLoc.getX());
        int minz = (int)Math.min(hitblockLoc.getZ(), idshiftz+hitblockLoc.getZ());
        World world = hitblockLoc.getWorld();
        for(int rocksearch = identifier; rocksearch <= identifier+8; rocksearch++){
            if(world.getBlockAt(minx, rocksearch, minz).getType().equals(Material.COBBLESTONE_STAIRS)){
                return world.getBlockAt(minx,rocksearch,minz);
            }
        }
        return null;
    }
    
    private int getIdentifierY(Location hitblock){
        World world = hitblock.getWorld();
        double IB_x = hitblock.getX();
        double IB_z = hitblock.getZ();
        for(double id = hitblock.getY(); id > hitblock.getY()-8; id--){
            if(isSmashRock(world.getBlockAt((int)IB_x, (int)id, (int)IB_z))){
                return (int)id;
            }
        }
        return -1;
    }
    
    private void makeRock(Block block){
        setLayer(block,Material.COBBLESTONE_STAIRS);
        setLayer(block.getRelative(0,1,0),Material.COBBLESTONE_STAIRS);
        randomizeLayer(block);
        randomizeLayer(block.getRelative(0,1,0));
    }
    
    private void setLayer(Block block, Material newmat){
        block.setType(newmat);
        block.getRelative(0,0,1).setType(newmat);
        block.getRelative(1,0,0).setType(newmat);
        block.getRelative(1,0,1).setType(newmat);
    }
    
    private void randomizeLayer(Block block){
        setFacingRandom(block);
        setFacingRandom(block.getRelative(0,0,1));
        setFacingRandom(block.getRelative(1,0,0));
        setFacingRandom(block.getRelative(1,0,1));
    }
    
    private void setFacingRandom(Block target){
        Stairs st = (Stairs)target.getBlockData();
        st.setFacing(faces.get(r.nextInt(4)));
        st.setHalf(Bisected.Half.values()[r.nextInt(2)]);
        target.setBlockData(st);
    }
}