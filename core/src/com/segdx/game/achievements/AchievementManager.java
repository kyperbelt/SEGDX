package com.segdx.game.achievements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.segdx.game.managers.Assets;
import com.segdx.game.managers.StateManager;
import com.segdx.game.states.GameState;
import com.segdx.game.tween.TableAccessor;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;

public class AchievementManager {
	
	private static AchievementManager achievements;
	
	public static void init(AchievementManager a){
		achievements = a;
	}
	
	public static AchievementManager get(){
		return achievements;
	}
	
	public static AchievementManager load(Kryo kryo){
		AchievementManager a = null;
		if(Gdx.files.external("achievements.bin").exists()){
			Input input;
			try {
				input = new Input(new FileInputStream(Gdx.files.external("achievements.bin").file())); 
			    a = kryo.readObject(input, AchievementManager.class);
				input.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			a = new AchievementManager();
			System.out.println("wtf");
		}
	   return a;
		
	}
	
	public static void save(Kryo kryo){
		Output output;
		try {
			output = new Output(new FileOutputStream(Gdx.files.external("achievements.bin").file())); 
			kryo.writeObject(output, get());
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	//the achievement manager takes in a stage 
	//to display the achievement on the provided stage
	//achievementNotification(Stage stage)
	
	private ObjectMap<String,Achievement> stat_achievements;
	private ObjectMap<String ,Achievement> game_achievements;
	
	private int totalpoints;
	private int points;
	
	private  AchievementManager(){
		stat_achievements = new ObjectMap<String, Achievement>();
		game_achievements = new ObjectMap<String, Achievement>();
		totalpoints = 0;
		points = 0;
		
		//First times
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("First Kill", "Your first kill! You monster!", 0, 5));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Enter Combat!", "Enter combat for the very first time", 1, 5));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("First Extraction", "Successfully achieve your first extraction",2, 5));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Hard Work Pays Off", "Complete any work for the very first time", 3, 50));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("To Infinity And Beyond", "Travel for the very first time", 4, 5));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Spend Money to Make Money", "Buy somethign at a trading post", 5, 5));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Getting Rid of Some Junk", "Sell something at a trading post", 6, 5));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("REFUELING", "buy fuel at a Rest Stop for the first time", 7, 5));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("A Mans Gotta Eat", "Buy food at a Rest Stop for the first time", 8, 5));
		
		//work
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("A Collectors Desire", "complete work: A Collectors Desire. Sell your fathers ship.", 9, 20));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		
		//most 
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Test", "this is a test achievement", 0, 10));
		
	}
	
	public int getTotalPoints(){
		return totalpoints;
	}
	
	public int getPoints(){
		return points;
	}
	
	public void addAchievement(int type,Achievement a){
		
		totalpoints+=a.points;
		
		switch (type) {
		case Achievement.STAT_ACHIEVEMENT:
			stat_achievements.put(a.name, a);
			break;
		case Achievement.GAMEPLAY_ACHIEMENT:
			game_achievements.put(a.name, a);
			break;
		default:
			break;
		}
	}
	
	public void grantAchievement(String name,int type,Stage stage,TweenManager tm){
		System.out.println(""+getPoints());
		if(getAchievement(name, type)==null||getAchievement(name, type).achieved)
			return;
		Achievement a = getAchievement(name, type);
		this.points+= a.points;
		final Table t = getAchievementTable((GameState)StateManager.get().getState(StateManager.GAME), a);
		t.setPosition(-t.getWidth()+10, stage.getHeight()*.8f);
		stage.addActor(t);
		Tween.to(t, TableAccessor.POSITION_X, 1).target(stage.getWidth()*.01f).repeatYoyo(1, 4).
		setCallback(new TweenCallback() {
			
			@Override
			public void onEvent(int type, BaseTween<?> arg1) {
				if(type == TweenCallback.COMPLETE){
					t.remove();
				}
			}
		}).start(tm);
		a.achieved = true;
	}
	
	private Achievement getAchievement(String name, int type){
		Achievement a = null;
		switch (type) {
		case Achievement.GAMEPLAY_ACHIEMENT:
			if(game_achievements.containsKey(name))
				a = game_achievements.get(name);
			break;
		case Achievement.STAT_ACHIEVEMENT:
			if(stat_achievements.containsKey(name))
				a = stat_achievements.get(name);
			break;
		default:
			break;
		}
		
		return a;
	}
	
	private Table getAchievementTable(GameState state,Achievement a){
		Table achievetable = new Table();
		achievetable.setSize(300, 64);
		achievetable.setBackground(state.defaultbackground);
		achievetable.setColor(Color.DARK_GRAY);
		Image icon = new Image(Assets.manager.get("ui/achievement.png",Texture.class));
		
		Table achieveinfotable = new Table();
		
		Label achievename = new Label(""+a.name, state.skin);
		achievename.setFontScale(.5f);
		Label achievementScore = new Label(""+a.points, state.skin);
		achievementScore.setFontScale(2);
		//achievementScore.setColor(Color.FIREBRICK);
		LabelStyle style = new LabelStyle(achievementScore.getStyle());
		style.background = icon.getDrawable();
		achievementScore.setStyle(style);
		TextTooltip achievedesc = new TextTooltip(""+a.desc, state.skin);
		achievedesc.getActor().setFontScale(.5f);
		achievedesc.setInstant(true);
		
		achievetable.addListener(achievedesc);
		
		achievetable.add(achievementScore).left().expand();
		achievetable.add(achievename).left().expand().fill();
		
		return achievetable;
	}

}
