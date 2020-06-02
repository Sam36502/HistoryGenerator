package ch.pearcenet.historygen.exc;

import ch.pearcenet.historygen.language.Alphabet;

public class InvalidAlphabetException extends Exception{

    private Alphabet invalidAlphabet;

    public InvalidAlphabetException(Alphabet alphabet, String msg) {
        super(msg);
        this.invalidAlphabet = alphabet;
    }

    public Alphabet getInvalidAlphabet() {
        return invalidAlphabet;
    }
}
