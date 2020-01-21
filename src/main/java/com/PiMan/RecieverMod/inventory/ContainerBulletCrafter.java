package com.PiMan.RecieverMod.inventory;

import com.PiMan.RecieverMod.Items.bullets.ItemBulletMediumCasing;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletShotgunCasing;
import com.PiMan.RecieverMod.Items.bullets.ItemBulletSmallCasing;
import com.PiMan.RecieverMod.tileEntity.TileEntityBulletCrafter;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBulletCrafter extends Container {

	private TileEntityBulletCrafter tileentity;
	
	private int cookTime, totalCookTime;
	
	public ContainerBulletCrafter(InventoryPlayer inventoryplayer, TileEntityBulletCrafter tileentity) {
		this.tileentity = tileentity;
		int i = 0;
		this.addSlotToContainer(new Slot(tileentity, i++, 21, 35) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() instanceof ItemBulletSmallCasing ||
					   stack.getItem() instanceof ItemBulletMediumCasing ||
					   stack.getItem() instanceof ItemBulletShotgunCasing;
			}
		});
		this.addSlotToContainer(new Slot(tileentity, i++, 39, 35) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() == Items.REDSTONE;
			}
		});
		this.addSlotToContainer(new Slot(tileentity, i++, 57, 35) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() == Items.GUNPOWDER;
			}
		});
		this.addSlotToContainer(new Slot(tileentity, i++, 75, 35) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return stack.getItem() == Items.IRON_INGOT;
			}
		});
		this.addSlotToContainer(new Slot(tileentity, i++, 131, 35) {
			@Override
			public boolean isItemValid(ItemStack stack) {
				return false;
			}
			@Override
			public ItemStack onTake(EntityPlayer thePlayer, ItemStack stack) {
				this.onCrafting(stack);
				return super.onTake(thePlayer, stack);
			}
		});
		
        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 9; ++x) {
                this.addSlotToContainer(new Slot(inventoryplayer, x + y * 9 + 9, 8 + x * 18, 84 + y * 18));
            }
        }

        for (int x = 0; x < 9; ++x) {
            this.addSlotToContainer(new Slot(inventoryplayer, x, 8 + x * 18, 142));
        }
	}
	
	public void addListener(IContainerListener listener)
    {
        super.addListener(listener);
        listener.sendAllWindowProperties(this, this.tileentity);
    }

    /**
     * Looks for changes made in the container, sends them to every listener.
     */
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < this.listeners.size(); ++i)
        {
            IContainerListener icontainerlistener = this.listeners.get(i);

            if (this.cookTime != this.tileentity.getField(2))
            {
                icontainerlistener.sendWindowProperty(this, 2, this.tileentity.getField(2));
            }

            if (this.totalCookTime != this.tileentity.getField(3))
            {
                icontainerlistener.sendWindowProperty(this, 3, this.tileentity.getField(3));
            }
        }

        this.cookTime = this.tileentity.getField(2);
        this.totalCookTime = this.tileentity.getField(3);
    }

    @SideOnly(Side.CLIENT)
    public void updateProgressBar(int id, int data)
    {
        this.tileentity.setField(id, data);
    }

    /**
     * Determines whether supplied player can use this container
     */
    public boolean canInteractWith(EntityPlayer playerIn)
    {
        return this.tileentity.isUsableByPlayer(playerIn);
    }

    /**
     * Handle when the stack in slot {@code index} is shift-clicked. Normally this moves the stack between the player
     * inventory and the other inventory(s).
     */
    public ItemStack transferStackInSlot(EntityPlayer playerIn, int index)
    {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (index >= 0 && index <= 4)
            {
                if (!this.mergeItemStack(itemstack1, 5, 41, true))
                {
                    return ItemStack.EMPTY;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else
            {
            	boolean flag = false;
            	int i;
            	for (i = 0; i < 5; i++) {
            		Slot otherslot = this.inventorySlots.get(i);
            		if (otherslot.isItemValid(itemstack1)) {
            			flag = true;
            			break;
            		}
            	}
            	if (flag) {
            		if (!this.mergeItemStack(itemstack1, i, i+1, false)) {
            			return ItemStack.EMPTY;
            		}
            	}
                else if (index >= 3 && index < 30)
                {
                    if (!this.mergeItemStack(itemstack1, 32, 41, false))
                    {
                        return ItemStack.EMPTY;
                    }
                }
                else if (index >= 30 && index < 39 && !this.mergeItemStack(itemstack1, 5, 32, false))
                {
                    return ItemStack.EMPTY;
                }
            }
            if (itemstack1.isEmpty())
            {
                slot.putStack(ItemStack.EMPTY);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.getCount() == itemstack.getCount())
            {
                return ItemStack.EMPTY;
            }

            slot.onTake(playerIn, itemstack1);
        }

        return itemstack;
    }

}
