package pl.tuso.xentities;

import org.bukkit.plugin.java.JavaPlugin;
import pl.tuso.xentities.listener.EntityEquipmentFixer;
import pl.tuso.xentities.listener.EntityDamageReciver;
import pl.tuso.xentities.type.FantasticBeast;

public final class XEntities extends JavaPlugin {
    private static XEntities instance;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        FantasticBeast.registerTypes();
        getServer().getPluginManager().registerEvents(new EntityEquipmentFixer(), this);
        getServer().getPluginManager().registerEvents(new EntityDamageReciver(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static XEntities getInstance() {
        return instance;
    }
}
