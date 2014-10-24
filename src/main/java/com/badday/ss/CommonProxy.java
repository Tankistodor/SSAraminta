package com.badday.ss;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.minecraft.entity.player.EntityPlayer;

public class CommonProxy {

	public boolean hasClient()
	  {
	    return false;
	  }

	  public EntityPlayer getPlayer() {
	    return null;
	  }

	  public void registerItem(int itemID)
	  {
	  }

	  public Object loadResource(String texture)
	  {
	    return null;
	  }

	public void load() {
		// TODO Auto-generated method stub
		
	}

	public void init(FMLInitializationEvent event) {
		
		
	}
	
}
