package lommie.onlycraftonce.yacl;

import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public record MaxCraftableEntry(Item item,int max) {
    static public HashMap<Item,Integer> toMap(List<MaxCraftableEntry> entries){
        HashMap<Item,Integer> out = new HashMap<>(entries.size());
        entries.forEach((e) -> {
            out.put(e.item,e.max);
        });
        return out;
    }

    static public List<MaxCraftableEntry> toList(HashMap<Item,Integer> map){
        ArrayList<MaxCraftableEntry> out = new ArrayList<>(map.size());
        map.forEach((i,max) -> {
            out.add(new MaxCraftableEntry(i, max));
        });
        return out;
    }
}
