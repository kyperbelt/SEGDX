package com.segdx.game.modules;

import com.segdx.game.entity.Player;

public class EngineBoosters extends ShipModule{
	
	private float base_boost;
	private float base_economy_loss;
	
	public EngineBoosters() {
		
		this.setLevel(this.getRandomLevel());
		this.setName("Engine Boost"+this.getLevel());
		this.setCost(2);
		this.setBaseValue(200);
		base_boost = 1.5f;
		base_economy_loss = 1f;
		this.setDesc("Trade fuel economy for ship speed.");
	}
	
	@Override
	public String getDesc() {
		
		return super.getDesc()+" \nIncreases speed by "+base_boost*getLevel()+" at the cost of "+base_economy_loss*getLevel()+""
				+ "fuel economy.";
		
	}

	@Override
	public boolean installModule(Player player) {
		if(canInstall(player)){
			this.setId(createID(player));
			player.getShip().setSpeed(player.getShip().getSpeed()+base_boost*getLevel());
			player.getShip().setFuelEconomy(player.getShip().getFuelEconomy()-base_economy_loss*getLevel());
			return true;
		}else{
			this.wasUnableToInstallDialog();
		}
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		
		removeModuleAbilities(player);
		player.getShip().setSpeed(player.getShip().getSpeed()-(base_boost*getLevel()));
		player.getShip().setFuelEconomy(player.getShip().getFuelEconomy()+(base_economy_loss*getLevel()));
		
		return this;
	}

}
