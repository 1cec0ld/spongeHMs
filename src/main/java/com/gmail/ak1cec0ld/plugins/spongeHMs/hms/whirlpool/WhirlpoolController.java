/*package com.gmail.ak1cec0ld.plugins.spongeHMs.hms.whirlpool;

import com.gmail.ak1cec0ld.plugins.pokemonserver.PokemonServer;
import com.gmail.ak1cec0ld.plugins.spongeHMs.SpongeHMs;
import org.bukkit.entity.Player;

public class WhirlpoolController {
    private SpongeHMs plugin;
    
    public WhirlpoolController(SpongeHMs pl){
        this.plugin = pl;
        plugin.getServer().getPluginManager().registerEvents(new WhirlpoolInteractListener(this), pl);
    }
    
    public SpongeHMs getPlugin(){
        return this.plugin;
    }

    public boolean permissionToBreak(Player player){
        return (plugin.getPlayerStorageManager().getPokemonChoice(player.getUniqueId().toString()) != -1);
    }
}
*/