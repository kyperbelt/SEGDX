package com.segdx.game.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntIntMap;
import com.badlogic.gdx.utils.ObjectMap;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.StateManager;
import com.segdx.game.modules.ShipModule;

public class TradePost {
	
	public static final float MIN = 1.1f;
	public static final float MAX = 5f;
	
	//buy modifiers for the different types of resoruces
	private ObjectMap<Integer, Float> buymodifiers;

	//the resources at this tradepost
	private IntIntMap resources;
	
	private Array<ShipModule> modules;
	
	private Ship ship;
	
	public TradePost(){
		modules = new Array<ShipModule>();
		
		resources = new IntIntMap();
		resources.put(ResourceStash.DRIDIUM.getId(), 0);
		resources.put(ResourceStash.KNIPTORYTE.getId(), 0);
		resources.put(ResourceStash.LATTERIUM.getId(), 0);
		resources.put(ResourceStash.NAQUIDRA.getId(), 0);
		resources.put(ResourceStash.SALVAGE.getId(), 0);
		
		
		buymodifiers = new ObjectMap<Integer, Float>();
		buymodifiers.put(ResourceStash.DRIDIUM.getId(), MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.KNIPTORYTE.getId(),MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.LATTERIUM.getId(),MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.NAQUIDRA.getId(), MathUtils.random(MIN, MAX));
		buymodifiers.put(ResourceStash.SALVAGE.getId(),MathUtils.random(MIN, MAX));
		
		
		
	}
	
	public Table getModulesTable(){
		final Player p = StateManager.get().getGameState().getSpaceMap().getPlayer();
		Table moduletable = new Table();
		final Skin skin = StateManager.get().getGameState().skin;
		Drawable back = StateManager.get().getGameState().defaultbackground;
		for (int i = 0; i < modules.size; i++) {
			final int ii = i;
			final ShipModule m = modules.get(i);
			Table mtable = new Table();
			mtable.setBackground(back);
			mtable.setColor(Color.LIME);
			Table iconnametable = new Table();
			Table buttonpricetable = new Table();
			
			TextTooltip tp = new TextTooltip(""+modules.get(i).getDesc()+"\n\nUpgradePoints:"+modules.get(i).getCost(), skin);
			tp.setInstant(true);
			tp.getActor().setFontScale(.5f);
			Image icon = new Image(Assets.manager.get("map/module.png",Texture.class));
			Label name  = new Label(""+modules.get(i).getName(), skin);
			name.setFontScale(.5f);
			TextButton install = new TextButton("Install", skin);
			install.getLabel().setFontScale(.8f);
			install.addListener(new ClickListener(){
				
				public void clicked(InputEvent event, float x, float y) {
					
					if(p.getUpgradePointsAvailable()<m.getCost()){
						ShipAbility.showMessage("Not Enough Points!       ", ""
								+ "You do not have enough upgrade points available to install"
								+ "this module. If you would like to install it please uninstall"
								+ "other modules until you have "+m.getCost()+" available",skin).show(StateManager.get().getGameState().uistage);
						return;
					}
					
					if(p.getCurrency()<m.getBaseValue()){
						ShipAbility.showMessage("Not Enough Funds!       ", 
								"You dont have enough currency to buy this and install this module.", skin)
						.show(StateManager.get().getGameState().uistage);
						return;
					}
					
					p.setCurrency(p.getCurrency()-m.getCost());
					p.installNewModule(modules.removeIndex(ii));
					StateManager.get().getGameState().updateTradeBar();
					StateManager.get().getGameState().updateAbilities();
					StateManager.get().getGameState().updateModsTab();
					
				}
			});
			Label points = new Label("pointsReq:"+modules.get(i).getCost(), skin);
			points.setFontScale(.3f);
			Label cost = new Label(" $:"+modules.get(i).getBaseValue(), skin);
			cost.setFontScale(.5f);
			icon.addListener(tp);
			name.addListener(tp);
			
			iconnametable.setBackground(back);
			iconnametable.setColor(Color.FOREST);
			iconnametable.add(icon).left().expand();
			iconnametable.add(name).left().expand().fill();
			
			buttonpricetable.add(cost).right().expand();
			buttonpricetable.add(install).right().expand();
			
			mtable.add(iconnametable).left().expand().fillX();
			mtable.add(buttonpricetable).left().expand();
			
			moduletable.add(mtable).left().expand().fillX().row();
		}
		
		
		return moduletable;
	}
	
	public boolean isEventModified(Resource r){
		if(getModifierFor(r)==1f)
			return true;
		else return false;
	}
	
	public float getModifierFor(Resource resource){
		return buymodifiers.get(resource.getId());
	}
	
	public void setModifierFor(Resource resource,float modifier){
		buymodifiers.remove(resource.getId());
		buymodifiers.put(resource.getId(), modifier);
	}
	
	public int getResourceBuyPrice(Resource resource){
		return (int) (resource.getBasevalue()*getModifierFor(resource));
	}
	
	public int getResourceSellPrice(Resource resource){
		return (int) ((resource.getBasevalue()*getModifierFor(resource))*.9f);
	}
	
	public boolean isResourceAvailbale(Resource resource,int amount){
		if(resources.get(resource.getId(), 0)>amount)
			return true;
		
		return false;
	}
	
	public Resource getResource(Resource resource){
		removeResources(resource, 1);
		return ResourceStash.RESOURCES.get(resource.getId()).clone();
	}
	
	public void removeResources(Resource resource,int amount){
		resources.getAndIncrement(resource.getId(), 0, -amount);
		if(resources.get(resource.getId(), 0)<0)
			resources.put(resource.getId(), 0);
	}
	
	public void addResource(Resource resource){
		this.addResource(resource, 1);
	}
	
	public void addResource(Resource resource,int amount){
		resources.getAndIncrement(resource.getId(), 0, amount);
		if(resources.get(resource.getId(), 0)>99)
			resources.put(resource.getId(), 99);
	}

	public IntIntMap getResources() {
		return resources;
	}

	public void setResources(IntIntMap resources) {
		this.resources = resources;
	}

	public Array<ShipModule> getModules() {
		return modules;
	}

	public void setModules(Array<ShipModule> modules) {
		this.modules = modules;
	}

	public Ship getShip() {
		return ship;
	}

	public void setShip(Ship ship) {
		this.ship = ship;
	}

}
