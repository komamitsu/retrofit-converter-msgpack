package org.komamitsu.retrofit.converter.msgpack;

import com.fasterxml.jackson.databind.ObjectReader;
import java.io.IOException;
import java.io.InputStream;
import okhttp3.ResponseBody;
import retrofit2.Converter;

class MessagePackResponseBodyConverter<T> implements Converter<ResponseBody, T> {
  private final ObjectReader adapter;

  MessagePackResponseBodyConverter(ObjectReader adapter) {
    this.adapter = adapter;
  }

  @Override
  public T convert(ResponseBody value) throws IOException {
    try (InputStream in = value.byteStream()) {
      return adapter.readValue(in);
    }
  }
}
