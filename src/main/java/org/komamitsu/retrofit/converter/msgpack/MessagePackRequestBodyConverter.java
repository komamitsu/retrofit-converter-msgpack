package org.komamitsu.retrofit.converter.msgpack;

import com.fasterxml.jackson.databind.ObjectWriter;
import java.io.IOException;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;

class MessagePackRequestBodyConverter<T> implements Converter<T, RequestBody> {
  private static final MediaType MEDIA_TYPE =
      MediaType.parse("application/x-msgpack; charset=UTF-8");

  private final ObjectWriter adapter;

  MessagePackRequestBodyConverter(ObjectWriter adapter) {
    this.adapter = adapter;
  }

  @Override
  public RequestBody convert(T value) throws IOException {
    byte[] bytes = adapter.writeValueAsBytes(value);
    return RequestBody.create(MEDIA_TYPE, bytes);
  }
}
