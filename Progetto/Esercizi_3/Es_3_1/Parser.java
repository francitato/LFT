package Progetto.Esercizi_3.Es_3_1;

import java.io.*;

public class Parser {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    public Parser(Lexer l, BufferedReader br) {
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
        if (look.tag == Tag.NUM || look.tag == '(') {
            expr();
            match(Tag.EOF);
        } else {
            error("syntax error start");
        }
    }

    private void expr() {
        if (look.tag == Tag.NUM || look.tag == '(') {
            term();
            exprp();
        } else {
            error("syntax error expr");
        }
    }

    private void exprp() {
        switch (look.tag) {
            case '+':
                match('+');
                term();
                exprp();
                break;
            case '-':
                match('-');
                term();
                exprp();
                break;
            case ')', Tag.EOF:
                break;
            default:
                error("Syntax error exprp");
        }
    }

    private void term() {
        if (look.tag == Tag.NUM || look.tag == '(') {
            fact();
            termp();
        } else {
            error("syntax error term");
        }
    }

    private void termp() {
        switch (look.tag) {
            case '*':
                match('*');
                fact();
                termp();
                break;
            case '/':
                match('/');
                fact();
                termp();
                break;
            case ')', '+', '-', Tag.EOF:
                break;
            default:
                error("Syntax error termp");
        }
    }

    private void fact() {
        switch (look.tag) {
            case '(':
                match('(');
                expr();
                match(')');
                break;
            case Tag.NUM:
                match(Tag.NUM);
                break;
            default:
                error("Syntax error fact");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "Progetto/Esercizi_3/Es_3_1/input.txt";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Parser parser = new Parser(lex, br);
            parser.start();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
