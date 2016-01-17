package com.segdx.game.abilities;


import com.badlogic.gdx.math.MathUtils;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class PowerShields extends ShipAbility{
	
	private float shields;
	
	private float perc;
	
	private SpaceEntity se;
	
	public PowerShields(int level) {
		shields = 10;
		perc = .08f;
		this.setLevel(NodeEvent.getRandomInt(1, 5));
		if(level > 0)
			this.setLevel(level);
		this.setName("Power Shields");
		this.setCooldown(10);
		this.setCombatCappable(true);
		this.setEnabled(true);
		this.setDesc("Gain an energy shield that is resistant to physical attacks. "
				+ "\n Requires you to have atleast one Dridium."
				+ "\n\nChance to expend the resource "+(level*perc)*100+"%");
		this.setOutOfCombat(false);
		this.setPlayerUsed(true);
		this.setAttack(false);
	}

	@Override
	public void performAbility(SpaceEntity target) {
		Player p = null;
		se = target;
		GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		if(getParentModule()!=null){
			 p = state.getSpaceMap().getPlayer();
			 p.getCurrentNode().addEntry(NodeEvent.RESOURCE_LOG_ENTRY, "You power up your shields");
			if(MathUtils.randomBoolean(this.getLevel()*perc)){
				p.removeResource(ResourceStash.DRIDIUM.getId());
				
				state.updateCargo();
			}
			p.getCurrentNode().getMap().createEnemyFrames(((CombatEvent)p.getCurrentNode().getEvent()).getEnemies());
		}
		if(target!=null){
			target.setCurrentShield(shields*this.getLevel());
			target.poweronShields(state,state.getSpaceMap().getStage(), target);
		}
		this.startCooldown();
	}

	@Override
	public boolean requirementsMet(SpaceEntity target) {
		Player p = (Player) target;
		if(p.containsResource(ResourceStash.DRIDIUM, 1)){
			return true;
		}
		return false;
	}

	@Override
	public void afterCooldown(GameState state) {
		se.setCurrentShield(0);
		
	}
	




}
