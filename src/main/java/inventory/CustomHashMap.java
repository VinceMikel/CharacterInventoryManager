package inventory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Objects;
/*
Vince Mikel Masong
Ram ID: R02102624

I certify that this submission is my
own individual work with the assistance of ChatGPT
 */

/**
 * Generic hash map using separate chaining. It does not use Java's built-in
 * HashMap or Hashtable.
 */
public class CustomHashMap<K, V> {
    private static final int DEFAULT_CAPACITY = 16;
    private static final double DEFAULT_LOAD_FACTOR = 0.75;

    private ArrayList<LinkedList<Node<K, V>>> buckets;
    private int size;
    private final double loadFactor;

    public CustomHashMap() {
        this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
    }

    public CustomHashMap(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public CustomHashMap(int initialCapacity, double loadFactor) {
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Capacity must be at least 1.");
        }
        if (loadFactor <= 0.0 || Double.isNaN(loadFactor)) {
            throw new IllegalArgumentException("Load factor must be positive.");
        }
        this.buckets = createBuckets(initialCapacity);
        this.loadFactor = loadFactor;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean containsKey(K key) {
        return findNode(key) != null;
    }

    public V get(K key) {
        Node<K, V> node = findNode(key);
        return node == null ? null : node.value;
    }

    /**
     * Inserts a new pair or replaces the value for an equal key.
     * Returns the old value, or null when the key was not already present.
     */
    public V put(K key, V value) {
        requireNonNullKey(key);
        Objects.requireNonNull(value, "Null values are not supported.");

        Node<K, V> existing = findNode(key);
        if (existing != null) {
            V oldValue = existing.value;
            existing.value = value;
            return oldValue;
        }

        if (size + 1 > buckets.size() * loadFactor) {
            resize();
        }

        int index = bucketIndex(key, buckets.size());
        buckets.get(index).add(new Node<>(key, value));
        size++;
        return null;
    }

    public V remove(K key) {
        requireNonNullKey(key);
        int index = bucketIndex(key, buckets.size());
        Iterator<Node<K, V>> iterator = buckets.get(index).iterator();
        while (iterator.hasNext()) {
            Node<K, V> node = iterator.next();
            if (node.key.equals(key)) {
                iterator.remove();
                size--;
                return node.value;
            }
        }
        return null;
    }

    public void clear() {
        buckets = createBuckets(DEFAULT_CAPACITY);
        size = 0;
    }

    /** Returns an ArrayList snapshot of the map's keys. */
    public ArrayList<K> keys() {
        ArrayList<K> result = new ArrayList<>(size);
        for (LinkedList<Node<K, V>> bucket : buckets) {
            for (Node<K, V> node : bucket) {
                result.add(node.key);
            }
        }
        return result;
    }

    /** Returns an ArrayList snapshot of the map's values. */
    public ArrayList<V> values() {
        ArrayList<V> result = new ArrayList<>(size);
        for (LinkedList<Node<K, V>> bucket : buckets) {
            for (Node<K, V> node : bucket) {
                result.add(node.value);
            }
        }
        return result;
    }

    /** Returns snapshot entries, not the map's internal nodes. */
    public ArrayList<MapEntry<K, V>> entrySet() {
        ArrayList<MapEntry<K, V>> result = new ArrayList<>(size);
        for (LinkedList<Node<K, V>> bucket : buckets) {
            for (Node<K, V> node : bucket) {
                result.add(new MapEntry<>(node.key, node.value));
            }
        }
        return result;
    }

    private Node<K, V> findNode(K key) {
        requireNonNullKey(key);
        int index = bucketIndex(key, buckets.size());
        for (Node<K, V> node : buckets.get(index)) {
            if (node.key.equals(key)) {
                return node;
            }
        }
        return null;
    }

    private void resize() {
        ArrayList<LinkedList<Node<K, V>>> oldBuckets = buckets;
        buckets = createBuckets(oldBuckets.size() * 2);
        int oldSize = size;
        size = 0;
        for (LinkedList<Node<K, V>> bucket : oldBuckets) {
            for (Node<K, V> node : bucket) {
                int index = bucketIndex(node.key, buckets.size());
                buckets.get(index).add(new Node<>(node.key, node.value));
                size++;
            }
        }
        if (size != oldSize) {
            throw new IllegalStateException("Map size changed unexpectedly during resize.");
        }
    }

    private static <K, V> ArrayList<LinkedList<Node<K, V>>> createBuckets(int capacity) {
        ArrayList<LinkedList<Node<K, V>>> result = new ArrayList<>(capacity);
        for (int i = 0; i < capacity; i++) {
            result.add(new LinkedList<>());
        }
        return result;
    }

    private static int bucketIndex(Object key, int capacity) {
        int hash = key.hashCode();
        int spreadHash = hash ^ (hash >>> 16);
        return (spreadHash & 0x7fffffff) % capacity;
    }

    private static void requireNonNullKey(Object key) {
        Objects.requireNonNull(key, "Null keys are not supported.");
    }

    private static final class Node<K, V> {
        private final K key;
        private V value;

        private Node(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    public static final class MapEntry<K, V> {
        private final K key;
        private final V value;

        private MapEntry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
