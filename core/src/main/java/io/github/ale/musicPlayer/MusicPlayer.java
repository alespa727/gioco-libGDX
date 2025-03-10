package io.github.ale.musicPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.GdxRuntimeException;

public class MusicPlayer {
    private FileHandle audioSong;
    private String songPath[];

    public MusicPlayer(int index) {
        songPath = new String[] 
        {
                "assets/mymusic.mp3",
        };
        Init(index);
    }

    private void Init (int index)
    {
        audioSong = new FileHandle(songPath[index]);
        try {
            Gdx.audio.newMusic(audioSong).play();
            System.out.println("Music started");
        } catch (GdxRuntimeException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        Gdx.audio.newMusic(audioSong).stop();
        audioSong = null;
        Gdx.app.log("MusicPlayer", "Music stopped");
    }
}