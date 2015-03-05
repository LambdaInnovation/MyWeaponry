package cn.weaponry.api.item;

/**
 * implements this instance in your Item class to indicate that it requres InfWeapon data.
 * Used to prevent useless data creating and destroying. If you don't mark this on your Item
 * and tried to get a InfWeapon, you will get an error.
 * @author WeathFolD
 *
 */
public interface IRequiresInfo {

}
