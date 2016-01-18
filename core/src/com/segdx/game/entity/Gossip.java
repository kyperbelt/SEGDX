package com.segdx.game.entity;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.segdx.game.states.GameState;
import com.segdx.game.tween.CameraAccessor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;

public class Gossip {
	
	public static final int EVENT_GOSSIP = 150;
	public static final int TRADE_GOSSIP = 300;
	public static final int WORK_GOSSIP = 600;
	
	private String name;
	private String Description;
	private SpaceNode node;
	private float cost;
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
	public SpaceNode getNode() {
		return node;
	}
	public void setNode(SpaceNode node) {
		this.node = node;
	}
	public float getCost() {
		return cost;
	}
	public void setCost(float cost) {
		this.cost = cost;
	}
	
	public void tweenTo(GameState state){
		SpaceMap map = state.getSpaceMap();
		TweenManager tm = map.getTm();
		OrthographicCamera cam = map.getCam();
		
		Tween.to(cam, CameraAccessor.POSITION_X_Y, 2).target(node.getX(),node.getY()).start(tm);
	}

}
