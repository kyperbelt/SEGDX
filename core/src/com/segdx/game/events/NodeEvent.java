package com.segdx.game.events;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Array;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public abstract class NodeEvent {
	
	public static final int RESOURCE = 1;
	public static final int HELP = 2;
	public static final int COMBAT = 3;
	
	public static final int RESOURCE_LOG_ENTRY = 32;
	public static final int HELP_LOG_ENTRY = 64;
	public static final int COMBAT_LOG_ENTRY = 128;
	
	//name of this event
	private String name;
	//the description given to the player for this even
	private String Description;
	//the id of this event
	private int id;
	//the parent node of this event
	private SpaceNode parentnode;
	//how many cycles does this event remain active
	private int cycleDuration;
	//should this event be removed;
	private boolean shouldRemove;
	
	private String[] genericdescs;
	
	private int type;
	
	private int startCycle;
	
	
	public abstract void update();
	public abstract void createGossip(Array<SpaceNode> nearbyrestnodes);
	public abstract void applyEconomics(Array<SpaceNode> nearbytradenodes);
	public abstract void removeNodeEvent();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return Description;
	}

	public void setDescription(String description) {
		Description = description;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public SpaceNode getParentnode() {
		return parentnode;
	}

	public void setParentnode(SpaceNode parentnode) {
		this.parentnode = parentnode;
	}
	public int getCycleDuration() {
		return cycleDuration;
	}
	public void setCycleDuration(int cycleDuration) {
		this.cycleDuration = cycleDuration;
	}
	public boolean isShouldRemove() {
		return shouldRemove;
	}
	public void setShouldRemove(boolean shouldRemove) {
		this.shouldRemove = shouldRemove;
	}
	
	public void setStartCycle(int startCycle){
		this.startCycle = startCycle;
	}
	
	public boolean hasCycles(){
		return (getParentnode().getMap().getTimer().getCurrentCycle()-startCycle)<getCycleDuration();
	}
	
	public int getStartCycle(){
		return startCycle;
	}
	
	public static int getRandomInt(int start, int end){
		return MathUtils.round(MathUtils.random(start, end));
	}
	
	
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
	public void setGenericDescs(String[] generic){
		this.genericdescs = generic;
	}
	
	public String getGenericNodeDesc(){
		int index = MathUtils.round(MathUtils.random(0, genericdescs.length-1));
		return genericdescs[index];
	}
	
	
}
