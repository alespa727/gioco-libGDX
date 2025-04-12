package progetto.utils;

import com.badlogic.gdx.math.Vector2;
import progetto.gameplay.GameScreen;
import progetto.menu.DebugWindow;

import java.util.Scanner;

public class TerminalCommandListener extends Thread {
    private Scanner scanner;
    private GameScreen gameScreen;

    public TerminalCommandListener(GameScreen gameScreen) {
        this.gameScreen = gameScreen;
        scanner = new Scanner(System.in);
    }
    public void run() {
        while (true) {
            String input = scanner.nextLine();  // Legge l'input dal terminale
            processCommand(input);  // Elabora il comando
        }
    }


    private void processCommand(String command) {
        // Elenco dei comandi e delle azioni associate
        String[] tokens = command.split(" ");
        switch (tokens[0]) {
            case "teleport":
                try {
                    int x = Integer.parseInt(tokens[1]);
                    int y = Integer.parseInt(tokens[2]);
                    teleportPlayer(x, y);
                    System.out.println("Teleporting the player...");
                }catch (Exception e) {
                    switch (tokens.length) {
                        case 1:
                            System.err.println("Inserire coordinate x e y");
                            break;
                        case 2:
                            System.err.println("Inserire coordinate y");
                            break;
                        default:
                            System.err.println("Comando non trovato");
                            break;
                    }
                }
                break;
            case "debug":
                try{
                    String line = tokens[1];
                    setDebugMode(line);
                }catch (Exception e) {
                    if (tokens.length == 1) {
                        System.err.println("Inserire lo stato");
                    } else {
                        System.err.println("Comando non trovato");
                    }
                }
                break;
            default:
                System.out.println("Comando non trovato: " + command);
        }
    }

    private void teleportPlayer(int x, int y) {
        // Teletrasporta il giocatore
        gameScreen.getEntityManager().player().teleport(new Vector2(x, y));
    }

    private void setDebugMode(String state) {
        switch (state){
            case "true":
                DebugWindow.setDebugMode(true);

                break;
            case "false":
                DebugWindow.setDebugMode(false);
                System.out.println("Debug mode is now disabled.");
                break;
            default:
                System.err.println("Inserire uno stato valido");
        }

        // Aggiungi eventuali operazioni legate al debug
    }

}
