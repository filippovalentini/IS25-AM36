package it.polimi.ingsw.galaxytrucker.ui.view;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
/** * This class is used to convert image IDs to Extended IDs (EIDs) and card descriptions.
 */
public class ImageIDToStringConverter {
    /*
        Used to retrieve the Extended ID(EID) of a component

        EID rules:
        String composed of XXYYYY
        XX: (component types)
            BD: Battery Double
            BS: Battery Single
            CA: CAbin
            CD: Cannon Double
            CS: Cannon Single
            ED: Engine Double
            EP: EmPty
            ES: Engine Single
            HD: cargo Hold Double
            HT: cargo Hold Triple
            LB: Life support Brown
            LP: Life support Purple
            SD: Special cargo hold Double
            SH: Shield
            SP: SPace
            SS: Special cargo hold Single
            ST: STructural
        Y: (connectors starting from the corresponding orientation side and going clockwise)
            0: Smooth
            1: Single
            2: Double
            3: Universal
     */
    private static final ImageIDToStringConverter instance = new ImageIDToStringConverter();

    /**
     * Private constructor to prevent instantiation.
     */
    private ImageIDToStringConverter() {
        idToEIDmap = jsonToMap("/it/polimi/ingsw/galaxytrucker/jsonImageMappings/componentsEID.json"); //loads EIDs from json
        idToCardDesc = jsonToMap("/it/polimi/ingsw/galaxytrucker/jsonImageMappings/cardDescriptions.json");
    }
    private static Map<String, String> idToEIDmap;
    private static Map<String, String> idToCardDesc;

    /**
     * Returns the singleton instance of ImageIDToStringConverter.
     * @return the singleton instance of ImageIDToStringConverter
     */
    public static ImageIDToStringConverter getInstance() {
        return instance;
    }

    /**
     * Converts a JSON file to a Map<String, String>.
     * @param path
     * @return a Map where keys are image IDs and values are either EIDs or card descriptions
     */
    private Map<String, String> jsonToMap(String path) {
        Map<String, String> result = new HashMap<>();
        try (InputStream jsonStream = getClass().getResourceAsStream(path)) {
            if (jsonStream == null) {
                System.err.println(path + " non trovato!");
                return result;
            }
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> idToPath = mapper.readValue(jsonStream, Map.class);
            for (Map.Entry<String, String> entry : idToPath.entrySet()) {
                String id = entry.getKey();
                String eid = entry.getValue();
                result.put(id, eid);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
        //converts ID to EID

    /**
     * Converts an image ID to its corresponding Extended ID (EID).
     * @param imageID
     * @return the Extended ID (EID) corresponding to the image ID, or "error" if not found
     */
        public static String imageIDtoEID(String imageID){
            String retStr = "error";
            if(idToEIDmap.containsKey(String.valueOf(imageID))){
                retStr = idToEIDmap.get(String.valueOf(imageID));
            }
            return retStr;
        }

        //converts ID to card description

    /**
     * Converts an image ID to its corresponding card description.
     * @param imageID
     * @return the card description corresponding to the image ID, or "error" if not found
     */
        public static String imageIDtoCardDesc(String imageID){
            String retStr = "error";
            if(idToCardDesc.containsKey(String.valueOf(imageID))){
                retStr = idToCardDesc.get(String.valueOf(imageID));
            }
            return retStr;
        }
    }
