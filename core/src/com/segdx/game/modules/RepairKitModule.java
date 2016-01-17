package com.segdx.game.modules;

import com.segdx.game.abilities.PatchUp;
import com.segdx.game.abilities.PowerShields;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.entity.Player;

public class RepairKitModule extends ShipModule {
	
	private ShipAbility ability;
	
	public RepairKitModule(int level) {
		this.setBaseValue(400);
		this.setCost(2);
		this.setLevel(getRandomLevel());
		if(level>0)
			this.setLevel(level);
		ability = new PatchUp(this.getLevel());
		ability.setParentModule(this);
		this.setName("Repair Kit");
		this.setDesc("A repair kit useful for patching up your ship when you are not near a Rest Post.");
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
