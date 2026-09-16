package osm;

import org.openstreetmap.osmosis.core.domain.v0_6.Node;

public class OsmNodeConverter {

    public static model.Node convert(
            Node osmNode) {

        String name =
                "OSM Node "
                        + osmNode.getId();

        return new model.Node(
                (int) osmNode.getId(),
                name,
                osmNode.getLatitude(),
                osmNode.getLongitude()
        );
    }
}