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
import com.segdx.game.entity.GameOver;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ResourceStash;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.StateManager;
import com.segdx.game.modules.EngineBoosters;
import com.segdx.game.modules.ShipModule;
import com.segdx.game.states.GameState;

public class FasterThanLight extends Work {

	int cost = 2000;
	int naquidra = 10;
	public FasterThanLight() {
		this.setName("Faster than Light");
		this.setDesc(" A Space travel science guy is braggin about having developed a new type of engine made out of Naquidra. He says that if you bring him some "
				+ "he will give you his time. For a small fee.");
	}
	@Override
	public boolean canComplete(Player p) {
		if(p.containsResource(ResourceStash.NAQUIDRA, naquidra)&&p.getCurrency()>=cost)
			return true;
		return false;
	}

	@Override
	public Table getWorkTable(final GameState state) {
		Table t = new Table();
		Table infotable = new Table();
		Label name  = new Label(""+this.getName(), state.skin);
		name.setFontScale(.8f);
		Label desc = new Label(""+this.getDesc()+"\nRequirements:\n-"+naquidra+" Naquidra\n-"
				+"$"+cost, state.skin);
		desc.setFontScale(.5f);
		desc.setWrap(true);
		
		ScrollPane descscroll = new ScrollPane(desc, state.skin);
		descscroll.setColor(Color.DARK_GRAY);
		
		infotable.add(name).left().expand().fillX().row();
		infotable.add(descscroll).left().expand().fill().row();
		
		Table optionstable = new Table();
		TextButton complete = new TextButton("Complete", state.skin);
		if(!canComplete(state.getSpaceMap().getPlayer())){
			complete.setDisabled(true);
			complete.setColor(Color.FIREBRICK);
		}else{
			complete.addListener(new ClickListener(){
				public void clicked(InputEvent event, float x, float y) {
					grantAchievement(state);
					for (int i = 0; i < naquidra; i++) {
						state.getSpaceMap().getPlayer().removeResource(ResourceStash.NAQUIDRA.getId());
					}
					Player p = state.getSpaceMap().getPlayer();
					p.setCurrency(p.getCurrency()-cost);
					ShipModule m = new EngineBoosters(10);
					m.setCost(0);
					((EngineBoosters)m).setEconLoss(.01f);
					p.installNewModule(m);
				}
			});
		}
		optionstable.add(complete).right().expand();
		
		t.add(infotable).left().expand().fill().row();
		t.add(optionstable).left().expand().fillX();
		
		return t;
	}

}
