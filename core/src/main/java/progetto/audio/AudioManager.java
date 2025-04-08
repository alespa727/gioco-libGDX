package progetto.audio;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;

public class AudioManager {

    Array<Music> playlist;
    Array<Sound> sounds;

    public AudioManager() {
        playlist = new Array<>();
        sounds = new Array<>();
    }

    public void addSound(String path){
        sounds.add(Gdx.audio.newSound(Gdx.files.internal(path)));
    }

    public void addMusic(String path){
        playlist.add(Gdx.audio.newMusic(Gdx.files.internal(path)));
    }

    public void playMusic(int index){
        playlist.get(index).play();
    }

    public void playSound(int index){
        sounds.get(index).play();
    }

    public void stopMusic(int index){
        playlist.get(index).stop();
    }

}
