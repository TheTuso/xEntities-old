package pl.tuso.xentities.animation;

import net.minecraft.core.Rotations;
import pl.tuso.xentities.type.IntelligentArmorStand;

public class ExampleAnimation extends AnimationManager {
    private float step = lerp(90.0F, 90.0F);

    public ExampleAnimation(IntelligentArmorStand armorStand, int frequency, int frameDuration) {
        super(armorStand, frequency, frameDuration);
    }

    @Override
    public void frame() {
        getArmorStand().setHeadPose(new Rotations(getArmorStand().getHeadPose().getX() + step, 0.0F, 0.0F));
        if (getArmorStand().getHeadPose().getX() == 90.0F) {
            step *= -1;
        } else if (getArmorStand().getHeadPose().getX() == 0.0F) {
            stop();
            start();
            step = lerp(90.0F, 90.0F);
        }
    }
}
