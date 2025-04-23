package it.polimi.ingsw.galaxytrucker.view;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;

public class ViewPlayer {
    private String nickname;
    private Color color;
    int credits;
    int lostComponents;
    private List<List<ViewComponent>> assembledComponents = new ArrayList<>();
    private List<ViewComponent> reservedComponents = new ArrayList<>();

    public ViewPlayer(String nickname, Color color, boolean firstFlight) {
        this.nickname = nickname;
        this.color = color;
        this.credits = 0;
        this.lostComponents = 0;
        initializeShipboard(firstFlight);
    }

    public String getNickname() {
        return nickname;
    }

    public Color getColor() {
        return color;
    }

    public int getCredits() {
        return credits;
    }

    public int getLostComponents() {
        return lostComponents;
    }

    public void visualize(){
        if (!reservedComponents.isEmpty()) {
            System.out.print("📦 Reserved Components: ");
            for (ViewComponent comp : reservedComponents) {
                System.out.print("[" + comp.getImageID() + "] ");
            }
            System.out.println();
        }

        System.out.println("\n🛰️  Ship Layout:\n");

        for (List<ViewComponent> row : assembledComponents) {
            StringBuilder top = new StringBuilder();
            StringBuilder middle = new StringBuilder();
            StringBuilder bottom = new StringBuilder();

            for (ViewComponent comp : row) {
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

    public void initializeShipboard(boolean firstFlight) {
        for (int i = 0; i < 5; i++) {
            List<ViewComponent> row = new ArrayList<>();
            for (int j = 0; j < 7; j++) {
                row.add(new ViewComponent("000"));
            }
            assembledComponents.add(row);
        }

        if(firstFlight) {
            assembledComponents.getFirst().set(0, new ViewComponent("003"));
            assembledComponents.getFirst().set(1, new ViewComponent("003"));
            assembledComponents.getFirst().set(2, new ViewComponent("003"));
            assembledComponents.getFirst().set(4, new ViewComponent("003"));
            assembledComponents.getFirst().set(5, new ViewComponent("003"));
            assembledComponents.getFirst().set(6, new ViewComponent("003"));
            assembledComponents.get(1).set(0, new ViewComponent("003"));
            assembledComponents.get(1).set(1, new ViewComponent("003"));
            assembledComponents.get(1).set(5, new ViewComponent("003"));
            assembledComponents.get(1).set(6, new ViewComponent("003"));
            assembledComponents.get(2).set(0, new ViewComponent("003"));
            assembledComponents.get(2).set(6, new ViewComponent("003"));
            assembledComponents.get(3).set(0, new ViewComponent("003"));
            assembledComponents.get(3).set(6, new ViewComponent("003"));
            assembledComponents.get(4).set(0, new ViewComponent("003"));
            assembledComponents.get(4).set(3, new ViewComponent("003"));
            assembledComponents.get(4).set(6, new ViewComponent("003"));
        }
        else{
            assembledComponents.getFirst().set(0, new ViewComponent("003"));
            assembledComponents.getFirst().set(1, new ViewComponent("003"));
            assembledComponents.getFirst().set(3, new ViewComponent("003"));
            assembledComponents.getFirst().set(5, new ViewComponent("003"));
            assembledComponents.getFirst().set(6, new ViewComponent("003"));
            assembledComponents.get(1).set(0, new ViewComponent("003"));
            assembledComponents.get(1).set(6, new ViewComponent("003"));
            assembledComponents.get(4).set(3, new ViewComponent("003"));
        }

        if(color == Color.BLUE){
            assembledComponents.get(2).set(3, new ViewComponent("318"));
        }
        else if(color == Color.GREEN){
            assembledComponents.get(2).set(3, new ViewComponent("319"));
        }
        else if(color == Color.RED){
            assembledComponents.get(2).set(3, new ViewComponent("320"));
        }
        else{
            assembledComponents.get(2).set(3, new ViewComponent("321"));
        }
    }

    public void updateReservedComponent(int imageID, boolean released) {
        if(released){
            reservedComponents.add(new ViewComponent(View.convertImageID(imageID)));
        }
        else{
            for(ViewComponent comp : reservedComponents){
                if(comp.getImageID().equals(View.convertImageID(imageID))){
                    reservedComponents.remove(comp);
                    break;
                }
            }
        }
    }

    public void updateAssembledComponent(int imageID, Orientation orientation, int x, int y) {
        assembledComponents.get(x).set(y, new ViewComponent(View.convertImageID(imageID)));
        assembledComponents.get(x).get(y).setOrientation(orientation);
    }

    public void updateDestroyedComponent(String nickname, int x, int y) {
        assembledComponents.get(x).set(y, new ViewComponent("000"));
    }

    public void loseReservedComponents() {
        lostComponents+=reservedComponents.size();
    }

    public void loseComponent(){
        lostComponents++;
    }

}
