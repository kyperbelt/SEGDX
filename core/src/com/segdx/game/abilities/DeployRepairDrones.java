package com.segdx.game.abilities;

import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.enemies.Enemy;
import com.badlogic.gdx.math.MathUtils;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class DeployRepairDrones extends ShipAbility{
	
	private float repair = 2;
	private float perc = .09f;
	public DeployRepairDrones(int level) {
		this.setLevel(NodeEvent.getRandomInt(1, 5));
		if(level > 0)
			this.setLevel(level);
		this.setName("Deploy Repair Drones");
		this.setCooldown(10);
		this.setCombatCappable(true);
		this.setEnabled(true);
		this.setDesc("Send out repair drones to periodically repair your ship.\n"
					+ "Uses up "+level+" salave on use and requires you to have atleast 1 Latterium."
							+ "\n"
							+ "\n"+(level*perc)*100+"% chance to use up Latterium.");
		this.setOutOfCombat(false);
		this.setPlayerUsed(true);
		this.setAttack(false);
	}

	@Override
	public void performAbility(final SpaceEntity target) {
		StateManager.get().getGameState().getSpaceMap().getTimer().addTimedTask(new TimedTask() {
			
			@Override
			public void onExecute() {
				if(target instanceof Enemy)
					if(((Enemy)target).shouldRemove())
						this.remove();
				
				target.repairDamage(repair*getLevel());
			}
		}).repeat(getCooldown()/2).setSleep(2);
		
		if(this.getParentModule()!=null){
			Player p =(Player) target;
			for (int i = 0; i < this.getLevel(); i++) {
				p.removeResource(ResourceStash.SALVAGE.getId());
			}
			if(MathUtils.randomBoolean(this.getLevel()*perc)){
				p.removeResource(ResourceStash.LATTERIUM.getId());
			}
		}
		this.startCooldown();
	}

	@Override
	public boolean requirementsMet(SpaceEntity target) {
		Player p = (Player) target;
		if(p.containsResource(ResourceStash.LATTERIUM,1)&&
				p.containsResource(ResourceStash.SALVAGE, this.getLevel())){
			return true;
		}
		return false;
	}

	@Override
	public void afterCooldown(GameState state) {
	}

}
