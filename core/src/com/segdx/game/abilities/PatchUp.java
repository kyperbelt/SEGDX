package com.segdx.game.abilities;

import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.states.GameState;

public class PatchUp extends ShipAbility{
	
	private float repair;
	
	public PatchUp(int level) {
		repair = 10;
		this.setLevel(NodeEvent.getRandomInt(1, 5));
		if(level > 0)
			this.setLevel(level);
		this.setName("Patch Ship");
		this.setCooldown(7);
		this.setCombatCappable(true);
		this.setEnabled(true);
		this.setDesc("Patch your ship with your Repair Kit."
				+ "Requires "+level+"Latterium");
		this.setOutOfCombat(true);
		this.setPlayerUsed(true);
		this.setAttack(false);
	}

	@Override
	public void performAbility(SpaceEntity target) {
		if(this.getParentModule()!=null){
			Player p =(Player) target;
			for (int i = 0; i < this.getLevel(); i++) {
				p.removeResource(ResourceStash.LATTERIUM.getId());
			}
		}
		target.repairDamage(repair*this.getLevel());
		this.startCooldown();
	}

	@Override
	public boolean requirementsMet(SpaceEntity target) {
		Player p = (Player) target;
		if(p.containsResource(ResourceStash.LATTERIUM,this.getLevel())){
			return true;
		}
		return false;
	}

	@Override
	public void afterCooldown(GameState state) {
	}

}
