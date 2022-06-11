package store_project.items;

import java.io.Serializable;
import java.util.Comparator;

/**
 * Item - holds 3 attributes:
 * 1. key
 * 2. namespace
 * 3. value
 */
public class Item implements ItemInterface, Comparable<Item> {

    private String key;
    private String namespace;
    private Serializable value;

    public Item(String key, String namespace, Serializable value) {
        this.key = key;
        this.namespace = namespace;
        this.value = value;
    }

    public Serializable getValue() {
        return value;
    }

    public void setValue(Serializable value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }


    private static int compareString(String a, String b) {
        return a.toLowerCase().compareTo(b.toLowerCase());
    }

    /**
     * compareTo is used for sorting the items by Comparable
     * @param item2 the object to be compared.
     * @return
     */
    @Override
    public int compareTo(Item item2) {
        if (this.getKey() != item2.getKey())
            return compareString(this.getKey(), item2.getKey());

        return compareString(this.getNamespace(), item2.getNamespace());
    }

    @Override
    public String toString(){
        return this.getKey() + ": " + this.getNamespace() + ": " + this.getValue();
    }
}
