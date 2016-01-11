package com.segdx.game.modules;

import com.segdx.game.entity.Player;
/**
 * Mining modules 
 * allows the player to mine for different type of resources 
 * depending on the level of the module.
 * Mining takes a few seconds and renders the player unable to move
 * @author Jonathan Camarena -kyperbelt
 *
 */
public class MiningModule extends ShipModule{

	@Override
	public boolean installModule(Player player) {
		
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		
		return null;
	}
	


}
