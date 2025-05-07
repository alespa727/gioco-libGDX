package progetto.lwjgl3;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import progetto.core.App;
import progetto.core.settings.model.ModelImpostazioni;

/** Launcher dell'applicazione */
public class Lwjgl3Launcher {
    public static void main(String[] args) {
        if (StartupHelper.startNewJvmIfRequired()) return; // Classe di LibGDX
        createApplication();
    }

    /**
     * Crea l'applicazione LWJGL
     */
    private static void createApplication() {
        new Lwjgl3Application(new App(), getDefaultConfiguration());
    }

    /**
     * Configurazione di default di LWJGL3
     */
    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Game");
        config.setDecorated(false);
        int width = ModelImpostazioni.getValoriSchermo().getValoreCorrente().width; //1920;
        int height = ModelImpostazioni.getValoriSchermo().getValoreCorrente().height; //1080;
        config.setWindowedMode(width, height);
        config.setForegroundFPS(Lwjgl3ApplicationConfiguration.getDisplayMode().refreshRate-1);
        return config;
    }
}
