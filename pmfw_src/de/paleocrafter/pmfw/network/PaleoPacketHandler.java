package de.paleocrafter.pmfw.network;

import java.util.logging.Logger;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import de.paleocrafter.pmfw.network.packet.PaleoPacket;
import de.paleocrafter.pmfw.network.packet.PaleoPacket.ProtocolException;

/**
 * 
 * PaleoMachineFramework
 * 
 * PaleoPacketHandler
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class PaleoPacketHandler implements IPacketHandler {

    public static String CHANNEL_NAME = "PMFW";

    public static void registerChannel() {
        NetworkRegistry.instance().registerChannel(new PaleoPacketHandler(),
                CHANNEL_NAME);
    }

    @Override
    public void onPacketData(INetworkManager manager,
            Packet250CustomPayload packet, Player player) {
        try {
            EntityPlayer entityPlayer = (EntityPlayer) player;
            ByteArrayDataInput in = ByteStreams.newDataInput(packet.data);
            int packetId = in.readUnsignedByte();
            PaleoPacket paleoPacket = PaleoPacket.constructPacket(packetId);
            paleoPacket.read(in);
            paleoPacket.execute(entityPlayer,
                    entityPlayer.worldObj.isRemote ? Side.CLIENT : Side.SERVER);
        } catch (ProtocolException e) {
            if (player instanceof EntityPlayerMP) {
                ((EntityPlayerMP) player).playerNetServerHandler
                        .kickPlayerFromServer("Protocol Exception!");
                Logger.getLogger("PaleoMachineFramework").warning(
                        "Player " + ((EntityPlayer) player).username
                                + " caused a Protocol Exception!");
            }
        } catch (ReflectiveOperationException e) {
            throw new RuntimeException(
                    "Unexpected Reflection exception during Packet construction!",
                    e);
        }
    }

}
