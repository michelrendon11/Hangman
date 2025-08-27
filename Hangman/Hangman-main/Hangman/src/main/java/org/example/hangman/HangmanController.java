package org.example.hangman;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputControl;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Game controller, single class, single game.
 * Can use words or phrases
 */

public class HangmanController implements Initializable {

    @FXML
    private AnchorPane pane;
    @FXML
    private Circle head;
    @FXML
    private Rectangle body;
    @FXML
    private Rectangle rightArm;
    @FXML
    private Rectangle leftArm;
    @FXML
    private Rectangle rightLeg;
    @FXML
    private Rectangle leftLeg;

    @FXML
    private Button test;
    @FXML
    private Button set;
    @FXML
    public Button reset;

    @FXML
    private Text guess;
    @FXML
    private Text game;
    @FXML
    private Text allLetters;

    @FXML
    private TextField hideText;
    @FXML
    private TextField letter;
    @FXML
    private TextField secret;

    private String letters = "";
    private int count;
    char[] tempChar;
    private List<Character> secretWord, revealWord, searchLetters;
    private List<Shape> man;
    List<TextField> textFields;
    List<Text> texts;
    private boolean already;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        already = false;
        game.setLayoutX(pane.getPrefWidth() / 2 - game.getBoundsInParent().getWidth() / 2 - 70);
        pane.widthProperty().addListener(_ -> game.setLayoutX(pane.getWidth() / 2 - game.getBoundsInParent().getWidth() / 2 - 70));
        man = new ArrayList<>(Arrays.asList(head, body, leftArm, rightArm, leftLeg, rightLeg));
        textFields = new ArrayList<>(Arrays.asList(hideText, letter, secret));
        texts = new ArrayList<>(Arrays.asList(game, guess, allLetters));
        searchLetters = new ArrayList<>();
        resetGame();
    }

    @FXML
    void set() {
        if (secret.getText().isEmpty()) {
            guess.setVisible(true);
            guess.setText("Please Enter a Word");
        } else {
            letter.setEditable(true);
            secret.setEditable(false);
            test.setDisable(false);
            set.setDisable(true);
            guess.setVisible(false);
            tempChar = secret.getText().toUpperCase().toCharArray();
            secret.clear();
            firstSet();
            updateHideText();
        }
    }

    private void firstSet() {
        secretWord = new ArrayList<>(tempChar.length);
        revealWord = new ArrayList<>(tempChar.length);
        for (Character c : tempChar) {
            secretWord.add(c);
        }
        for (Character c : tempChar) {
            if (c.equals(' ')) {
                revealWord.add(' ');
            } else {
                revealWord.add('*');
            }
        }
    }

    private void updateHideText() {
        hideText.clear();
        for (Character c : revealWord) {
            hideText.appendText(c.toString().toUpperCase());
        }
    }

    @FXML
    void test() {
        if (letter.getText().isEmpty()) {
            guess.setVisible(true);
            guess.setText("Please Enter a Letter");
        } else {
            guess.setVisible(false);
            if (checkIfOneLetter()) {
                char l = getLetter();
                if (searchLetters.contains(l)) {
                    already = true;
                    guess.setVisible(true);
                    guess.setText("Letter \"" + l + "\" has already been used");
                }
                if (!searchLetters.contains(l)) {
                    already = false;
                    updateLetters(l);
                }
                if (secretWord.contains(l)) {
                    checkIfYouWin(l);
                } else {
                    updateHangman();
                }
            } else {
                letter.clear();
                guess.setText("Please Enter Only ONE Letter");
                guess.setVisible(true);
            }
        }
        letter.clear();
    }

    private boolean checkIfOneLetter() {
        char[] input = letter.getText().toCharArray();
        return input.length <= 1;
    }

    private char getLetter() {
        return letter.getText().toUpperCase().charAt(0);
    }

    @FXML
    void reset() {
        resetGame();
    }

    private void resetGame() {
        letters = "";
        count = 0;
        already = false;
        test.setDisable(true);
        set.setDisable(false);
        secret.setEditable(true);
        letter.setEditable(false);
        man.forEach(shape -> shape.setVisible(false));
        textFields.forEach(TextInputControl::clear);
        texts.forEach(c -> c.setVisible(false));
        searchLetters.clear();
    }

    private void gameOver() {
        game.setText("GAME OVER !!");
        setBeginning();
    }

    private void youWin() {
        game.setText("YOU WON !!");
        setBeginning();
    }

    private void setBeginning() {
        guess.setVisible(false);
        letter.setEditable(false);
        test.setDisable(true);
        game.setVisible(true);
    }

    private void updateLetters(char l) {
        searchLetters.add(l);
        letters = MessageFormat.format("{0} {1}", letters, l);
        allLetters.setText(letters);
        allLetters.setVisible(true);
    }

    private void checkIfYouWin(char l) {
        for (int i = 0; i < tempChar.length; i++) {
            if (secretWord.get(i).equals(l)) {
                revealWord.set(i, l);
                updateHideText();
            }
        }
        if (revealWord.toString().equals(Arrays.toString(tempChar))) {
            youWin();
        }
    }

    private void updateHangman() {
        guess.setVisible(true);
        if (!already) {
            guess.setText("Bad Guess!!");
        }
        man.get(count).setVisible(true);
        count++;
        if (count >= man.size()) {
            gameOver();
        }
    }
}