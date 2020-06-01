package ch.pearcenet.historygen;

import ch.pearcenet.historygen.exc.InvalidAlphabetException;
import ch.pearcenet.historygen.language.Language;
import ch.pearcenet.historygen.world.World;
import org.fusesource.jansi.AnsiConsole;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        AnsiConsole.systemInstall();
        Scanner input = new Scanner(System.in);
        Language lang = null;

        System.out.print("Language Seed: ");
        long seed = Long.parseLong(input.nextLine());

        System.out.print("Alphabet to use: ");
        String alpha = input.nextLine();

        try {
            lang = new Language(alpha, seed);
        } catch (InvalidAlphabetException e) {
            e.printStackTrace();
            System.exit(1);
        }

        String name = lang.translateTo("language");
        System.out.println(name + " translator:\n-------------------");

        while (true) {
            System.out.print("String to translate: ");
            String orig = input.nextLine();
            String translation = lang.translateTo(orig);
            System.out.println(orig + " ---> " + translation);
            String reverse = lang.translateFrom(translation);
            System.out.println(reverse + " <--- " + lang.translateTo(reverse) + "\n");

            if ("exit".equals(orig)) break;
        }

        input.close();
        AnsiConsole.systemUninstall();
    }

}
