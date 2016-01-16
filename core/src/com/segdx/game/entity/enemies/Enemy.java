package com.segdx.game.entity.enemies;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.entity.SpaceEntity;
import com.segdx.game.events.CombatEvent;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

/**
 * this is like the player class which contains a ship 
 * information liek current health hull fuel ect
 * but for ai enemies that appear on nodes.
 * @author Jonathan Camarena -kyperbelt
 *
 */
public abstract class Enemy extends SpaceEntity{
	
	private String name;
	private Array<ShipAbility> abilities;
	private NodeEvent parentevent;
	private boolean remove;
	protected GameState state =  (GameState) StateManager.get().getState(StateManager.GAME);;
	
	public abstract void update(SpaceEntity Target);
	
	public Array<ShipAbility> getAbilities() {
		return abilities;
	}
	
	public boolean shouldRemove(){
		return remove;
	}
	
	public void remove(){
		this.remove = true;
	}

	public void setAbilities(Array<ShipAbility> abilities) {
		this.abilities = abilities;
	}
	
	@Override
	public void setCurrentHull(float currentHull) {
		if(getParentevent()!=null)
			getParentevent().getParentnode().getMap().createEnemyFrames(((CombatEvent)getParentevent()).getEnemies());
		super.setCurrentHull(currentHull);
	}

	public NodeEvent getParentevent() {
		return parentevent;
	}

	public void setParentevent(NodeEvent parentevent) {
		this.parentevent = parentevent;
	}
	
	@Override
	public boolean inflictDamage(float damage, boolean isEnergy) { 
		getParentevent().getParentnode().addEntry(NodeEvent.COMBAT_LOG_ENTRY, this.getName()+" was hit for "+damage+" "+(isEnergy ? "energy":"physical")+" damage");
		
		return super.inflictDamage(damage, isEnergy);
	}
	
	public Rectangle getRect(){
		return new Rectangle(getShip().getSprite().getBoundingRectangle()); 
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
