package de.paleocrafter.pmfw.network.data;

import net.minecraft.nbt.NBTTagList;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

/**
 * 
 * PaleoMachineFramework
 * 
 * IDataObject
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public interface IDataObject {
    public void writeToNBT(NBTTagList nbtTag);

    public void readFromNBT(NBTTagList nbtTag);

    public void writeToPacket(ByteArrayDataOutput out);

    public void readFromPacket(ByteArrayDataInput in);
}
