package ic2_fix;

import ic2.core.init.InternalName;
import ic2.core.item.tool.ItemDrillStandard;
import net.minecraft.item.ItemStack;

public class ItemDrillStandardFix extends ItemDrillStandard {

	public ItemDrillStandardFix(InternalName internalName) {
		super(internalName);
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return 4;	   
	}
}
