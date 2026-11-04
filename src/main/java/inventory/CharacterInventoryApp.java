package inventory;

import java.util.ArrayList;
import java.util.Scanner;


public class CharacterInventoryApp {
    private static final CustomHashMap<CharacterProfile, ArrayList<InventoryItem>> CHARACTERS =
            new CustomHashMap<>();
    private static final Scanner INPUT = new Scanner(System.in);

    public static void main(String[] args) {
        loadStartingData();
        System.out.println("Character Inventory Manager");
        runMenu();
    }

    private static void loadStartingData() {
        CharacterProfile aria = new CharacterProfile("C-101", "Aria", "Eldoria");
        ArrayList<InventoryItem> ariaItems = new ArrayList<>();
        ariaItems.add(new InventoryItem("I-1", "Iron Sword", "Weapon", 1, "Common", true));
        ariaItems.add(new InventoryItem("I-2", "Healing Potion", "Consumable", 5, "Common", false));
        CHARACTERS.put(aria, ariaItems);

        CharacterProfile toren = new CharacterProfile("C-202", "Toren", "Eldoria");
        ArrayList<InventoryItem> torenItems = new ArrayList<>();
        torenItems.add(new InventoryItem("I-3", "Oak Shield", "Armor", 1, "Uncommon", true));
        torenItems.add(new InventoryItem("I-4", "Silver Key", "Quest Item", 1, "Rare", false));
        CHARACTERS.put(toren, torenItems);

        CharacterProfile luna = new CharacterProfile("C-303", "Luna", "Starfall Online");
        CHARACTERS.put(luna, new ArrayList<>());
    }

    private static void runMenu() {
        boolean running = true;
        while (running) {
            printMenu();
            String choice = readNonEmpty("Choose an option: ");
            switch (choice) {
                case "1" -> listAllInventories();
                case "2" -> addCharacter();
                case "3" -> addItem();
                case "4" -> updateItem();
                case "5" -> deleteItem();
                case "6" -> showInventory();
                case "7" -> deleteCharacter();
                case "0" -> {
                    running = false;
                    System.out.println("Goodbye.");
                }
                default -> System.out.println("Please choose one of the listed options.");
            }
        }
    }

    private static void printMenu() {
        System.out.println();
        System.out.println("1. List all characters and their items");
        System.out.println("2. Add a character");
        System.out.println("3. Add an item to a character");
        System.out.println("4. Update an item");
        System.out.println("5. Delete an item");
        System.out.println("6. Find a character and show its inventory");
        System.out.println("7. Delete a character and its inventory");
        System.out.println("0. Exit");
    }

    private static void listAllInventories() {
        if (CHARACTERS.isEmpty()) {
            System.out.println("There are no characters.");
            return;
        }
        for (CustomHashMap.MapEntry<CharacterProfile, ArrayList<InventoryItem>> entry
                : CHARACTERS.entrySet()) {
            printInventory(entry.getKey(), entry.getValue());
        }
    }

    private static void addCharacter() {
        String id = readNonEmpty("Character ID: ");
        CharacterProfile lookupKey = lookupKey(id);
        if (CHARACTERS.containsKey(lookupKey)) {
            System.out.println("A character with that ID already exists.");
            return;
        }

        CharacterProfile profile = new CharacterProfile(
                id,
                readNonEmpty("Character name: "),
                readNonEmpty("Game title: "));
        CHARACTERS.put(profile, new ArrayList<>());
        System.out.println("Character added.");
    }

    private static void addItem() {
        String characterId = promptForExistingCharacter();
        if (characterId == null) {
            return;
        }

        ArrayList<InventoryItem> items = inventoryFor(characterId);
        String itemId = readNonEmpty("Item ID: ");
        if (findItemIndex(items, itemId) >= 0) {
            System.out.println("That item ID is already used for this character.");
            return;
        }

        InventoryItem item = new InventoryItem(
                itemId,
                readNonEmpty("Item name: "),
                readNonEmpty("Category: "),
                readPositiveInt("Quantity: "),
                readNonEmpty("Rarity: "),
                readYesNo("Is it equipped? (y/n): "));
        items.add(item);
        System.out.println("Item added.");
    }

    private static void updateItem() {
        String characterId = promptForExistingCharacter();
        if (characterId == null) {
            return;
        }

        ArrayList<InventoryItem> items = inventoryFor(characterId);
        String itemId = readNonEmpty("Item ID to update: ");
        int index = findItemIndex(items, itemId);
        if (index < 0) {
            System.out.println("Item not found.");
            return;
        }

        InventoryItem oldItem = items.get(index);
        System.out.println("Press Enter to keep the current value.");
        String name = readOptional("Name [" + oldItem.getItemName() + "]: ");
        String category = readOptional("Category [" + oldItem.getCategory() + "]: ");
        int quantity = readOptionalPositiveInt("Quantity [" + oldItem.getQuantity() + "]: ");
        String rarity = readOptional("Rarity [" + oldItem.getRarity() + "]: ");
        Boolean equipped = readOptionalYesNo(
                "Equipped [" + (oldItem.isEquipped() ? "y" : "n") + "] (y/n): ");

        items.set(index, new InventoryItem(
                oldItem.getItemId(),
                name.isEmpty() ? oldItem.getItemName() : name,
                category.isEmpty() ? oldItem.getCategory() : category,
                quantity < 0 ? oldItem.getQuantity() : quantity,
                rarity.isEmpty() ? oldItem.getRarity() : rarity,
                equipped == null ? oldItem.isEquipped() : equipped));
        System.out.println("Item updated.");
    }

    private static void deleteItem() {
        String characterId = promptForExistingCharacter();
        if (characterId == null) {
            return;
        }

        ArrayList<InventoryItem> items = inventoryFor(characterId);
        String itemId = readNonEmpty("Item ID to delete: ");
        int index = findItemIndex(items, itemId);
        if (index < 0) {
            System.out.println("Item not found.");
            return;
        }
        items.remove(index);
        System.out.println("Item deleted.");
    }

    private static void showInventory() {
        String characterId = promptForExistingCharacter();
        if (characterId == null) {
            return;
        }
        CharacterProfile profile = findStoredProfile(characterId);
        printInventory(profile, inventoryFor(characterId));
    }

    private static void deleteCharacter() {
        String characterId = promptForExistingCharacter();
        if (characterId == null) {
            return;
        }
        CHARACTERS.remove(lookupKey(characterId));
        System.out.println("Character and its inventory deleted.");
    }

    private static String promptForExistingCharacter() {
        String id = readNonEmpty("Character ID: ");
        if (!CHARACTERS.containsKey(lookupKey(id))) {
            System.out.println("Character not found.");
            return null;
        }
        return id;
    }

    private static ArrayList<InventoryItem> inventoryFor(String characterId) {
        return CHARACTERS.get(lookupKey(characterId));
    }

    private static CharacterProfile lookupKey(String characterId) {
        return new CharacterProfile(characterId, "lookup", "lookup");
    }

    private static CharacterProfile findStoredProfile(String characterId) {
        for (CharacterProfile profile : CHARACTERS.keys()) {
            if (profile.getCharacterId().equalsIgnoreCase(characterId.trim())) {
                return profile;
            }
        }
        return null;
    }

    private static int findItemIndex(ArrayList<InventoryItem> items, String itemId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getItemId().equalsIgnoreCase(itemId.trim())) {
                return i;
            }
        }
        return -1;
    }

    private static void printInventory(CharacterProfile profile, ArrayList<InventoryItem> items) {
        System.out.println();
        System.out.println("Character: " + profile);
        if (items.isEmpty()) {
            System.out.println("  Inventory is empty.");
            return;
        }
        for (InventoryItem item : items) {
            System.out.println("  " + item);
        }
    }

    private static String readNonEmpty(String prompt) {
        while (true) {
            System.out.print(prompt);
            String value = INPUT.nextLine().trim();
            if (!value.isEmpty()) {
                return value;
            }
            System.out.println("A value is required.");
        }
    }

    private static String readOptional(String prompt) {
        System.out.print(prompt);
        return INPUT.nextLine().trim();
    }

    private static int readPositiveInt(String prompt) {
        while (true) {
            String value = readNonEmpty(prompt);
            try {
                int number = Integer.parseInt(value);
                if (number > 0) {
                    return number;
                }
            } catch (NumberFormatException ignored) {
                // Show the same helpful message for non-numbers and non-positive values.
            }
            System.out.println("Enter a whole number greater than zero.");
        }
    }

    /** Returns -1 when the user leaves the field unchanged. */
    private static int readOptionalPositiveInt(String prompt) {
        while (true) {
            String value = readOptional(prompt);
            if (value.isEmpty()) {
                return -1;
            }
            try {
                int number = Integer.parseInt(value);
                if (number > 0) {
                    return number;
                }
            } catch (NumberFormatException ignored) {
                // Show the same helpful message for non-numbers and non-positive values.
            }
            System.out.println("Enter a whole number greater than zero, or press Enter to keep it.");
        }
    }

    private static boolean readYesNo(String prompt) {
        while (true) {
            String value = readNonEmpty(prompt);
            if (value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes")) {
                return true;
            }
            if (value.equalsIgnoreCase("n") || value.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("Enter y or n.");
        }
    }

    /** Returns null when the user leaves the field unchanged. */
    private static Boolean readOptionalYesNo(String prompt) {
        while (true) {
            String value = readOptional(prompt);
            if (value.isEmpty()) {
                return null;
            }
            if (value.equalsIgnoreCase("y") || value.equalsIgnoreCase("yes")) {
                return true;
            }
            if (value.equalsIgnoreCase("n") || value.equalsIgnoreCase("no")) {
                return false;
            }
            System.out.println("Enter y, n, or press Enter to keep it.");
        }
    }
}
