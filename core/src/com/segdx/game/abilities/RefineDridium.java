package com.segdx.game.abilities;


import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class RefineDridium extends ShipAbility{
	//TODO: a better way to do this possibly to have an ability bank class
	//that stores all the abilities as variables without having to remake subclasses
	
	private int basefuel;
	private int dridiumcost;

	public RefineDridium(int level) {
		basefuel = 30;
		dridiumcost = 1;
		this.setLevel(level);
		this.setCombatCappable(false);
		this.setOutOfCombat(true);
		this.setEnabled(true);
		this.setId(33);
		this.setCooldown(3);
		this.setPlayerUsed(true);
		this.setName("Refine Dridium");
		this.setDesc("Use "+dridiumcost*getLevel()+" dridium ore to produce "+basefuel*level+" fuel.");
	}
	
	@Override
	public void performAbility(SpaceEntity target) {
		
		Player player = (Player) target;
		if(player.containsResource(ResourceStash.DRIDIUM, dridiumcost*this.getLevel())){
			player.setCurrentFuel(player.getCurrentFuel()+(basefuel*this.getLevel()));
			for (int i = 0; i < this.getLevel(); i++) {
				player.removeResource(ResourceStash.DRIDIUM.getId());
			}
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
		if(player.containsResource(ResourceStash.DRIDIUM, dridiumcost*getLevel())&&isEnabled()&&!isOnCooldown())
			return true;
		return false;
	}

	@Override
	public void afterCooldown(GameState state) {
	}

}
