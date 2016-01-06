package com.segdx.game.modules;

import com.badlogic.gdx.math.MathUtils;
import com.segdx.game.entity.Player;

public class EngineBoosters extends ShipModule{
	
	public static final int VALUE = 200;
	public static final int UPGRADE_COST = 1;
	public static final int base_boost = 1;
	public static final float base_economy_loss = .5f;
	
	private int level;
	
	public EngineBoosters() {
		
		this.level = MathUtils.random(ShipModule.MIN_LVL, ShipModule.MAX_LVL);
		this.setName("Engine Boost"+level);
		this.setCost(UPGRADE_COST*level);
		this.setBaseValue(VALUE*level);
		this.setDesc("Trade fuel economy for ship speed.");
	}
	
	@Override
	public String getDesc() {
		
		return super.getDesc()+" \nIncreases speed by "+base_boost*level+" at the cost of "+base_economy_loss*level+""
				+ "fuel economy.";
		
	}

	@Override
	public boolean installModule(Player player) {
		if(player.getUpgradePointsAvailable()>=this.getCost()){
			int id = player.getModules().size;
			while(player.containsModuleId(id)){
				id++;
			}
			this.setId(id);
			player.getShip().setSpeed(player.getShip().getSpeed()+base_boost*level);
			player.getShip().setFuelEconomy(player.getShip().getFuelEconomy()-base_economy_loss*level);
			return true;
		}
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		
		player.getShip().setSpeed(player.getShip().getSpeed()-(base_boost*level));
		player.getShip().setFuelEconomy(player.getShip().getFuelEconomy()+(base_economy_loss*level));
		
		return this;
	}

}
