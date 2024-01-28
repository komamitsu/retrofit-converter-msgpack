package org.komamitsu.retrofit.converter.msgpack;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Objects;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import org.junit.jupiter.api.*;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class MessagePackConverterFactoryTest {
  private static MockWebServer server;

  private Service service;

  public static class Pojo {
    private final int i;
    private final float f;
    private final String s;

    public Pojo(@JsonProperty("i") int i, @JsonProperty("f") float f, @JsonProperty("s") String s) {
      this.i = i;
      this.f = f;
      this.s = s;
    }

    @JsonProperty("i")
    public int getI() {
      return i;
    }

    @JsonProperty("f")
    public float getF() {
      return f;
    }

    @JsonProperty("s")
    public String getS() {
      return s;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) return true;
      if (o == null || getClass() != o.getClass()) return false;
      Pojo pojo = (Pojo) o;
      return i == pojo.i && Float.compare(pojo.f, f) == 0 && Objects.equals(s, pojo.s);
    }

    @Override
    public int hashCode() {
      return Objects.hash(i, f, s);
    }
  }

  interface Service {
    @POST("/")
    Call<Pojo> postPojo(@Body Pojo pojo);
  }

  @BeforeAll
  public static void beforeAll() throws IOException {
    server = new MockWebServer();
    server.start();
  }

  @AfterAll
  public static void afterAll() throws IOException {
    server.close();
  }

  @BeforeEach
  public void setUp() {
    Retrofit retrofit =
        new Retrofit.Builder()
            .baseUrl(server.url("/"))
            .addConverterFactory(MessagePackConverterFactory.create())
            .build();
    service = retrofit.create(Service.class);
  }

  @Test
  public void requestBodyConverter() throws IOException, InterruptedException {
    ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

    Pojo requestPojo = new Pojo(42, (float) Math.PI, "Hello");
    Pojo responsePojo = new Pojo(99, 1.23f, "World");
    try (Buffer buffer = new Buffer()) {
      buffer.write(objectMapper.writeValueAsBytes(responsePojo));
      server.enqueue(new MockResponse().setBody(buffer));

      Response<Pojo> response = service.postPojo(requestPojo).execute();
      Assertions.assertEquals(responsePojo, response.body());

      RecordedRequest recordedRequest = server.takeRequest();
      Pojo recordedPojo =
          objectMapper.readValue(recordedRequest.getBody().readByteArray(), Pojo.class);
      Assertions.assertEquals(requestPojo, recordedPojo);
    }
  }
}
