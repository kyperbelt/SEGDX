package com.segdx.game.achievements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextTooltip;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
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
		if(Gdx.files.local("achievements.bin").exists()){
			Input input;
			try {
				input = new Input(new FileInputStream(Gdx.files.local("achievements.bin").file())); 
			    a = kryo.readObject(input, AchievementManager.class);
				input.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			a = new AchievementManager();
		}
	   return a;
		
	}
	
	public static void save(Kryo kryo){
		Output output;
		try {
			output = new Output(new FileOutputStream(Gdx.files.local("achievements.bin").file())); 
			kryo.writeObject(output, get());
			output.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	
	public Achievement getAchievementValue(int i){
		return game_achievements.get(stat_achievements.get(i));
	}
	
	public String getAchievementName(int i){
		return stat_achievements.get(i);
	}
	
	//the achievement manager takes in a stage 
	//to display the achievement on the provided stage
	//achievementNotification(Stage stage)
	
	private ObjectMap<Integer, String> stat_achievements;
	public ObjectMap<String, Achievement> game_achievements;
	
	private int totalpoints;
	private int points;
	
	
	private  AchievementManager(){
		stat_achievements = new ObjectMap<Integer, String>();
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
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("A New Tomorrow", "complete work: A New Tomorrow", 10, 20));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Faster than Light", "complete work: Faster than Light", 11, 20));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Scanner Research!", "complete work: Scanner Research", 12, 20));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("A Way To Peace", "complete work: A Way To Peace", 13, 20));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Your Legend.... ended", "You died bro", 39, 20));
		//most 
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Overwhelming Hunger", "Accumulate over 100 food at any given time",14, 15));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Faster Than The Falcon", "Get ship speed past 10", 15, 15));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Long Live You", "live happy after selling your fathers ship", 16, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Well..Thats Karma", "Immediately get robbed after selling your father ship", 17, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("You Cant Eat Money", "Die from lack of food while at a trade post", 18, 15));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("That Was Farther Than I Thought", "lose by running out of fuel", 18, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Forgot To Pack A Lunch", "lose by running out of food while traveling", 19, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Uncle Shamm Wants You!", "Empire drafts you", 20, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Maybe Some Other Time", "Pay off the draft", 21, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Not In A Million Years", "Pay off the draft 10 times in a single game", 22, 20));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Moving Up", "Accumulate $10000 in a single game", 23, 20));
		
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Go Getter", "Accumulate $50000 in a single game", 26, 40));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("You Cant Catch Me!", "succesfuly flee from combat", 27, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Cowards Live Forever", "flee from combat 30 times", 28, 25));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Executioner", "kill 20 enemies", 23, 30));
		
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Lets Get Some Booty", "Obtain the Raider Ship", 24, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("For The Empire!", "Obtain the Sentinel Ship", 25, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Too Fast To Care", "Obtain the Interceptor Ship", 26, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Thick Hull", "Obtain the Marauder Ship", 27, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Ride Deaths Steed", "Obtain the Guillotine Ship", 28, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Im Just Cruising", "Obtain the Cruiser Ship", 29, 10));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("Delivery Guy","Obtain the Carrier Ship", 30, 15));
		addAchievement(Achievement.GAMEPLAY_ACHIEMENT, new Achievement("The Big Honcho", "Obtain the Enterprise Ship", 31, 30));
		
		
	}
	
	public int getTotalPoints(){
		return totalpoints;
	}
	
	public int getPoints(){
		return points;
	}
	

	
	public void addAchievement(int type,Achievement a){
		
			totalpoints+=a.points;
		
			stat_achievements.put(stat_achievements.size, a.name);
			game_achievements.put(a.name, a);
	}
	
	public void grantAchievement(String n){
		this.grantAchievement(n, Achievement.GAMEPLAY_ACHIEMENT, StateManager.get().getGameState().uistage, StateManager.get().getGameState().tm);
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
			a = game_achievements.get(name);
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
	
	public Table getAchievementTableDos(Skin skin,Achievement a){
		Table achievetable = new Table();
		achievetable.setSize(300, 64);
		Drawable background = new Button(skin).getBackground();
		achievetable.setBackground(background);
		achievetable.setColor(Color.DARK_GRAY);
		Image icon = new Image(Assets.manager.get("ui/achievement.png",Texture.class));
		
		Table achieveinfotable = new Table();
		
		Label achievename = new Label(""+a.name, skin);
		achievename.setColor(Color.GOLD);
		achievename.setFontScale(.5f);
		Label achievementScore = new Label(""+a.points, skin);
		achievementScore.setFontScale(2);
		if(!a.achieved)
			achievementScore.setColor(Color.DARK_GRAY);
		LabelStyle style = new LabelStyle(achievementScore.getStyle());
		style.background = icon.getDrawable();
		achievementScore.setStyle(style);
		Label achievedesc = new Label(""+a.desc,skin);
		achievedesc.setFontScale(.4f);
		achievedesc.setWrap(true);
		
		Table aaa = new Table();
		aaa.add(achievename).left().expand().fillX().row();
		aaa.add(achievedesc).left().expand().fill().row();
		achievetable.add(achievementScore).left().expand();
		achievetable.add(aaa).left().expand().fill();
		
		return achievetable;
	}

}
