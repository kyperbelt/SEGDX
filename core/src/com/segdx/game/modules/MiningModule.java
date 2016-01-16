package com.segdx.game.modules;

import com.segdx.game.abilities.ExtractResource;
import com.segdx.game.entity.Player;
/**
 * Mining module
 * Mining takes a few seconds and renders the player unable to move
 * higher level modules allows you to mine more resources faster.
 * @author Jonathan Camarena -kyperbelt
 *
 */
public class MiningModule extends ShipModule{
	
	private ExtractResource extract;
	
	public MiningModule(int level) {
		this.setBaseValue(300);
		this.setCost(3);
		this.setLevel(getRandomLevel());
		if(level>0)
			this.setLevel(level);
		extract = new ExtractResource(this.getLevel());
		extract.setParentModule(this);
		this.setName("Mining Module");
		this.setDesc("Allows you to [SKY]Extract[] resources from deposits. Higher tiers allow for faster extraction.");
	}

	@Override
	public boolean installModule(Player player) {
		if(canInstall(player)){
			this.setId(this.createID(player));
			player.getAbilities().add(extract);
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
