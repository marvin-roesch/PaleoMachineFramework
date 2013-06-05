package de.paleocrafter.pmfw.tileentity;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.google.common.primitives.Primitives;

import cpw.mods.fml.common.network.PacketDispatcher;
import de.paleocrafter.pmfw.network.data.IDataObject;
import de.paleocrafter.pmfw.network.data.TileData;
import de.paleocrafter.pmfw.network.packet.TileDataPacket;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.packet.Packet;
import net.minecraft.tileentity.TileEntity;

/**
 * 
 * PaleoMachineFramework
 * 
 * TileEntityWithData
 * 
 * @author PaleoCrafter
 * @license Lesser GNU Public License v3 (http://www.gnu.org/licenses/lgpl.html)
 * 
 */
public class TileEntityWithData extends TileEntity {

    @Override
    public void writeToNBT(NBTTagCompound tag) {
        NBTTagCompound subFields = new NBTTagCompound("TileData");

        Class<?> clazz = this.getClass();
        int iteration = 0;
        do {
            NBTTagCompound clazzTag = new NBTTagCompound(
                    String.valueOf(iteration));
            writeNBTFromClass(clazzTag, clazz);
            tag.setCompoundTag(String.valueOf(iteration), clazzTag);
            iteration++;
        } while ((clazz = clazz.getSuperclass()) != TileEntityWithData.class);
        tag.setInteger("TileDataCount", iteration - 1);

        tag.setCompoundTag("TileData", subFields);
    }

    @Override
    public void readFromNBT(NBTTagCompound tag) {
        NBTTagCompound subFields = tag.getCompoundTag("TileData");
        int dataCount = tag.getInteger("TileDataCount");
        for (int i = 0; i <= dataCount; i++) {
            Class<?> clazz = this.getClass();
            for (int j = 0; j < i; j++) {
                if (clazz != null)
                    clazz = clazz.getSuperclass();
            }

            readNBTToClass(subFields.getCompoundTag(String.valueOf(i)), clazz);
        }
    }

    private void writeNBTFromClass(NBTTagCompound tag, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Annotation anno = field.getAnnotation(TileData.class);
            if (anno != null) {
                if (((TileData) anno).storeToNBT()) {
                    try {
                        String className = null;
                        Class<?> primType = Primitives.wrap(field.getType());
                        Object value = field.get(this);
                        if (primType != null || value instanceof String
                                || value instanceof IDataObject) {
                            Class<?> type = field.getType();
                            if (primType != null) {
                                type = primType;
                            }
                            className = type.getSimpleName();
                            className = Character.toUpperCase(className
                                    .charAt(0)) + className.substring(1);
                            if (className.equals("Character")) {
                                className = "String";
                                value = String.valueOf(value);
                            }
                            if (className != null) {
                                if (value instanceof IDataObject) {
                                    NBTTagList list = new NBTTagList(
                                            field.getName());
                                    ((IDataObject) value).writeToNBT(list);
                                    tag.setTag(field.getName(), list);
                                } else {
                                    Method method = tag.getClass().getMethod(
                                            "set" + className, String.class, field.getType());
                                    method.invoke(tag, field.getName(), value);
                                }
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void readNBTToClass(NBTTagCompound tag, Class<?> clazz) {
        NBTBase[] subTags = (NBTBase[]) tag.getTags().toArray(
                new NBTBase[tag.getTags().size()]);
        for (NBTBase field : subTags) {
            try {
                Field classField = clazz.getDeclaredField(field.getName());
                classField.setAccessible(true);

                Annotation anno = classField.getAnnotation(TileData.class);

                if (anno != null && ((TileData) anno).storeToNBT()) {

                    String className = field.getClass().getSimpleName()
                            .replace("NBTTag", "");
                    Method method = tag.getClass().getMethod("get" + className);
                    if (classField.getType().isAssignableFrom(Character.class)) {
                        classField
                                .set(this,
                                        (Character) method.invoke(tag,
                                                field.getName()));
                    } else if (field instanceof NBTTagList) {
                        try {
                            IDataObject obj = (IDataObject) classField
                                    .getType().newInstance();
                            obj.readFromNBT((NBTTagList) field);
                            classField.set(this, obj);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    } else
                        classField.set(this,
                                method.invoke(tag, field.getName()));
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    public byte[] getPacketData() {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        Class<?> clazz = this.getClass();
        int superClassCount = 0;
        while ((clazz = clazz.getSuperclass()) != TileEntityWithData.class) {
            superClassCount++;
        }

        out.writeInt(superClassCount);

        clazz = this.getClass();
        int iteration = 0;
        do {
            writePacketFromClass(out, clazz, iteration);
            iteration++;
        } while ((clazz = clazz.getSuperclass()) != TileEntityWithData.class);

        return out.toByteArray();
    }

    public void readFromPacket(ByteArrayDataInput in) {
        int superClassCount = in.readInt();
        for (int i = 0; i <= superClassCount; i++) {
            Class<?> clazz = this.getClass();
            for (int j = 0; j < i; j++) {
                if (clazz != null)
                    clazz = clazz.getSuperclass();
            }
            readPacketToClass(in, clazz, i);
        }
    }

    public void syncWithClient() {
        if (!worldObj.isRemote) {
            PacketDispatcher.sendPacketToAllAround(xCoord, yCoord, zCoord, 32,
                    worldObj.provider.dimensionId, getDescriptionPacket());
        }
    }

    private void writePacketFromClass(ByteArrayDataOutput out, Class<?> clazz,
            int iteration) {
        int fieldCount = 0;
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Annotation anno = field.getAnnotation(TileData.class);
            if (anno != null)
                fieldCount++;
        }
        out.writeInt(fieldCount);
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Annotation anno = field.getAnnotation(TileData.class);
            if (anno != null) {
                if (((TileData) anno).storeToPacket()) {
                    try {
                        String className = null;
                        className = field.getType().getSimpleName();
                        className = Character.toUpperCase(className.charAt(0))
                                + className.substring(1);
                        if (field.getType().isAssignableFrom(String.class))
                            className = "UTF";

                        if (className != null) {
                            Object value = field.get(this);
                            Class<?> type = field.getType();
                            if (Primitives.wrap(type) != null
                                    || value instanceof String
                                    || value instanceof IDataObject) {
                                if (value instanceof Byte
                                        || value.getClass() == byte.class) {
                                    type = int.class;
                                    value = (byte) value;
                                }
                                if (value instanceof IDataObject) {
                                    out.writeUTF(field.getName());
                                    ((IDataObject) value).writeToPacket(out);
                                } else {
                                    Method method = out.getClass().getMethod(
                                            "write" + className, type);
                                    out.writeUTF(field.getName());
                                    method.setAccessible(true);
                                    method.invoke(out, value);
                                }
                            }
                        }

                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (SecurityException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void readPacketToClass(ByteArrayDataInput in, Class<?> clazz,
            int iteration) {
        try {
            int fieldCount = in.readInt();
            for (int i = 0; i < fieldCount; i++) {
                try {
                    String fieldName = in.readUTF();
                    Field field = clazz.getDeclaredField(fieldName);
                    field.setAccessible(true);
                    String className = field.getType().getSimpleName();
                    className = Character.toUpperCase(className.charAt(0))
                            + className.substring(1);
                    if (field.getType().isAssignableFrom(String.class))
                        className = "UTF";
                    if (className != null) {
                        if (IDataObject.class.isAssignableFrom(field.getType())) {
                            IDataObject data = (IDataObject) field.getType()
                                    .newInstance();

                            data.readFromPacket(in);
                            field.set(this, data);
                        } else {
                            Method method = in.getClass().getMethod(
                                    "read" + className);
                            method.setAccessible(true);
                            field.set(this, method.invoke(in));
                        }
                    }
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public Packet getDescriptionPacket() {
        TileDataPacket packet = new TileDataPacket(xCoord, yCoord, zCoord,
                getPacketData());
        return packet.makePacket();
    }
}
