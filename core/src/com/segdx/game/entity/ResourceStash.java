package com.segdx.game.entity;

public class ResourceStash {
	//storing all my resources here
	
	public static Resource KNIPTORYTE = new Resource(0, "map/kniptoryte.png","Kniptoryte", "A simple ore used in the construction of outposts and mining colonies,"
			+ "severe alergic reactions have been known to occur.", 60f, 1, 30);
	public static Resource DRIDIUM = new Resource(1, "map/dridium.png","Dridium", "Refined and used to create fuel for inter-stellar travel",
			35, 2.5f, 83);
	public static Resource LATTERIUM = new Resource(2,"map/latterium.png", "Latterium", "An additive in most superdense alloys.Very useful"
			+ " in the construction of spacecraft", 45, 2, 77);
	public static Resource NAQUIDRA = new Resource(3,"map/naquidra.png", "Naquidra", "A radioactive isotope with military and scientific applications.", 
			15, 3, 105);
	
	public static Resource SALVAGE = new Resource(10, "map/latterium.png","Salvage", "A mixture of scraps and alloys recovered "
			+ "and salvaged.", 90, .5f, 50);
	
	public static Resource get(Resource resource){
		return resource.clone();
	}
	

}
