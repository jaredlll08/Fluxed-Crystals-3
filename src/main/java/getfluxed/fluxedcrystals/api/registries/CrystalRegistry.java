package getfluxed.fluxedcrystals.api.registries;

import getfluxed.fluxedcrystals.api.registries.crystal.Crystal;

import java.util.TreeMap;

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
}
