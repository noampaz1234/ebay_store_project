package store_project.threads;

import store_project.items.Item;
import store_project.stores.StoreServices;

import java.util.*;
import java.util.concurrent.Callable;

/**
 * thread for putting items into the store
 */
public class PutItemsInStoreThread implements Callable<Void> {

    private List<Item> items = new ArrayList<Item>();
    private Map<String, SortedSet<Item>> key_to_itemSet;

    public PutItemsInStoreThread(List<Item> items, Map<String, SortedSet<Item>> key_to_itemSet) {
        System.out.println("Starting thread with " + items.size() + " items");
        this.items = items;
        this.key_to_itemSet = key_to_itemSet;
    }

    @Override
    public Void call() throws Exception {
        for (Item item : items) {
            put(item);
            System.out.println("PutItemsInStoreThread: finished adding one item");
        }
        System.out.println("PutItemsInStoreThread: finished adding all items");
        return null;
    }

    private void put(Item obj) throws Exception {
        if (obj == null)
            throw new Exception("object is not found");

        if (StoreServices.foundItem(obj, key_to_itemSet))
            throw new Exception("object is already in the store");

        // add into existing tree set, or create a new tree set
        if (key_to_itemSet.containsKey(obj.getKey()))
            key_to_itemSet.get(obj.getKey()).add(obj);
        else{
            TreeSet<Item> treeSet = new TreeSet<Item>();
            treeSet.add(obj);
            key_to_itemSet.put(obj.getKey(), treeSet);
        }
    }
}
