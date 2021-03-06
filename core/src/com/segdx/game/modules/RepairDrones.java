package com.segdx.game.modules;

import com.segdx.game.abilities.DeployRepairDrones;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.entity.Player;

public class RepairDrones extends ShipModule{

	private ShipAbility ability;
	
	public RepairDrones(int level) {
		this.setBaseValue(600);
		this.setCost(5);
		this.setLevel(getRandomLevel());
		if(level>0)
			this.setLevel(level);
		ability = new DeployRepairDrones(this.getLevel());
		ability.setParentModule(this);
		this.setName("Repair Drones");
		this.setDesc("A Repair Drone module ablt to hold and deploy drones to repair your ship.");
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
		return null;
	}
}
