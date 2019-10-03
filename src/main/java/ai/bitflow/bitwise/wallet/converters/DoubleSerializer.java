package ai.bitflow.bitwise.wallet.converters;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;


public class DoubleSerializer extends JsonSerializer<Double> {

    @Override
    public void serialize(Double value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException {
        if (null == value) {
            jgen.writeNull();
        } else {
            final String serializedValue = String.format("%.8f", value);
            // do your customization here
            jgen.writeNumber(serializedValue);
        }
    }

}