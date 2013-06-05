package de.paleocrafter.pmfw.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.packet.Packet;

import com.google.common.collect.BiMap;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.relauncher.Side;
import de.paleocrafter.pmfw.network.PaleoPacketHandler;

/**
 *
 * PaleoMachineFramework
 *
 * PaleoPacket
 *
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 *
 */
public abstract class PaleoPacket {
    private static final BiMap<Integer, Class<? extends PaleoPacket>> idMap;

    static {
        ImmutableBiMap.Builder<Integer, Class<? extends PaleoPacket>> builder = ImmutableBiMap
                .builder();
        
        builder.put(Integer.valueOf(0), TileDataPacket.class);
        
        idMap = builder.build();
    }

    public static PaleoPacket constructPacket(int packetId)
            throws ProtocolException, ReflectiveOperationException {
        Class<? extends PaleoPacket> clazz = idMap.get(Integer
                .valueOf(packetId));
        if (clazz == null) {
            throw new ProtocolException("Unknown Packet Id!");
        } else {
            return clazz.newInstance();
        }
    }

    @SuppressWarnings("serial")
    public static class ProtocolException extends Exception {

        public ProtocolException() {
        }

        public ProtocolException(String message, Throwable cause) {
            super(message, cause);
        }

        public ProtocolException(String message) {
            super(message);
        }

        public ProtocolException(Throwable cause) {
            super(cause);
        }
    }

    public final int getPacketId() {
        if (idMap.inverse().containsKey(getClass())) {
            return idMap.inverse().get(getClass()).intValue();
        } else {
            throw new RuntimeException("Packet " + getClass().getSimpleName()
                    + " is missing a mapping!");
        }
    }

    public final Packet makePacket() throws IllegalArgumentException {
        if (PaleoPacketHandler.CHANNEL_NAME != null) {
            ByteArrayDataOutput out = ByteStreams.newDataOutput();
            out.writeByte(getPacketId());
            write(out);
            return PacketDispatcher.getPacket(PaleoPacketHandler.CHANNEL_NAME,
                    out.toByteArray());
        }
        throw new IllegalArgumentException(
                "You have to define a channel for the PowerGrid packets first!");
    }

    public abstract void write(ByteArrayDataOutput out);

    public abstract void read(ByteArrayDataInput in);

    public abstract void execute(EntityPlayer player, Side side);
}

