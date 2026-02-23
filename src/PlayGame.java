import javax.swing.*;

public class PlayGame {
    private GameGUI gameGUI;
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new GameGUI().setVisible(true);
        });
    }
}
