package pl.tuso.xentities.entity.human;

import net.minecraft.core.Rotations;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import pl.tuso.xentities.entity.human.animation.ArmsAnimation;
import pl.tuso.xentities.model.ModelFactory;
import pl.tuso.xentities.type.FantasticBeast;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.type.Parent;
import pl.tuso.xentities.type.Part;

public class Human extends Parent {
    private final ModelFactory HEAD = new ModelFactory(Material.PAPER, 6);
    private final ModelFactory RIGHT_ARM = new ModelFactory(Material.PAPER, 7);
    private final ModelFactory LEFT_ARM = new ModelFactory(Material.PAPER, 8);
    private final ModelFactory BODY = new ModelFactory(Material.PAPER, 9);
    private final ModelFactory LEFT_LEG = new ModelFactory(Material.PAPER, 10);
    private final ModelFactory RIGHT_LEG = new ModelFactory(Material.PAPER, 10);

    private Part body;

    private ArmsAnimation armsAnimation = new ArmsAnimation(this, 0, 1);

    public Human(EntityType<? extends IntelligentArmorStand> entitytypes, Level world) {
        super(entitytypes, world);
        this.setInvisible(true);
        this.setItemSlot(EquipmentSlot.HEAD, HEAD.getNMSCopy());
        this.setItemSlot(EquipmentSlot.MAINHAND, RIGHT_ARM.getNMSCopy());
        this.setItemSlot(EquipmentSlot.OFFHAND, LEFT_ARM.getNMSCopy());
        this.setDisplacementY(-0.05F);

        body = new Part(this, FantasticBeast.PART, world);
        body.setInvisible(true);
        body.setItemSlot(EquipmentSlot.HEAD, BODY.getNMSCopy());
        body.setItemSlot(EquipmentSlot.MAINHAND, RIGHT_LEG.getNMSCopy());
        body.setItemSlot(EquipmentSlot.OFFHAND, LEFT_LEG.getNMSCopy());

        this.spawnParts(world);

        armsAnimation.start();

        this.setRightArmPose(new Rotations(0.0F, 0.0F, 5.5F));
        this.setLeftArmPose(new Rotations(0.0F, 0.0F, -5.5F));
    }

    @Override
    public void tick() {
        super.tick();
        Bukkit.getOnlinePlayers().forEach(player -> {
            //TODO fix (currently only works on south)
            if (player.getLocation().distanceSquared(this.getBukkitEntity().getLocation()) < getFollowRange()) {
                Location fromLocation = this.getBukkitEntity().getLocation();
                Location toLocation = player.getLocation();

                double xDiff = toLocation.getX() - fromLocation.getX();
                double yDiff = toLocation.getY() - fromLocation.getY();
                double zDiff = toLocation.getZ() - fromLocation.getZ();
                double distanceXZ = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
                double distanceY = Math.sqrt(distanceXZ * distanceXZ + yDiff * yDiff);
                double yaw = Math.toDegrees(Math.acos(xDiff / distanceXZ)) - 90.0D;
                double pitch = Math.toDegrees(Math.acos(yDiff / distanceY)) - 90.0D;

                if (zDiff < 0.0D) {
                    yaw += Math.abs(180.0D - yaw) * 2.0D;
                }

                if (yaw < this.getYRot() + 45.0F && yaw > this.getYRot() - 45.0D) {
                    this.setHeadPose(new Rotations((float) pitch, (float) yaw, 0.0F));
                } else if (yaw > 360.0D || yaw < 0.0D) {
                    this.setHeadPose(new Rotations((float) pitch, this.getYRot() - 45.0F, 0.0F));
                } else if (yaw < 360.0D) {
                    this.setHeadPose(new Rotations((float) pitch, this.getYRot() + 45.0F, 0.0F));
                }
//                Bukkit.broadcast(Component.text(yaw));
            } else {
                this.setHeadPose(new Rotations(0.0F, 0.0F, 0.0F));
            }
        });
    }

    @Override
    public void animationTick() {
        armsAnimation.animate();
    }

    @Override
    public void positionSubEntityTick() {
        body.setYRot(getYRot());
        body.positionPartRelatively(this, 0.0F, -0.75F, 0.0F);
    }

    @Override
    public IntelligentArmorStand getEntity() {
        return this;
    }
}
