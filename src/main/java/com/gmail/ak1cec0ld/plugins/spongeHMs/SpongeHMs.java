package com.gmail.ak1cec0ld.plugins.spongeHMs;

import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.cut.CutController;
import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.dive.DiveController;
import com.gmail.ak1cec0ld.plugins.spongeHMs.hms.rocksmash.RSmashController;
import com.google.inject.Inject;
import org.slf4j.Logger;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

@Plugin(id = "spongehms", name = "Sponge HMs", version = "1.0", description = "Example")
public class SpongeHMs{

    private static SpongeHMs instance;

    @Inject
    private Logger logger;




    @Listener
    public void onServerStart(GameStartedServerEvent event) {
        logger.info("Loading SpongeHMs");
        instance = this;

        new CutController(this);
        new DiveController(this);
        new RSmashController(this);
    }

    public static SpongeHMs instance(){
        return instance;
    }

    public Logger logger(){
        return logger;
    }

}
