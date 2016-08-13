package getfluxed.fluxedcrystals.api.registries;

import getfluxed.fluxedcrystals.api.crystals.Crystal;

import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import static getfluxed.fluxedcrystals.api.crystals.Crystal.NULL;

/**
 * Created by Jared on 4/30/2016.
 */
public class CrystalRegistry {

    private static TreeMap<String, Crystal> crystalMap = new TreeMap<>();

    public static void register(Crystal crystal){
        crystalMap.put(crystal.getName(), crystal);
    }

    public static Crystal getCrystal(String name){
        return getCrystalMap().get(name);
    }

    public static  TreeMap<String, Crystal> getCrystalMap() {
        return crystalMap;
    }


    private static boolean editing = false;


    public static boolean isEditing(){
        return  editing;
    }

    public static void setEditing(boolean editing) {
        CrystalRegistry.editing = editing;
    }


    public static Crystal getRandomCrystal(){
        Crystal crystal = NULL;
        Random rand = new Random();
        for (Map.Entry<String, Crystal> ent : crystalMap.entrySet()) {
            if(rand.nextInt(crystalMap.size())==0){
                crystal = ent.getValue();
            }
        }

        return crystal;
    }
}
