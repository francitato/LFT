import java.io.*;

public class Lexer {

    public static int line = 1;
    private char peek = ' ';

    private void readch(BufferedReader br) {
        try {
            peek = (char) br.read();
        } catch (IOException exc) {
            peek = (char) -1;
        }
    }

    public Token lexical_scan(BufferedReader br) {
        while (peek == ' ' || peek == '\t' || peek == '\n' || peek == '\r') {
            if (peek == '\n') {
                line++;
            }
            readch(br);
        }

        switch (peek) {
            case '!':
                peek = ' ';
                return Token.not;

            case '(':
                peek = ' ';
                return Token.lpt;

            case ')':
                peek = ' ';
                return Token.rpt;

            case '[':
                peek = ' ';
                return Token.lpq;

            case ']':
                peek = ' ';
                return Token.rpq;

            case '{':
                peek = ' ';
                return Token.lpg;

            case '}':
                peek = ' ';
                return Token.rpg;

            case '+':
                peek = ' ';
                return Token.plus;

            case '-':
                peek = ' ';
                return Token.minus;

            case '*':
                peek = ' ';
                return Token.mult;

            case '/':
                peek = ' ';
                return Token.div;

            case ';':
                peek = ' ';
                return Token.semicolon;

            case ',':
                peek = ' ';
                return Token.comma;

            case '|':
                readch(br);
                if (peek == '|') {
                    peek = ' ';
                    return Word.or;
                } else {
                    System.err.println("Erroneous character"
                            + " after | : " + peek);
                    return null;
                }

            case '&':
                readch(br);
                if (peek == '&') {
                    peek = ' ';
                    return Word.and;
                } else {
                    System.err.println("Erroneous character"
                            + " after & : " + peek);
                    return null;
                }

            case '<':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.le;
                } else if (peek == '>') {
                    peek = ' ';
                    return Word.ne;
                } else {
                    peek = ' ';
                    return Word.lt;
                }

            case '>':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.ge;
                } else {
                    peek = ' ';
                    return Word.gt;
                }

            case '=':
                readch(br);
                if (peek == '=') {
                    peek = ' ';
                    return Word.eq;
                } else {
                    System.err.println("Erroneous character"
                            + " after = : " + peek);
                    return null;
                }

            case (char) -1:
                return new Token(Tag.EOF);

            default:
                String tmp = "";
                if (Character.isLetter(peek)) {
                    while (Character.isLetter(peek) || Character.isDigit(peek)) {
                        tmp += Character.toString(peek);
                        readch(br);
                    }
                    switch (tmp) {
                        case "assign":
                            return Word.assign;
                        case "to":
                            return Word.to;
                        case "conditional":
                            return Word.conditional;
                        case "option":
                            return Word.option;
                        case "do":
                            return Word.dotok;
                        case "else":
                            return Word.elsetok;
                        case "while":
                            return Word.whiletok;
                        case "begin":
                            return Word.begin;
                        case "end":
                            return Word.end;
                        case "print":
                            return Word.print;
                        case "read":
                            return Word.read;
                        default:
                            return new Word(Tag.ID, tmp);
                    }
                } else if (Character.isDigit(peek)) {
                    if (peek == '0') {
                        tmp = tmp + peek;
                        readch(br);
                        if (Character.isDigit(peek)) {
                            System.err.println("Erroneous character"
                                    + " Sequence start with number 0 " + peek);
                            return null;
                        }
                    } else {
                        while (Character.isDigit(peek)) {
                            tmp += Character.toString(peek);
                            readch(br);
                        }
                        if (Character.isLetter(peek) || peek == '_') {
                            System.err.println("Erroneous character"
                                    + " Sequence start with number " + peek);
                            return null;
                        }
                    }
                    return new NumberTok(Tag.NUM, Integer.parseInt(tmp));
                } else {
                    System.err.println("Erroneous character: "
                            + peek);
                    return null;
                }
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Esercizi/Esercizi_2/Es_2_1/input.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Token tok;
            do {
                tok = lex.lexical_scan(br);
                System.out.println("Scan: " + tok);
            } while (tok.tag != Tag.EOF);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
