package com.segdx.game.entity;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.ObjectMap;

public class ResourceStash {
	
	
	
	//storing all my resources here
	
	public  static Resource KNIPTORYTE = new Resource(0, "map/kniptoryte.png","Kniptoryte", "A simple ore used in the construction of outposts and mining colonies,"
			+ "severe alergic reactions have been known to occur.", 80, 1, 30);
	public static Resource DRIDIUM = new Resource(1, "map/dridium.png","Dridium", "Refined and used to create fuel for inter-stellar travel",
			40, 2.5f, 83);
	public static Resource LATTERIUM = new Resource(2,"map/latterium.png", "Latterium", "An additive in most superdense alloys.Very useful"
			+ " in the construction of spacecraft", 50, 2, 77);
	public static Resource NAQUIDRA = new Resource(3,"map/naquidra.png", "Naquidra", "A radioactive isotope with military and scientific applications.", 
			20, 3, 105);
	
	public static Resource SALVAGE = new Resource(10, "map/latterium.png","Salvage", "A mixture of scraps and alloys recovered "
			+ "and salvaged.", 60, .5f, 50);
	
	public static Resource get(Resource resource){
		return resource.clone();
	}
	
	public static final ObjectMap<Integer, Resource> RESOURCES = new ObjectMap<Integer, Resource>();
	public static int Resourcerarity = 0;
	public static void init(){
		
		RESOURCES.put(NAQUIDRA.getId(), NAQUIDRA);
		RESOURCES.put(DRIDIUM.getId(), DRIDIUM);
		RESOURCES.put(LATTERIUM.getId(), LATTERIUM);
		RESOURCES.put(KNIPTORYTE.getId(), KNIPTORYTE);
		RESOURCES.put(SALVAGE.getId(), SALVAGE);
		
		for (int i = 0; i < RESOURCES.size; i++) {
			Resourcerarity+=RESOURCES.get(RESOURCE_IDS[i]).getRarity();
		}
		
	}
	
	public static final int[] RESOURCE_IDS = new int[]{
			KNIPTORYTE.getId(),
			DRIDIUM.getId(),
			LATTERIUM.getId(),SALVAGE.getId(),
			NAQUIDRA.getId()
			
			
	};
	
	public static int randomID(){
		int number =MathUtils.random(0, Resourcerarity);
		int id = -1;
		int last_num = 0;
		for (int i = 0; i < RESOURCES.size; i++) {
			if(number>=last_num&&number<=last_num+RESOURCES.get(RESOURCE_IDS[i]).getRarity()){
				id = RESOURCES.get(RESOURCE_IDS[i]).getId();
				break;
			}
			else
				last_num+=RESOURCES.get(RESOURCE_IDS[i]).getRarity();
		}
		if(id==-1)
			id = randomID();
		
		return id;
	}

}
