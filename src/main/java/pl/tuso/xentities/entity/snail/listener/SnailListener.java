package pl.tuso.xentities.entity.snail.listener;

import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.entity.snail.Snail;
import pl.tuso.xentities.entity.snail.animation.RollAnimation;

import java.util.Random;

public class SnailListener implements Listener {
    private Random random = new Random();
    private BukkitRunnable temp;

    @EventHandler
    public void onSnailDamageByEntity(@NotNull EntityDamageByEntityEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof Snail snail) {
            snail.setHidden(true);
            //snail.hurtModel();

            RollAnimation rollAnimation = new RollAnimation(snail, 0, 0);
            rollAnimation.start();
            rollAnimation.forcePlay(0, 0);

            Vector vector = event.getDamager().getLocation().getDirection();
            snail.lookAt(((CraftEntity) event.getDamager()).getHandle(), 360, 0);
            snail.setMovementSpeed(0.0D);

            if (snail.isKnocked()) {
                snail.getBukkitEntity().setVelocity(new Vector(vector.getX(), 0.0D, vector.getZ()).multiply(0.5D));
            } else {
                snail.getBukkitEntity().setVelocity(new Vector(vector.getX(), 0.75D, vector.getZ()).multiply(0.5D));
                snail.setKnocked(true);
                Bukkit.getOnlinePlayers().forEach(player -> player.playSound(snail.getBukkitEntity().getLocation(), Sound.BLOCK_SLIME_BLOCK_PLACE, 0.25F, 1.0F + random.nextFloat()));
            }

            BukkitRunnable reset = new BukkitRunnable() {
                @Override
                public void run() {
                    if (snail.isKnocked() && snail.isAlive()) {
                        snail.setHidden(false);
                    }
                }
            };

            int taskIdOrFailed;
            try {
                taskIdOrFailed = temp.getTaskId();
            } catch (Exception exception) {
                taskIdOrFailed = -1;
            }

            if (taskIdOrFailed != -1) {
                if (Bukkit.getScheduler().isQueued(temp.getTaskId()) || Bukkit.getScheduler().isCurrentlyRunning(temp.getTaskId())) {
                    Bukkit.getScheduler().cancelTask(temp.getTaskId());
                }
            }
            reset.runTaskLater(XEntities.getInstance(), 10 * 20);
            temp = reset;
        }
    }

    @EventHandler
    public void onSnailDamage(@NotNull EntityDamageEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof Snail snail) {
            Bukkit.getOnlinePlayers().forEach(player -> player.playSound(snail.getBukkitEntity().getLocation(), Sound.BLOCK_SLIME_BLOCK_BREAK, 0.25F, 1.0F + random.nextFloat()));
            snail.hurtModel();
        }
    }

    @EventHandler
    public void onSlimeDeath(@NotNull EntityDeathEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof Snail snail) {
            snail.setHidden(true);
            if (event.getEntity().getKiller() != null) {
                Vector vector = event.getEntity().getKiller().getLocation().getDirection();
                snail.lookAt(((CraftEntity) event.getEntity().getKiller()).getHandle(), 360, 0);
                snail.getBukkitEntity().setVelocity(new Vector(vector.getX(), 0.75D, vector.getZ()).multiply(0.5D));
                if (random.nextBoolean()) {
                    event.getDrops().add(snail.SNAIL_SHELL_MISC_ITEM_MODEL);
                }
            }
        }
    }

    private int timer;
    private int frequency;
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

    @EventHandler
    public void onPlayerCollidesWithSnail(@NotNull PlayerMoveEvent event) {
        for (Entity entity : event.getPlayer().getNearbyEntities(0.5D, 0.5D, 0.5D)) {
            if (((CraftEntity) entity).getHandle() instanceof Snail snail) {
                if (snail.isHidden() && snail.getBukkitEntity().getVelocity().getX() != 0.0D && snail.getBukkitEntity().getVelocity().getZ() != 0.0D) {
                    snail.lookAt(((CraftEntity) event.getPlayer()).getHandle(), 360, 0);
                    RollAnimation rollAnimation = new RollAnimation(snail, 0, 0);
                    rollAnimation.start();
                    rollAnimation.frame();
                }
            }
        }
    }
}
