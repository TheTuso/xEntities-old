package pl.tuso.xentities.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.level.Level;
import pl.tuso.xentities.type.FantasticBeast;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.type.Parent;
import pl.tuso.xentities.type.Part;
import pl.tuso.xentities.util.PacketUtil;

public class TestEntity extends Parent {
    private Part back;
    private ArmorStand preview;

    public TestEntity(EntityType<? extends IntelligentArmorStand> entitytypes, Level world) {
        super(entitytypes, world);
        this.setMovementSpeed(0.25D);
        this.setDisplacementZ(-0.5F);
        this.setInvisible(false);
        this.getBukkitEntity().customName(Component.text("front").color(TextColor.color(234, 161, 70)));

        back = new Part(this, FantasticBeast.PART, world);
        back.getBukkitEntity().customName(Component.text("back").color(TextColor.color(149, 234, 68)));
        back.setCustomNameVisible(true);

        this.spawnParts(world);

        preview = new ArmorStand(EntityType.ARMOR_STAND, world);
        preview.setMarker(true);
        preview.setSmall(true);
        preview.getBukkitEntity().customName(Component.text("origin point").color(TextColor.color(234, 57, 73)));
        preview.setCustomNameVisible(true);
        world.addFreshEntity(preview);
    }

    @Override
    public void tick() {
        super.tick();

        this.setCustomNameVisible(true);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
    }

    @Override
    public void positionSubEntityTick() {
        back.setYRot(getYRot());
        back.positionPartRelatively(this, 0.0F, 0.0F, 0.5F);

        preview.setPos(getX(), getY() + getDisplacementY(), getZ());
        preview.setYRot(getYRot());
        PacketUtil.sendPackets(PacketUtil.teleportWithPackets(preview, getX(), getY() + getDisplacementY(), getZ()));
    }

    @Override
    public IntelligentArmorStand getEntity() {
        return this;
    }
}
