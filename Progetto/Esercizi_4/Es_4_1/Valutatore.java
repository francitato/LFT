package Progetto.Esercizi_4.Es_4_1;

import java.io.*;

public class Valutatore {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Valutatore(Lexer l, BufferedReader br) {
        lex = l;
        pbr = br;
        move();
    }

    void move() {
        look = lex.lexical_scan(pbr);
        System.out.println("token = " + look);
    }

    void error(String s) {
        throw new Error("near line " + lex + ": " + s);
    }

    void match(int t) {
        if (look.tag == t) {
            if (look.tag != Tag.EOF)
                move();
        } else
            error("syntax error");
    }

    public void start() {
        int expr_val;
        if (look.tag == Tag.NUM || look.tag == '(') {
            expr_val = expr();
            System.out.println(expr_val);
        } else {
            error("syntax error start");
        }
    }

    private int expr() {
        int term_val, exprp_val = 0;
        if (look.tag == Tag.NUM || look.tag == '(') {
            term_val = term();
            exprp_val = exprp(term_val);
        } else {
            error("syntax error expr");
        }
        return exprp_val;
    }

    private int exprp(int exprp_i) {
        int term_val, exprp_val = 0;
        switch (look.tag) {
            case '+':
                match('+');
                term_val = term();
                exprp_val = exprp(exprp_i + term_val);
                break;
            case '-':
                match('-');
                term_val = term();
                exprp_val = exprp(exprp_i - term_val);
                break;
            case ')', Tag.EOF:
            exprp_val = exprp_i;
                break;
            default:
                error("Syntax error exprp");
        }
        return exprp_val;

    }

    private int term() {
        int fact_val, termp_val = 0;
        if (look.tag == Tag.NUM || look.tag == '(') {
            fact_val = fact();
            termp_val = termp(fact_val);
        } else {
            error("syntax error term");
        }
        return termp_val;
    }

    private int termp(int termp_i) {
        int fact_val, term_val = 0;
        switch (look.tag) {
            case '*':
                match('*');
                fact_val = fact();
                term_val = termp(termp_i * fact_val);
                break;
            case '/':
                match('/');
                fact_val = fact();
                term_val = termp(termp_i / fact_val);
                break;
            case ')', '+', '-', Tag.EOF:
                term_val = termp_i;
                break;
            default:
                error("Syntax error termp");
        }
        return term_val;
    }

    private int fact() {
        int fact_val = 0;
        switch (look.tag) {
            case '(':
                match('(');
                fact_val = expr();
                match(')');
                break;
            case Tag.NUM:
                fact_val = ((NumberTok)look).lexeme;
                match(Tag.NUM);
                break;
            default:
                error("Syntax error fact");
        }
        return fact_val;
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Progetto/Esercizi_4/Es_4_1/input.txt"; // il percorso del file da leggere
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Valutatore valutatore = new Valutatore(lex, br);
            valutatore.start();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
