package com.segdx.game.entity;

import java.text.DecimalFormat;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;
import com.segdx.game.events.NodeEvent;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class RestStop {
	
	public static final float FUEL_COST_MIN = 1.5f;
	public static final float FUEL_COST_MAX = 3.0f;
	
	public static final float FOOD_COST_MIN = .5f;
	public static final float FOOD_COST_MAX = 2.0f;
	
	private float fuelprice;
	
	private float foodprice;
	
	private float foodmodifier;
	
	private float fuelmodifier;
	
	private NodeEvent event;
	
	private ObjectMap<Integer, Gossip> gossip;
	
	public RestStop(float fuelprice,float foodprice){
		GameState state = (GameState) StateManager.get().getState(StateManager.GAME);
		this.fuelprice = Float.parseFloat(state.df.format(fuelprice));
		this.foodprice = Float.parseFloat(state.df.format(foodprice));
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
}
