package com.alexvinall.mappingconsolidator;

import static org.apache.commons.lang3.StringUtils.contains;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Generic mapper class.
 *
 * Accepts a collection of pairs of strings and 
 * returns a map that aggregates the relationships
 * between the strings.
 * <p>
 * E.g.
 * <p>
 * Input:
 * <p>
 * A -> 1 <p>
 * A -> 2 <p>
 * B -> 3 <p>
 * C -> 4 <p>
 * D -> 4 <p>
 * <p>
 * Output:
 * <p>
 * [A]:1,2 <p>
 * [B]:3 <p>
 * [C+D]:4
 */
public class MappingConsolidator {

  public Map<String, Set<String>> map(Collection<Pair<String, String>> pairCollection) {
    Map<String, Set<String>> result = Maps.newHashMap();

    for (Pair<String, String> mappedPair : pairCollection) {
      boolean foundMatch = false;

      // First mapping always goes into the result
      if (result.isEmpty()) {
        result.put(mappedPair.getKey(), Sets.newHashSet(mappedPair.getValue()));
        continue;
      }

      // Check to see whether we have already seen this key, if not, add it
      if (!result.keySet().contains(mappedPair.getKey())) {
        // Check to see if it is part of a compound key instead
        for (String resultKey : result.keySet()) {
          if (contains(resultKey, mappedPair.getKey()+"+")
              || contains(resultKey, "+"+mappedPair.getKey())) {
            // It is part of a compound key, so added the mapped value
            result.get(resultKey).add(mappedPair.getValue());
            foundMatch = true;
            break;
          }
        }

        // If we have already seen this mapped value, create/add to the compound key
        for (Entry<String, Set<String>> existingResult : result.entrySet()) {
          if (existingResult.getValue().contains(mappedPair.getValue())) {
            String newKey = existingResult.getKey()+"+"+mappedPair.getKey();
            result.put(newKey, existingResult.getValue());
            result.remove(existingResult.getKey());
            foundMatch = true;
            break;
          }
        }

        // If we have not seen either the key or value before, add it to the map
        if (!foundMatch) {
          result.put(mappedPair.getKey(), Sets.newHashSet(mappedPair.getValue()));
        }
      } else {
        // If we already have this key, then add the value to the set of mapped values for this key
        result.get(mappedPair.getKey()).add(mappedPair.getValue());
      }
    }

    return result;
  }
}