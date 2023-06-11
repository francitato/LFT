import java.io.*;

public class Translator {
    private Lexer lex;
    private BufferedReader pbr;
    private Token look;

    SymbolTable st = new SymbolTable();
    CodeGenerator code = new CodeGenerator();
    int count = 0;

    public Translator(Lexer l, BufferedReader br) {
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

    public void prog() {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.WHILE
                || look.tag == Tag.COND || look.tag == '{') {
            int lnext_prog = code.newLabel();
            statlist(lnext_prog);
            code.emitLabel(lnext_prog);
            match(Tag.EOF);
            try {
                code.toJasmin();
            } catch (java.io.IOException e) {
                System.out.println("IO error\n");
            }
        } else {
            error("syntax error prog");
        }
    }

    private void statlist(int lnext_prog) {
        if (look.tag == Tag.ASSIGN || look.tag == Tag.PRINT || look.tag == Tag.READ || look.tag == Tag.WHILE
                || look.tag == Tag.COND || look.tag == '{') {
            int lnext_stat = code.newLabel();
            stat(lnext_stat);
            code.emitLabel(lnext_stat);
            statlistp();
            code.emit(OpCode.GOto, lnext_prog);
        } else {
            error("syntax error statlist");
        }
    }

    private void statlistp() {
        switch (look.tag) {
            case ';':
                match(';');
                int lnext_stat = code.newLabel();
                stat(lnext_stat);
                code.emitLabel(lnext_stat);
                statlistp();
                break;
            case '}':
            case Tag.EOF:
                break;
            default:
                error("Syntax error statlistp");
        }
    }

    public void stat(int lnext_stat) {
        switch (look.tag) {
            case Tag.ASSIGN:
                match(Tag.ASSIGN);
                expr();
                match(Tag.TO);
                idlist(0);
                code.emit(OpCode.GOto, lnext_stat);
                break;
            case Tag.PRINT:
                match(Tag.PRINT);
                match('[');
                exprlist(1);
                match(']');
                code.emit(OpCode.GOto, lnext_stat);
                break;
            case Tag.READ:
                match(Tag.READ);
                match('[');
                idlist(1);
                match(']');
                code.emit(OpCode.GOto, lnext_stat);
                break;
            case Tag.WHILE:
                int Loop = code.newLabel();
                int whileTrue = code.newLabel();
                code.emitLabel(Loop);
                match(Tag.WHILE);
                match('(');
                bexpr(whileTrue, lnext_stat);
                code.emitLabel(whileTrue);
                match(')');
                stat(Loop);
                break;
            case Tag.COND:
                int condElse = code.newLabel();
                int condTrue = code.newLabel();
                int condFalse = code.newLabel();
                match(Tag.COND);
                match('[');
                optlist(condTrue, condFalse, condElse, lnext_stat);
                match(']');
                endif(condElse, lnext_stat);
                break;
            case '{':
                match('{');
                statlist(lnext_stat);
                match('}');
                break;
            default:
                error("Syntax error stat");
        }
    }

    private void endif(int condElse, int lnext_stat) {
        switch (look.tag) {
            case Tag.END:
                match(Tag.END);
                code.emitLabel(condElse);
                break;
            case Tag.ELSE:
                match(Tag.ELSE);
                code.emitLabel(condElse);
                stat(lnext_stat);
                match(Tag.END);
                break;
            default:
                error("Syntax error endif");
        }
    }

    private void idlist(int i) {
        if (look.tag == Tag.ID) {
            int id_addr = st.lookupAddress(((Word) look).lexeme);
            if (id_addr == -1) {
                id_addr = count;
                st.insert(((Word) look).lexeme, count++);
            }
            if (i == 1) {
                code.emit(OpCode.invokestatic, 0);
            }
            code.emit(OpCode.istore, id_addr);
            match(Tag.ID);
            idlistp(i, id_addr);
        } else {
            error("Syntax error idlist");
        }
    }

    private void idlistp(int i, int num) {
        switch (look.tag) {
            case ',':
                match(',');
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    id_addr = count;
                    st.insert(((Word) look).lexeme, count++);
                }
                if (i == 1) {
                    code.emit(OpCode.invokestatic, 0);
                }
                if (i == 0) {
                    code.emit(OpCode.iload, num);
                }
                code.emit(OpCode.istore, id_addr);
                match(Tag.ID);
                idlistp(i, num);
                break;
            case ')':
            case ']':
            case '}':
            case ';':
            case Tag.ELSE:
            case Tag.END:
            case Tag.EOF:
                break;
            default:
                error("Syntax error idlistp");
        }
    }
    
    private void optlist(int condTrue, int condFalse, int condElse, int lnext_stat) {
        if (look.tag == Tag.OPTION) {
            optitem(condTrue, condFalse, lnext_stat);
            code.emitLabel(condFalse);
            int condNextTrue = code.newLabel();
            optlistp(condNextTrue, condElse, lnext_stat);
        } else {
            error("Syntax error optlist");
        }
    }

    private void optlistp(int condTrue, int condFalse, int lnext_stat) {
        switch (look.tag) {
            case Tag.OPTION:
                optitem(condTrue, condFalse, lnext_stat);
                optlistp(condTrue, condFalse, lnext_stat);
                break;
            case ']':
                break;
            default:
                error("Syntax error optlistp");
        }
    }

    private void optitem(int condTrue, int condFalse, int lnext_stat) {
        if (look.tag == Tag.OPTION) {
            match(Tag.OPTION);
            match('(');
            bexpr(condTrue, condFalse);
            code.emitLabel(condTrue);
            match(')');
            match(Tag.DO);
            stat(lnext_stat);
        } else {
            error("Syntax error optitem");
        }
    }

    private void bexpr(int condTrue, int condFalse) {
        if (look.tag == Tag.RELOP) {
            String relop = ((Word) look).lexeme;
            match(Tag.RELOP);
            expr();
            expr();
            switch (relop) {
                case ">":
                    code.emit(OpCode.if_icmpgt, condTrue);
                    break;
                case "<":
                    code.emit(OpCode.if_icmplt, condTrue);
                    break;
                case "==":
                    code.emit(OpCode.if_icmpeq, condTrue);
                    break;
                case ">=":
                    code.emit(OpCode.if_icmpge, condTrue);
                    break;
                case "<=":
                    code.emit(OpCode.if_icmple, condTrue);
                    break;
                case "<>":
                    code.emit(OpCode.if_icmpne, condTrue);
                    break;
            }
            code.emit(OpCode.GOto, condFalse);
        } else {
            error("Syntax error bexpr");
        }
    }

    private void expr() {
        switch (look.tag) {
            case '+':
                match('+');
                match('(');
                exprlist(2);
                match(')');
                break;
            case '-':
                match('-');
                expr();
                expr();
                code.emit(OpCode.isub);
                break;
            case '*':
                match('*');
                match('(');
                exprlist(3);
                match(')');
                break;
            case '/':
                match('/');
                expr();
                expr();
                code.emit(OpCode.idiv);
                break;
            case Tag.NUM:
                code.emit(OpCode.ldc, ((NumberTok) look).lexeme);
                match(Tag.NUM);
                break;
            case Tag.ID:
                int id_addr = st.lookupAddress(((Word) look).lexeme);
                if (id_addr == -1) {
                    error("Identifier never created: " + look);
                }
                code.emit(OpCode.iload, id_addr);
                match(Tag.ID);
                break;
            default:
                error("Syntax error expr");
        }
    }

    private void exprlist(int i) {
        if (look.tag == '+' || look.tag == '-' || look.tag == '*' || look.tag == '/' || look.tag == Tag.NUM
                || look.tag == Tag.ID) {
            expr();
            if (i == 1) {
                code.emit(OpCode.invokestatic, 1);
            }
            exprlistp(i);
        } else {
            error("Syntax error exprlist");
        }
    }

    private void exprlistp(int i) {
        switch (look.tag) {
            case ',':
                match(',');
                expr();
                if (i == 1) {
                    code.emit(OpCode.invokestatic, 1);
                }
                if (i == 2) {
                    code.emit(OpCode.iadd);
                }
                if (i == 3) {
                    code.emit(OpCode.imul);
                }
                exprlistp(i);
                break;
            case ')':
            case ']':
                break;
            default:
                error("Syntax error exprlistp");
        }
    }

    public static void main(String[] args) {
        Lexer lex = new Lexer();
        String path = "input.lft";
        try {
            BufferedReader br = new BufferedReader(new FileReader(path));
            Translator translator = new Translator(lex, br);
            translator.prog();
            System.out.println("Input OK");
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
