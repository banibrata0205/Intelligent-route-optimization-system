package osm;

public class OsmTest {

    public static void main(String[] args) {

        String osmFile =
                "data/osm/eastern-zone-latest.osm.pbf";

        OsmPbfReader reader =
                new OsmPbfReader();

        reader.read(osmFile);
    }
}