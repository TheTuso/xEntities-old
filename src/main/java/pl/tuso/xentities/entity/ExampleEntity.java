package pl.tuso.xentities.entity;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import pl.tuso.xentities.animation.ExampleAnimation;
import pl.tuso.xentities.model.ModelFactory;
import pl.tuso.xentities.type.FantasticBeast;
import pl.tuso.xentities.type.IntelligentArmorStand;
import pl.tuso.xentities.type.Parent;
import pl.tuso.xentities.type.Part;

public class ExampleEntity extends Parent {
    private Part part1;
    private Part part2;
    private MainHitbox hitbox;
    public ExampleAnimation animation = new ExampleAnimation(this, 20, 0);

    private ModelFactory model = new ModelFactory(Material.PAPER, 1);

    public ExampleEntity(EntityType<? extends ExampleEntity> entitytypes, Level world) {
        super(entitytypes, world);
        this.setYRotAsYaw(true);
        this.setDisplacementY(-0.5F);

        part1 = new Part(this, FantasticBeast.PART, world) {
            @Override
            public void registerGoals() {
                this.goalSelector.addGoal(0, new FloatGoal(this));
                this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 6.0F));
                this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
            }
        };
        part1.setDisplacementY(-0.5F);
        part2 = new Part(this, FantasticBeast.PART, world);
        part2.setDisplacementY(-0.5F);
        hitbox = new MainHitbox(this, world);

        Bukkit.broadcast(Component.text(this.getParts().toString()).color(TextColor.color(100, 209, 124)));
        Bukkit.broadcast(Component.text(this.getHitboxes().toString()).color(TextColor.color(189, 209, 62)));

        this.setItemSlot(EquipmentSlot.HEAD, model.getNMSCopy());

        this.spawnParts(world);
        this.addHitboxes(world);
        this.initHitboxes();

        animation.start();
    }

    @Override
    public void tick() {
        super.tick();
        //Other tick stuff
    }

    @Override
    public void animationTick() {
        animation.animate();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
    }

    @Override
    public void positionSubEntityTick() {
        part1.positionPartRelatively(this, 0, 0, -1);
        part2.positionPartRelatively(this, 0, 0, 1);

        if (part1.getBukkitYaw() >= getYRot() - 45 && part1.getBukkitYaw() <= getYRot() + 45) {
            part1.setYRot(part1.getBukkitYaw());
        } else {
            part1.setYRot(getYRot());
        }
        part2.setYRot(getYRot() + 90F);

        hitbox.positionHitboxRelatively(this, 0, 1.5, 0);
    }

    @Override
    public IntelligentArmorStand getEntity() {
        return this;
    }
}