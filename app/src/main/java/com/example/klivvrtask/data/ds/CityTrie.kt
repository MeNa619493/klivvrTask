package com.example.klivvrtask.data.ds

import com.example.klivvrtask.domain.model.City
/**
 * Trie data structure optimized for prefix-based city searches.
 * Provides O(m) search complexity where m is the prefix length.
 * This is significantly more efficient than linear O(n) searching
 * for large datasets (200k+ cities).
 */
class CityTrie {
    private val root = TrieNode()
    // Store all cities for getAllCities() method
    private val allCities = mutableListOf<City>()

    class TrieNode {
        val children = HashMap<Char, TrieNode>()
        val cities = mutableListOf<City>()
        var isEndOfWord = false
    }

    /**
     * Populate the trie with a list of cities.
     */
    fun populateTrie(cities: List<City>) {
        // Clear existing data
        root.children.clear()
        allCities.clear()

        // Store all cities for getAllCities()
        allCities.addAll(cities)

        // Insert each city into the trie
        for (city in cities) {
            insert(city)
        }
    }

    /**
     * Insert a city into the trie using its display name as the key.
     * Time Complexity: O(k) where k is the length of city display name
     */
    fun insert(city: City) {
        var current = root
        val key = city.displayName.lowercase()

        // Insert character by character
        for (char in key) {
            if (char !in current.children) {
                current.children[char] = TrieNode()
            }
            current = current.children[char]!!
            // Store city reference at each node for prefix matching
            current.cities.add(city)
        }
        current.isEndOfWord = true
    }

    /**
     * Search for cities with given prefix.
     * Time Complexity: O(m + k) where m is prefix length, k is result size
     * This is much faster than O(n) linear search for large datasets
     */
    fun searchByPrefix(prefix: String): List<City> {
        if (prefix.isEmpty()) return allCities

        var current = root
        val lowerPrefix = prefix.lowercase()

        // Navigate to prefix node
        for (char in lowerPrefix) {
            current = current.children[char] ?: return emptyList()
        }

        // Return all cities with this prefix, sorted alphabetically
        return current.cities.sortedBy { it.displayName }
    }
}