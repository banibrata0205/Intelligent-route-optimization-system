package osm;

import java.util.HashMap;
import java.util.Map;

public class OsmNodeMapper {

    private final Map<Long, Integer> idMap =
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

        return newId;
    }

    public boolean contains(
            long osmNodeId) {

        return idMap.containsKey(
                osmNodeId
        );
    }

    public int size() {

        return idMap.size();
    }
}