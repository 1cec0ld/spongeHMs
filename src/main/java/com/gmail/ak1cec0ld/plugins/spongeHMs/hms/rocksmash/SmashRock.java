package com.gmail.ak1cec0ld.plugins.spongeHMs.hms.rocksmash;

import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.IBreakable;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

public class SmashRock implements IBreakable {

    private final Location<World> root;

    public SmashRock(Location<World> initial){
        root = initial;
    }

    @Override
    public void vanishAndRebuild() {

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
}
