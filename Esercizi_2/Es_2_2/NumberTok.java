package Esercizi.Esercizi_2.Es_2_2;

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