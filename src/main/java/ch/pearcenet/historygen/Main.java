package ch.pearcenet.historygen;

import ch.pearcenet.historygen.world.Nation;
import ch.pearcenet.historygen.world.World;
import org.fusesource.jansi.Ansi;
import org.fusesource.jansi.AnsiConsole;

import java.util.Collections;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        Scanner input = new Scanner(System.in);

        // Get seed
        System.out.print("Enter world seed: ");
        int seed = Integer.parseInt(input.nextLine());

        World world = new World(seed);

        System.out.println("\nThe year of our lord " + world.getYear() + "\nGeographic Map:");
        System.out.println(world.getGeoMap());

        System.out.println("\nFertility Map:");
        System.out.println(world.getFerMap());

        System.out.println("\nPolitical Map:");
        System.out.println(world.getPolMap());

        System.out.println("\nList of generated nations:\n---------------------------");
        for (Nation n: world.getNations()) {
            System.out.println("" +
                    "The nation of '" + n.getName() +
                    "' which speaks '" + n.getLanguage().getName() +
                    "' is coloured on the map like so: " + n.getAnsiDisplay());
        }

        while (true) {
            System.out.println("Press enter to skip 5 years: ");
            if ("exit".equals(input.nextLine())) break;
            System.out.println(Ansi.ansi().eraseScreen().cursor(0, 0));

            for (int i=0;i<5;i++) world.nextYear();
            System.out.println("\nThe year of our lord " + world.getYear() + "\nPolitical Map:");
            System.out.println(world.getPolMap());
        }

        System.out.println("\n\nNations ranked by size:\n------------------------");
        Collections.sort(world.getNations());
        Collections.reverse(world.getNations());
        for (int rank=1; rank<=world.getNations().size(); rank++) {
            Nation n = world.getNations().get(rank - 1);
            System.out.println(rank + ": " +
                    n.getName() + " - " + n.getAnsiDisplay() +
                    "; They speak '" + n.getLanguage().getName() + "'.");
        }

        input.close();
        AnsiConsole.systemUninstall();
    }

}
