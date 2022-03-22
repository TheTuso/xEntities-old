package pl.tuso.xentities.entity.human.animation;

import net.minecraft.core.Rotations;
import pl.tuso.xentities.animation.AnimationManager;
import pl.tuso.xentities.entity.human.Human;
import pl.tuso.xentities.type.IntelligentArmorStand;

public class ArmsAnimation extends AnimationManager {
    private static final int SIZE = 16;
    private int a = SIZE / 2;
    private int b = a;
    private int radius = 4 * SIZE / 5;
    private int steps = SIZE * 4;
    private int count = 0;

    public ArmsAnimation(IntelligentArmorStand armorStand, int frequency, int frameDuration) {
        super(armorStand, frequency, frameDuration);
    }

    @Override
    public void frame() {
        if (count >= steps) {
            count = 0;
        }
        double t = 2.0D * Math.PI * count / steps;
        double x = Math.round(a + radius * Math.cos(t));
        double y = Math.round(b + radius * Math.sin(t));
        x -= SIZE / 2.0D;
        y -= SIZE / 2.0D;
        y *= -1.0D;
        x /= 10.0D;
        y /= 10.0D;
        x /= 2.0D;
        y /= 4.0D;

        Rotations rightArmPose = this.getArmorStand().getRightArmPose();
        Rotations lefttArmPose = this.getArmorStand().getLeftArmPose();

        if (this.getArmorStand().getBukkitEntity().getHandle() instanceof Human human) {
            //RIGHT ARM
            if (!human.waveAnimation.isRunning()) {
                this.getArmorStand().setRightArmPose(new Rotations((float) (rightArmPose.getX() + x), rightArmPose.getY(), (float) (rightArmPose.getZ() + y)));
            }
            //LEFT ARM
            this.getArmorStand().setLeftArmPose(new Rotations((float) (lefttArmPose.getX() + x * -1), lefttArmPose.getY(), (float) (lefttArmPose.getZ() + y * -1)));
        }
        count++;
    }
}
