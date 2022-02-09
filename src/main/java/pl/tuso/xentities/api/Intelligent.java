package pl.tuso.xentities.api;

import net.minecraft.core.Rotations;
import pl.tuso.xentities.type.IntelligentArmorStand;

public interface Intelligent {
    void setDisplacement(float displacement);

    float getDisplacement();

    void setSmall(boolean flag);

    boolean isSmall();

    void setShowArms(boolean flag);

    boolean isShowArms();

    void setNoBasePlate(boolean flag);

    boolean isNoBasePlate();

    void setMarker(boolean flag);

    boolean isMarker();

    void setYRotAsYaw(boolean flag);

    boolean isYRotAsYaw();

    void setHeadPose(Rotations vector3f);

    void setBodyPose(Rotations vector3f);

    void setLeftArmPose(Rotations vector3f);

    void setRightArmPose(Rotations vector3f);

    void setLeftLegPose(Rotations vector3f);

    void setRightLegPose(Rotations vector3f);

    Rotations getHeadPose();

    Rotations getBodyPose();

    Rotations getLeftArmPose();

    Rotations getRightArmPose();

    Rotations getLeftLegPose();

    Rotations getRightLegPose();

    void setMovementSpeed(double speed);

    double getMovementSpeed();

    void setFlyingSpeed(double speed);

    double getFlyingSpeed();

    void setMaxBaseHealth(double health);

    double getMaxBaseHealth();

    void setKnockbackResistance(double resistance);

    double getKnockbackResistance();

    void setFollowRange(double range);

    double getFollowRange();

    void animationTick();
}
