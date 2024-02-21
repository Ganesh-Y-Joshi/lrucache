LRU Cache Implementation
This Java package provides an implementation of a Least Recently Used (LRU) cache, which is a type of cache where the least recently used items are removed when the cache reaches its maximum size. The package includes the following components:

LruCache class: Represents the LRU cache itself. It maintains key-value pairs in LRU order and provides methods for accessing, updating, and removing entries, as well as for resizing the cache and retrieving statistics.

Data class: Represents a key-value pair stored in the cache. It provides methods for accessing the key and value.

LruCacheIterator class: Provides an iterator over the entries of the LRU cache.

Usage
To use the LRU cache in your Java application, follow these steps:

Import the package:

java
Copy code
import org.joshi.gyj.cache.LruCache;
Create an instance of LruCache:

java
Copy code
// Specify the maximum size of the cache
int maxSize = 100;
LruCache<String, Integer> cache = new LruCache<>(maxSize);
Interact with the cache:

java
Copy code
// Add entries to the cache
cache.putEntry("key1", 1);

// Retrieve a value from the cache
Integer value = cache.getValue("key1");

// Update a value in the cache
cache.updateValue("key1", 2);

// Remove an entry from the cache
cache.removeValue("key1");
Retrieve cache statistics:

java
Copy code
// Get cache statistics
int size = cache.getSize();
int hitCount = cache.getHitCount();
Iterate over cache entries:

java
Copy code
// Create an iterator
LruCache.LruCacheIterator<String, Integer> iterator = new LruCache.LruCacheIterator<>(cache);

// Iterate over entries
while (iterator.hasNext()) {
    Map.Entry<String, Integer> entry = iterator.next();
    // Do something with the entry
}
Features
Implements an LRU cache, ensuring efficient removal of least recently used items when the cache is full.
Supports resizing of the cache and provides statistics such as hit count, miss count, etc.
Allows iteration over cache entries using a custom iterator.
Dependencies
This package does not have any external dependencies and can be used independently in Java applications.

Contributions
Contributions to this package are welcome. If you find any bugs or have suggestions for improvements, please open an issue or submit a pull request on the GitHub repository.

License
This package is distributed under the MIT License. See the LICENSE file for more information.

Author
This package is authored by Joshi GYJ. For inquiries or feedback, please contact joshi.gyj@example.com.

Additional Information
For more detailed information on how to use the LRU cache or its internals, refer to the Javadoc comments provided within the source code.
