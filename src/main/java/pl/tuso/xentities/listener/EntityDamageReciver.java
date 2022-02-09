package pl.tuso.xentities.listener;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.world.damagesource.DamageSource;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.TestOnly;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.api.SubEntity;
import pl.tuso.xentities.entity.ExampleEntity;

public class EntityDamageReciver implements Listener {
    @EventHandler
    public void onHitboxDamage(@NotNull EntityDamageEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof SubEntity entity) {
            if (event.getCause() == EntityDamageEvent.DamageCause.SUFFOCATION) {
                event.setCancelled(true);
                return;
            } else {
                entity.getOwner().hurt(DamageSource.MAGIC, (float) event.getDamage());
                XEntities.getInstance().getServer().getPluginManager().callEvent(new EntityDamageEvent(entity.getOwner().getBukkitEntity(), event.getCause(), event.getDamage()));
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onHitboxDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof SubEntity entity) {
            Vector vector = event.getDamager().getLocation().getDirection();
            entity.getOwner().getBukkitEntity().setVelocity(new Vector(vector.getX(), 0.75D, vector.getZ()).multiply(0.5D));
            XEntities.getInstance().getServer().getPluginManager().callEvent(new EntityDamageByEntityEvent(event.getDamager(), entity.getOwner().getBukkitEntity(), event.getCause(), event.getDamage()));
            event.setCancelled(true);
        }
    }

    @TestOnly
    @EventHandler
    public void onEntityHit(@NotNull EntityDamageByEntityEvent event) {
        //TODO fix part spam
        if (((CraftEntity) event.getEntity()).getHandle() instanceof ExampleEntity entity) {
            Bukkit.broadcast(Component.text(entity.toString()).color(TextColor.color(234, 77, 85)));
            Bukkit.broadcast(Component.text(event.getDamager().toString()).color(TextColor.color(144, 234, 53)));
            Bukkit.broadcast(Component.text(event.getDamage()).color(TextColor.color(234, 216, 71)));
            Bukkit.broadcast(Component.text(event.getCause().toString()).color(TextColor.color(234, 140, 69)));
        }
    }
}
