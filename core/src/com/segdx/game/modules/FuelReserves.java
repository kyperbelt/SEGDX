package com.segdx.game.modules;

import com.segdx.game.entity.Player;

public class FuelReserves extends ShipModule{

	//GIVE MORE FUEL CAPACITY
	
	@Override
	public boolean installModule(Player player) {
		
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		
		return null;
	}

}
