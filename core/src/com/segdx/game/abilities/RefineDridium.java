package com.segdx.game.abilities;

import com.badlogic.gdx.math.MathUtils;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class RefineDridium extends ShipAbility{
	//TODO: a better way to do this possibly to have an ability bank class
	//that stores all the abilities as variables without having to remake subclasses

	public RefineDridium() {
		this.setCombatCappable(false);
		this.setOutOfCombat(true);
		this.setEnabled(true);
		this.setId(33);
		this.setCooldown(3);
		this.setPlayerUsed(true);
		this.setName("Refine Dridium");
		this.setDesc("Use 2 dridium ore to produce fuel.");
	}
	
	@Override
	public void performAbility(SpaceEntity target) {
		
		Player player = (Player) target;
		if(player.containsResource(ResourceStash.DRIDIUM, 2)){
			player.setCurrentFuel(player.getCurrentFuel()+((int)MathUtils.random(35, 65)));
			player.removeResource(ResourceStash.DRIDIUM.getId());
			player.removeResource(ResourceStash.DRIDIUM.getId());
			startCooldown();
			((GameState)StateManager.get().getState(StateManager.GAME)).updateRestBar();
			((GameState)StateManager.get().getState(StateManager.GAME)).updateTradeBar();
			
		}else{
			//not enough dridium
			//do something else like open up a dialog or something
		}
	}

	@Override
	public boolean requirementsMet(SpaceEntity target) {
		Player player = (Player) target;
		if(player.containsResource(ResourceStash.DRIDIUM, 2)&&isEnabled()&&!isOnCooldown())
			return true;
		return false;
	}

}
