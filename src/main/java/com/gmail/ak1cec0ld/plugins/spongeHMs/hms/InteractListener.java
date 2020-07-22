package com.gmail.ak1cec0ld.plugins.spongeHMs.hms;

import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.cut.CutTree;
import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.dive.DiveSpot;
import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.rocksmash.SmashRock;
import org.spongepowered.api.block.BlockSnapshot;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.block.InteractBlockEvent;
import org.spongepowered.api.text.chat.ChatTypes;

import java.util.Optional;

public class InteractListener {

    @Listener
    public void onInteract(InteractBlockEvent.Secondary.OffHand event){
        Optional<Player> playerCause = event.getCause().first(Player.class);
        if(!playerCause.isPresent())return;
        Player player = playerCause.get();
        BlockSnapshot blockSnapshot = event.getTargetBlock();
        IBreakable construct = getBreakable(blockSnapshot);
        if(construct == null)return;
        event.setCancelled(true);

        if(!construct.allowBreaking(player)){
            player.sendMessage(ChatTypes.ACTION_BAR, construct.failMessage());
            return;
        }
        construct.vanishAndRebuild();
    }

    private IBreakable getBreakable(BlockSnapshot block){
        if(CutTree.exists(block.getLocation().get()))return new CutTree(block.getLocation().get());
        if(DiveSpot.exists(block.getLocation().get()))return new DiveSpot(block.getLocation().get());
        if(SmashRock.exists(block.getLocation().get()))return new SmashRock(block.getLocation().get());
        return null;
    }
}
