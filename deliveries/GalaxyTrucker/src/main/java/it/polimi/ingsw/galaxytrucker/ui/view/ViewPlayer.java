package it.polimi.ingsw.galaxytrucker.ui.view;

import it.polimi.ingsw.galaxytrucker.model.enumerations.Color;
import it.polimi.ingsw.galaxytrucker.model.enumerations.Orientation;

import java.util.ArrayList;
import java.util.List;

public class ViewPlayer {
    private String nickname;
    private Color color;
    int credits;
    int lostComponents;
    boolean abandoned;
    private List<List<ViewComponent>> assembledComponents = new ArrayList<>();
    private List<ViewComponent> reservedComponents = new ArrayList<>();

    public ViewPlayer(String nickname, Color color, boolean firstFlight) {
        this.nickname = nickname;
        this.color = color;
        this.credits = 0;
        this.lostComponents = 0;
        this.abandoned = false;
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

    public boolean hasAbandoned() {
        return abandoned;
    }

    public int getLostComponents() {
        return lostComponents;
    }

    public List<List<ViewComponent>> getAssembledComponents() {
        return new ArrayList<>(assembledComponents);
    }

    public List<ViewComponent> getReservedComponents() {
        return new ArrayList<>(reservedComponents);
    }

    public void visualize(){
        if (!reservedComponents.isEmpty()) {
            System.out.print("📦 Reserved Components: ");
            for (ViewComponent comp : reservedComponents) {
                System.out.print("[" + comp.toString() + "] ");
            }
            System.out.println();
        }

        if(abandoned){
            System.out.println("\n*** ABANDONED THE FLIGHT ***\n");
        }

        System.out.println("\n🛰️  Ship Layout:\n");

        for (List<ViewComponent> row : assembledComponents) {
            StringBuilder top = new StringBuilder();
            StringBuilder middle1 = new StringBuilder();
            StringBuilder middle2 = new StringBuilder();
            StringBuilder bottom = new StringBuilder();

            for (ViewComponent comp : row) {
                top.append("╔═══════╗ ");

                char ori = comp.getOrientation().toString().charAt(0);
                String id = String.valueOf(comp.getImageID());
                String eid = ImageIDToEIDConverter.imageIDtoEID(String.valueOf(comp.getImageID()));
                String content1="";
                String content2="";
                if(id.equals("000")){
                    content1 ="     ";
                    content2 ="     ";
                }
                else if(id.equals("003")){
                    content1 ="/////";
                    content2 ="/////";
                }
                else{
                    content1 = ori + eid; //ZYYYYYY where Z is the orientation and Y...Y the EID
                    content2 = generateComponentGraphics(comp);
                }

                content1 = String.format("%-4s", content1); // padding for align

                middle1.append("║" + content1 + "║ "); //is wider
                middle2.append("║ " + content2 + " ║ ");
                bottom.append("╚═══════╝ ");
            }

            System.out.println(top.toString());
            System.out.println(middle1.toString());
            System.out.println(middle2.toString());
            System.out.println(bottom.toString());
            System.out.println(); // space between lines
        }
    }

    public String generateComponentGraphics(ViewComponent comp){
        if (comp.getCrew() == 1) {
            return "C    ";
        }
        else if (comp.getCrew() == 2) {
            return "CC   ";
        }
        else if (comp.isBrownAlien()) {
            return "AB   ";
        }
        else if (comp.isPurpleAlien()) {
            return "AP   ";
        }
        else if(comp.getBatteries() == 1){
            return "B    ";
        }
        else if(comp.getBatteries() == 2){
            return "BB   ";
        }
        else if(comp.getBatteries() == 3){
            return "BBB  ";
        }
        else if(!comp.getGoods().isEmpty()){
            List<Color> goods = comp.getGoods();
            String goodsString = "";
            for (Color good : goods) {
                goodsString = goodsString + Color.convertColorIntoLetter(good);
            }
            int numberGoods = comp.getGoods().size();
            if (numberGoods == 1) {
                return goodsString + "    ";
            }
            else if (numberGoods == 2) {
                return goodsString + "   ";
            }
            else{
                return goodsString + "  ";
            }
        }
        else{
            return "     ";
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

    public void updateDestroyedComponent(int x, int y) {
        assembledComponents.get(x).set(y, new ViewComponent("000"));
    }

    public void updateShipControl(){
        reservedComponents.clear();
    }

    public void loseReservedComponents() {
        lostComponents+=reservedComponents.size();
    }

    public void loseComponent(){
        lostComponents++;
    }

    public void updateAlienChange(int x, int y, boolean isPurple) {
        assembledComponents.get(x).get(y).updateAlien(isPurple);
    }

    public void updateCrewChange(int x, int y, int change) {
        assembledComponents.get(x).get(y).updateCrew(change);
    }

    public void updateBatteries(int x, int y, int change) {
        assembledComponents.get(x).get(y).updateBatteries(change);
    }

    public void updateCredits(int change) {
        this.credits+=change;
    }

    public void updateLoadedGood(int x, int y, Color good) {
        assembledComponents.get(x).get(y).loadGood(good);
    }

    public void updateRemovedGoods(int x, int y, Color good, int numberGoods) {
        for(int i = 0; i<numberGoods; i++){
            assembledComponents.get(x).get(y).removeGood(good);
        }
    }

    public void updateQuit(){
        abandoned = true;
    }

}
