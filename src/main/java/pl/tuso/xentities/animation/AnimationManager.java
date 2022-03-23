package pl.tuso.xentities.animation;

import org.bukkit.Bukkit;
import pl.tuso.xentities.XEntities;
import pl.tuso.xentities.api.Animation;
import pl.tuso.xentities.type.IntelligentArmorStand;

public abstract class AnimationManager implements Animation {
    private final IntelligentArmorStand armorStand;
    private int frequency;
    private int frameDuration;
    private boolean isRunning = false;
    private int frequencyTimer = 0;
    private int frameDurationTimer = 0;

    public AnimationManager(IntelligentArmorStand armorStand, int frequency, int frameDuration) {
        this.armorStand = armorStand;
        this.frequency = frequency;
        this.frameDuration = frameDuration;
    }

    @Override
    public void play(long delay, long peroid) {
        if (isRunning) {
            return;
        }
        start();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(XEntities.getInstance(), () -> animate(), delay, peroid);
    }

    @Override
    public void forcePlay(long delay, long peroid) {
        if (isRunning) {
            stop();
        }
        start();
        Bukkit.getScheduler().scheduleSyncRepeatingTask(XEntities.getInstance(), () -> animate(), delay, peroid);
    }

    @Override
    public void start() {
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.isRunning = false;
        this.frequencyTimer = 0;
    }

    @Override
    public abstract void frame();

    @Override
    public void animate() {
        if (this.armorStand.isRemoved()) {
            this.stop();
            return;
        }
        if (frequencyTimer != frequency) {
            frequencyTimer++;
            this.isRunning = false;
            return;
        } else {
            this.isRunning = true;
        }
        if (!isRunning()) {
            return;
        }
        if (frameDurationTimer != frameDuration) {
            frameDurationTimer++;
            return;
        }
        frameDurationTimer = 0;
        frame();
    }

    @Override
    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    @Override
    public int getFrequency() {
        return this.frequency;
    }

    @Override
    public void setFrameDuration(int duration) {
        this.frameDuration = frameDurationTimer;
    }

    @Override
    public int getFrameDuration() {
        return this.frameDuration;
    }

    @Override
    public IntelligentArmorStand getArmorStand() {
        return this.armorStand;
    }
}
