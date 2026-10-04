package osm;

import java.util.HashMap;
import java.util.Map;

public class OsmNodeMapper {

    // OSM node ID -> internal Java node ID
    private final Map<Long, Integer> idMap =
            new HashMap<>();

    // Internal Java node ID -> OSM node ID
    private final Map<Integer, Long> reverseIdMap =
            new HashMap<>();

    private int nextInternalId = 1;

    public int getOrCreateInternalId(
            long osmNodeId) {

        Integer existingId =
                idMap.get(osmNodeId);

        if (existingId != null) {

            return existingId;
        }

        int newId =
                nextInternalId++;

        idMap.put(
                osmNodeId,
                newId
        );

        reverseIdMap.put(
                newId,
                osmNodeId
        );

        return newId;
    }

    public boolean contains(
            long osmNodeId) {

        return idMap.containsKey(
                osmNodeId
        );
    }

    public Long getOsmNodeId(
            int internalNodeId) {

        return reverseIdMap.get(
                internalNodeId
        );
    }

    public int size() {

        return idMap.size();
    }
}