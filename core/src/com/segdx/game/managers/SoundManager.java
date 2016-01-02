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
	
	private static SoundManager manager;
	
	public static SoundManager get(){
		if(manager==null){
			manager = new SoundManager();
		}
		return manager;
	}
	
	private float volume = .01f;
	private Array<String> music;
	private Array<String> sounds;
	private int currentmusicindex;
	private Music currentmusic;
	private SoundManager(){
		music  = new Array<String>();
		sounds = new Array<String>();
		currentmusicindex = 0;
		music.add(BLINDSHIFT);
		music.add(STARLIGHT);
		
		sounds.add(OPTIONPRESSED);
	}
	
	public void playSound(String sound){
		Assets.manager.get(sound,Sound.class).play(volume);
	}
	
	public void playMusic(String music){
		if(currentmusic!=null){
			currentmusic.stop();
		}
		Music m = Assets.manager.get(music,Music.class);
		m.setVolume(getVolume());
		currentmusic = m;
		m.setLooping(true);
		m.play();
		
	}
	
	public void playMusicList(){
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

	public void setVolume(float volume) {
		this.volume = volume;
	}

}
