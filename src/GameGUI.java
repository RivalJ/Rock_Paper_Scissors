import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;

public class GameGUI extends JFrame {//FIXME: All methods need to have javadocs
    private JPanel options, stats, results;
    private String computerMove,gameResults, previousMove;
    private int playerWins, computerWins, ties;
    private JTextField playerWinsField, computerWinsField, tiesField;
    private JTextArea resultsArea;
    private int playerRockCount, playerPaperCount, playerScissorsCount;

    public GameGUI(){
        playerWins = computerWins = ties = 0;
        Setup();
    }

    /**
     * combines all setup code into one place
     * intended to make setup code cleaner and easier to read
     * also handles overall layout of the GUI and frame setup
     */
    private void Setup(){
        //add a title label
        Dimension baseScreenSize = Toolkit.getDefaultToolkit().getScreenSize();
        float applicationScaleFactorWidth = 1f/3f;
        float applicationScaleFactorHeight = 2f/3f;
        Dimension applicationSize = new Dimension(
                (int)(baseScreenSize.width * applicationScaleFactorWidth),
                (int)(baseScreenSize.height * applicationScaleFactorHeight)
        );

        super.setTitle("Rock Paper Scissors");
        super.setSize(applicationSize.width, applicationSize.height);
        super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        super.setLayout(new BorderLayout());

        SetupOptions();
        SetupStats();
        SetupResults();

        super.setLocationRelativeTo(null);
    }

    /**
     * sets up the options panel
     * intended to make setup code cleaner and easier to read
     */
    private void SetupOptions(){
        options = new JPanel();
        //create buttons
        JButton rock, paper, scissors, quit;
        rock = new JButton("Rock");
        paper = new JButton("Paper");
        scissors = new JButton("Scissors");
        quit = new JButton("Quit");
        //get images for buttons
        int imageSize = 100;

        //set up button images and formatting
        SetUpButtonVisuals(rock, "Rock.png", imageSize);
        SetUpButtonVisuals(paper, "Paper.png", imageSize);
        SetUpButtonVisuals(scissors, "Scissors.png", imageSize);
        SetUpButtonVisuals(quit, "Quit.jpg", imageSize);

        //assign actions to buttons
        rock.addActionListener(new ButtonListener());
        paper.addActionListener(new ButtonListener());
        scissors.addActionListener(new ButtonListener());
        quit.addActionListener(new ButtonListener());

        options.add(rock);
        options.add(paper);
        options.add(scissors);
        options.add(quit);

        Border border = BorderFactory.createMatteBorder(10,10,10,10, Color.BLACK);
        options.setBorder(border);


        super.add(options, BorderLayout.PAGE_START);
    }

    private class ButtonListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();
            if (command.equals("Rock")) {
                ResolveMove("Rock");
            } else if (command.equals("Paper")) {
                ResolveMove("Paper");
            } else if (command.equals("Scissors")) {
                ResolveMove("Scissors");
            }
            else if (command.equals("Quit")) {
                System.exit(0);
            }
            else {
                System.out.println("Invalid command: " + command);
            }
        }
    }

    /**
     * compresses repitive button layout setup code into one method
     * @param button the button to set up
     * @param imageName the name of the image file to use
     * @param imageSize the size that the image will be scaled to
     */
    private void SetUpButtonVisuals(JButton button, String imageName, int imageSize){
        //set button text to be centered above icon
        button.setVerticalTextPosition(SwingConstants.TOP);
        button.setHorizontalTextPosition(SwingConstants.CENTER);

        Image icon = new ImageIcon(getClass().getResource(imageName)).getImage();
        icon = icon.getScaledInstance(imageSize,imageSize,Image.SCALE_SMOOTH);

        button.setIcon(new ImageIcon(icon));
        button.setFont(new Font("Arial", Font.BOLD, 30));
    }

    /**
     * sets up the stats panel
     * intended to make setup code cleaner and easier to read
     */
    private void SetupStats(){
        stats = new JPanel();
        JPanel playerWins, computerWins, ties;
        Font font = new Font("Arial", Font.BOLD, 20);

        //create text fields
        playerWinsField = new JTextField(2);
        computerWinsField = new JTextField(2);
        tiesField = new JTextField(2);
        UpdateStats();

        //set non-editable
        playerWinsField.setEditable(false);
        playerWinsField.setFont(font);

        computerWinsField.setEditable(false);
        computerWinsField.setFont(font);

        tiesField.setEditable(false);
        tiesField.setFont(font);


        //create labels
        JLabel playerWinsLabel = new JLabel("Player Wins:");
        playerWinsLabel.setFont(font);

        JLabel computerWinsLabel = new JLabel("Computer Wins:");
        computerWinsLabel.setFont(font);

        JLabel tiesLabel = new JLabel("Ties:");
        tiesLabel.setFont(font);


        //add labels and text fields to corresponding panels
        //this makes it easier to manage them visually as one component, should I need to do so
        playerWins = new JPanel();
        playerWins.add(playerWinsLabel);
        playerWins.add(playerWinsField);

        computerWins = new JPanel();
        computerWins.add(computerWinsLabel);
        computerWins.add(computerWinsField);

        ties = new JPanel();
        ties.add(tiesLabel);
        ties.add(tiesField);

        //NOTE: read only means it's read only for the user, not for the computer. updating values should be no problem

        stats.add(playerWins);
        stats.add(computerWins);
        stats.add(ties);
        super.add(stats, BorderLayout.PAGE_END);
    }

    /**
     * sets up the results panel
     * intended to make setup code cleaner and easier to read
     */
    private void SetupResults() {
        results = new JPanel();

        resultsArea = new JTextArea("", 25, 50);
        resultsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultsArea);

        results.add(scrollPane);

        super.add(results, BorderLayout.CENTER);
    }

    /**
     * handles main gameplay logic.
     * when the player presses an option button this method is called
     * this method prompts the computer to pick a strategy to choose a move
     * options are then compared and a winner is decided
     * @param playerMove the player's move
     */
    private void ResolveMove(String playerMove){
        switch(playerMove){//increment the appropriate counter
            case "Rock": playerRockCount++; break;
            case "Paper": playerPaperCount++; break;
            case "Scissors": playerScissorsCount++; break;
        }

        //get a "random" move from the computer
        //it will actually be based on a strat that the comp chooses
        String stratUsed = GetComputerStrategy(playerMove);

        if(playerMove.equals(computerMove)){
            ties++;
            gameResults = GetResultsString(playerMove, computerMove) + " Tie!";
        }
        else if(
                playerMove.equals("Rock") && computerMove.equals("Scissors") ||
                        playerMove.equals("Paper") && computerMove.equals("Rock") ||
                        playerMove.equals("Scissors") && computerMove.equals("Paper")
        ){
            playerWins++;
            gameResults = GetResultsString(playerMove, computerMove) + " You Win!";
        }
        else{
            computerWins++;
            gameResults = GetResultsString(playerMove, computerMove) + " You Lose!";
        }
        UpdateStats();
        resultsArea.append(gameResults + " " + stratUsed + "\n");
        previousMove = playerMove;
    }

    /**
     * used to determine the text to display in the results text area
     * intended to be combined with additional text as to who won
     * Not meant to determine who won, only to generate text based on how the game ended
     * @param playerMove the player's move
     * @param computerMove the computer's move
     * @return the text to display in the results text area
     */
    private String GetResultsString(String playerMove, String computerMove){
        if((playerMove.equals("Rock") && computerMove.equals("Scissors")) ||
                (playerMove.equals("Scissors") && computerMove.equals("Rock"))){
            return "Rock beats Scissors.";
        }
        else if((playerMove.equals("Rock") && computerMove.equals("Paper")) ||
                (playerMove.equals("Paper") && computerMove.equals("Rock"))){
            return "Paper beats Rock.";
        }
        else if((playerMove.equals("Paper") && computerMove.equals("Scissors")) ||
                (playerMove.equals("Scissors") && computerMove.equals("Paper"))){
            return "Scissors beats Paper.";
        }
        else{
            return playerMove + " vs " + computerMove + ".";
        }

    }

    /**
     * used to update the stats panels text fields
     * is typically run inside of the "ResolveMove" method
     */
    private void UpdateStats(){
        playerWinsField.setText(Integer.toString(playerWins));
        computerWinsField.setText(Integer.toString(computerWins));
        tiesField.setText(Integer.toString(ties));
    }


    private String GetComputerStrategy(String playerMove){
        String returnString = "Strategy used: ";
        int random = (int)(Math.random() * 100) + 1;
        if(random <= 10){
            computerMove = new Strategy_Cheat().getMove(playerMove);
            returnString += "Cheat";
        }
        else if(random <= 30){
            computerMove = new Strategy_LeastUsed().getMove(playerMove);
            returnString += "LeastUsed";
        }
        else if(random <= 50){
            computerMove = new Strategy_MostUsed().getMove(playerMove);
            returnString += "MostUsed";
        }
        else if(random <= 70){
            computerMove = new Strategy_LastUsed().getMove(playerMove);
            returnString += "LastUsed";
        }
        else{
            computerMove = new Strategy_Random().getMove(playerMove);
            returnString += "Random";
        }
        return returnString;
    }

    /**
     * use Math.minto determine the least and most used move
     * then returns what would beat the least used move
     */
    public class Strategy_LeastUsed implements IStrategy{
        public String getMove(String playerMove){
            int leastUsed = Math.min(playerRockCount, Math.min(playerPaperCount, playerScissorsCount));
            if(leastUsed == playerRockCount) return "Paper";
            else if(leastUsed == playerPaperCount) return "Scissors";
            else return "Rock";
        }
    }

    /**
     * use Math.max determine the most and least used move
     * then returns what would beat the most used move
     */
    public class Strategy_MostUsed implements IStrategy{
        public String getMove(String playerMove){
            int mostUsed = Math.max(playerRockCount, Math.max(playerPaperCount, playerScissorsCount));
            if(mostUsed == playerRockCount) return "Paper";
            else if(mostUsed == playerPaperCount) return "Scissors";
            else return "Rock";
        }
    }

    /**
     * use previous move to determine what to beat
     * if previous move is null, get a random move instead
     */
    public class Strategy_LastUsed implements IStrategy{
        public String getMove(String playerMove){
            if (previousMove == null) {
                return new Strategy_Random().getMove(playerMove);
            }
            else if(previousMove.equals("Rock")) return "Paper";
            else if(previousMove.equals("Paper")) return "Scissors";
            else return "Rock";
        }
    }
}

