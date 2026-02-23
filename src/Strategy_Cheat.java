public class Strategy_Cheat implements IStrategy{
    /**
     * reads the players current move and then returns what would beat it
     */
    public String getMove(String playerMove){
        if(playerMove == "Rock") return "Paper";
        else if(playerMove == "Paper") return "Scissors";
        else return "Rock";
    }
}
