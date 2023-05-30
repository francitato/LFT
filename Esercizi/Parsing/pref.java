package Esercizi.Parsing;

public class pref extends Parser{

    protected void S(){
        switch(peek()){

            case '*':
                match('*');
                S();
                S();
            break;

            case '+':
                match('+');
                S();
                S();
            break;

            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                match();
            break;

            default:
                error("S");
        }
    }
    
}
