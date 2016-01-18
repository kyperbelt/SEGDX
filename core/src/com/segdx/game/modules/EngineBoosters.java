package com.segdx.game.modules;

import com.segdx.game.entity.Player;

public class EngineBoosters extends ShipModule{
	
	private float base_boost;
	private float base_economy_loss;
	private float original_value;
	
	public EngineBoosters(int level) {
		this.setLevel(level);
		if(level<=0)
			this.setLevel(this.getRandomLevel());
		this.setName("Engine Boost"+this.getLevel());
		this.setCost(2);
		this.setBaseValue(200);
		base_boost = 1.5f;
		base_economy_loss = .05f;
		this.setDesc("Trade fuel economy for ship speed.");
	}
	
	public void setEconLoss(float i){
		base_economy_loss = i;
	}
	
	@Override
	public String getDesc() {
		
		return super.getDesc()+" \nIncreases speed by "+base_boost*getLevel()+" at the cost of "+(base_economy_loss*getLevel())*100+"% "
				+ "fuel economy.";
		
	}

	@Override
	public boolean installModule(Player player) {
		if(canInstall(player)){
			this.setId(createID(player));
			this.original_value = player.getShip().getFuelEconomy();
			player.getShip().setSpeed(player.getShip().getSpeed()+base_boost*getLevel());
			player.getShip().setFuelEconomy(player.getShip().getFuelEconomy()-(player.getShip().getFuelEconomy()*(base_economy_loss*getLevel())));
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
		player.getShip().setFuelEconomy(player.getShip().getFuelEconomy()+(original_value*(base_economy_loss*getLevel())));
		
		return this;
	}

}
