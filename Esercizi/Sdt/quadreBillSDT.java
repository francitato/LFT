package Esercizi.Sdt;

public class quadreBillSDT extends Parser{

    protected void start(){
        System.out.println(S());
    }

    private int S(){
        switch(peek()){
            case '[':{
                match('[');
                int m = S();
                match(']');
                int n = S();
                return Math.max(m+1, n);
            }

            case '$':
            case ']':{
                return 0;
            }

            default:
                throw error("S");

        }
    }

    public static void main (String [] args){
        new quadreBillSDT().parse("[[]]");
    }
    
}
