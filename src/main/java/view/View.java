package view;

public interface View {
    void gameStart();
    void giveMessage(String title, String mex);
    void giveError(String error);
    boolean loadWeapon();
    void myTurn();
    void notMyTurn(String nameOfWhoPlay);
    void showBoard();

}
