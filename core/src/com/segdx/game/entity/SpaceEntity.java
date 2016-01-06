package com.segdx.game.entity;

public class SpaceEntity {
	
	protected Ship ship;
	protected float currentHull;
	protected SpaceNode currentNode;
	public Ship getShip() {
		return ship;
	}
	public void setShip(Ship ship) {
		this.ship = ship;
	}
	public float getCurrentHull() {
		return currentHull;
	}
	public void setCurrentHull(float currentHull) {
		this.currentHull = currentHull;
	}
	public SpaceNode getCurrentNode() {
		return currentNode;
	}
	public void setCurrentNode(SpaceNode currentNode) {
		this.currentNode = currentNode;
	}

}
