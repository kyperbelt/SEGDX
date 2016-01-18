package com.segdx.game.managers;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.segdx.game.entity.CycleTimer.CycleTask;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.achievements.Stats;
import com.segdx.game.entity.GameOver;
import com.segdx.game.entity.Player;
import com.segdx.game.states.GameState;

public class Draft {
	
	private float currentcost;
	private int diff;
	private int timespayed;
	
	public Draft(int difficulty) {
		timespayed = 0;
		diff = difficulty;
		currentcost = 100+((difficulty*100)*.5f);
		
		StateManager.get().getGameState().getSpaceMap().getTimer().addCycleTask(new CycleTask() {
			
			@Override
			public void onCycle() {
				draft();
			}
		}).repeat().setRepeatEvery(5);
		// TODO Auto-generated constructor stub
	}
	
	public void draft(){
		timespayed++;
		Stats.get().replaceIfHigher("most consecutive drafts payed", timespayed);
		showDraft("You are Being DRAFTED!","The Arboros Empire has sent their Recruiters to fetch up anyone in the vicinity. Im sure if you pay them enough they will look the other way."
				+ "", StateManager.get().getGameState().skin).show(
				StateManager.get().getGameState().uistage);
		
	}
	
	public  Dialog showDraft(String title, String content, Skin skin) {
		final GameState state = StateManager.get().getGameState();
		state.getSpaceMap().getTimer().setPaused(true);
		final Player p = StateManager.get().getGameState().getSpaceMap().getPlayer();
		Dialog d = new Dialog(title, skin){
			@Override
			protected void result(Object object) {
				boolean option = (Boolean)object;
				if(option){
					p.setCurrency(p.getCurrency()-currentcost);
					setCurrentcost(getCurrentcost()*2);
					state.getSpaceMap().getTimer().setPaused(false);
					Stats.get().increment("times draft payed", 1);
					timespayed++;
					Stats.get().replaceIfHigher("most consecutive drafts payed", timespayed);
					AchievementManager.get().grantAchievement("Maybe Some Other Time");
					if(timespayed>=10)
						AchievementManager.get().grantAchievement("Not In A Million Years");
				}else{
					GameOver.setCurrentGameOver(new GameOver(GameOver.GOT_DRAFTED, p, diff, state.size));
					Assets.loadBlock(Assets.GAMEOVER_ASSETS);
					StateManager.get().changeState(StateManager.LOAD);
					Stats.get().increment("times drafted", 1);
				}
			}
		};
		d.setSize(200, 200);
		d.getTitleLabel().setFontScale(.7f);
		Label l = new Label(content, skin);
		l.setFontScale(.5f);
		l.setWrap(true);
		d.getContentTable().add(l).center().expand().fill();
		d.button("get drafted",false);
		if(p.getCurrency()>currentcost)
			d.button("Pay $"+currentcost, true);
		((TextButton) d.getButtonTable().getChildren().get(0)).getLabel().setFontScale(.7f);

		return d;
	}

	public float getCurrentcost() {
		return currentcost;
	}

	public void setCurrentcost(float currentcost) {
		this.currentcost = currentcost;
	}
	
	

}
