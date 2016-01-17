package com.segdx.game.modules;

import com.segdx.game.entity.Player;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class FuelReserves extends ShipModule{

	private float fuel_cap;
	
	//GIVE MORE FUEL CAPACITY
	public FuelReserves(int level) {
		fuel_cap = 25;
		this.setBaseValue(500);
		this.setCost(6);
		this.setLevel(this.getRandomLevel());
		if(level>0)
			this.setLevel(level);
		this.setName("Fuel Tanks ");
		this.setDesc("Allows you to hold "+fuel_cap*this.getLevel()+" more fuel than you normally would be able to. Great"
				+ "for long distance travel.");
	}
	
	@Override
	public boolean installModule(Player player) {
		if(canInstall(player)){
			this.setId(this.createID(player));
			player.getShip().setMaxfuel(player.getShip().getMaxfuel()+(fuel_cap*this.getLevel()));
			return true;
		}else{
			wasUnableToInstallDialog();
		}
		
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		this.removeModuleAbilities(player);
		player.getShip().setMaxfuel(player.getShip().getMaxfuel()-(fuel_cap*this.getLevel()));
		player.setCurrentFuel(player.getCurrentFuel());
		((GameState)StateManager.get().getState(StateManager.GAME)).updateRestBar();
		return this;
	}

}
