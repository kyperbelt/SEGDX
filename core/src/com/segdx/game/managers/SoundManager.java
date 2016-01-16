package com.segdx.game.managers;

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Music.OnCompletionListener;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;

public class SoundManager {
	public static final String BLINDSHIFT = "audio/blindshift.mp3";
	public static final String STARLIGHT = "audio/starlight.mp3";
	public static final String BOARDINGPARTY = "audio/boardingparty.mp3";
	
	public static final String OPTIONPRESSED = "audio/optionpressed2.wav";
	public static final String NODESELECT = "audio/nodeselect.wav";
	public static final String SELECT2 = "audio/select2.wav";
	public static final String WRONGCHOICE = "audio/tradebuy.wav";
	public static final String EXPLOSION = "audio/explosion.wav";
	public static final String DAMAGE = "audio/damage.wav";
	
	private static SoundManager manager;
	
	public static SoundManager get(){
		if(manager==null){
			manager = new SoundManager();
		}
		return manager;
	}
	
	private float volume = .6f;
	private Array<String> music;
	private Array<String> sounds;
	private int currentmusicindex;
	private Music currentmusic;
	private boolean inplaylist;
	private SoundManager(){
		music  = new Array<String>();
		sounds = new Array<String>();
		currentmusicindex = 0;
		music.add(BLINDSHIFT);
		music.add(STARLIGHT);
		
		sounds.add(OPTIONPRESSED);
	}
	
	public void playSound(String sound){
		Assets.manager.get(sound,Sound.class).play(volume*.6f);
	}
	
	public void playMusic(String music){
		inplaylist = false;
		if(currentmusic!=null){
			currentmusic.stop();
		}
		Music m = Assets.manager.get(music,Music.class);
		m.setVolume(getVolume()*.4f);
		currentmusic = m;
		m.play();
		m.setLooping(true);
		
		
	}
	
	public void playMusicList(){
		inplaylist = true;
		if(currentmusic!=null){
			currentmusic.stop();
		}
		Music m = Assets.manager.get(music.get(currentmusicindex),Music.class);
		m.setVolume(volume);
		currentmusic = m;
		m.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(Music musics) {
				if(currentmusicindex<music.size-1)
					currentmusicindex++;
				else
					currentmusicindex = 0;
				playMusicList();
			}
		});
		m.play();
	}
	
	public void skipToNextMusic(){
		if(currentmusic!=null)
			currentmusic.stop();
		if(currentmusicindex<music.size-1)
			currentmusicindex++;
		else
			currentmusicindex = 0;
		playMusicList();
	}

	public float getVolume() {
		return volume;
	}
	
	public boolean isInPlayList(){
		return inplaylist;
	}
	
	public void setVolume(float volume) {
		this.volume = volume;
		if(currentmusic!=null)
			currentmusic.setVolume(volume*.7f);
	}

}
