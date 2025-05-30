package it.polimi.ingsw.galaxytrucker.ui.gui;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Classe che legge un file di testo e crea una mappa imageIDmap
 * con codici come chiavi e nomi file come valori
 */
public class ImageManager {

    private Map<Integer, String> imageIDmap;

    public ImageManager() {
        imageIDmap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/it/polimi/ingsw/galaxytrucker/images/images_mapping.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Salta righe vuote e righe che sono solo intestazioni
                if (line.isEmpty() ||
                        line.equals("SHIP BOARDS") ||
                        line.equals("CARDS") ||
                        line.equals("COMPONENTS")) {
                    continue;
                }

                // Cerca righe con formato "numero\tfilename.jpg"
                String[] parts = line.split("\t");
                if (parts.length == 2) {
                    try {
                        Integer code = Integer.parseInt(parts[0].trim());
                        String filename = parts[1].trim();
                        imageIDmap.put(code, filename);
                    } catch (NumberFormatException e) {
                        // Ignora righe con formato non valido
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Errore nella lettura del file: " + e.getMessage());
        }
    }

    public Map<Integer, String> getImageIDMap() {
        return imageIDmap;
    }

    public boolean containsCode(int code) {
        return imageIDmap.containsKey(code);
    }

    public void printMap() {
        System.out.println("=== imageIDmap (" + imageIDmap.size() + " elementi) ===");
        imageIDmap.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry ->
                        System.out.println(entry.getKey() + " -> " + entry.getValue()));
    }

}