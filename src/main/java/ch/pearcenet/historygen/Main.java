package ch.pearcenet.historygen;

import ch.pearcenet.historygen.world.World;
import org.fusesource.jansi.AnsiConsole;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        Scanner input = new Scanner(System.in);

        while (true) {
            System.out.print("Enter world seed: ");
            int seed = Integer.parseInt(input.nextLine());

            if (seed == -1L) break;

            System.out.println("Generated world with seed " + seed);
            World world = new World(seed);
            System.out.println(world);
        }

        input.close();
        AnsiConsole.systemUninstall();
    }

}
