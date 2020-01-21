package com.PiMan.RecieverMod.init;

import java.util.ArrayList;
import java.util.List;

import com.PiMan.RecieverMod.Items.IItemInit;
import com.PiMan.RecieverMod.Items.ItemBase;
import com.PiMan.RecieverMod.Items.ItemBinoculars;
import com.PiMan.RecieverMod.Items.ItemFrag;
import com.PiMan.RecieverMod.Items.ItemLens;
import com.PiMan.RecieverMod.Items.ItemSmallGunpowder;
import com.PiMan.RecieverMod.Items.accesories.ItemAccessories;
import com.PiMan.RecieverMod.Items.bullets.ItemBullet;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletMedium;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletMediumBullet;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletMediumCasing;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletShotgun;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletShotgunCasing;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletShotgunPellets;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletSmall;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletSmallBullet;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletSmallCasing;
import com.PiMan.RecieverMod.Items.guns.Item44Magnum;
import com.PiMan.RecieverMod.Items.guns.ItemColt;
import com.PiMan.RecieverMod.Items.guns.ItemGlock;
import com.PiMan.RecieverMod.Items.guns.ItemModel10;
import com.PiMan.RecieverMod.Items.guns.ItemRPG7;
import com.PiMan.RecieverMod.Items.guns.ItemRifle;
import com.PiMan.RecieverMod.Items.guns.ItemShotgun;
import com.PiMan.RecieverMod.Items.guns.ItemThompson;
import com.PiMan.RecieverMod.Items.mags.ItemClip;
import com.PiMan.RecieverMod.Items.mags.ItemClipColt;
import com.PiMan.RecieverMod.Items.mags.ItemClipGlock;
import com.PiMan.RecieverMod.Items.mags.ItemClipThompson;
import com.PiMan.RecieverMod.Items.ItemCassette;

import net.minecraft.item.Item;

public class ModItems {

	public static final List<Item> ITEMS = new ArrayList<Item>();
	
	public static final Item COLT = new ItemColt("44magnumfull.bbmodel");
	public static final Item COLTCLIP = new ItemClipColt("_clip_colt");
	public static final Item GLOCK = new ItemGlock("_gun_glock");
	public static final Item GLOCKCLIP = new ItemClipGlock("_clip_glock");
	public static final Item MODEL_10 = new ItemModel10("_gun_model_10");
	public static final Item MAGNUM44 = new Item44Magnum("_gun_44magnum");
	public static final Item RIFLE = new ItemRifle("_gun_boltrifle");
	public static final Item SHOTGUN = new ItemShotgun("_gun_remington870");
	public static final Item THOMPSON = new ItemThompson("_gun_thompson");
	public static final Item THOMPSONCLIP = new ItemClipThompson("thompson_mag");
	public static final Item RPG7 = new ItemRPG7("_gun_rpg");
	public static final Item RPG = new ItemBase("rpg").setMaxStackSize(1); 
	public static final Item BULLET45 = new ItemBulletMedium("bullet45", 0.45f, 20f, "bullet45");
	public static final Item BULLET45CASING = new ItemBulletMediumCasing("bullet45casing");
	//public static final Item BULLET45BULLET = new ItemBulletMediumBullet("bullet45bullet");
	public static final Item BULLET9MM = new ItemBulletMedium("bullet9mm", 0.45f, 20f, "bullet45");
	public static final Item BULLET9MMCASING = new ItemBulletMediumCasing("bullet9mmcasing");
	//public static final Item BULLET9MMBULLET = new ItemBulletMediumBullet("bullet9mmbullet");
	public static final Item BULLET38SPECIAL = new ItemBulletMedium("bullet38special", 0.38f, 29f, "bullet45");
	public static final Item BULLET38SPECIALCASING = new ItemBulletMediumCasing("bullet38specialcasing");
	//public static final Item BULLET38SPECIALBULLET = new ItemBulletMediumBullet("bullet38specialbullet");
	public static final Item BULLET22 = new ItemBulletSmall("bullet22");
	public static final Item BULLET22CASING = new ItemBulletSmallCasing("bullet22casing");
	//public static final Item BULLET22BULLET = new ItemBulletSmallBullet("bullet22bullet");
	public static final Item BULLETSHOTGUN = new ItemBulletShotgun("shotgun_shell", 12f, 20, 40);
	public static final Item BULLETSHOTGUNCASING = new ItemBulletShotgunCasing("shotgun_casing");
	//public static final Item BULLETSHOTGUNPELLETS = new ItemBulletShotgunPellets("shotgun_pellets");
	public static final Item LENSE = new ItemLens("lens");
	public static final Item BINOCULARS = new ItemBinoculars("binoculars");
	//public static final Item SMALLGUNPOWDER = new ItemSmallGunpowder("small_gunpowder");
	public static final Item FRAGGRENADE = new ItemFrag("frag_grenade");
	public static final Item CASSETTE = new ItemCassette("cassette");
	public static final Item SCOPE = new ItemAccessories("rifle_scope", 1);
}