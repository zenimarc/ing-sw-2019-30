package view;

public interface View {
    public void gameStart();
    public void giveMessage(String title, String mex);
    public void giveError(String error);
    public boolean loadWeapon();
    public void myTurn();
    public void showBoard();

}
