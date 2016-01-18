package com.segdx.game.abilities;

import com.badlogic.gdx.math.MathUtils;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.achievements.Stats;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.events.ResourceEvent;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class ExtractResource extends ShipAbility{
	
	private float extract_rate[];
	private int miningid;
	
	public ExtractResource(int level) {
		extract_rate = new float[]{2,2+level};
		this.setLevel(level);
		this.setName("Extract ");
		this.setCooldown(11);
		this.setOnCooldown(false);
		this.setCombatCappable(false);
		this.setOutOfCombat(true);
		this.setEnabled(true);
		this.setDesc("Extract "+extract_rate[0]+" to "+extract_rate[1]+" [FOREST]Resources[] from deposits."
				+ "become immobile while extracting.");
		
	}

	@Override
	public void performAbility(SpaceEntity target) {
		Player player = (Player)target;
		GameState state = ((GameState)StateManager.get().getState(StateManager.GAME));
		state.travelbar.setVisible(false);
		startCooldown();
	}
	
	@Override
	public int getCooldown() {
		return cooldown-this.getLevel();
	}

	@Override
	public boolean requirementsMet(SpaceEntity target) {
		if(target.getCurrentNode().getEvent()!=null){
			
			if(target.getCurrentNode().getEvent().getType()==NodeEvent.RESOURCE&&
					((ResourceEvent)target.getCurrentNode().getEvent()).requiresMining()
					&&isEnabled()&&!isOnCooldown()){
				
				return true;
			}
		}
		return false;
	}

	@Override
	public void afterCooldown(GameState state) {
		state.travelbar.setVisible(true);
		int extractnum = MathUtils.round(MathUtils.random(extract_rate[0], extract_rate[1]));
		SpaceNode sn = state.getSpaceMap().getPlayer().getCurrentNode();
		if(sn.getEvent()==null||sn.getEvent().getType()!=NodeEvent.RESOURCE||
				!((ResourceEvent)sn.getEvent()).requiresMining()){
			ShipAbility.showMessage("Extraction Failed", "Something went wrong while perfor"
					+ "ming an extraction. You Lost the deposit!", state.skin).show(state.uistage);
			return;
		}
		
		if(((ResourceEvent)sn.getEvent()).getResourcesAvailable()<extractnum)
			extractnum = ((ResourceEvent)sn.getEvent()).getResourcesAvailable();
		
		state.travelbar.invalidate();
		for (int i = 0; i < extractnum; i++) {
			state.getSpaceMap().getPlayer().getCurrentNode().getLoot()
			.add(((ResourceEvent)sn.getEvent()).fetchResource());
			Stats.get().increment("resources extracted", 1);
			AchievementManager.get().grantAchievement("");
		}
		state.actiontabs.setChecked("Loot");
		state.updateActionbar();
		state.updateAbilities();
		
	}

}
