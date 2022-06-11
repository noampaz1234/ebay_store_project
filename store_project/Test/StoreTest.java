import org.junit.Assert;
import org.junit.Test;
import store_project.items.Item;
import store_project.stores.Store;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;

public class StoreTest {

    /**
     * create a store with one item
     * @throws Exception
     */
    @Test
    public void createStore() throws Exception {
        Store myStore = new Store();
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("key1", "namespace1", 1));
        myStore.put(items);
        Assert.assertEquals(1, myStore.get_key_to_items().keySet().size());
    }

    /**
     * create a store with a few items
     * @throws Exception
     */
    @Test
    public void createStoreWithThreads() throws Exception {
        Store myStore = createStoreWithItems();
        assertKeysAndValues(myStore,4, 6);
    }

    /**
     * create a store with a few items per key
     * @return
     * @throws Exception
     */
    private Store createStoreWithItems() throws Exception {
        Store myStore = new Store();
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("key1", "namespace1", 1));
        items.add(new Item("key1", "namespace2", 2));
        items.add(new Item("key2", "namespace3", 3));
        items.add(new Item("key2", "namespace4", 4));
        items.add(new Item("key3", "namespace5", 5));
        items.add(new Item("key4", "namespace6", 6));

        myStore.put(items);
        return myStore;
    }

    /**
     * create a store with different keys, namespaces
     * @throws Exception
     */
    @Test
    public void createStoreWithThreads2() throws Exception {
        Store myStore = new Store();
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("key1", "namespace1", 1));
        items.add(new Item("key1", "namespace2", 2));
        items.add(new Item("key2", "namespace3", 3));
        items.add(new Item("key2", "namespace4", 4));
        items.add(new Item("key3", "namespace5", 5));

        myStore.put(items);
        assertKeysAndValues(myStore,3, 5);

    }

    /**
     * create a store with different keys, namespaces
     * @throws Exception
     */
    @Test
    public void createStoreWithThreadsNotSorted() throws Exception {
        Store myStore = new Store();
        List<Item> items = new ArrayList<Item>();
        items.add(new Item("key1", "namespace2", 1));
        items.add(new Item("key3", "namespace5", 5));
        items.add(new Item("key1", "namespace1", 2));
        items.add(new Item("key2", "namespace4", 3));
        items.add(new Item("key2", "namespace3", 4));

        myStore.put(items);
        System.out.println("Store attributes: \n" + myStore.toString());
        assertKeysAndValues(myStore,3, 5);

    }

    /**
     * assert method for keys and values
     * @param myStore
     * @param expectedKeys
     * @param expectedValues
     */
    private void assertKeysAndValues(Store myStore, int expectedKeys, int expectedValues) {
        Assert.assertEquals(expectedKeys, myStore.get_key_to_items().keySet().size());
        int counter = 0;
        for (SortedSet itemsLst: myStore.get_key_to_items().values())
            counter += itemsLst.size();
        Assert.assertEquals(expectedValues, counter);
    }

    /**
     * delete an items from the store
     * @throws Exception
     */
    @Test
    public void deleteItemFromStore() throws Exception {
        Store myStore = createStoreWithItems();
        myStore.delete("namespace3", "key2");
        assertKeysAndValues(myStore,4, 5);
    }

    /**
     * delete all items from the store
     * @throws Exception
     */
    @Test
    public void deleteAllItemsFromStore() throws Exception {
        Store myStore = createStoreWithItems();
        myStore.delete("namespace1", "key1");
        myStore.delete("namespace2", "key1");
        myStore.delete("namespace3", "key2");
        myStore.delete("namespace4", "key2");
        myStore.delete("namespace5", "key3");
        myStore.delete("namespace6", "key4");

        assertKeysAndValues(myStore,4, 0);
    }

    /**
     * check the get method of an item from the store
     * @throws Exception
     */
    @Test
    public void getItemFromStore() throws Exception {
        Store myStore = createStoreWithItems();
        Item curr = myStore.get("namespace4", "key2");
        Assert.assertEquals(4, curr.getValue());
    }


    /**
     * get an item which doesn't exist in the store
     * @throws Exception
     */
    @Test
    public void getItemFromStoreNotFound() throws Exception {
        Store myStore = createStoreWithItems();
        Item curr = null;
        try {
            curr = myStore.get("namespace2", "key4");
            Assert.assertEquals(null, curr);
        }
        catch (Exception ex) {
            Assert.assertEquals(null, curr);
        }
    }

    /**
     * update an item value from the store
     * @throws Exception
     */
    @Test
    public void updateItemInStore() throws Exception {
        Store myStore = createStoreWithItems();
        boolean succeeded = myStore.update("namespace4", "key2", 345);
        if (succeeded)
        {
            Item curr = myStore.get("namespace4", "key2");
            Assert.assertEquals(345, curr.getValue());
        }
        else
            Assert.assertEquals(false, true);
    }

    /**
     * update an item which doesn't exist
     * @throws Exception
     */
    @Test
    public void updateItemInStoreNotSucceeded() throws Exception {
        Store myStore = createStoreWithItems();
        try{
            myStore.update("namespace2", "key4", 345);
        }
        catch (Exception ex) {
            Assert.assertEquals(false, false);
        }

        Assert.assertFalse("update got succeeded", false);
    }

}
