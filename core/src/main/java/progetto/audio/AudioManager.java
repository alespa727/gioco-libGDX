package progetto.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class AudioManager {

    private static final Array<Music> playlist = new Array<>();
    private static final Array<Sound> sounds = new Array<>();

    public AudioManager() {

    }

    public static void addSound(String path){
        sounds.add(Gdx.audio.newSound(Gdx.files.internal(path)));
    }

    public static void addMusic(String path){
        playlist.add(Gdx.audio.newMusic(Gdx.files.internal(path)));
    }

    public static void playMusic(int index){
        playlist.get(index).play();
    }

    public static void playSound(int index){
        sounds.get(index).play();
    }

    public static void stopMusic(int index){
        playlist.get(index).stop();
    }

}
