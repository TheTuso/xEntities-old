package pl.tuso.xentities.api;

import pl.tuso.xentities.type.IntelligentArmorStand;

public interface Animation {
    void play(long delay, long peroid);

    void forcePlay(long delay, long peroid);

    void start();

    void stop();

    void frame();

    void animate();

    default float lerp(float pose, float steps) {
        return  pose / steps;
    }

    boolean isRunning();

    void setFrequency(int frequency);

    int getFrequency();

    void setFrameDuration(int duration);

    int getFrameDuration();

    IntelligentArmorStand getArmorStand();
}
