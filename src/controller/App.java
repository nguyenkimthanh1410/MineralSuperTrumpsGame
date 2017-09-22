package controller;

import model.GameLogic;
import view.MainFrame;

//For running GUI
public class App {
    App(){
        MainFrame mainFrame = new MainFrame();
        GameLogic gameLogic = new GameLogic();
        Controller controller = new Controller(mainFrame, gameLogic);
    }
    public static void main(String[] args) {
       App app = new App();

    }//end App method

}//end class App
