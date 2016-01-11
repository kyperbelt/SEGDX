package com.segdx.game.modules;

import com.segdx.game.abilities.RefineDridium;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.entity.Player;

public class RefineryModule extends ShipModule{
	
	private ShipAbility refinedridium;
	
	public RefineryModule() {
		refinedridium = new RefineDridium();
		refinedridium.setParentModule(this);
		this.setCost(2);
		this.setBaseValue(500);
		this.setName("Dridium Refinery");
		this.setDesc("Gives you the ability to refine Dridium into usable fuel.");
	}

	@Override
	public boolean installModule(Player player) {
		if(player.getUpgradePointsAvailable()>=this.getCost()){
			int id = player.getModules().size;
			while(player.containsModuleId(id)){
				id++;
			}
			this.setId(id);
			player.getAbilities().add(refinedridium);
			return true;
		}
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		for (int i = 0; i < player.getAbilities().size; i++) {
			if(player.getAbilities().get(i).getParentModule().getId()==this.getId()){
				player.getAbilities().removeIndex(i);
			}
		}
		return this;
	}

}
