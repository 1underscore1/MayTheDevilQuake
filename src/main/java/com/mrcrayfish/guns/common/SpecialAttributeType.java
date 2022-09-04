package com.mrcrayfish.guns.common;

import java.util.EnumMap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;

public enum SpecialAttributeType {
	/**
     * Overrides the amount of ticks before the gun starts being reloaded (which is by default, 5)
     * <p>
     * Cast to integer.
     */
	FASTER_RELOAD(SpecialDataType.INT_DATA),
	/**
     * Reduces the amount of ticks each reload takes (which is by default, 10, but enchantments can decrease it)
     * <p>
     * Cast to float.
     */
	FASTER_RELOAD_INTERVAL(SpecialDataType.FLOAT_DATA),
	/**
     * Multiplies ADS speed by this number.
     * <p>
     * Cast to float.
     */
	FASTER_ADS(SpecialDataType.FLOAT_DATA),
	/**
     * Multiplies the projectile's speed by this number every tick.
     * <p>
     * Cast to float.
     */
	SLOWING_PROJECTILE(SpecialDataType.FLOAT_DATA),
	/**
     * Reverses gravity for projectiles.
     * <p>
     * Has no attached data.
     */
	INVERSE_GRAVITY(SpecialDataType.NO_DATA),
	/**
     * Overrides how much splash damage this projectile should deal, if it is an exploding projectile.
     * <p>
     * Cast to float.
     */
	OVERRIDE_SPLASH_DAMAGE(SpecialDataType.FLOAT_DATA),
	/**
     * Indicates this weapon is a BFG. BFG-class weapons inflict the "BFG Sickness" status effect once you stop firing them, and
     * cannot be fired while you have BFG Sickness. The attached data is the duration of the status effect, in ticks.
     * <p>
     * Cast to integer.
     */
	BFG(SpecialDataType.INT_DATA);
	private SpecialDataType specialDataType;
	private enum SpecialDataType {
		NO_DATA{
			protected void writeToTag(CompoundTag tag, SpecialAttributeType key, Object value)
			{
				tag.putInt(key.toString(), 0);
			}
			protected Object readFromTag(CompoundTag tag, SpecialAttributeType key)
			{
				return 0;
			}
			protected void addToJsonObject(JsonObject object, SpecialAttributeType key, Object value)
			{
				object.addProperty(key.toString(), 0);
			}
			protected Object readFromJsonElement(JsonElement element)
			{
				return 0;
			}
		},
		INT_DATA{
			protected void writeToTag(CompoundTag tag, SpecialAttributeType key, Object value)
			{
				tag.putInt(key.toString(), (int) value);
			}
			protected Object readFromTag(CompoundTag tag, SpecialAttributeType key)
			{
				return tag.getInt(key.toString());
			}
			protected void addToJsonObject(JsonObject object, SpecialAttributeType key, Object value)
			{
				object.addProperty(key.toString(), (int) value);
			}
			protected Object readFromJsonElement(JsonElement element)
			{
				return element.getAsInt();
			}
		},
		FLOAT_DATA{
			protected void writeToTag(CompoundTag tag, SpecialAttributeType key, Object value)
			{
				tag.putFloat(key.toString(), (float) value);
			}
			protected Object readFromTag(CompoundTag tag, SpecialAttributeType key)
			{
				return tag.getFloat(key.toString());
			}
			protected void addToJsonObject(JsonObject object, SpecialAttributeType key, Object value)
			{
				object.addProperty(key.toString(), (float) value);
			}
			protected Object readFromJsonElement(JsonElement element)
			{
				return element.getAsFloat();
			}
		};
		protected abstract void writeToTag(CompoundTag tag, SpecialAttributeType key, Object value);
		protected abstract Object readFromTag(CompoundTag tag, SpecialAttributeType key);
		protected abstract void addToJsonObject(JsonObject object, SpecialAttributeType key, Object value);
		protected abstract Object readFromJsonElement(JsonElement element);
	}
	SpecialAttributeType(SpecialDataType sdt)
	{
		this.specialDataType = sdt;
	}
	
	public void writeToTag(CompoundTag tag, Object value)
	{
		this.specialDataType.writeToTag(tag, this, value);
	}
	public Object readFromTag(CompoundTag tag)
	{
		return this.specialDataType.readFromTag(tag, this);
	}
	public void addToJsonObject(JsonObject object, Object value)
	{
		this.specialDataType.addToJsonObject(object, this, value);
	}
	
	public static EnumMap<SpecialAttributeType, Object> ListFromJson(JsonObject object)
	{
		EnumMap<SpecialAttributeType, Object> result = new EnumMap<>(SpecialAttributeType.class);
		object.keySet().forEach(key -> {
			SpecialAttributeType specialAttribute = SpecialAttributeType.valueOf(key);
			result.put(specialAttribute, specialAttribute.specialDataType.readFromJsonElement(object.get(key)));
		});
		return result;
	}
}
