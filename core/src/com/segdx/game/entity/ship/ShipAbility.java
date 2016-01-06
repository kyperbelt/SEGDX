package com.segdx.game.entity.ship;

import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.managers.StateManager;
import com.segdx.game.modules.ShipModule;
import com.segdx.game.states.GameState;

/**
 * abilities that the ship can have
 * @author Jonathan Camarena -kyperbelt
 *
 */
public abstract class ShipAbility {
	
	//TODO: make base ship ability class and then extend it into individual ability types
	//abstract class performAbility(Target)
	
	private String name;
	private String desc;
	private ShipModule parentModule;
	private int id;
	private int cooldown;
	private int cctimer = 0;
	private boolean enabled;
	private boolean onCooldown;
	private boolean combatCappable;
	private boolean outOfCombat;
	private boolean playerUsed;
	
	public abstract void performAbility(SpaceEntity target);
	public abstract boolean requirementsMet(SpaceEntity target);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}
	
	public boolean hasCooldown(){
		return (cooldown > 0);
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getId() {
		return id;
	}
	
	public int getCooldownLeft(){
		return cooldown-cctimer;
	}
	
	public void startCooldown(){
		setOnCooldown(true);
		final GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		state.getSpaceMap().getTimer().addTimedTask(new TimedTask() {
			
			@Override
			public void onExecute() {
				cctimer++;
				System.out.println(getCooldownLeft());
				state.updateAbilities();
				if(cctimer>=cooldown){
					cctimer = 0;
					setOnCooldown(false);
					state.updateAbilities();
					
				}
				
				
			}
		}).repeat(3);
	}
	
	public boolean hasParentModule(){
		return (parentModule!=null);
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isCombatCappable() {
		return combatCappable;
	}

	public void setCombatCappable(boolean combatCappable) {
		this.combatCappable = combatCappable;
	}

	public boolean isOutOfCombat() {
		return outOfCombat;
	}

	public void setOutOfCombat(boolean outOfCombat) {
		this.outOfCombat = outOfCombat;
	}

	public boolean isPlayerUsed() {
		return playerUsed;
	}

	public void setPlayerUsed(boolean playerUsed) {
		this.playerUsed = playerUsed;
	}

	public ShipModule getParentModule() {
		return parentModule;
	}

	public void setParentModule(ShipModule parentModule) {
		this.parentModule = parentModule;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}
	public boolean isEnabled() {
		return enabled;
	}
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	public boolean isOnCooldown() {
		return onCooldown;
	}
	public void setOnCooldown(boolean onCooldown) {
		this.onCooldown = onCooldown;
	}

}
