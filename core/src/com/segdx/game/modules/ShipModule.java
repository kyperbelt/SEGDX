package com.segdx.game.modules;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.segdx.game.entity.Player;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

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
	
	public static final int MAX_SUPER_LEVEL = 10;
	
	//TODO: create the base ship module class and possibly extend it on to specific modules
	//maybe it can all be handled in this class.who knows.
	//installModule(Player player) -- apply modifiers and abilities in this method
	//removeModule(Player player) -- remove all modifiers and abilities related to this module
	
	private String name;
	private float baseValue;
	private String desc;
	private int cost;
	private int id;
	private int level;
	
	/**
	 * install the ship module to the 
	 * @param player
	 * @return
	 */
	public abstract boolean installModule(Player player);
	
	public abstract ShipModule removeModule(Player player);

	public String getName() {
		return name+" "+level;
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
		return cost*level;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public float getBaseValue() {
		return baseValue*level;
	}

	public void setBaseValue(float baseValue) {
		this.baseValue = baseValue;
	}
	
	public int getRandomLevel(){
		return MathUtils.round(MathUtils.random(ShipModule.MIN_LVL, ShipModule.MAX_LVL));
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}
	
	public int createID(Player player){
		int id = player.getModules().size;
		while(player.containsModuleId(id)){
			id++;
		}
		return id;
	}
	
	public boolean canInstall(Player player){
		return player.getUpgradePointsAvailable()>=this.getCost();
	}
	
	public void wasUnableToInstallDialog(){
		GameState state = ((GameState)StateManager.get().getState(StateManager.GAME));
		Dialog d = new Dialog("Install fail      ", state.skin);
		d.getTitleLabel().setFontScale(.7f);
		Label l = new Label("You were unable to install the module. Maybe you do not have enough upgrade points or"
				+ "There is conflicting modules already installed.", state.skin);
		l.setFontScale(.5f);
		l.setWrap(true);
		d.getContentTable().add(l).center().expand().fill();
		d.button("OK",true);
		((TextButton)d.getButtonTable().getChildren().get(0)).getLabel().setFontScale(.7f);
		d.show(state.uistage);
		
	}
	
	public void removeModuleAbilities(Player player){
		for (int i = 0; i < player.getAbilities().size; i++) {
			if(player.getAbilities().get(i).getParentModule().getId()==this.getId()){
				player.getAbilities().removeIndex(i);
			}
		}
	}

}
