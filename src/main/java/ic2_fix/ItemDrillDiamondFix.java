package ic2_fix;

import cpw.mods.fml.common.registry.GameRegistry;
import ic2.core.init.InternalName;
import ic2.core.item.tool.ItemDrillDiamond;
import net.minecraft.item.ItemStack;

public class ItemDrillDiamondFix extends ItemDrillDiamond {

	public ItemDrillDiamondFix(InternalName internalName) {
		super(internalName);
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass)
	{
		return 6;
	}

}
