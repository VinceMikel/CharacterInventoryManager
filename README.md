# Character Inventory Manager

A Java console application for managing items carried by video game characters. It stores each custom CharacterProfile key with an ArrayList of custom InventoryItem values.

## Open and run

1. Open this folder in IntelliJ IDEA and allow it to load the Maven project.
2. Use Java 17 or newer.
3. Open src/main/java/inventory/CharacterInventoryApp.java and run its main method.

The program initializes sample characters and items in code whenever it starts. It does not use a database or external data files.

## Menu functions

- List all characters and their inventories
- Add a character
- Add, update, or delete an item
- Find one character and show its inventory
- Delete a character and its inventory

## Data model

- CharacterProfile is the custom map key. Its fields are character ID, character name, and game title. Character ID defines key equality.
- InventoryItem is the custom value type. Its fields are item ID, item name, category, quantity, rarity, and equipped status.
- Each map value is an ArrayList of InventoryItem objects, allowing a character to carry multiple items.

## Hash map

CustomHashMap<K, V> stores entries in an ArrayList of LinkedList buckets and resolves collisions with separate chaining. It supports put, get, remove, containsKey, keys, values, entrySet, size, isEmpty, and clear. It resizes when its load factor threshold is exceeded. It does not use Java's built-in HashMap or Hashtable.
