package ch.pearcenet.historygen;

import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

public class Main {

    public static void main(String[] args) {
        AnsiConsole.systemInstall();

        System.out.println("Generating world with seed 69:");
        World world = new World(69L, 100, 50);
        System.out.println(Ansi.ansi().render(world.toString()));

        AnsiConsole.systemUninstall();
    }

}
