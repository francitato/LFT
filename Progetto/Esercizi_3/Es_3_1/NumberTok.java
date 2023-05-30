package Progetto.Esercizi_3.Es_3_1;

public class NumberTok extends Token {
    public int lexeme;

    public NumberTok(int tag, int s) {
        super(tag);
        lexeme = s;
    }

    public String toString() {
        return "<" + tag + ", " + lexeme + ">";
    }

}