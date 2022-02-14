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
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.type.Parent;

import java.util.ArrayList;
import java.util.List;

public class EntityDamageReciver implements Listener {
    private List<IntelligentArmorStand> canBeUsedVelocity = new ArrayList<>();

    @EventHandler
    public void onHitboxDamage(@NotNull EntityDamageEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof SubEntity entity) {
            event.setCancelled(true);
            if (event.getCause() != EntityDamageEvent.DamageCause.SUFFOCATION) {
                entity.getOwner().hurt(DamageSource.MAGIC, (float) event.getDamage());
                XEntities.getInstance().getServer().getPluginManager().callEvent(new EntityDamageEvent(entity.getOwner().getBukkitEntity(), event.getCause(), event.getDamage()));
            }
        }
    }

    @EventHandler
    public void onHitboxDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
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

    @TestOnly
    @EventHandler
    public void onEntityHit(@NotNull EntityDamageByEntityEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof Parent entity) {
            Bukkit.broadcast(Component.text(entity.toString()).color(TextColor.color(234, 77, 85)));
            Bukkit.broadcast(Component.text(event.getDamager().toString()).color(TextColor.color(144, 234, 53)));
            Bukkit.broadcast(Component.text(event.getDamage()).color(TextColor.color(234, 216, 71)));
            Bukkit.broadcast(Component.text(event.getCause().toString()).color(TextColor.color(234, 140, 69)));
            Bukkit.broadcast(Component.text(event.getDamager().getVelocity().toString()).color(TextColor.color(205, 120, 234)));
        }
    }
}
