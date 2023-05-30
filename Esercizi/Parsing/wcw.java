package Esercizi.Parsing;

public class wcw extends Parser{
    protected void S(){
        switch(peek()){
            case('0'):
                match('0');
                S();
                match('0');
            break;

            case('1'):
                match('1');
                S();
                match('1');
            break;

            case 'c':
                match('c');
            break;

            default:
                throw error("S");
        }
    }

    public static void main (String[] args){
        new wcw().parse("010c010");
    }

}