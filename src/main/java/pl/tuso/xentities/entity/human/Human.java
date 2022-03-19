package pl.tuso.xentities.entity.human;

import net.minecraft.core.Rotations;
import net.minecraft.network.chat.Component;
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
        this.setCustomName(Component.nullToEmpty("Human"));
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
                Location humanLocation = this.getBukkitEntity().getLocation();
                Location playerLocation = player.getLocation();
                if (player.isSneaking()) {
                    playerLocation.setY(playerLocation.getY() - 0.5F);
                }
                humanLocation.setDirection(playerLocation.subtract(humanLocation).toVector());
                float yaw = humanLocation.getYaw() - this.getYRot();
                float pitch = humanLocation.getPitch();

                Bukkit.broadcast(net.kyori.adventure.text.Component.text("before " + yaw));
                while (yaw < 0.0F || yaw > 360.0F) {
                    if (yaw > 360.0F) {
                        yaw -= 360.0F;
                    } else if (yaw < 0.0F) {
                        yaw += 360.0F;
                    }
                }
                Bukkit.broadcast(net.kyori.adventure.text.Component.text("after " + yaw));

                if (yaw < 45.0F || yaw > 360.0F - 45.0D) {
                    this.setHeadPose(new Rotations(pitch, yaw, 0.0F));
                }
                if (yaw > 45.0F && yaw < 180.0F) {
                    this.setYRot(this.getYRot() + 15.0F);
                    this.setHeadPose(new Rotations(pitch, 45.0F, 0.0F));
                } else if (yaw < 360.0F - 45.0F && yaw > 180.0F) {
                    this.setYRot(this.getYRot() - 15.0F);
                    this.setHeadPose(new Rotations(pitch, 360.0F - 45.0F, 0.0F));
                }
//                } else if (yaw > this.getYRot() + 45.0F && yaw < 180.0F) {
//                    this.setHeadPose(new Rotations(pitch, this.getYRot() + 45.0F, 0.0F));
//                } else if (yaw < this.getYRot() - 45.0F && yaw > 180.0F) {
//                    this.setHeadPose(new Rotations(pitch, this.getYRot() - 45.0F, 0.0F));
//                }
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
