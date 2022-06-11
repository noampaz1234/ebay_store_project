package store_project.stores;

import store_project.items.Item;

import java.util.Map;
import java.util.SortedSet;

public class StoreServices {

    /**
     * check if an item already exists in the treeset
     */
    public static boolean foundItem(Item obj, Map<String, SortedSet<Item>> key_to_itemSet)
    {
        if (!key_to_itemSet.containsKey(obj.getKey()))
            return false;

        for (Item item: key_to_itemSet.get(obj.getKey()))
            return (item.compareTo(obj) == 0);
        return false;
    }
}
