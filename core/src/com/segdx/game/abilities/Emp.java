package com.segdx.game.abilities;

import com.badlogic.gdx.math.MathUtils;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.entity.enemies.Enemy;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class Emp extends ShipAbility {
	
	private int disable = 10;
	
	private Enemy target;
	
	private float perc=.1f;
	
	public Emp(int level) {
		this.setLevel(NodeEvent.getRandomInt(1, 5));
		if(level > 0)
			this.setLevel(level);
		this.setName("Fire EM Pulse ");
		this.setCooldown(10);
		this.setCombatCappable(true);
		this.setEnabled(true);
		this.setDesc("Fire a magnetic pulse at the target disabling them for "+disable*level+"seconds\n"
				+ "Requires you to have 1 naquidra in your haul. \n"
				+ "Chance to use resource "+(perc*this.getLevel())*100+"%");
		this.setOutOfCombat(false);
		this.setPlayerUsed(true);
		this.setAttack(true);
	}

	@Override
	public void performAbility(SpaceEntity target) {
		
		if(target instanceof Enemy){
			this.target = (Enemy) target;
			((Enemy) target).setDisabled(true);
			 Player p = StateManager.get().getGameState().getSpaceMap().getPlayer();
				if(MathUtils.randomBoolean(this.getLevel()*perc)){
					p.removeResource(ResourceStash.NAQUIDRA.getId());
					StateManager.get().getGameState().updateCargo();
				}
			this.startCooldown();
			StateManager.get().getGameState().getSpaceMap().createEnemyFrames(((Enemy) target).getParentCombatEvent().getEnemies());
		}
	}

	@Override
	public boolean requirementsMet(SpaceEntity atarget) {
		Player p = (Player) atarget;
		if(p.containsResource(ResourceStash.NAQUIDRA, 1))
			return true;
		return false;
	}

	@Override
	public void afterCooldown(GameState state) {
		state.getSpaceMap().createEnemyFrames(target.getParentCombatEvent().getEnemies());
		((Enemy)target).setDisabled(false);
	}

}
