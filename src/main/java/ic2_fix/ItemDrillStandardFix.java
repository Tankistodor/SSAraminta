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
