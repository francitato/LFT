package Esercizi.Parsing;

public class BooleanLL extends Parser{

    public void S(){
        B();
    }

    protected void B(){
        switch(peek()){
            case '!':
            case '(':
            case 't':
            case 'f':
                C();
                B1();
            break;
            
            default:
                error("B");
        }
    }

    protected void B1(){
        switch(peek()){
            case '|':
                match('|');
                B();
            break;
            
            case '$':
            case ')':
            break;

            default:
                error("B1");
        }
    }

    protected void C(){
        switch(peek()){
            case '!':
            case '(':
            case 't':
            case 'f':
                D();
                C1();
            break;

            default:
                error("C");
        }
    }

    protected void C1(){
        switch(peek()){
            case '&':
                match('&');
                C();
            break;
            
            case '$':
            case ')':
            case '|':
            break;

            default:
                error("C1");
        }
    }

    protected void D(){
        switch(peek()){
            case '!':
                match('!');
                D();
            break;
            
            case '(':
                match('(');
                B();
                match(')');
            break;

            case 't':
            case 'f':
                match();
            break;

            default:
                error("D");
        }
    }
    
}
