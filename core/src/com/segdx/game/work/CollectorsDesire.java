package com.segdx.game.work;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.segdx.game.achievements.Achievement;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.entity.CycleTimer.TimedTask;
import com.segdx.game.entity.GameOver;
import com.segdx.game.entity.Player;
import com.segdx.game.entity.ships.StarterShip;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;

public class CollectorsDesire extends Work{

	public CollectorsDesire() {
		this.setName("A Collectors Desire");
		this.setDesc("A colelctor has approached you and asked you about your ship. He mentions"
				+ "that he has been looking for it for a very long time and offers you a small fortune.");
	}
	@Override
	public boolean canComplete(Player p) {
		if(p.getShip() instanceof StarterShip)
			return true;
		return false;
	}

	@Override
	public Table getWorkTable(final GameState state) {
		Table t = new Table();
		Table infotable = new Table();
		Label name  = new Label(""+this.getName(), state.skin);
		name.setFontScale(.8f);
		Label desc = new Label(""+this.getDesc()+"\nRequirements:\n-Your Fathers Ship", state.skin);
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
					state.getSpaceMap().getTimer().setPaused(true);
					AchievementManager.get().grantAchievement(getName(), Achievement.GAMEPLAY_ACHIEMENT, state.uistage, state.tm);
					state.getSpaceMap().getTimer().addTimedTask(new TimedTask() {
						
						@Override
						public void onExecute() {
							GameOver.setCurrentGameOver(new GameOver(GameOver.SOLD_FATHERS_SHIP,state.getSpaceMap().getPlayer(),state.difficulty,state.size));
							Assets.loadBlock(Assets.GAMEOVER_ASSETS);
							StateManager.get().changeState(StateManager.LOAD);
						}
					}).repeat(1).setSleep(5);
				}
			});
		}
		optionstable.add(complete).right().expand();
		
		t.add(infotable).left().expand().fill().row();
		t.add(optionstable).left().expand().fillX();
		
		return t;
	}

}
