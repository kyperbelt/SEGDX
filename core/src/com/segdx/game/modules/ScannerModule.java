package com.segdx.game.modules;

import com.badlogic.gdx.math.MathUtils;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.Ship;

public class ScannerModule extends ShipModule{
	
	//increases player detection
	public ScannerModule(int level) {
		this.setCost(10);
		this.setLevel(MathUtils.round(MathUtils.random(1,2)));
		if(level>0){
			this.setLevel(level);
		}
		this.setBaseValue(2100);
		this.setName("Scanners ");
		this.setDesc("Allows you to get more detailed descriptions of travel nodes when targeting them. "
				+ "Removes a lot of the guess work. Only one scanner per ship.");
		
	}

	@Override
	public boolean installModule(Player player) {
		if(canInstall(player)&&player.getShip().getDetectionLevel()==Ship.NO_DETECTION){
			player.getShip().setDetectionLevel(this.getLevel());
			this.setId(this.createID(player));
			return true;
		}else{
			wasUnableToInstallDialog();
		}
		
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		player.getShip().setDetectionLevel(Ship.NO_DETECTION);
		removeModuleAbilities(player);
		return this;
	}

}
