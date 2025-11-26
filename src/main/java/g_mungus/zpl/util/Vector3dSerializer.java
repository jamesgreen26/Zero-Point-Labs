package g_mungus.zpl.util;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.joml.Vector3d;

import java.io.IOException;

public class Vector3dSerializer extends JsonSerializer<Vector3d> {
    @Override
    public void serialize(Vector3d value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        gen.writeStartObject();
        gen.writeNumberField("x", value.x);
        gen.writeNumberField("y", value.y);
        gen.writeNumberField("z", value.z);
        gen.writeEndObject();
    }
}
