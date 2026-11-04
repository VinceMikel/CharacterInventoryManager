package inventory;

import java.util.Locale;
import java.util.Objects;

/** Custom value object describing one item in a character's inventory. */
public final class InventoryItem {
    private final String itemId;
    private final String itemName;
    private final String category;
    private final int quantity;
    private final String rarity;
    private final boolean equipped;

    public InventoryItem(String itemId, String itemName, String category,
                         int quantity, String rarity, boolean equipped) {
        this.itemId = requireText(itemId, "Item ID").toUpperCase(Locale.ROOT);
        this.itemName = requireText(itemName, "Item name");
        this.category = requireText(category, "Category");
        if (quantity < 1) {
            throw new IllegalArgumentException("Quantity must be at least 1.");
        }
        this.quantity = quantity;
        this.rarity = requireText(rarity, "Rarity");
        this.equipped = equipped;
    }

    public String getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getCategory() {
        return category;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getRarity() {
        return rarity;
    }

    public boolean isEquipped() {
        return equipped;
    }

    @Override
    public String toString() {
        return itemId + " | " + itemName + " | " + category
                + " | qty: " + quantity + " | " + rarity
                + (equipped ? " | equipped" : "");
    }

    private static String requireText(String value, String fieldName) {
        Objects.requireNonNull(value, fieldName + " cannot be null.");
        String trimmed = value.trim();
        if (trimmed.isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be blank.");
        }
        return trimmed;
    }
}
