package pl.tuso.xentities.entity.snail.animation;

import net.minecraft.core.Rotations;
import pl.tuso.xentities.animation.AnimationManager;
import pl.tuso.xentities.type.IntelligentArmorStand;

public class RollAnimation extends AnimationManager {
    private float minus = 10.0F;

    public RollAnimation(IntelligentArmorStand armorStand, int frequency, int frameDuration) {
        super(armorStand, frequency, frameDuration);
    }

    @Override
    public void frame() {
        Rotations rotations = getArmorStand().getHeadPose();
        if (minus > 0.0F) {
            getArmorStand().setHeadPose(new Rotations(rotations.getX() - minus, rotations.getY(), rotations.getZ()));
            minus -= 0.5F;
        } else {
            stop();
        }
    }
}
