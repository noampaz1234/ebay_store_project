package store_project.stores;

import store_project.items.Item;
import store_project.threads.PutItemsInStoreThread;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Executors;

// assumption: no lower case/ capital case issues

/**
 * Store: holds the following attribute:
 * concurrentKeyToItemSet - key to set of items with the same key.
 *      using concurrent in order to keep thread safety
 */
public class Store implements StoreInterface {

    final int threads = 2;
    final int itemsInThread = 3;

    private Map<String, SortedSet<Item>> concurrentKeyToItemSet = new ConcurrentHashMap<String, SortedSet<Item>>();

    public Store(){
        concurrentKeyToItemSet = new ConcurrentHashMap<String, SortedSet<Item>>();
    }

    public Map<String, SortedSet<Item>> get_key_to_items(){
        return concurrentKeyToItemSet;
    }

    /**
     * put all items inside a store using multi-threading
     */
    @Override
    public void put(List<Item> itemsLst) throws Exception{
        // how many items we should put per thread
        final int itemsPerThread = Math.min((int)Math.ceil(itemsLst.size() / (0.0 + threads)), itemsInThread);

        final CompletionService<Void> completionService =
                new ExecutorCompletionService<>(
                        Executors.newFixedThreadPool(threads));

        // run over sub lists of items and put them into the store items
        int threadsCounter = 0;
        int itemsToCount = Math.min(itemsPerThread, itemsLst.size());
        for (int from = 0; from < itemsLst.size(); from += itemsToCount) {
            itemsToCount = Math.min(itemsPerThread, itemsLst.size() - from);
            completionService.submit(new PutItemsInStoreThread(itemsLst.subList(from, from + itemsToCount ), concurrentKeyToItemSet));
            threadsCounter ++;
        }

        System.out.println("finished adding all items");

        for (int i = 0; i < threadsCounter; i++) {
            completionService.take().get();
        }
    }

    /**
     * deleting an item from the store
     * @param namespace
     * @param key
     * @throws Exception
     */
    @Override
    public void delete(String namespace, String key) throws Exception {
        if (concurrentKeyToItemSet.containsKey(key)) {
            SortedSet<Item> treeSet = concurrentKeyToItemSet.get(key);
            treeSet.removeIf(x -> x.getNamespace().equals(namespace));
        }
        else
            throw new Exception("key was not found");

    }

    /**
     * get an item from the store items
     * @param namespace
     * @param key
     * @return
     * @throws Exception
     */
    @Override
    public Item get(String namespace, String key) throws Exception {
        if (concurrentKeyToItemSet.containsKey(key)) {
            SortedSet<Item> treeSet = concurrentKeyToItemSet.get(key);
            Item findFirst = getItem(namespace, treeSet);
            if (findFirst != null) return findFirst;
        }

        throw new Exception("item was not found");

    }

    private Item getItem(String namespace, SortedSet<Item> treeSet) {
        Optional<Item> findFirst = treeSet.stream().filter(e -> e.getNamespace().equals(namespace)).findFirst();
        if (findFirst.isPresent()) {
            return findFirst.get();
        }
        return null;
    }

    /**
     * update a value of item per namespace and key
     * @param namespace
     * @param key
     * @param value
     * @return
     * @throws Exception
     */
    @Override
    public boolean update(String namespace, String key, Serializable value) throws Exception {
        Item curr = this.get(namespace, key);
        if (curr != null) {
            curr.setValue(value);
            return true;
        }

        throw new Exception("item was not found");

    }

    @Override
    public String toString(){
        StringBuffer str =  new StringBuffer();
        for (SortedSet<Item> itemSet: concurrentKeyToItemSet.values()) {
            for (Item itm: itemSet)
                str.append(itm.toString() + "\n");
        }
        return str.toString();
    }

}
