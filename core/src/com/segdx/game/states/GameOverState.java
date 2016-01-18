package com.segdx.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.segdx.game.SEGDX;
import com.segdx.game.achievements.Achievement;
import com.segdx.game.achievements.AchievementManager;
import com.segdx.game.entity.GameOver;
import com.segdx.game.entity.SpaceNode;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.StateManager;

import aurelienribon.tweenengine.TweenManager;

public class GameOverState implements Screen{
	
	private Stage gostage;
	private Skin skin;
	private Sprite background;
	private TweenManager tm;

	@Override
	public void show() {

		
		tm = new TweenManager();
		gostage = new Stage(new ScreenViewport());
		gostage.setDebugAll(SEGDX.DEBUG);
		skin = Assets.manager.get("ui/uiskin.json",Skin.class);
		background = new Sprite(Assets.manager.get("map/godsandidols3.png",Texture.class));
		
		Gdx.input.setInputProcessor(gostage);
		Table holder = new Table();
		holder.setFillParent(true);
		Label gameoverlabel = new Label("Game Over", skin);
		gameoverlabel.setFontScale(2);
		
		Label Description;
		
		TextButton mainmenu = new TextButton("Main Menu", skin);
		mainmenu.setScale(2);
		mainmenu.addListener(new ClickListener(){
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Assets.loadBlock(Assets.MENU_ASSETS);
				StateManager.get().changeState(StateManager.LOAD);
			}
		});
		
		Label desc = new Label(getGameOverDesc(), skin);
		desc.setWrap(true);
		
		holder.add(gameoverlabel).center().row();
		holder.add(desc).center().expand().fill().row();
		holder.add(mainmenu);
		
		gostage.addActor(holder);
		
		((GameState)StateManager.get().getState(StateManager.GAME)).disposeMap();
	}
	
	public String getGameOverDesc(){
		String s = "";
		GameOver o = GameOver.getGameOver();
		
		switch (o.getType()) {
		case GameOver.GOT_DRAFTED:
			s+="You got drafted by the empire... i guess there is worst things than Servitude.";
			AchievementManager.get().grantAchievement("Uncle Shamm Wants You!", Achievement.GAMEPLAY_ACHIEMENT	, this.gostage, tm);
			
			break;
		case GameOver.JOINED_THE_SBA:
			
			break;
		case GameOver.OUT_OF_FOOD:
			s+="You ran out of food";
			if(o.getPlayer().isTraveling()){
				s+=". Maybe next time you should plan better before enbarking on a long distance journey ";
				AchievementManager.get().grantAchievement("Forgot To Pack A Lunch", Achievement.GAMEPLAY_ACHIEMENT	, this.gostage, tm);
			}else if(o.getPlayer().getCurrentNode().getNodeType()==SpaceNode.TRADE){
				s+=" while you were at a Trade Post. ";
				AchievementManager.get().grantAchievement("You Cant Eat Money", Achievement.GAMEPLAY_ACHIEMENT	, this.gostage, tm);
				
			}
			s+="Remember to keep an eye on your food.";
			
			break;
		case GameOver.OUT_OF_FUEL:
			s+="Maybe you couldnt see the distance displayed on the bottom right of the screen next to The travel button. It happens....";
			AchievementManager.get().grantAchievement("That Was Farther Than I Thought", Achievement.GAMEPLAY_ACHIEMENT	, this.gostage, tm);
			break;
		case GameOver.OUT_OF_HP:
			s+=" Your ship was destroyed. Now your body roams infinitate space forever...until it reaches a black hole or something.";
			AchievementManager.get().grantAchievement("Your Legend.... ended", Achievement.GAMEPLAY_ACHIEMENT	, this.gostage, tm);
			break;
		case GameOver.SOLD_FATHERS_SHIP:
			s+="You sold your dead fathers ship...";
			if(MathUtils.randomBoolean(.6f)){
				s+="You used the money to settle down and live happily ever after, So there is that i geuss.";
				AchievementManager.get().grantAchievement("Long Live You", Achievement.GAMEPLAY_ACHIEMENT	, this.gostage, tm);
			}else{
				s+="Strange man see you waiting on the outskirts of the Rest Stop. They then proceed to rob you of everything."
						+ "including your underwear...Brown Stains!";
				AchievementManager.get().grantAchievement("Well..Thats Karma", Achievement.GAMEPLAY_ACHIEMENT	, this.gostage, tm);
			}
			break;

		default:
			break;
		}
		
		return s;
	}

	@Override
	public void render(float delta) {
		SEGDX.clear();
		
		gostage.getBatch().begin();
		background.draw(gostage.getBatch());
		gostage.getBatch().end();
		
		gostage.draw();
		gostage.act(delta);
		gostage.getCamera().update();
		
		tm.update(delta);
	}

	@Override
	public void resize(int width, int height) {
	}

	@Override
	public void pause() {
		tm.pause();
	}

	@Override
	public void resume() {
		tm.resume();
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
	}

}
