package it.polimi.ingsw.galaxytrucker.client;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;
import java.util.*;

public class TUI {
    private String gameState;
    private String nickname;
    private Color color;
    private List<List<Component>> assembledComponents = new ArrayList<>();
    private List<Component> reservedComponents = new ArrayList<>();
    private Component pickedComponent;
    private List<Integer> pickedDeck = new ArrayList<>();
    private List<Component> shownComponents = new ArrayList<>();

    public TUI(String nickname, Color color, boolean firstFlight) {
        this.nickname = nickname;
        this.color = color;
        this.gameState = "WAITING FOR OTHER PLAYERS....";
        pickedComponent = null;
        initializeShipboard(firstFlight);
    }

    public static void main(String[] args){
        Integer imageID = 0;
        TUI tui = new TUI("Filippo", Color.BLUE, true);
        tui.updatePickedComponent(124, false);
        tui.updatePickedComponent(124, true);
        tui.updateShownComponent(124, true);
        tui.updatePickedComponent(125, false);
        tui.updatePickedComponent(125, true);
        tui.updateShownComponent(125, true);
        tui.updateShownComponent(125, false);
        tui.visualize();
    }


    public void visualize(){
        System.out.println("╔════════════════════════════╗");
        System.out.println("║     GALAXY TRUCKER TUI     ║");
        System.out.println("╚════════════════════════════╝");

        System.out.println("🚀 Game State: " + gameState);
        System.out.println("👨‍🚀 Player: " + nickname + " (" + color + ")");

        if (!shownComponents.isEmpty()) {
            System.out.print("🪐 Shown Components: ");
            for (Component comp : shownComponents) {
                System.out.print("[" + comp.getImageID() + "] ");
            }
            System.out.println();
        }

        if (!reservedComponents.isEmpty()) {
            System.out.print("📦 Reserved Components: ");
            for (Component comp : reservedComponents) {
                System.out.print("[" + comp.getImageID() + "] ");
            }
            System.out.println();
        }

        if (pickedComponent != null) {
            System.out.println("🛠️ Picked Component: [" + pickedComponent.getImageID() + " " + pickedComponent.getOrientation().toString().charAt(0) + "]");
        }

        System.out.println("\n🛰️  Ship Layout:\n");

        for (List<Component> row : assembledComponents) {
            StringBuilder top = new StringBuilder();
            StringBuilder middle = new StringBuilder();
            StringBuilder bottom = new StringBuilder();

            for (Component comp : row) {
                top.append("╔═══════╗ ");

                char ori = comp.getOrientation().toString().charAt(0);
                String id = String.valueOf(comp.getImageID());

                String content;
                if(id.equals("000")){
                    content ="     ";
                }
                else if(id.equals("003")){
                    content ="/////";
                }
                else{
                    content = ori + " " + id;
                }

                content = String.format("%-4s", content); // padding per allineamento

                middle.append("║ " + content + " ║ ");
                bottom.append("╚═══════╝ ");
            }

            System.out.println(top.toString());
            System.out.println(middle.toString());
            System.out.println(bottom.toString());
            System.out.println(); // spazio tra righe
        }
    }

    public String convertImageID(int imageID){
        return String.format("%03d", imageID);
    }

    public void initializeShipboard(boolean firstFlight) {
        for (int i = 0; i < 5; i++) {
            List<Component> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                row.add(new Component("000"));
            }
            assembledComponents.add(row);
        }

        if(firstFlight) {
            assembledComponents.getFirst().set(0, new Component("003"));
            assembledComponents.getFirst().set(1, new Component("003"));
            assembledComponents.getFirst().set(2, new Component("003"));
            assembledComponents.getFirst().set(4, new Component("003"));
            assembledComponents.getFirst().set(5, new Component("003"));
            assembledComponents.getFirst().set(6, new Component("003"));
            assembledComponents.get(1).set(0, new Component("003"));
            assembledComponents.get(1).set(1, new Component("003"));
            assembledComponents.get(1).set(5, new Component("003"));
            assembledComponents.get(1).set(6, new Component("003"));
            assembledComponents.get(2).set(0, new Component("003"));
            assembledComponents.get(2).set(6, new Component("003"));
            assembledComponents.get(3).set(0, new Component("003"));
            assembledComponents.get(3).set(6, new Component("003"));
            assembledComponents.get(4).set(0, new Component("003"));
            assembledComponents.get(4).set(3, new Component("003"));
            assembledComponents.get(4).set(6, new Component("003"));
        }
        else{
            assembledComponents.getFirst().set(0, new Component("003"));
            assembledComponents.getFirst().set(1, new Component("003"));
            assembledComponents.getFirst().set(3, new Component("003"));
            assembledComponents.getFirst().set(5, new Component("003"));
            assembledComponents.getFirst().set(6, new Component("003"));
            assembledComponents.get(1).set(0, new Component("003"));
            assembledComponents.get(1).set(6, new Component("003"));
            assembledComponents.get(4).set(3, new Component("003"));
        }

        if(color == Color.BLUE){
            assembledComponents.get(2).set(3, new Component("318"));
        }
        else if(color == Color.GREEN){
            assembledComponents.get(2).set(3, new Component("319"));
        }
        else if(color == Color.RED){
            assembledComponents.get(2).set(3, new Component("320"));
        }
        else{
            assembledComponents.get(2).set(3, new Component("321"));
        }
    }

    public void updateStartAssembling(){
        gameState = "START ASSEMBLING";
    }

    public void updatePickedComponent(int imageID, boolean released) {
        if(released){
            pickedComponent = null;
        }
        else{
            pickedComponent = new Component(convertImageID(imageID));
        }
    }

    public void updateShownComponent(int imageID, boolean released) {
        if(released){
            shownComponents.add(new Component(convertImageID(imageID)));
        }
        else{
            for(Component comp : shownComponents){
                if(comp.getImageID().equals(convertImageID(imageID))){
                    shownComponents.remove(comp);
                    break;
                }
            }
        }
    }

    public void updateReservedComponent(String nickname, int imageID, boolean released) {
        if(!nickname.equals(this.nickname)){
            return;
        }
        if(released){
            reservedComponents.add(new Component(convertImageID(imageID)));
        }
        else{
            for(Component comp : reservedComponents){
                if(comp.getImageID().equals(convertImageID(imageID))){
                    reservedComponents.remove(comp);
                    break;
                }
            }
        }
    }

    public void updateRotatePickedComponent() {
        pickedComponent.rotateLeft();
    }

    public void updateAssembledComponent(String nickname, int imageID, Orientation orientation, int x, int y) {
        if(!nickname.equals(this.nickname)){
            return;
        }
        assembledComponents.get(x).set(y, new Component(convertImageID(imageID)));
        assembledComponents.get(x).get(y).setOrientation(orientation);
    }

    public void updatePickedDeck(List<Integer> deckIDs) {}

    public void updateReleasedDeck() {}

    public void updateFinishAssembling() {
        gameState = "FINISHED ASSEMBLING, WAITING FOR OTHER PLAYERS";
    }

    public void updateShipControl() {
        gameState = "SHIP CONTROL";
    }
}
