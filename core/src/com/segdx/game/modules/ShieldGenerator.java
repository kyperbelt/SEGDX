package com.segdx.game.modules;

import com.segdx.game.abilities.PowerShields;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.entity.Player;

public class ShieldGenerator extends ShipModule {
	
	private ShipAbility shields;
	
	public ShieldGenerator(int level) {
		this.setBaseValue(550);
		this.setCost(4);
		this.setLevel(getRandomLevel());
		if(level>0)
			this.setLevel(level);
		shields = new PowerShields(this.getLevel());
		shields.setParentModule(this);
		this.setName("Shield Gen.");
		this.setDesc("A shield generator that allows you to power up shields and take further punishment. Can only have 1 shield gen");
	}

	@Override
	public boolean installModule(Player player) {
		if(canInstall(player)){
			this.setId(this.createID(player));
			player.getAbilities().add(shields);
			return true;
		}else{
			wasUnableToInstallDialog();
		}
		return false;
	}
	
	@Override
	public boolean canInstall(Player player) {
		
		return super.canInstall(player)&&!player.containsModuleClass(this);
	}

	@Override
	public ShipModule removeModule(Player player) {
		removeModuleAbilities(player);
		return null;
	}

}
