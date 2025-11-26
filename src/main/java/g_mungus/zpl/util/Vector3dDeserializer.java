package g_mungus.zpl.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.joml.Vector3d;

import java.io.IOException;

public class Vector3dDeserializer extends JsonDeserializer<Vector3d> {
    @Override
    public Vector3d deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);
        double x = node.get("x").asDouble();
        double y = node.get("y").asDouble();
        double z = node.get("z").asDouble();
        return new Vector3d(x, y, z);
    }
}
