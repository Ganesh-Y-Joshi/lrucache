package org.joshi.gyj.cache;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

public class LruCache<K extends Comparable<K>, V> {
    // Internal LinkedHashMap to store key-value pairs in LRU order
    private final LinkedHashMap<K, V> map;

    // Current size of the cache
    private int size;

    // Maximum size allowed for the cache
    private int maxSize;

    // Count of put operations
    private int putCount;

    // Count of eviction operations
    private int evictionCount;

    // Count of cache hits
    private int hitCount;

    // Count of cache misses
    private int missCount;

    /**
     * Constructs an LRU cache with the specified maximum size.
     *
     * @param maxSize the maximum size of the cache
     * @throws IllegalArgumentException if maxSize is less than or equal to 0
     */
    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than 0");
        }

        this.maxSize = maxSize;
        // Using a LinkedHashMap with access order set to true for LRU behavior
        this.map = new LinkedHashMap<>(0, 0.75f, true);
    }

    /**
     * Resizes the cache to the specified maximum size.
     *
     * @param maxSize the new maximum size of the cache
     * @throws IllegalArgumentException if maxSize is less than or equal to 0
     */
    public void resize(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize must be greater than 0");
        }

        this.maxSize = maxSize;
        trimToSize(maxSize);
    }

    /**
     * Trims the cache to the specified maximum size by evicting the least recently used entries.
     *
     * @param maxSize the target maximum size of the cache
     */
    public void trimToSize(int maxSize) {
        while (size > 0 && size > maxSize) {
            // Evict the least recently used entry
            Map.Entry<K, V> toEvict = map.entrySet().iterator().next();
            K key = toEvict.getKey();
            V value = toEvict.getValue();

            // Remove the entry from the map
            map.remove(key);

            // Update size and eviction count
            size--;
            evictionCount++;
        }
    }

    /**
     * Retrieves the value associated with the given key from the cache.
     *
     * @param key the key whose associated value is to be retrieved
     * @return the value associated with the key, or null if the key is not present in the cache
     * @throws IllegalArgumentException if the provided key is null
     */
    public V getValue(K key) {
        if (key == null) {
            throw new IllegalArgumentException("The provided key is null");
        }

        // Attempt to get the value from the map
        V mapValue = map.get(key);

        // Update hit or miss counts accordingly
        if (mapValue != null) {
            hitCount++;
            return mapValue;
        } else {
            missCount++;
            return null;
        }
    }

    /**
     * Removes the entry associated with the given key from the cache.
     *
     * @param key the key whose entry is to be removed
     * @return true if an entry was removed, false otherwise
     * @throws IllegalArgumentException if the provided key is null
     */
    public boolean removeValue(K key) {
        if (key == null) {
            throw new IllegalArgumentException("The provided key is null");
        }

        // Attempt to remove the entry from the map
        V removeValue = map.remove(key);

        // Update size and eviction count accordingly
        if (removeValue != null) {
            size--;
            evictionCount++;
            return true;
        } else {
            return false;
        }
    }

    /**
     * Adds the specified key-value pair to the cache. If the cache exceeds its maximum size,
     * it trims down to 80% of the maximum size before inserting the new entry.
     *
     * @param key   the key to be added to the cache
     * @param value the value to be associated with the key
     * @return the previous value associated with the key, or null if there was no previous value
     * @throws NullPointerException if the provided key or value is null
     */
    public V putEntry(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException(
                    key == null ? "Key is null" : "Value is null"
            );
        }

        V previousValue;

        // Check if the cache has reached its maximum size
        if (size >= maxSize) {
            // Trim the cache to 80% of the maximum size
            trimToSize((int) (0.8 * maxSize));
        }

        // Increment putCount and update size
        putCount++;
        size++;

        // Attempt to put the new entry in the map
        previousValue = map.put(key, value);

        // If the key already existed, update the map and return the previous value
        if (previousValue != null) {
            map.put(key, previousValue);
            return previousValue;
        }

        // If the key is new, return the provided value
        return value;
    }

    /**
     * Updates the value associated with the given key in the cache.
     *
     * @param key   the key whose associated value is to be updated
     * @param value the new value to be associated with the key
     * @return a Data object containing the key and the new value
     * @throws IllegalArgumentException if the provided key or value is null
     */
    public Data<K, V> updateValue(K key, V value) {
        if (key == null || value == null) {
            throw new IllegalArgumentException("The key or value cannot be null");
        }

        // Remove the existing entry for the key and get the removed value
        boolean isRemoved = this.removeValue(key);

        // Add the new key-value pair to the cache and get the new value
        V val = this.putEntry(key, value);

        // Return a Data object containing the key and the new value
        return new Data<>(key, value);
    }

    /**
     * Removes all entries from the cache.
     */
    public void evictAll() {
        map.clear();
    }

    /**
     * Returns a snapshot of the cache as a new LinkedHashMap.
     *
     * @return a LinkedHashMap containing all key-value pairs in the cache
     */
    public LinkedHashMap<K, V> getSnapshot() {
        return new LinkedHashMap<>(map);
    }

    // Getter methods for various statistics

    /**
     * Gets the current size of the cache.
     *
     * @return the current size of the cache
     */
    public int getSize() {
        return size;
    }

    /**
     * Gets the maximum size allowed for the cache.
     *
     * @return the maximum size allowed for the cache
     */
    public int getMaxSize() {
        return maxSize;
    }

    /**
     * Gets the count of put operations on the cache.
     *
     * @return the count of put operations
     */
    public int getPutCount() {
        return putCount;
    }

    /**
     * Gets the count of eviction operations on the cache.
     *
     * @return the count of eviction operations
     */
    public int getEvictionCount() {
        return evictionCount;
    }

    /**
     * Gets the count of cache hits.
     *
     * @return the count of cache hits
     */
    public int getHitCount() {
        return hitCount;
    }

    /**
     * Gets the count of cache misses.
     *
     * @return the count of cache misses
     */
    public int getMissCount() {
        return missCount;
    }

    /**
     * Returns a string representation of the LruCache.
     *
     * @return a string representation of the LruCache
     */
    @Override
    public String toString() {
        return "LruCache{" +
                "map=" + map +
                ", size=" + size +
                ", maxSize=" + maxSize +
                ", putCount=" + putCount +
                ", evictionCount=" + evictionCount +
                ", hitCount=" + hitCount +
                ", missCount=" + missCount +
                '}';
    }

    /**
     * Data class representing a key-value pair.
     *
     * @param <K> the type of keys stored in the cache, must extend Comparable
     * @param <V> the type of values stored in the cache
     */
    public static class Data<K extends Comparable<K>, V> {
        private final K key;
        private final V value;

        public Data(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Gets the key of the key-value pair.
         *
         * @return the key of the key-value pair
         */
        public K getKey() {
            return key;
        }

        /**
         * Gets the value of the key-value pair.
         *
         * @return the value of the key-value pair
         */
        public V getValue() {
            return value;
        }

        /**
         * Returns a string representation of the Data object.
         *
         * @return a string representation of the Data object
         */
        @Override
        public String toString() {
            return "Data{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    /**
     * LruCacheIterator provides an iterator over the entries of the LRU cache.
     *
     * @param <K> the type of keys stored in the cache, must extend Comparable
     * @param <V> the type of values stored in the cache
     */
    public static class LruCacheIterator<K extends Comparable<K>, V> implements Iterator<Map.Entry<K, V>> {
        private final Iterator<Map.Entry<K, V>> iterator;

        public LruCacheIterator(LruCache<K, V> lruCache) {
            if (lruCache.map.isEmpty()) {
                throw new NullPointerException("Cannot operate on the empty cache");
            }

            this.iterator = lruCache.map.entrySet().iterator();
        }

        /**
         * Checks if there are more entries in the iterator.
         *
         * @return true if there are more entries, false otherwise
         */
        @Override
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /**
         * Retrieves the next entry in the iterator.
         *
         * @return the next entry in the iterator
         */
        @Override
        public Map.Entry<K, V> next() {
            return iterator.next();
        }

        @Override
        public void remove() {
            iterator.remove();
        }
    }
}
