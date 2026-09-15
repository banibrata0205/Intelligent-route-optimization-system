package osm;

import java.io.File;
import java.util.Map;

import org.openstreetmap.osmosis.core.container.v0_6.EntityContainer;
import org.openstreetmap.osmosis.core.task.v0_6.Sink;
import org.openstreetmap.osmosis.pbf2.v0_6.PbfReader;

public class OsmPbfReader {

    private long nodeCount = 0;
    private long wayCount = 0;
    private long relationCount = 0;

    public void read(String filePath) {

        File file = new File(filePath);

        if (!file.exists()) {

            throw new IllegalArgumentException(
                    "OSM file not found: " + filePath
            );
        }

        System.out.println(
                "Reading OSM file:"
        );

        System.out.println(
                file.getAbsolutePath()
        );

        PbfReader reader =
                new PbfReader(
                        file,
                        4
                );

        reader.setSink(
                new Sink() {

                    @Override
                    public void initialize(
                            Map<String, Object> metaData) {

                        System.out.println(
                                "OSM reader initialized."
                        );
                    }

                    @Override
                    public void process(
                            EntityContainer entityContainer) {

                        switch (
                                entityContainer
                                        .getEntity()
                                        .getType()
                        ) {

                            case Node:
                                nodeCount++;
                                break;

                            case Way:
                                wayCount++;
                                break;

                            case Relation:
                                relationCount++;
                                break;

                            default:
                                break;
                        }
                    }

                    @Override
                    public void complete() {

                        System.out.println();
                        System.out.println(
                                "===== OSM DATA SUMMARY ====="
                        );

                        System.out.println(
                                "Nodes: "
                                        + nodeCount
                        );

                        System.out.println(
                                "Ways: "
                                        + wayCount
                        );

                        System.out.println(
                                "Relations: "
                                        + relationCount
                        );

                        System.out.println();
                        System.out.println(
                                "OSM reading completed."
                        );
                    }

                    @Override
                    public void close() {

                        System.out.println(
                                "OSM reader closed."
                        );
                    }
                }
        );

        reader.run();
    }
}