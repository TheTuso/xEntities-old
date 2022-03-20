package pl.tuso.xentities.entity.human.animation;

import net.minecraft.core.Rotations;
import pl.tuso.xentities.animation.AnimationManager;
import pl.tuso.xentities.entity.human.Human;
import pl.tuso.xentities.type.IntelligentArmorStand;

public class WaveAnimation extends AnimationManager {
    private float waveArea = 16.0F;
    private Rotations startPose = new Rotations(0.0F, 0.0F, 5.5F);
    private boolean isWaving = false;
    private int waves = 3;
    float step = this.lerp(160.0F + waveArea, 32.0F);

    public WaveAnimation(IntelligentArmorStand armorStand, int frequency, int frameDuration) {
        super(armorStand, frequency, frameDuration);
    }

    @Override
    public void frame() {
        Rotations rightArmPose = this.getArmorStand().getRightArmPose();
        Rotations headPose = this.getArmorStand().getHeadPose();
        IntelligentArmorStand body = null;
        float xDisplacement = this.getArmorStand().getDisplacementX();
        float yDisplacement = this.getArmorStand().getDisplacementY();
        if (this.getArmorStand().getBukkitEntity().getHandle() instanceof Human human) {
            body = human.body.getEntity();
        }
        Rotations bodyPose = body.getHeadPose();
        Rotations rightLegPose = body.getRightArmPose();
        if (!isWaving && rightArmPose.getZ() <= 160.0F + waveArea) {
            this.getArmorStand().setRightArmPose(new Rotations(0.0F, 0.0F, rightArmPose.getZ() + this.lerp(160.0F + waveArea, 24.0F)));
            if (headPose.getZ() < 32.0F) {
                this.getArmorStand().setHeadPose(new Rotations(headPose.getX(), headPose.getY(), headPose.getZ() + this.lerp(32.0F, 16.0F)));
                if (bodyPose.getZ() < 16.0F) {
                    body.setHeadPose(new Rotations(bodyPose.getX(), bodyPose.getY(), bodyPose.getZ() + this.lerp(16.0F, 16.0F)));
                    this.getArmorStand().setDisplacementX(xDisplacement - (this.lerp(16.0F, 14.0F)) / 100);
                }
            }
        } else {
            isWaving = true;
            this.getArmorStand().setRightArmPose(new Rotations(0.0F, 0.0F, 160.0F + waveArea));
        }
        if (isWaving) {
            if (rightArmPose.getZ() >= 160.0F + waveArea) { //to left
                step *= -1;
            } else if (rightArmPose.getZ() <= 160.0F - waveArea) { //to right
                step *= -1;
            }
            float headRot = step;
            float bodyRot = step;
            float legRot = step;
            float displacement = step / -8.0F;

            this.getArmorStand().setRightArmPose(new Rotations(0.0F, 0.0F, rightArmPose.getZ() + step));
            body.setHeadPose(new Rotations(bodyPose.getX(), bodyPose.getY(), bodyPose.getZ() + (bodyRot / -8.0F)));
            this.getArmorStand().setHeadPose(new Rotations(headPose.getX(), headPose.getY(), headPose.getZ() + (headRot / 8.0F)));
            this.getArmorStand().setDisplacementX(xDisplacement - displacement / 100.0F);
            this.getArmorStand().setDisplacementY(yDisplacement - displacement / 100.0F);
            body.setRightArmPose(new Rotations(rightLegPose.getX(), rightLegPose.getY(), rightLegPose.getZ() + (legRot / -8.0F)));
        }
    }
}
