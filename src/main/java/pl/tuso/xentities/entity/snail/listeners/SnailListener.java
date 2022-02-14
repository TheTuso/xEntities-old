package pl.tuso.xentities.entity.snail.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.entity.snail.Snail;

import java.util.Random;

public class SnailListener implements Listener {
    @EventHandler
    public void onSnailHit(@NotNull EntityDamageByEntityEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof Snail snail) {
            Vector vector = event.getDamager().getLocation().getDirection();
            snail.getBukkitEntity().setVelocity(new Vector(vector.getX(), 0.0D, vector.getZ()).multiply(0.5));
            snail.hurtModel();
            //TODO Animations
        }
    }

    private int timer;
    private int frequency;
    private Random random = new Random();
    @EventHandler
    public void onSnailMove(@NotNull EntityMoveEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof Snail snail) {
            if (timer == frequency) {
                timer = 0;
                frequency = random.nextInt(20) + 20;
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(snail.getBukkitEntity().getLocation(), Sound.BLOCK_HONEY_BLOCK_SLIDE, 0.25F, 1.0F + random.nextFloat()));
            } else {
                timer++;
            }
        }
    }
}
