package pl.tuso.xentities.type;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.attribute.Attributable;
import org.bukkit.attribute.Attribute;
import org.bukkit.util.Vector;
import pl.tuso.xentities.api.Intelligent;
import pl.tuso.xentities.util.RotDirectionUtil;
import pl.tuso.xentities.util.PacketUtil;

import javax.annotation.Nullable;
import javax.annotation.OverridingMethodsMustInvokeSuper;
import java.util.Optional;

public class IntelligentArmorStand extends PathfinderMob implements Intelligent {
    //LivingEntity
    protected static final EntityDataAccessor<Byte> DATA_LIVING_ENTITY_FLAGS = EntityDataSerializers.BYTE.createAccessor(8);
    public static final EntityDataAccessor<Float> DATA_HEALTH_ID = EntityDataSerializers.FLOAT.createAccessor(9);
    private static final EntityDataAccessor<Integer> DATA_EFFECT_COLOR_ID = EntityDataSerializers.INT.createAccessor(10);
    private static final EntityDataAccessor<Boolean> DATA_EFFECT_AMBIENCE_ID = EntityDataSerializers.BOOLEAN.createAccessor(11);
    public static final EntityDataAccessor<Integer> DATA_ARROW_COUNT_ID = EntityDataSerializers.INT.createAccessor(12);
    private static final EntityDataAccessor<Integer> DATA_STINGER_COUNT_ID = EntityDataSerializers.INT.createAccessor(13);
    private static final EntityDataAccessor<Optional<BlockPos>> SLEEPING_POS_ID = EntityDataSerializers.OPTIONAL_BLOCK_POS.createAccessor(14);
    //ArmorStand
    private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_LEG_POSE = new Rotations(0.0F, 0.0F, 01.0F);
    private static final Rotations DEFAULT_RIGHT_LEG_POSE = new Rotations(0.0F, 0.0F, 0.0F);

    public static final EntityDataAccessor<Byte> DATA_CLIENT_FLAGS = EntityDataSerializers.BYTE.createAccessor(15);
    public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = EntityDataSerializers.ROTATIONS.createAccessor(16);
    public static final EntityDataAccessor<Rotations> DATA_BODY_POSE = EntityDataSerializers.ROTATIONS.createAccessor(17);
    public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE = EntityDataSerializers.ROTATIONS.createAccessor(18);
    public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE = EntityDataSerializers.ROTATIONS.createAccessor(19);
    public static final EntityDataAccessor<Rotations> DATA_LEFT_LEG_POSE = EntityDataSerializers.ROTATIONS.createAccessor(20);
    public static final EntityDataAccessor<Rotations> DATA_RIGHT_LEG_POSE = EntityDataSerializers.ROTATIONS.createAccessor(21);

    private Rotations headPose = DEFAULT_HEAD_POSE;
    private Rotations bodyPose = DEFAULT_BODY_POSE;
    private Rotations leftArmPose = DEFAULT_LEFT_ARM_POSE;
    private Rotations rightArmPose = DEFAULT_RIGHT_ARM_POSE;
    private Rotations leftLegPose = DEFAULT_LEFT_LEG_POSE;
    private Rotations rightLegPose = DEFAULT_LEFT_LEG_POSE;

    private float displacementX = 0.0F;
    private float displacementY = 0.0F;
    private float displacementZ = 0.0F;
    private boolean yRotAsYaw = false;

    public IntelligentArmorStand(EntityType<? extends IntelligentArmorStand> entitytypes, Level world) {
        super(entitytypes, world);
        this.getBukkitEntity().customName(Component.text("IntelligentArmorStand"));
        setMovementSpeed(0.25D);
        setShowArms(true);
        setNoBasePlate(true);

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            this.setDropChance(slot, 0.0F);
        }
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void addAdditionalSaveData(CompoundTag nbttagcompound) {
        super.addAdditionalSaveData(nbttagcompound);
        Bukkit.broadcast(net.kyori.adventure.text.Component.text("saved entity!").color(TextColor.color(238, 211, 38)));
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void readAdditionalSaveData(CompoundTag nbttagcompound) {
        super.readAdditionalSaveData(nbttagcompound);
        Bukkit.broadcast(net.kyori.adventure.text.Component.text("load entity!").color(TextColor.color(129, 232, 11)));
    }

    @Override
    @OverridingMethodsMustInvokeSuper
    public void tick() {
        super.tick();
            Location originalLocation = this.getBukkitEntity().getLocation();
            Vector direction = RotDirectionUtil.toDirection(this.getYRot(), this.getXRot());
            Vector i = new Vector(0, 1, 0);

            Vector xPos = i.clone().crossProduct(direction).multiply(displacementX);
            Vector zPos = direction.clone().multiply(displacementZ);
            Location fakeLocation = originalLocation.clone().subtract(xPos).subtract(zPos);

            PacketUtil.sendPackets(PacketUtil.teleportWithPackets(this, fakeLocation.getX(), fakeLocation.getY() + displacementY, fakeLocation.getZ()));
        if (yRotAsYaw) {
            setYRot(getBukkitYaw());
        }
        animationTick();
    }

    @Override
    protected void defineSynchedData() {
        //LivingEntity
        this.entityData.define(DATA_LIVING_ENTITY_FLAGS, (byte)0);
        this.entityData.define(DATA_EFFECT_COLOR_ID, 0);
        this.entityData.define(DATA_EFFECT_AMBIENCE_ID, false);
        this.entityData.define(DATA_ARROW_COUNT_ID, 0);
        this.entityData.define(DATA_STINGER_COUNT_ID, 0);
        this.entityData.define(DATA_HEALTH_ID, 1.0F);
        this.entityData.define(SLEEPING_POS_ID, Optional.empty());
        //ArmorStand
        this.entityData.define(DATA_CLIENT_FLAGS, (byte)0);
        this.entityData.define(DATA_HEAD_POSE, DEFAULT_HEAD_POSE);
        this.entityData.define(DATA_BODY_POSE, DEFAULT_BODY_POSE);
        this.entityData.define(DATA_LEFT_ARM_POSE, DEFAULT_LEFT_ARM_POSE);
        this.entityData.define(DATA_RIGHT_ARM_POSE, DEFAULT_RIGHT_ARM_POSE);
        this.entityData.define(DATA_LEFT_LEG_POSE, DEFAULT_LEFT_LEG_POSE);
        this.entityData.define(DATA_RIGHT_LEG_POSE, DEFAULT_RIGHT_LEG_POSE);
    }

    @Override
    protected void registerGoals() {

    }
    //Property
    @Override
    public boolean isNoAi() {
        return false;
    }

    @Override
    public void setInvisible(boolean flag) {
        super.setInvisible(flag);
        this.persistentInvisibility = flag;
    }

    @Override
    public void setDisplacementX(float displacementX) {
        this.displacementX = displacementX;
    }

    @Override
    public float getDisplacementX() {
        return this.displacementX;
    }

    @Override
    public void setDisplacementY(float displacementY) {
        this.displacementY = displacementY;
    }

    @Override
    public float getDisplacementY() {
        return this.displacementY;
    }

    @Override
    public void setDisplacementZ(float displacementZ) {
        this.displacementZ = displacementZ;
    }

    @Override
    public float getDisplacementZ() {
        return this.displacementZ;
    }

    @Override
    public void setSmall(boolean flag) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 1, flag));
    }

    @Override
    public boolean isSmall() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 1) != 0;
    }

    @Override
    public void setShowArms(boolean flag) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 4, flag));
    }

    @Override
    public boolean isShowArms() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 4) != 0;
    }

    @Override
    public void setNoBasePlate(boolean flag) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 8, flag));
    }

    @Override
    public boolean isNoBasePlate() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 8) != 0;
    }

    @Override
    public void setMarker(boolean flag) {
        this.entityData.set(DATA_CLIENT_FLAGS, this.setBit(this.entityData.get(DATA_CLIENT_FLAGS), 16, flag));
    }

    @Override
    public boolean isMarker() {
        return (this.entityData.get(DATA_CLIENT_FLAGS) & 16) != 0;
    }

    @Override
    public void setYRotAsYaw(boolean flag) {
        this.yRotAsYaw = flag;
    }

    @Override
    public boolean isYRotAsYaw() {
        return this.yRotAsYaw;
    }

    private byte setBit(byte b0, int i, boolean flag) {
        if (flag) {
            b0 = (byte)(b0 | i);
        } else {
            b0 = (byte)(b0 & ~i);
        }

        return b0;
    }
    //Pose
    @Override
    public void setHeadPose(Rotations vector3f) {
        this.headPose = vector3f;
        this.entityData.set(DATA_HEAD_POSE, vector3f);
    }

    @Override
    public void setBodyPose(Rotations vector3f) {
        this.bodyPose = vector3f;
        this.entityData.set(DATA_BODY_POSE, vector3f);
    }

    @Override
    public void setLeftArmPose(Rotations vector3f) {
        this.leftArmPose = vector3f;
        this.entityData.set(DATA_LEFT_ARM_POSE, vector3f);
    }

    @Override
    public void setRightArmPose(Rotations vector3f) {
        this.rightArmPose = vector3f;
        this.entityData.set(DATA_RIGHT_ARM_POSE, vector3f);
    }

    @Override
    public void setLeftLegPose(Rotations vector3f) {
        this.leftLegPose = vector3f;
        this.entityData.set(DATA_LEFT_LEG_POSE, vector3f);
    }

    @Override
    public void setRightLegPose(Rotations vector3f) {
        this.rightLegPose = vector3f;
        this.entityData.set(DATA_RIGHT_LEG_POSE, vector3f);
    }

    @Override
    public Rotations getHeadPose() {
        return this.headPose;
    }

    @Override
    public Rotations getBodyPose() {
        return this.bodyPose;
    }

    @Override
    public Rotations getLeftArmPose() {
        return this.leftArmPose;
    }

    @Override
    public Rotations getRightArmPose() {
        return this.rightArmPose;
    }

    @Override
    public Rotations getLeftLegPose() {
        return this.leftLegPose;
    }

    @Override
    public Rotations getRightLegPose() {
        return this.rightLegPose;
    }
    //Attribute
    @Override
    public void setMovementSpeed(double speed) {
        ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(speed);
    }

    @Override
    public double getMovementSpeed() {
        return ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getBaseValue();
    }

    @Override
    public void setFlyingSpeed(double speed) {
        ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FLYING_SPEED).setBaseValue(speed);
    }

    @Override
    public double getFlyingSpeed() {
        return ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FLYING_SPEED).getBaseValue();
    }

    @Override
    public void setMaxBaseHealth(double health) {
        ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
    }

    @Override
    public double getMaxBaseHealth() {
        return ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
    }

    @Override
    public void setKnockbackResistance(double resistance) {
        ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).setBaseValue(resistance);
    }

    @Override
    public double getKnockbackResistance() {
        return ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE).getBaseValue();
    }

    @Override
    public void setFollowRange(double range) {
        ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(range);
    }

    @Override
    public double getFollowRange() {
        return ((Attributable) this.getBukkitEntity()).getAttribute(Attribute.GENERIC_FOLLOW_RANGE).getBaseValue();
    }

    @Override
    public void animationTick() {

    }
    //Sound
    @Override
    public Fallsounds getFallSounds() {
        return new Fallsounds(SoundEvents.ARMOR_STAND_FALL, SoundEvents.ARMOR_STAND_FALL);
    }

    @Override
    @Nullable
    protected SoundEvent getHurtSound(DamageSource damagesource) {
        return SoundEvents.ARMOR_STAND_HIT;
    }

    @Override
    @Nullable
    public SoundEvent getDeathSound() {
        return SoundEvents.ARMOR_STAND_BREAK;
    }
}