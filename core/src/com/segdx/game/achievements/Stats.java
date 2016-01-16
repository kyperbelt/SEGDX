package com.segdx.game.achievements;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ObjectMap;
import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class Stats{
	//TODO: make this class serializable with kryo

	
	public ObjectMap<String, Integer> stattable;
	
	private static Stats stats;
	
	private Stats(){
		stattable = new ObjectMap<String, Integer>();
		
		stattable.put("kills", 0);
		stattable.put("pirate kills", 0);
		stattable.put("berserker kills", 0);
		stattable.put("sba kills", 0);
		stattable.put("empire kills", 0);
		stattable.put("rebel kills", 0);
		
		stattable.put("most food", 0);
		stattable.put("food bought", 0);
		stattable.put("fuel bought", 0);
		stattable.put("hull repaired", 0);
		
		stattable.put("resources bought", 0);
		stattable.put("resources sold", 0);

		stattable.put("most currency", 0);
		stattable.put("currency earned", 0);
		stattable.put("currency spent", 0);
		
		stattable.put("most distance traveled", 0);
		stattable.put("total distance traveled", 0);
		
		stattable.put("deaths", 0);
		stattable.put("times pirated", 0);
		stattable.put("times drafted", 0);
		stattable.put("times draft payed", 0);
		
		stattable.put("times fled", 0);
		stattable.put("succesful bribes", 0);
		stattable.put("failed bribes", 0);
		
		stattable.put("wreckages searched", 0);
		stattable.put("resources extracted", 0);
		
	}
	
	public void increment(String n,int amount){
		if(stattable.containsKey(n)){
			stattable.put(n, stattable.get(n)+amount);
		}else{
			System.out.println(n+" is not a recognized stat.");
		}
	}
	
	public static void init(Stats statts){
		stats = statts;
	}
	
	public static Stats get(){
		return stats;
	}
	
	public static void save(Kryo kryo){
		Output output;
		try {
			output = new Output(new FileOutputStream(Gdx.files.external("stats.bin").file())); 
			kryo.writeObject(output, get());
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}
	
	public static Stats load(Kryo kryo){
		Stats stats = null;
		if(Gdx.files.external("stats.bin").exists()){
			Input input;
			try {
				input = new Input(new FileInputStream(Gdx.files.external("stats.bin").file())); 
			    stats = kryo.readObject(input, Stats.class);
				input.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			stats = new Stats();
			System.out.println("wtf222");
		}
	   return stats;
	}
}
