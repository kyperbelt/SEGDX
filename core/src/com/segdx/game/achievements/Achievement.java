package com.segdx.game.achievements;
/**
 * will add some sort of achievement system
 * to reward the player for different types of playstyles
 * hopefully to add replayability.
 * 
 * maybe integrate with google achievement system if time allows
 * @author Jonathan Camarena -kyperbelt
 *
 */
public class Achievement {
	
	public static final int STAT_ACHIEVEMENT = 0;
	public static final int GAMEPLAY_ACHIEMENT =1;
	
	//should like something like :
	//public Achievement(int id,int points,String name,String descritopn)
	
	//PLANNED ACHIEVEMENTS
	/*
	 * Lost Your booty
	 * 		-lose resources to pirates
	 * Better Safe Than Sorry
	 * 		-Sell your ship in the reststop you spawn at
	 * 		-Live happily ever after with the money it acquired you(40%)
	 * Funny but Unfortunate
	 * 		-Sell your ship in the rest stop you spawn at
	 * 		-Immediately get robbed after leaving the rest stop (60%)
	 * You Can't Eat Money
	 * 		-Lose from lack of food while at a trade post
	 * That Was Farther Than I Thought
	 * 		-Lose by running out of fuel
	 * Didn't Pack A Lunch
	 * 		-Lose By running out of Food while traveling
	 * Uncle Shamm Wants You!
	 * 		-Get drafted by the Empire
	 * Maybe Some Other Time
	 * 		-Pay off the draft
	 * Not In a Million Years
	 * 		-Pay off the draft 10 times
	 * Moving Up!
	 * 		-Accumulate 10000 currency
	 * A True Go Getter
	 * 		-Accumulate 50000 currency
	 * Taste For Blood
	 * 		-Defeat Your first enemy
	 * You Can't Catch Me!
	 * 		-Flee from combat
	 * Cowards Live Forever 
	 * 		-Flee from combat 15 times;
	 * Died A Fool
	 * 		-Be defeated by an enemy while being able to flee
	 * Heart Of Gold
	 * 		-helped someone in need
	 * Cold Blooded
	 * 		-Attack someone in need of help
	 * Executioner
	 * 		-Defeat 50 enemies
	 * Blood Thirsty
	 * 		-Kill 40 innocents
	 * To Infinity And Beyond
	 * 		-Use warp travel
	 *Overwhelming Hunger
	 *		-Accumulate over 100 food
	 *Pacifist
	 *		-survive 50 turns without engaging in combat
	 *Kessle Run In 11 Parsecs
	 *		-Get ship speed up to or over 10  
	 *
	 * 
	 * 
	 */
	
	//name of this achieve
	public String name;
	//desc of this achieve
	public String desc;
	//id of this achieve
	public int id;
	//points this achieve is worth
	public int points;
	//is this achieve complete
	public boolean achieved;
	
	public Achievement(){
		
	}
	
	public Achievement(String name,String desc,int id,int points){
		this.name = name;
		this.desc = desc;
		this.id = id;
		this.points = points;
	}

}
