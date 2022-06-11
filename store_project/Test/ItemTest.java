import org.junit.Assert;
import org.junit.Test;
import store_project.items.Item;

public class ItemTest {

    /**
     * get the item attributes after creating the item
     * @throws Exception
     */
    @Test
    public void getItem() throws Exception {
        Item myItem = new Item("myItemkey", "myItemNameSpace", "myItemValue");
        Assert.assertEquals("myItemkey",myItem.getKey());
        Assert.assertEquals("myItemNameSpace",myItem.getNamespace());
        Assert.assertEquals("myItemValue",myItem.getValue());
    }

    /**
     * compare two equal items
     * @throws Exception
     */
    @Test
    public void compareEqualItems() throws Exception{
        Item item1 = new Item("1", "2", "value1");
        Item item2 = new Item("1", "2", "value2");
        Assert.assertEquals(0, item1.compareTo(item2));
    }

    /**
     * compare two non-equal items
     * @throws Exception
     */
    @Test
    public void compareDifferentKeyItems() throws Exception{
        Item item1 = new Item("1", "2", "value1");
        Item item2 = new Item("2", "2", "value2");
        Assert.assertEquals(-1, item1.compareTo(item2));
    }


    /**
     * compare two items with same key but different namespaces
     * @throws Exception
     */
    @Test
    public void compareDifferentNamespaceItems() throws Exception{
        Item item1 = new Item("1", "2", "value1");
        Item item2 = new Item("1", "3", "value2");
        Assert.assertEquals(-1, item1.compareTo(item2));
    }
}
