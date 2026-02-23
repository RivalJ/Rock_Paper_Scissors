public class Strategy_Random implements IStrategy{
    /**
     * returns a random move
     */
    public String getMove(String playerMove){
        String returnString = "";
        int random = (int)(Math.random()*3);
        switch(random){
            case 0: returnString = "Rock"; break;
            case 1: returnString = "Paper"; break;
            case 2: returnString = "Scissors"; break;
        }
        return returnString;
    }
}
