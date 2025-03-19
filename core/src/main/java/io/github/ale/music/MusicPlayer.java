package io.github.ale.music;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.ArrayMap;

public class MusicPlayer {
    private final ArrayMap<String, Music> playlist;

    public MusicPlayer(String path) {
        playlist = new ArrayMap<>(5);
        playlist.put(path, Gdx.audio.newMusic(Gdx.files.internal(path)));
    }

    public void add(String path){
        playlist.put(path, Gdx.audio.newMusic(Gdx.files.internal(path)));
    }

    public void playfromthestart(){
        for (int i = 0; i < playlist.size; i++) {
            if(playlist.get(playlist.getKeyAt(i)).isPlaying()) playlist.get(playlist.getKeyAt(i)).setPosition(0f);
        }
    }

    public void play(String path){
        playlist.get(path).play();
    }

    public void play(int index){
        playlist.getValueAt(index).play();
    }

    public void pause(String path){
        playlist.get(path).pause();
    }

    public void stop(){
        for (int i = 0; i < playlist.size; i++) {
            playlist.getValueAt(i).pause();
        }
    }

    public void setLooping(String path, boolean looping){
        playlist.get(path).setLooping(looping);
    }

    public void setLooping(int index, boolean looping){
        playlist.getValueAt(index).setLooping(looping);
    }

    public void setVolume(float volume){
        for (int i = 0; i < playlist.size; i++) {
            playlist.getValueAt(i).setVolume(volume);
        }
    }

    public void empty(){
        for (int i = 0; i < playlist.size; i++) {
            playlist.getValueAt(i).dispose();
        }
        playlist.clear(5);
    }


}