package com.segdx.game.modules;

import com.segdx.game.abilities.Emp;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.entity.Player;

public class MPGModule extends ShipModule {

	ShipAbility ability;
	
	public MPGModule(int level) {
		this.setBaseValue(890);
		this.setCost(5);
		this.setLevel(getRandomLevel());
		if(level>0)
			this.setLevel(level);
		ability = new Emp(this.getLevel());
		ability.setParentModule(this);
		this.setName("MPG");
		this.setDesc("Magnetic Pulse Generator \n Allows you to generate magnetic pulses to disable enemy ships.");
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
