package com.segdx.game.modules;

import com.segdx.game.entity.Player;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class ExtraHaul extends ShipModule{

	private float haul_cap;
	
	//GIVE MORE FUEL CAPACITY
	public ExtraHaul(int level) {
		haul_cap = 5;
		this.setBaseValue(300);
		this.setCost(2);
		this.setLevel(this.getRandomLevel());
		if(level>0)
			this.setLevel(level);
		this.setName("Extra Haul ");
		this.setDesc("Increases the capacity of your ship by  "+haul_cap*this.getLevel()+". Useful "
				+ "for long distance travel.");
	}
	
	@Override
	public boolean installModule(Player player) {
		if(canInstall(player)){
			this.setId(this.createID(player));
			player.getShip().setCapacity((player.getShip().getCapacity()+(haul_cap*this.getLevel())));
			return true;
		}else{
			wasUnableToInstallDialog();
		}
		
		return false;
	}

	@Override
	public ShipModule removeModule(Player player) {
		this.removeModuleAbilities(player);
		player.getShip().setCapacity((player.getShip().getCapacity()-(haul_cap*this.getLevel())));
		((GameState)StateManager.get().getState(StateManager.GAME)).updateRestBar();
		return this;
	}

}
