package Esercizi.Parsing;

public class AnBnCm extends Parser{

    protected void S(){
        switch(peek()){

            case 'a':
            case 'c':
            case '$':
                X();
                C();
            break;

            default:
                throw error("S");

        }
    }

    protected void X(){
        switch(peek()){

            case 'a':
                match('a');
                X();
                match('b');
            break;

            case 'b':
            case 'c':
            case '$':
            break;

            default:
                throw error("X");
        }
    }

    protected void C(){
        switch(peek()){

            case 'c':
                match('c');
                C();
            break;

            case '$':
            break;

            default:
                throw error("X");
        }
    }

    public static void main(String [] args){
        new AnBnCm().parse("aabcc");
    }
}
