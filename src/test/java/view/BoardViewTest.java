package view;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardViewTest {

    @Test

    public void drawMap(){
        BoardView boardView = new BoardView();
        boardView.drawCLI();
    }

}