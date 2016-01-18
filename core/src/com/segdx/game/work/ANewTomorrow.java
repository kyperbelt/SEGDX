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
import com.segdx.game.modules.ExtraHaul;
import com.segdx.game.modules.ShipModule;
import com.segdx.game.states.GameState;

public class ANewTomorrow extends Work {
	
	int kniptoryte = 0;
	
	public ANewTomorrow() {
		this.setName("A New Tomorrow");
		this.setDesc("After the devestation brought on by the war against the Kartek United Federation a lot of people were left planetless and by extension HomeLess."
				+ "Help us collect enough Material to rebuild our colonies and we might make it worth your while.");
	}

	@Override
	public boolean canComplete(Player p) {
		if(kniptoryte>=200)
			return true;
		return false;
	}
	
	public void turnin(Player p){
		kniptoryte++;
		p.removeResource(ResourceStash.KNIPTORYTE.getId());
	}

	@Override
	public Table getWorkTable(final GameState state) {
		Table t = new Table();
		Table infotable = new Table();
		Label name  = new Label(""+this.getName(), state.skin);
		name.setFontScale(.8f);
		Label desc = new Label(""+this.getDesc()+"\nRequirements:\n-"+(200-kniptoryte)+" Kniptoryte", state.skin);
		desc.setFontScale(.5f);
		desc.setWrap(true);
		
		ScrollPane descscroll = new ScrollPane(desc, state.skin);
		descscroll.setColor(Color.DARK_GRAY);
		
		infotable.add(name).left().expand().fillX().row();
		infotable.add(descscroll).left().expand().fill().row();
		
		Table optionstable = new Table();
		TextButton turnin = new TextButton("Turn In", state.skin);
		if(!state.getSpaceMap().getPlayer().containsResource(ResourceStash.KNIPTORYTE, 1)||kniptoryte==200){
			turnin.setDisabled(true);
			turnin.setColor(Color.FIREBRICK);
		}
		turnin.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				turnin(state.getSpaceMap().getPlayer());
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
					ShipModule m = new ExtraHaul(10);
					m.setCost(0);
					state.getSpaceMap().getPlayer().installNewModule(m);
					ShipAbility.showMessage("Thank You!              ", "You have helped numerous people. "
							+ "They will now be able to establish a new colony. In thanks they have given you all their Extra Haul Modules(no points cost)", state.skin);
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
