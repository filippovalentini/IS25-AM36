package it.polimi.ingsw.galaxytrucker;

import it.polimi.ingsw.galaxytrucker.ui.cli.CliInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.GuiInterface;
import it.polimi.ingsw.galaxytrucker.ui.gui.JavaFxLauncher;
import javafx.application.Application;

import java.util.Scanner;

public class MainClient {
    public static void main(String[] args) {
        System.out.println("||| WELCOME TO GALAXY TRUCKER | ||");
        System.out.print("Do you want to use CLI (1) or GUI (2)? ");
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

        if(interfaceNumber == 2){
            new GuiInterface().launch();
        }
        else{
            new CliInterface().launch(args[0]);
        }
    }
}
