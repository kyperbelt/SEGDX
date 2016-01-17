package com.segdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;
import com.segdx.game.work.Work;

public class RestStop {
	
	public static final float FUEL_COST_MIN = 1f;
	public static final float FUEL_COST_MAX = 5f;
	
	public static final float FOOD_COST_MIN = 1;
	public static final float FOOD_COST_MAX = 10;
	
	private float fuelprice;
	
	private float foodprice;
	
	private float foodmodifier;
	
	private float fuelmodifier;
	
	private float hullrepairprice;
	
	private NodeEvent event;
	
	private Work work;
	
	private SpaceNode parent;
	
	private ObjectMap<Integer, Gossip> gossip;
	
	public RestStop(SpaceNode parent,float fuelprice,float foodprice){
		this.parent = parent;
		GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		this.fuelprice = MathUtils.round(fuelprice);
		this.foodprice = MathUtils.round(foodprice);
		this.setHullrepairprice(MathUtils.round(MathUtils.random(5, 20)));
		gossip = new ObjectMap<Integer, Gossip>();
		fuelmodifier = 1f;
		foodmodifier = 1f;
	}

	public float getFuelprice() {
		return fuelprice*getFuelmodifier();
	}

	public void setFuelprice(float fuelprice) {
		this.fuelprice = fuelprice;
	}

	public float getFoodprice() {
		return foodprice*getFoodmodifier();
	}

	public void setFoodprice(float foodprice) {
		this.foodprice = foodprice;
	}

	public float getFoodmodifier() {
		return foodmodifier;
	}

	public void setFoodmodifier(float foodmodifier) {
		this.foodmodifier = foodmodifier;
	}

	public float getFuelmodifier() {
		return fuelmodifier;
	}

	public void setFuelmodifier(float fuelmodifier) {
		this.fuelmodifier = fuelmodifier;
	}

	public NodeEvent getEvent() {
		return event;
	}

	public void setEvent(NodeEvent event) {
		this.event = event;
	}

	public ObjectMap<Integer, Gossip> getGossip() {
		return gossip;
	}

	public void setGossip(ObjectMap<Integer, Gossip> gossip) {
		this.gossip = gossip;
	}

	public float getHullrepairprice() {
		return hullrepairprice;
	}

	public void setHullrepairprice(float hullrepairprice) {
		this.hullrepairprice = hullrepairprice;
	}

	public Work getWork() {
		return work;
	}

	public void setWork(Work work) {
		this.work = work;
	}

	public SpaceNode getParent() {
		return parent;
	}

	public void setParent(SpaceNode parent) {
		this.parent = parent;
	}
}
