package pl.tuso.xentities.animation;

import org.bukkit.scheduler.BukkitRunnable;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.type.IntelligentArmorStand;

public class AnimationManager {
    private final IntelligentArmorStand armorStand;
    private int frequency;//how often
    private int per;//frame per tick
    private boolean stopped = false;
    //timers currently per animation
    public AnimationManager(IntelligentArmorStand armorStand, int frequency, int per) {
        this.armorStand = armorStand;
        this.frequency = frequency;
        this.per = per;
    }

    public void animate() {
        if (armorStand.isRemoved()) {
            stop();
        }
    }

    private BukkitRunnable animation = new BukkitRunnable() {
        @Override
        public void run() {
            animate();
        }
    };

    public void run(long delay, long peroid) {
        this.play();
        animation.runTaskTimer(XEntities.getInstance(), delay, peroid);
    }

    public void play() {
        stopped = false;
    }

    public void stop() {
        stopped = true;

        int taskIdOrFailed;
        try {
            taskIdOrFailed = animation.getTaskId();
        } catch (Exception e) {
            taskIdOrFailed = -1;
        }
        boolean failed = taskIdOrFailed == -1;
        if (!failed) {
            animation.cancel();
        }
    }

    public IntelligentArmorStand getArmorStand() {
        return armorStand;
    }

    public float interpolate(float pose, float steps) {
        return pose / steps;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getPer() {
        return per;
    }

    public void setPer(int per) {
        this.per = per;
    }

    public boolean isStopped() {
        return stopped;
    }
}
