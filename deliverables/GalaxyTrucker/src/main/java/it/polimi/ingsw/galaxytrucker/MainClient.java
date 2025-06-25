package it.polimi.ingsw.galaxytrucker;

import it.polimi.ingsw.galaxytrucker.ui.tui.TuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;

import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        boolean noParams = true;
        for (String arg : args) { // with command line parameters
            if (arg.equals("--gui")) {
                noParams = false;
                new GuiInterface().launch();
                break;
            }else if (arg.equals("--tui")) {
                noParams = false;
                new TuiInterface().launch();
                break;
            }
        }
        if (noParams) { // without command line parameters
            int interfaceNumber = askInterface();
            if(interfaceNumber == 2){
                new GuiInterface().launch();
            }
            else{
                new TuiInterface().launch();
            }
        }
    }

    public static int askInterface(){
        System.out.println("||| WELCOME TO GALAXY TRUCKER | ||");
        System.out.print("Do you want to use TUI (1) or GUI (2)? ");
        Scanner scanner = new Scanner(System.in);
        int interfaceNumber;
        do{
            interfaceNumber = 0;
            try{
                interfaceNumber = Integer.parseInt(scanner.nextLine());
            }
            catch(NumberFormatException e){
                System.out.println("Required integer argument");
            }
        }while(interfaceNumber != 1 && interfaceNumber != 2);
        return interfaceNumber;
    }
}
