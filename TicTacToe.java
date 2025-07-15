/**
 * Objective:
 * Build a JavaFX Tic-Tac-Toe game where two players take turns marking cells
 * on a 3x3 grid. The program should determine when a player wins or the game ends in a draw,
 * and display appropriate messages.
 *
 * Algorithm:
 * 1. Create a 3x3 grid using a GridPane to hold Cell objects.
 * 2. Each Cell detects mouse clicks and draws X or O.
 * 3. After each move, the program checks for a win or draw condition.
 * 4. If game ends, it displays the result and stops further moves.
 *
 * Input and Output:
 * Input: Mouse clicks on grid cells.
 * Output: Visual placement of X or O, and status messages at the bottom.
 *
 * Created by: Thang Doan
 * Date: 07/13/2025
 * Version: 1.0
 */

import javafx.application.Application; // Import base JavaFX application class
import javafx.stage.Stage; // Import stage for main window
import javafx.scene.Scene; // Import scene for placing UI elements
import javafx.scene.control.Label; // Import label for status messages
import javafx.scene.layout.BorderPane; // Import layout to organize top, center, bottom, etc.
import javafx.scene.layout.GridPane; // Import layout for a grid of cells
import javafx.scene.layout.Pane; // Import base pane for custom drawing
import javafx.scene.paint.Color; // Import color for drawing shapes
import javafx.scene.shape.Line; // Import line shape to draw X
import javafx.scene.text.Font; // Import font for text (used for O)
import javafx.scene.text.Text; // Import text node to display O

public class TicTacToe extends Application {
    private Cell[][] cell = new Cell[3][3]; // Create a 3x3 array to hold cell objects
    private char currentPlayer = 'X'; // Keep track of current player, starting with X
    private Label lblStatus = new Label("X's turn to play"); // Create label to show game status

    @Override
    public void start(Stage primaryStage) {
        GridPane pane = new GridPane(); // Create grid layout to hold cells

        // Loop through 3 rows and 3 columns to add Cell objects
        for (int i = 0; i < 3; i++) { // Loop for rows
            for (int j = 0; j < 3; j++) { // Loop for columns
                cell[i][j] = new Cell(); // Initialize each cell
                pane.add(cell[i][j], j, i); // Add cell to grid at column j, row i
            }
        }

        BorderPane borderPane = new BorderPane(); // Create border layout
        borderPane.setCenter(pane); // Place grid pane in the center
        borderPane.setBottom(lblStatus); // Place status label at the bottom

        Scene scene = new Scene(borderPane, 450, 470); // Create scene with given size
        primaryStage.setTitle("TicTacToe"); // Set window title
        primaryStage.setScene(scene); // Attach scene to stage
        primaryStage.show(); // Show the main window
    }

    public class Cell extends Pane { // Define inner class for each cell
        private char token = ' '; // Each cell holds a character: 'X', 'O', or ' ' if empty

        public Cell() {
            setStyle("-fx-border-color: black"); // Add black border to cell
            setPrefSize(150, 150); // Set fixed size of each cell
            setOnMouseClicked(e -> handleClick()); // Register mouse click handler
        }

        private void handleClick() {
            if (token == ' ' && !isGameOver()) { // Only act if cell is empty and game not over
                setToken(currentPlayer); // Set current player's token in this cell

                if (hasWon(currentPlayer)) { // Check if current player has won
                    lblStatus.setText(currentPlayer + " has won!"); // Show win message
                    disableAllCells(); // Stop further moves
                } else if (isBoardFull()) { // Check for draw condition
                    lblStatus.setText("Draw! The game is over."); // Show draw message
                } else {
                    currentPlayer = (currentPlayer == 'X') ? 'O' : 'X'; // Switch players
                    lblStatus.setText(currentPlayer + "'s turn to play"); // Update turn message
                }
            }
        }

        public void setToken(char c) {
            token = c; // Assign token to this cell

            if (token == 'X') {
                Line line1 = new Line(10, 10, getWidth() - 10, getHeight() - 10); // Create first diagonal
                Line line2 = new Line(10, getHeight() - 10, getWidth() - 10, 10); // Create second diagonal

                // Bind line endpoints to cell size so they scale
                line1.endXProperty().bind(widthProperty().subtract(10));
                line1.endYProperty().bind(heightProperty().subtract(10));
                line2.endXProperty().bind(widthProperty().subtract(10));
                line2.startYProperty().bind(heightProperty().subtract(10));

                getChildren().addAll(line1, line2); // Add lines to this cell
            } else if (token == 'O') {
                Text text = new Text("O"); // Create text node for O
                text.setFont(Font.font(100)); // Set font size for O
                text.setFill(Color.BLUE); // Set color for O
                text.xProperty().bind(widthProperty().divide(2).subtract(25)); // Center horizontally
                text.yProperty().bind(heightProperty().divide(2).add(35)); // Center vertically
                getChildren().add(text); // Add O to this cell
            }
        }

        public char getToken() {
            return token; // Return token in this cell
        }
    }

    public boolean hasWon(char player) {
        // Check all rows for a win
        for (int i = 0; i < 3; i++) {
            if (cell[i][0].getToken() == player &&
                cell[i][1].getToken() == player &&
                cell[i][2].getToken() == player) {
                return true; // Player won on this row
            }
        }

        // Check all columns for a win
        for (int j = 0; j < 3; j++) {
            if (cell[0][j].getToken() == player &&
                cell[1][j].getToken() == player &&
                cell[2][j].getToken() == player) {
                return true; // Player won on this column
            }
        }

        // Check top-left to bottom-right diagonal
        if (cell[0][0].getToken() == player &&
            cell[1][1].getToken() == player &&
            cell[2][2].getToken() == player) {
            return true; // Player won on main diagonal
        }

        // Check top-right to bottom-left diagonal
        if (cell[0][2].getToken() == player &&
            cell[1][1].getToken() == player &&
            cell[2][0].getToken() == player) {
            return true; // Player won on anti-diagonal
        }

        return false; // No win condition met
    }

    public boolean isBoardFull() {
        // Check all cells to see if any are still empty
        for (int i = 0; i < 3; i++) { // Loop through rows
            for (int j = 0; j < 3; j++) { // Loop through columns
                if (cell[i][j].getToken() == ' ') {
                    return false; // Found an empty cell
                }
            }
        }
        return true; // No empty cells found; board is full
    }

    public boolean isGameOver() {
        return hasWon('X') || hasWon('O') || isBoardFull(); // Check for win or draw
    }

    public void disableAllCells() {
        // Remove mouse click events from all cells
        for (int i = 0; i < 3; i++) { // Loop rows
            for (int j = 0; j < 3; j++) { // Loop columns
                cell[i][j].setOnMouseClicked(null); // Disable click
            }
        }
    }

    public static void main(String[] args) {
        launch(args); // Start JavaFX application
    }
}
