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
	    if ((toolClass.equals("pickaxe")) || (toolClass.equals("shovel"))) {
	      return this.toolMaterial.getHarvestLevel();
	    }
	    return super.getHarvestLevel(stack, toolClass);
	}
	
	public void setHarvertLevel(String str, int level) {
		this.setHarvestLevel(str, level);
	}
	
	public void setHarvertLevel(ItemStack toolClass, int level) {
		this.setHarvestLevel("pickaxe", level);
	}
}
