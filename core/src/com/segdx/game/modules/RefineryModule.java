package com.segdx.game.modules;

import com.segdx.game.abilities.RefineDridium;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.entity.Player;

public class RefineryModule extends ShipModule{
	
	private ShipAbility refinedridium;
	
	public RefineryModule() {
		
		this.setLevel(getRandomLevel());
		this.setCost(2);
		this.setBaseValue(350);
		this.setName("Dridium Refinery");
		
		this.setDesc("Gives you the ability to refine Dridium into usable fuel. Higher tiers allow you to refine more [FOREST]Dridium[]");
		refinedridium = new RefineDridium(this.getLevel());
		refinedridium.setParentModule(this);
	}

	@Override
	public boolean installModule(Player player) {
		if(this.canInstall(player)){
			this.setId(this.createID(player));
			player.getAbilities().add(refinedridium);
			return true;
		}else{
			wasUnableToInstallDialog();
		}
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		removeModuleAbilities(player);
		return this;
	}

}
