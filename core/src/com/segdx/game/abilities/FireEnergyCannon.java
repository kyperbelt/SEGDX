package com.segdx.game.abilities;

import com.badlogic.gdx.math.MathUtils;
import com.segdx.game.achievements.Achievement;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.achievements.Stats;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.entity.enemies.Enemy;
import com.segdx.game.entity.enemies.Pirate;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class FireEnergyCannon extends ShipAbility {
	
	float damage = 1;
	float fuelcost = 1;
	public FireEnergyCannon(int level) {
		this.setLevel(NodeEvent.getRandomInt(1, 5));
		if(level > 0)
			this.setLevel(level);
		this.setName("Fire ENERGY C. ");
		this.setCooldown(10);
		this.setCombatCappable(true);
		this.setEnabled(true);
		this.setDesc("Fire your energy cannon  to inflict "+damage*level+" damage tothe enemy. x4 agasint shielded foes. Costs "+
		fuelcost*level+" fuel to use");
		this.setOutOfCombat(false);
		this.setPlayerUsed(true);
		this.setAttack(true);
	}
	
	@Override
	public int getCooldown() {
		
		return (super.getCooldown()/this.getLevel())+this.getLevel();
	}

	@Override
	public void performAbility(SpaceEntity target) {
		Player p = null;
		GameState state = ((GameState)StateManager.get().getState(StateManager.GAME));
		if(getParentModule()!=null){
			 p = state.getSpaceMap().getPlayer();
				p.setCurrentFuel(p.getCurrentFuel()-(fuelcost*this.getLevel()));
				state.updateCargo();
			
			if(p.getCurrentEnemy()==null)
				return;
			p.getCurrentNode().getMap().createEnemyFrames(((CombatEvent)p.getCurrentNode().getEvent()).getEnemies());
		}
		if(target!=null){
			if(target.inflictDamage(damage*getLevel(), true)){
				SoundManager.get().playSound(SoundManager.EXPLOSION);
				
				if(target instanceof Enemy){
					
					Stats.get().increment("kills", 1);
					if(target instanceof Pirate){
						
						Stats.get().increment("pirate kills", 1);
						AchievementManager.get().grantAchievement("First Kill", Achievement.GAMEPLAY_ACHIEMENT, StateManager.get().getGameState().uistage,
								StateManager.get().getGameState().tm);
					}
					
					((Enemy) target).remove();
					((CombatEvent)((Enemy) target).getParentevent()).removeDeadEntities();
					((CombatEvent)((Enemy) target).getParentevent()).getParentnode().addEntry(NodeEvent.COMBAT_LOG_ENTRY, ((Enemy) target).getName()+" was destroyed.");
					p.setCurrentEnemy(null);
				}
				target.visualizeDamage(state, state.getSpaceMap().getStage(), target);
				
			}else{
				target.visualizeDamage(state, state.getSpaceMap().getStage(), target);
			}
			this.startCooldown();
		}
	}

	@Override
	public boolean requirementsMet(SpaceEntity target) {

		return true;
	}

	@Override
	public void afterCooldown(GameState state) {
	}

}
