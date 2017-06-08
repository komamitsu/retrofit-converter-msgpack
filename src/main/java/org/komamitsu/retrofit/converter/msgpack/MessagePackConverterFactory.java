package org.komamitsu.retrofit.converter.msgpack;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.ObjectWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import retrofit2.Converter;
import retrofit2.Retrofit;

public class MessagePackConverterFactory
    extends Converter.Factory
{
    public static MessagePackConverterFactory create()
    {
        return create(new ObjectMapper(new MessagePackFactory()));
    }

    @SuppressWarnings("ConstantConditions") // Guarding public API nullability.
    public static MessagePackConverterFactory create(ObjectMapper mapper)
    {
        if (mapper == null) {
            throw new IllegalArgumentException("'mapper' is null");
        }

        if (!(mapper.getFactory() instanceof MessagePackFactory)) {
            throw new IllegalArgumentException("'mapper' doesn't have MessagePackFactory");
        }

        return new MessagePackConverterFactory(mapper);
    }

    private final ObjectMapper mapper;

    private MessagePackConverterFactory(ObjectMapper mapper)
    {
        this.mapper = mapper;
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type,
            Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit)
    {
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        ObjectWriter writer = mapper.writerFor(javaType);
        return new MessagePackRequestBodyConverter<>(writer);
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type,
            Annotation[] annotations, Retrofit retrofit)
    {
        JavaType javaType = mapper.getTypeFactory().constructType(type);
        ObjectReader reader = mapper.readerFor(javaType);
        return new MessagePackResponseBodyConverter<>(reader);
    }
}
