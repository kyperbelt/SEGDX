package com.segdx.game.modules;

import com.segdx.game.entity.Player;

/**
 * Ship modules used to upgrade your ship stats or add
 * abilities to your ship.
 * @author Jonathan Camarena -kyperbelt
 *
 */
public abstract class ShipModule {
	
	public static final String ICON = "map/module.png";
	
	public static final int MIN_LVL = 1;
	public static final int MAX_LVL = 5;
	
	//TODO: create the base ship module class and possibly extend it on to specific modules
	//maybe it can all be handled in this class.who knows.
	//installModule(Player player) -- apply modifiers and abilities in this method
	//removeModule(Player player) -- remove all modifiers and abilities related to this module
	
	private String name;
	private float baseValue;
	private String desc;
	private int cost;
	private int id;
	
	/**
	 * install the ship module to the 
	 * @param player
	 * @return
	 */
	public abstract boolean installModule(Player player);
	
	public abstract ShipModule removeModule(Player player);

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public float getBaseValue() {
		return baseValue;
	}

	public void setBaseValue(float baseValue) {
		this.baseValue = baseValue;
	}

}
