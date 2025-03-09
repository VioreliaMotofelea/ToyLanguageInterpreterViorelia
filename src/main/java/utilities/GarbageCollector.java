package utilities;

import model.values.IValue;
import model.values.RefValue;

import java.util.*;
import java.util.stream.Collectors;

public class GarbageCollector {
    public static Map<Integer, IValue> unsafeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, IValue> heap) {
        return heap.entrySet().stream()
                .filter(entry -> symTableAddresses.contains(entry.getKey()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    public static Map<Integer, IValue> safeGarbageCollector(List<Integer> symTableAddresses, Map<Integer, IValue> heap) {
        List<Integer> addresses = new ArrayList<>(symTableAddresses);

        boolean changed;
        do {
            List<Integer> newAddresses = heap.entrySet().stream()
                    .filter(entry -> addresses.contains(entry.getKey()))
                    .map(entry -> {
                        if (entry.getValue() instanceof RefValue) {
                            return ((RefValue) entry.getValue()).getAddress();
                        }
                        return null;
                    })
                    .filter(Objects::nonNull)
                    .toList();

            changed = addresses.addAll(newAddresses.stream()
                    .filter(address -> !addresses.contains(address))
                    .toList());
        } while (changed);

        return unsafeGarbageCollector(addresses, heap);
    }

    public static List<Integer> getAddressesFromSymTable(Collection<IValue> symTableValues) {
        return symTableValues.stream()
                .filter(value -> value instanceof RefValue)
                .map(value -> ((RefValue) value).getAddress())
                .collect(Collectors.toList());
    }

    public static List<Integer> getAddressesFromHeap(Collection<IValue> heapValues) {
        return heapValues.stream()
                .filter(value -> value instanceof RefValue)
                .map(value -> ((RefValue) value).getAddress())
                .collect(Collectors.toList());
    }

    public static List<Integer> getAddressesFromSymTableAndHeap(Collection<IValue> symTableValues, Collection<IValue> heapValues) {
        List<Integer> symTableAddresses = symTableValues.stream()
                .filter(value -> value instanceof RefValue)
                .map(value -> ((RefValue) value).getAddress())
                .collect(Collectors.toList());

        List<Integer> heapAddresses =  heapValues.stream()
                .filter(value -> value instanceof RefValue)
                .map(value -> ((RefValue) value).getAddress())
                .collect(Collectors.toList());

        symTableAddresses.addAll(heapAddresses);
        return symTableAddresses;
    }
}
