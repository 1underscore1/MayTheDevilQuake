package com.mrcrayfish.guns.client;

public class MuzzleFlashType {
	public static final int TOTAL_FLASHES = 3;

	public static final MuzzleFlashType NORMAL = new MuzzleFlashType(0);
	public static final MuzzleFlashType LASER = new MuzzleFlashType(1);
	public static final MuzzleFlashType POOF = new MuzzleFlashType(2);
	
	private final int flashID;
	
	MuzzleFlashType(int flashID)
	{
		this.flashID = flashID;
	}
	
	public int getFlashInt()
	{
		return this.flashID;
	}
}
