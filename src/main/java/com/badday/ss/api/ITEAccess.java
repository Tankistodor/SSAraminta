package com.badday.ss.api;

import net.minecraft.entity.Entity;

import com.badday.ss.entity.player.SSPlayerRoles;

public interface ITEAccess {

	public boolean isPlayerHaveAccess(Entity player);
	public void addRole(SSPlayerRoles role);
	public void removeRole(SSPlayerRoles role);
	/**
	 * Add or remove role.
	 * @param role
	 * @return true - if role added, false - if role removed;
	 */
	public boolean switchRole(SSPlayerRoles role);
	
}
