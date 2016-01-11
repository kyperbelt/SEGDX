package com.segdx.game.events;

import com.badlogic.gdx.utils.Array;
import com.segdx.game.entity.SpaceNode;

public abstract class NodeEvent {
	
	public static final int RESOURCE = 0;
	public static final int HELP = 1;
	public static final int COMBAT = 2;
	
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
	
	
}
