package cn.weaponry.api.inf;

/**
 * Parts that can be attached onto InfWeapon. They are all registered at
 * mod init via @RegInfWeaponPart and is constructed automatically. You must
 * keep the <code>InfWeaponPart(InfWeapon)</code> Ctor in your implementation.
 * @author WeathFolD
 */
public abstract class InfWeaponPart {

	protected final InfWeapon info;
	
	/**
	 * The ctor will be called every time player switches item and replace the previous part data.
	 */
	public InfWeaponPart(InfWeapon _inf) {
		info = _inf;
	}
	
	/**
	 * Called when player init or a new item is switched. (That is, right after part construction)
	 */
	public abstract void onAdded();
	
	/**
	 * Called each tick the part is alive.
	 */
	public abstract void onTick();
	
	/**
	 * Called when player changes item or dead or something. you don't need to clear the data
	 * because this instance will be disposed right away.
	 */
	public abstract void onRemoved();

}
