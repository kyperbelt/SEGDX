package com.segdx.game.work;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.segdx.game.abilities.ShipAbility;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.modules.FuelReserves;
import com.segdx.game.modules.RailGun;
import com.segdx.game.modules.ShipModule;
import com.segdx.game.states.GameState;

public class WayToPeace extends Work {
	int railguns;
	
	public WayToPeace() {
		this.setName("A Way To Peace");
		this.setDesc("A political group within the Empire has started to raise awareness on the"
				+ " state of turmoil we are in. They say they are willing to reward all those who"
				+ "turn in their Rail Guns.");
		
	}

	@Override
	public boolean canComplete(Player p) {
		if(railguns>=10)
			return true;
		return false;
	}
	
	public void addGun(Player p){
		railguns++;
		p.removeShipModuleClass(new RailGun(0));
	}

	@Override
	public Table getWorkTable(final GameState state) {
		Table t = new Table();
		Table infotable = new Table();
		Label name  = new Label(""+this.getName(), state.skin);
		name.setFontScale(.8f);
		Label desc = new Label(""+this.getDesc()+"\nRequirements:\n-"+(10-railguns)+" Rail Guns", state.skin);
		desc.setFontScale(.5f);
		desc.setWrap(true);
		
		ScrollPane descscroll = new ScrollPane(desc, state.skin);
		descscroll.setColor(Color.DARK_GRAY);
		
		infotable.add(name).left().expand().fillX().row();
		infotable.add(descscroll).left().expand().fill().row();
		
		Table optionstable = new Table();
		TextButton turnin = new TextButton("Turn In", state.skin);
		if(!state.getSpaceMap().getPlayer().containsResource(ResourceStash.KNIPTORYTE, 1)||railguns==10){
			turnin.setDisabled(true);
			turnin.setColor(Color.FIREBRICK);
		}
		turnin.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				addGun(state.getSpaceMap().getPlayer());
				state.updateRestBar();
			}
		});
		TextButton complete = new TextButton("Complete", state.skin);
		if(!canComplete(state.getSpaceMap().getPlayer())){
			complete.setDisabled(true);
			complete.setColor(Color.FIREBRICK);
		}else{
			complete.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y) {
					grantAchievement(state);
					ShipModule m = new FuelReserves(10);
					m.setCost(0);
					state.getSpaceMap().getPlayer().installNewModule(m);
					ShipAbility.showMessage("Thank You!              ", " They are happy for your contribution and reward you with top of the line Fuel Reserve ranks", state.skin);
				}
			});
		}
		
		optionstable.add(turnin).left().expand();
		optionstable.add().fillX();
		optionstable.add(complete).right().expand();
		
		t.add(infotable).left().expand().fill().row();
		t.add(optionstable).left().expand().fillX();
		return t;
	}

}
