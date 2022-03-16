package pl.tuso.xentities.listener;

import net.minecraft.world.damagesource.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.api.SubEntity;
import pl.tuso.xentities.type.IntelligentArmorStand;

import java.util.ArrayList;
import java.util.List;

public class EntityDamageReciver implements Listener {
    private List<IntelligentArmorStand> canBeUsedVelocity = new ArrayList<>();

    @EventHandler
    public void onSubEntityDamage(@NotNull EntityDamageEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof SubEntity entity) {
            event.setCancelled(true);
            if (event.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) {
                entity.getOwner().hurt(DamageSource.MAGIC, (float) event.getDamage());
                XEntities.getInstance().getServer().getPluginManager().callEvent(new EntityDamageEvent(entity.getOwner().getBukkitEntity(), event.getCause(), event.getDamage()));
            }
        }
    }

    @EventHandler
    public void onSubEntityDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof SubEntity entity) {
            event.setCancelled(true);
            XEntities.getInstance().getServer().getPluginManager().callEvent(new EntityDamageByEntityEvent(event.getDamager(), entity.getOwner().getBukkitEntity(), event.getCause(), event.getDamage()));

            if (!canBeUsedVelocity.contains(entity.getOwner()) && entity.getOwner().getKnockbackResistance() != 1.0D) {
                canBeUsedVelocity.add(entity.getOwner());
                Vector vector = event.getDamager().getLocation().getDirection();
                entity.getOwner().getBukkitEntity().setVelocity(new Vector(vector.getX(), 0.75, vector.getZ()).multiply(0.5));
                Bukkit.getScheduler().scheduleSyncDelayedTask(XEntities.getInstance(), () -> canBeUsedVelocity.remove(entity.getOwner()), 10);
            }
        }
    }
}
