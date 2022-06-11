package store_project.stores;

import store_project.items.Item;

import java.io.Serializable;
import java.util.*;

public interface StoreInterface {

    public void put(List<Item> itemsLst) throws Exception;

    public void delete(String namespace, String key) throws Exception;

    public Item get(String namespace, String key) throws Exception;

    public boolean update(String namespace, String key, Serializable value) throws Exception;

    public String toString();
}


