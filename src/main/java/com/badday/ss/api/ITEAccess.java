package com.badday.ss.api;

import com.badday.ss.entity.player.SSPlayerRoles;

import net.minecraft.entity.Entity;

public interface ITEAccess {

	public boolean isPlayerHaveAccess(Entity player);
	public void addRole(SSPlayerRoles role);
	public void removeRole(SSPlayerRoles role);	
	
}
