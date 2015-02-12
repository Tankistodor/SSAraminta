package ic2_fix;

import ic2.core.init.InternalName;
import ic2.core.item.tool.ItemDrillIridium;
import net.minecraft.item.ItemStack;

public class ItemDrillIridiumFix extends ItemDrillIridium {

	public ItemDrillIridiumFix(InternalName internalName) {
		super(internalName);
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return 8;
	}

}
