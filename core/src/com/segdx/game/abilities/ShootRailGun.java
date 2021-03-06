package com.segdx.game.abilities;

import com.badlogic.gdx.math.MathUtils;
import com.segdx.game.achievements.Achievement;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.achievements.Stats;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.entity.enemies.Berserker;
import com.segdx.game.entity.enemies.Enemy;
import com.segdx.game.entity.enemies.Pirate;
import com.segdx.game.entity.enemies.SbaScout;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.SoundManager;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class ShootRailGun extends ShipAbility{
	
	public float damage = 3;
	private float perc = .05f;

	public ShootRailGun(int level) {
		this.setLevel(NodeEvent.getRandomInt(1, 5));
		if(level > 0)
			this.setLevel(level);
		this.setName("Shoot RG");
		this.setCooldown(3);
		this.setCombatCappable(true);
		this.setEnabled(true);
		this.setDesc("Shoot your rail gun,must have some Salvage in your haul. \n\nChance to expend the resource "+(level*perc)*100+"%");
		this.setOutOfCombat(false);
		this.setPlayerUsed(true);
		this.setAttack(true);
		
	}
	@Override
	public void performAbility(SpaceEntity target) {
		Player p = null;
		GameState state = ((GameState)StateManager.get().getState(StateManager.GAME));
		if(getParentModule()!=null){
			 p = state.getSpaceMap().getPlayer();
			if(MathUtils.randomBoolean(this.getLevel()*perc)){
				p.removeResource(ResourceStash.SALVAGE.getId());
				state.updateCargo();
			}
			if(p.getCurrentEnemy()==null)
				return;
			p.getCurrentNode().getMap().createEnemyFrames(((CombatEvent)p.getCurrentNode().getEvent()).getEnemies());
		}
		if(target!=null){
			if(target.inflictDamage(damage+(this.getLevel()*2), false)){
				SoundManager.get().playSound(SoundManager.EXPLOSION);
				
				if(target instanceof Enemy){
					AchievementManager.get().grantAchievement("First Kill", Achievement.GAMEPLAY_ACHIEMENT, StateManager.get().getGameState().uistage,
								StateManager.get().getGameState().tm);
					Stats.get().increment("kills", 1);
					if(Stats.get().stattable.get("kills")>=20){
						AchievementManager.get().grantAchievement("Executioner", Achievement.GAMEPLAY_ACHIEMENT, StateManager.get().getGameState().uistage,
								StateManager.get().getGameState().tm);
					}
					if(target instanceof Pirate){
						
						Stats.get().increment("pirate kills", 1);
						
					}else if(target instanceof Berserker){
						
						Stats.get().increment("berserker kills", 1);
					}else if(target instanceof SbaScout){
						Stats.get().increment("sba kills", 1);
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
	public int getCooldown() {
		
		return (int) ((super.getCooldown()/getLevel())+(getLevel()*.5f));
	}
	

	@Override
	public boolean requirementsMet(SpaceEntity target) {
			Player p = (Player) target;
		if(p.containsResource(ResourceStash.SALVAGE, 1)){
			return true;
		}
		return false;
	}

	@Override
	public void afterCooldown(GameState state) {
	}

}
