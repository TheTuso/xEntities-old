package pl.tuso.xentities.util;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket;
import net.minecraft.world.entity.Entity;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;

public class PacketUtil {
    public static void sendPackets(Packet<?> @NotNull ... packets) {
        for (Packet packet : packets) {
            Bukkit.getOnlinePlayers().forEach(player -> ((CraftPlayer) player).getHandle().connection.send(packet));
        }
    }

    public static void sendPackets(Player player, Packet<?> @NotNull ... packets) {
        for (Packet packet : packets) {
            ((CraftPlayer) player).getHandle().connection.send(packet);
        }
    }

    public static ClientboundTeleportEntityPacket teleportWithPackets(Entity entity, double x, double y, double z) {
        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(entity);
        setXYZ(teleportEntityPacket, x, y, z);
        return teleportEntityPacket;
    }

    public static ClientboundTeleportEntityPacket teleportWithPackets(Entity entity, double x, double y, double z, float yRot, float xRot) {
        ClientboundTeleportEntityPacket teleportEntityPacket = new ClientboundTeleportEntityPacket(entity);
        setXYZ(teleportEntityPacket, x, y, z);
        byte byteYRot = (byte)((int)(yRot * 256.0F / 360.0F));
        byte byteXRot = (byte)((int)(xRot * 256.0F / 360.0F));
        try {
            final Field yRotField = teleportEntityPacket.getClass().getDeclaredField("e");
            final Field xRotField = teleportEntityPacket.getClass().getDeclaredField("f");
            yRotField.setAccessible(true);
            xRotField.setAccessible(true);
            yRotField.setByte(teleportEntityPacket, byteYRot);
            xRotField.setByte(teleportEntityPacket, byteXRot);
            yRotField.setAccessible(false);
            xRotField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return teleportEntityPacket;
    }

    private static void setXYZ(@NotNull ClientboundTeleportEntityPacket packet, double x, double y, double z) {
        try {
            final Field xField = packet.getClass().getDeclaredField("b");
            final Field yField = packet.getClass().getDeclaredField("c");
            final Field zField = packet.getClass().getDeclaredField("d");
            xField.setAccessible(true);
            yField.setAccessible(true);
            zField.setAccessible(true);
            xField.setDouble(packet, x);
            yField.setDouble(packet, y);
            zField.setDouble(packet, z);
            xField.setAccessible(false);
            yField.setAccessible(false);
            zField.setAccessible(false);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}