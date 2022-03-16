package pl.tuso.xentities.entity.human.animation;

import net.minecraft.core.Rotations;
import pl.tuso.xentities.animation.AnimationManager;
import pl.tuso.xentities.type.IntelligentArmorStand;

public class ArmsAnimation extends AnimationManager {
    public ArmsAnimation(IntelligentArmorStand armorStand, int frequency, int frameDuration) {
        super(armorStand, frequency, frameDuration);
    }

    private float rightFrontOrBack = -0.3F;
    private float rightLeftOrRight = 0.3F;
    private float rightMinus;
    private float leftFrontOrBack = 0.3F;
    private float leftLeftOrRight = -0.3F;
    private float leftMinus;
    @Override
    public void frame() {
        Rotations rightArmPose = this.getArmorStand().getRightArmPose();
        Rotations lefttArmPose = this.getArmorStand().getLeftArmPose();
        //RIGHT ARM
        if (this.round(rightArmPose.getZ()) >= 0.0F) {
            rightLeftOrRight -= 0.01F;
        } else {
            rightLeftOrRight *= -1;
        }
        //TODO FIX
        if (rightFrontOrBack <= -0.3F) {
            rightMinus = 0.01F;
        } else if (rightFrontOrBack >= 0.3F) {
            rightMinus = -0.01F;
        }

        rightFrontOrBack += rightMinus;

        this.getArmorStand().setRightArmPose(new Rotations(rightArmPose.getX() + rightFrontOrBack, rightArmPose.getY(), rightArmPose.getZ() + rightLeftOrRight));
        //LEFT ARM
        if (this.round(lefttArmPose.getZ()) <= 0.0F) {
            leftLeftOrRight += 0.01F;
        } else {
            leftLeftOrRight *= -1;
        }

        if (leftFrontOrBack <= -0.3F) {
            leftMinus = 0.01F;
        } else if (leftFrontOrBack >= 0.3F) {
            leftMinus = -0.01F;
        }

        leftFrontOrBack += leftMinus;

        this.getArmorStand().setLeftArmPose(new Rotations(lefttArmPose.getX() + leftFrontOrBack, lefttArmPose.getY(), lefttArmPose.getZ() + leftLeftOrRight));

    }

    private float round(float value) {
        return Math.round(value * 100.0F) / 100.0F;
    }
}
