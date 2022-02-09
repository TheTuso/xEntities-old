package pl.tuso.xentities.listener;

import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.util.EntityEquipmentPacketBlocker;

public class EntityEquipmentFixer implements Listener {
    @EventHandler
    public void onEntityDeath(@NotNull EntityDeathEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof IntelligentArmorStand armorStand) {
            EntityEquipmentPacketBlocker listener = new EntityEquipmentPacketBlocker(armorStand);
            Bukkit.getOnlinePlayers().forEach(player -> listener.injectPlayer(player));
            Bukkit.getScheduler().scheduleSyncDelayedTask(XEntities.getInstance(), () -> Bukkit.getOnlinePlayers().forEach(player -> listener.removePlayer(player)), 2);
        }
    }
}
