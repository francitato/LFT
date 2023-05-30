package Esercizi.Parsing;

public class quadreBil extends Parser{

    protected void S(){
        switch(peek()){
            case '[':
                match('[');
                S();
                match(']');
                S();
                break;

            case '$':
            case ']':
                break;

            default:
                throw error("S");

        }
    }
    
    public static void main (String [] args){
        new quadreBil().parse("[]");
    }
}