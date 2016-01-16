package com.segdx.game.modules;


import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.abilities.ShootRailGun;
import com.segdx.game.entity.Player;

public class RailGun extends ShipModule{
	
	private ShipAbility shoot;

	public RailGun(int level) {
		this.setBaseValue(350);
		this.setCost(2);
		this.setLevel(getRandomLevel());
		if(level>0)
			this.setLevel(level);
		shoot = new ShootRailGun(this.getLevel());
		shoot.setParentModule(this);
		this.setName("Rail Gun");
		this.setDesc("Mounts a Rail gun on your ship to combat enemies.");
	}
	@Override
	public boolean installModule(Player player) {
		if(canInstall(player)){
			this.setId(this.createID(player));
			player.getAbilities().add(shoot);
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
