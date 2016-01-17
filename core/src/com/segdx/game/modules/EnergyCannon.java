package com.segdx.game.modules;

import com.segdx.game.abilities.FireEnergyCannon;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.entity.Player;

public class EnergyCannon extends ShipModule{
	
	ShipAbility ability;
	
	public EnergyCannon(int level) {
		this.setBaseValue(550);
		this.setCost(4);
		this.setLevel(getRandomLevel());
		if(level>0)
			this.setLevel(level);
		ability = new FireEnergyCannon(this.getLevel());
		ability.setParentModule(this);
		this.setName("Energy Cannon");
		this.setDesc("An energy cannon that allows you to inflict energy damage. Great against shielded foes.");
	
	}

	@Override
	public boolean installModule(Player player) {
		if(canInstall(player)){
			this.setId(this.createID(player));
			player.getAbilities().add(ability);
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
