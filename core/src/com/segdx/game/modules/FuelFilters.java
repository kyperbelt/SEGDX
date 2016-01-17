package com.segdx.game.modules;

import com.segdx.game.entity.Player;

public class FuelFilters extends ShipModule{

	@Override
	public boolean installModule(Player player) {
		
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		
		return null;
	}

}
