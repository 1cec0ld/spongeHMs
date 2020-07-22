package com.gmail.ak1cec0ld.plugins.spongeHMs.hms;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

public interface IBreakable {

    void vanishAndRebuild();

    boolean allowBreaking(Player player);

    Text successMessage();
    Text failMessage();
}
