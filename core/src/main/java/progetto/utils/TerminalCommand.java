package progetto.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import org.fusesource.jansi.Ansi;
import progetto.gameplay.GameScreen;

import java.util.Scanner;

public class TerminalCommand extends Thread {
    private Scanner scanner;
    private GameScreen gameScreen;

    public TerminalCommand(GameScreen gameScreen) {
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
        if(tokens[0].isEmpty()){
            return;
        }
        switch (tokens[0]) {
            case "teleport":
                try {
                    int x = Integer.parseInt(tokens[1]);
                    int y = Integer.parseInt(tokens[2]);
                    printMessage("Teleporting the player...");
                    sleep(500);
                    teleportPlayer(x, y);
                    printMessage("Command executed!");
                }catch (Exception e) {
                    switch (tokens.length) {
                        case 1:
                            printError("Inserire coordinate x e y");
                            break;
                        case 2:
                            printError("Inserire coordinate y");
                            break;
                        default:
                            printError("Sintassi non corretta");
                            break;
                    }
                }
                break;
            case "debug":
                try{
                    String state = tokens[1];
                    switch (state){
                        case "true":
                            printMessage("Enabling debug mode...");
                            sleep(500);
                            DebugWindow.setDebugMode(true);
                            break;
                        case "false":
                            printMessage("Disabling debug mode...");
                            sleep(500);
                            DebugWindow.setDebugMode(false);
                            break;
                        default:
                            printError("Sintassi non corretta");
                    }
                }catch (Exception e) {
                    if (tokens.length == 1) {
                        printError("Inserire lo stato");
                    } else {
                        printError("Sintassi non corretta");
                    }
                }
                break;
            default:
                printError("Comando non trovato: " + command);
        }
    }

    private void teleportPlayer(int x, int y) {
        // Teletrasporta il giocatore
        gameScreen.getEntityManager().player().teleport(new Vector2(x, y));
    }

    public static void printMessage(String message) {
        System.out.println(Ansi.ansi().fg(Ansi.Color.BLUE).a(message).reset());
    }

    public static void printError(String message) {
        Gdx.app.log("Error", Ansi.ansi().fg(Ansi.Color.RED).a(message).reset().toString());
    }

}
