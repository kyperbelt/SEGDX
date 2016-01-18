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
	public ObjectMap<Integer,String> reftable;
	
	private static Stats stats;
	
	private Stats(){
		stattable = new ObjectMap<String, Integer>();
		reftable = new ObjectMap<Integer, String>();
		
		stattable.put("kills", 0);
		reftable.put(0, "kills");
		stattable.put("pirate kills", 0);
		reftable.put(1, "pirate kills");
		stattable.put("berserker kills", 0);
		reftable.put(2, "berserker kills");
		stattable.put("sba kills", 0);
		reftable.put(3, "sba kills");
		stattable.put("empire kills", 0);
		reftable.put(4, "empire kills");
		stattable.put("rebel kills", 0);
		reftable.put(5, "rebel kills");
		
		stattable.put("most food", 0);
		reftable.put(6, "most food");
		stattable.put("food bought", 0);
		reftable.put(7, "food bought");
		stattable.put("fuel bought", 0);
		reftable.put(8, "fuel bought");
		stattable.put("hull repaired", 0);
		reftable.put(9, "hull repaired");
		
		stattable.put("resources bought", 0);
		reftable.put(10, "resources bought");
		stattable.put("resources sold", 0);
		reftable.put(11, "resources sold");

		stattable.put("most currency", 0);
		reftable.put(12, "most currency");
		stattable.put("currency earned", 0);
		reftable.put(13, "currency earned");
		stattable.put("currency spent", 0);
		reftable.put(14, "currency spent");
		
		stattable.put("most distance traveled", 0);
		reftable.put(15, "most distance traveled");
		stattable.put("total distance traveled", 0);
		reftable.put(16, "total distance traveled");
		
		stattable.put("deaths", 0);
		reftable.put(17, "deaths");
		stattable.put("times pirated", 0);
		reftable.put(18, "times pirated");
		stattable.put("times drafted", 0);
		reftable.put(19, "times drafted");
		stattable.put("most consecutive drafts payed", 0);
		reftable.put(20, "most consecutive drafts payed");
		stattable.put("times draft payed", 0);
		reftable.put(21, "times draft payed");
		
		stattable.put("times fled", 0);
		reftable.put(22, "times fled");
		stattable.put("succesful bribes", 0);
		reftable.put(23, "succesful bribes");
		stattable.put("failed bribes", 0);
		reftable.put(24, "failed bribes");
		
		stattable.put("wreckages searched", 0);
		reftable.put(25, "wreckages searched");
		stattable.put("resources extracted", 0);
		reftable.put(26, "resources extracted");
		
	}
	
	public int getStatValue(int i){
		return stattable.get(reftable.get(i));
	}
	
	public String getStatName(int i){
		return reftable.get(i);
	}
	
	public void increment(String n,int amount){
		if(stattable.containsKey(n)){
			stattable.put(n, stattable.get(n)+amount);
		}else{
			System.out.println(n+" is not a recognized stat.");
		}
	}
	
	public void replaceIfHigher(String n,int value){
		if(stattable.containsKey(n)){
			if(stattable.get(n)<value)
				stattable.put(n, value);
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
			output = new Output(new FileOutputStream(Gdx.files.local("stats.bin").file())); 
			kryo.writeObject(output, get());
			output.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	   
	}
	
	public static Stats load(Kryo kryo){
		Stats stats = null;
		if(Gdx.files.local("stats.bin").exists()){
			Input input;
			try {
				input = new Input(new FileInputStream(Gdx.files.local("stats.bin").file())); 
			    stats = kryo.readObject(input, Stats.class);
				input.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}else{
			stats = new Stats();
			System.out.println("wtf222");
		}
	   return stats;
	}
}
