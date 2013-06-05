package de.paleocrafter.pmfw.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.relauncher.Side;
import de.paleocrafter.pmfw.tileentity.TileEntityWithData;

/**
 * 
 * PaleoMachineFramework
 * 
 * TileDataPacket
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TileDataPacket extends PaleoPacket {

    private byte[] data;
    private int x, y, z;

    public TileDataPacket() {
        x = 0;
        y = 0;
        z = 0;
        data = null;
    }

    public TileDataPacket(int x, int y, int z, byte[] data) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.data = data;
    }

    @Override
    public void write(ByteArrayDataOutput out) {
        out.writeInt(x);
        out.writeInt(y);
        out.writeInt(z);
        out.writeInt(data.length);
        out.write(data);
    }

    @Override
    public void read(ByteArrayDataInput in) {
        this.x = in.readInt();
        this.y = in.readInt();
        this.z = in.readInt();
        int dataLength = in.readInt();
        data = new byte[dataLength];
        try {
            in.readFully(data);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("");
        }
    }

    @Override
    public void execute(EntityPlayer player, Side side) {
        if (side.isClient()) {
            TileEntity tile = player.worldObj.getBlockTileEntity(x, y, z);
            if (tile instanceof TileEntityWithData) {
                ((TileEntityWithData) tile).readFromPacket(ByteStreams
                        .newDataInput(data));
            }
        }
    }

}
