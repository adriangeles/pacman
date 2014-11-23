/**
 * Proyecto fin de carrera: Android Pacman
 * <p/>
 * Autor: Adrián Ángeles Ramón
 * Universidad: Politécnica de Madrid
 * Centro: Facultad de Informatica
 * Matricula: 980092
 * <p/>
 */
package es.upm.fi.f980092.tfc.gameengine.sound;

import java.util.HashMap;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.preference.PreferenceManager;
import android.util.Log;
import es.upm.fi.f980092.tfc.pacman.activities.Preferences;

/**
 * El SoundEngine es responsable de gestionar los efectos de un juego y las melodias
 * Exiten dos tipos de musica, las melodias que se ejecutan basicamente 1 única vez y 
 * solamente una simultaneamente y los efectos especiales que se ejecutan cada vez que
 * se realiza una acción en el juego, por ejemplo al chocar dos objetos.
 * 
 */
public class SoundEngine  {

	private static final String TAG ="TfcGameEngine";
	private static final String TAG2 = TAG + "::SoundEngine - ";
		
	private MediaPlayer mp;
	private SoundPool sp;
	private Context context;
	private boolean active;
	
	public void setActive(boolean active) {
		this.active = active;
	}

	private static SoundEngine instance;
	private AudioManager audiomanager;
	private HashMap<Integer, SoundData> soundsRef;
	private int loading = 0;
	
	private SoundEngine(Context context, int channels) {
		instance = this;		
		this.context = context;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);	
		active = prefs.getBoolean(Preferences.SOUND, true);
		soundsRef = new HashMap<Integer, SoundData>();
		initSP(channels);
	}

	public static SoundEngine getInstance() {
		if (instance == null) {
			throw new IllegalAccessError("You must init the class with the method getInstance(Context context)");							
		}
		return instance;
	} 
	
	public static SoundEngine init(Context context, int channels) {
		if (instance == null) {
			instance = new SoundEngine(context,channels);
		}
		return instance;
	}
	
	
	// Carga un efecto especial
	public void addSoundFx(int soundID){
		addSoundInSP(soundID);		
	}

	
	public void play(int soundId) {
		if (active) {
			SoundData data = soundsRef.get(soundId);
			playInSP(data.internalId, false);
		}
	}
	
	public void play(String soundFile) {
		if (active) {
			playInMP(soundFile, true);
		}
	}
	
	public void pause() {
		if (active)
			mp.pause();
	}
	
	public void resume() {
		if (active)
			mp.start();
	}
	
	public void stop() {
		if (active) {
			mp.stop();
			for ( SoundData data : soundsRef.values()) {
				if ( data.streamId != null) {
					sp.stop(data.streamId);
				}
			}
		}
	}

	public void addListener(OnLoadCompleteListener listener) {
		sp.setOnLoadCompleteListener(listener);		
	}
	
	// Sound pool
	
	private void initSP(int channels) {
		audiomanager =  (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
		sp = new SoundPool(channels, AudioManager.STREAM_MUSIC, 0);
	}
	
	private void addSoundInSP(int soundID)
	{
		int internalId = sp.load(context, soundID, 1);
		SoundData data = new SoundData();
		data.internalId = internalId;
		soundsRef.put(soundID, data);	
		loading++;
	}
	
	private void playInSP(int id, boolean looping) {	
		float streamVolume = audiomanager.getStreamVolume(AudioManager.STREAM_MUSIC);
		streamVolume = streamVolume / audiomanager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);		
		int streamId = sp.play(id, streamVolume, streamVolume, 1, (looping) ? -1 : 0, 1f);
		Log.i(TAG,TAG2 + "Stream: " + streamId);
	}
		
	// Media player
	
	private void initMP() {
		if (mp != null) {
			stopMP();
		}
		mp = new MediaPlayer();
		mp.setAudioStreamType(AudioManager.STREAM_MUSIC);
	}
	
	private void playInMP(String file, boolean looping) {
		try {
			initMP();
			mp.setLooping(looping);
			mp.setDataSource(file);
			mp.prepare();
			mp.start();
		} catch (Exception e) {
			Log.e(TAG,TAG2 + e.getMessage());
		}
	}

	private void stopMP() {
		mp.stop();
		mp = null;
	}
	
	// Estructuras de datos internas
	private class SoundData {
		Integer internalId = null;
		Integer streamId = null;
		
	}
}
