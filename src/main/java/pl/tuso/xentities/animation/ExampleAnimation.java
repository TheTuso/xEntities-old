package pl.tuso.xentities.animation;

import net.minecraft.core.Rotations;
import pl.tuso.xentities.type.IntelligentArmorStand;

public class ExampleAnimation extends AnimationManager {
    public ExampleAnimation(IntelligentArmorStand armorStand) {
        super(armorStand, 60, 0);
    }

    private int timer1 = 0;
    private int timer2 = 0;
    private float step = interpolate(90.0F, 60.0F);

    @Override
    public void animate() {
        super.animate();
        if (timer1 == getFrequency()) {
            if (timer2 == getPer()) {
                timer2 = 0;
                float currentHeadX = getArmorStand().getHeadPose().getX();
                float currentLeftArmZ = getArmorStand().getLeftArmPose().getZ();
                float currentRightArmZ = getArmorStand().getRightArmPose().getZ();
                getArmorStand().setHeadPose(new Rotations(currentHeadX + step, 0, 0));
                getArmorStand().setLeftArmPose(new Rotations(0, 0, currentLeftArmZ - step));
                getArmorStand().setRightArmPose(new Rotations(0, 0, currentRightArmZ + step));
                if (getArmorStand().getHeadPose().getX() == 90.0F) {
                    step *= -1;
                } else if (getArmorStand().getHeadPose().getX() == 0.0) {
                    stop();
                    step *= -1;
                    timer1 = 0;
                    return;
                }
            } else {
                timer2++;
            }
        } else {
            timer1++;
        }
    }
}
