package com.alexvinall.mappingconsolidator;

import static com.google.common.collect.Sets.newHashSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * Tests for {@link MappingConsolidator}.
 */
public class TestMappingConsolidator {

  private final MappingConsolidator genericMapper = new MappingConsolidator();

  /**
   * Asserts basic mapping capabilities.
   *
   * Input:
   * <p>
   * 1 -> A <p>
   * 2 -> A <p>
   * 3 -> B <p>
   * 4 -> C <p>
   * 4 -> D <p>
   * <p>
   * Expected output:
   * <p>
   * [A]:1,2 <p>
   * [B]:3 <p>
   * [C+D]:4
   */
  @Test
  public void testBasicMap() {
    // Given
    Collection<Pair<String, String>> input = Lists.newArrayList();
    input.add(Pair.of("A", "1"));
    input.add(Pair.of("A", "2"));
    input.add(Pair.of("B", "3"));
    input.add(Pair.of("C", "4"));
    input.add(Pair.of("D", "4"));

    // When
    Map<String, Set<String>> result = genericMapper.map(input);

    // Then
    System.out.println(result);
    assertEquals("Incorrect number of results", 3, result.size());

    assertTrue(result.keySet().contains("A"));
    assertTrue(result.keySet().contains("B"));
    assertTrue(result.keySet().contains("C+D"));

    assertTrue(result.get("A").size() == 2);
    assertTrue(result.get("B").size() == 1);
    assertTrue(result.get("C+D").size() == 1);

    assertTrue(result.get("A").containsAll(newHashSet("1", "2")));
    assertTrue(result.get("B").contains("3"));
    assertTrue(result.get("C+D").contains("4"));
  }


  @Test
  public void testLargeMap() {
    // Given
    Collection<Pair<String, String>> input = Lists.newArrayList();

    Random random = new Random();
    String[] keys = {"A", "B", "C", "D", "E", "F", "G", "H","I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

    for (int i = 0; i < 1000000; i++) {
      input.add(Pair.of(keys[random.nextInt(26)], String.valueOf(i)));
    }

    // When
    Map<String, Set<String>> result = genericMapper.map(input);

    // Then
    System.out.println("Large map:");
    int i = 0;
    for (Entry<String, Set<String>> entry : result.entrySet()) {
      System.out.println(entry.getKey() + ": " + entry.getValue().size() + " entries.");
      i += entry.getValue().size();
    }
    assertEquals(1000000, i);
  }

  // TODO tell nulls
  // TODO test duplicates
}
