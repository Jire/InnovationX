package src.innovationx.classic.model.items;

public class ItemList {

    public ItemList(int _itemId) {
        itemId = _itemId;
    }
    public int itemId;
    public String itemName;
    public String itemDescription;
    public double ShopValue;
    public double LowAlch;
    public double HighAlch;
    public int[] Bonuses = new int[14];
}
