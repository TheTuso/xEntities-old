package pl.tuso.xentities.entity.snail.listeners;

import io.papermc.paper.event.entity.EntityMoveEvent;
import net.minecraft.core.Rotations;
import net.minecraft.world.entity.EquipmentSlot;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.entity.snail.Snail;
import pl.tuso.xentities.entity.snail.animation.RollAnimation;
import pl.tuso.xentities.model.ModelFactory;

import java.util.Random;

public class SnailListener implements Listener {
    private ModelFactory snail_model = new ModelFactory(Material.PAPER, 1);
    private ModelFactory snail_shell_model = new ModelFactory(Material.PAPER, 2);
    private BukkitRunnable temp;
    @EventHandler
    public void onSnailHit(@NotNull EntityDamageByEntityEvent event) {
        if (((CraftEntity) event.getEntity()).getHandle() instanceof Snail snail) {
            Vector vector = event.getDamager().getLocation().getDirection();

            snail.setItemSlot(EquipmentSlot.HEAD, snail_shell_model.getNMSCopy());
            snail.hurtModel();

            RollAnimation animation = new RollAnimation(snail, 0, 0);
            animation.start();
            animation.forcePlay(0, 0);

            Location location = snail.getBukkitEntity().getLocation();
            location.setDirection(event.getDamager().getLocation().getDirection().multiply(-1));
            snail.getBukkitEntity().teleport(location);
            snail.setMovementSpeed(0.0D);

            if (snail.isKnocked()) {
                snail.getBukkitEntity().setVelocity(new Vector(vector.getX(), 0.0D, vector.getZ()).multiply(0.5D));
            } else {
                snail.getBukkitEntity().setVelocity(new Vector(vector.getX(), 0.75D, vector.getZ()).multiply(0.5D));
                snail.setKnocked(true);
            }

            BukkitRunnable reset = new BukkitRunnable() {
                @Override
                public void run() {
                    if (snail.isKnocked()) {
                        snail.setItemSlot(EquipmentSlot.HEAD, snail_model.getNMSCopy());
                        snail.setMovementSpeed(0.1D);
                        snail.getBukkitEntity().setVelocity(new Vector(0.0D, 0.75D, 0.0D).multiply(0.5D));
                        snail.setHeadPose(new Rotations(0.0F, snail.getHeadPose().getY(), snail.getHeadPose().getZ()));
                        snail.setKnocked(false);
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
            reset.runTaskLater(XEntities.getInstance(), 5 * 20);
            temp = reset;
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
