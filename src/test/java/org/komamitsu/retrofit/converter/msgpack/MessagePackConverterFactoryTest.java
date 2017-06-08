package org.komamitsu.retrofit.converter.msgpack;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import okio.Buffer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.msgpack.jackson.dataformat.MessagePackFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.POST;

import java.io.IOException;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

public class MessagePackConverterFactoryTest
{
    @Rule
    public final MockWebServer server = new MockWebServer();
    private Service service;

    public static class Pojo
    {
        private final int i;
        private final float f;
        private final String s;

        public Pojo(
                @JsonProperty("i") int i,
                @JsonProperty("f") float f,
                @JsonProperty("s") String s)
        {
            this.i = i;
            this.f = f;
            this.s = s;
        }

        @JsonProperty("i")
        public int getI()
        {
            return i;
        }

        @JsonProperty("f")
        public float getF()
        {
            return f;
        }

        @JsonProperty("s")
        public String getS()
        {
            return s;
        }

        @Override
        public boolean equals(Object o)
        {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }

            Pojo pojo = (Pojo) o;

            if (i != pojo.i) {
                return false;
            }
            if (Float.compare(pojo.f, f) != 0) {
                return false;
            }
            return s != null ? s.equals(pojo.s) : pojo.s == null;
        }

        @Override
        public int hashCode()
        {
            int result = i;
            result = 31 * result + (f != +0.0f ? Float.floatToIntBits(f) : 0);
            result = 31 * result + (s != null ? s.hashCode() : 0);
            return result;
        }
    }

    interface Service
    {
        @POST("/")
        Call<Pojo> postPojo(@Body Pojo pojo);
    }

    @Before
    public void setUp()
    {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(server.url("/"))
                .addConverterFactory(MessagePackConverterFactory.create())
                .build();
        service = retrofit.create(Service.class);
    }

    @Test
    public void requestBodyConverter()
            throws IOException, InterruptedException
    {
        ObjectMapper objectMapper = new ObjectMapper(new MessagePackFactory());

        Pojo requestPojo = new Pojo(42, 3.14f, "Hello");
        Pojo responsePojo = new Pojo(99, 1.23f, "World");
        server.enqueue(new MockResponse().setBody(
                new Buffer().write(objectMapper.writeValueAsBytes(responsePojo))));

        Response<Pojo> response = service.postPojo(requestPojo).execute();
        assertThat(response.body(), is(responsePojo));

        RecordedRequest recordedRequest = server.takeRequest();
        Pojo recordedPojo = objectMapper.readValue(recordedRequest.getBody().readByteArray(), Pojo.class);
        assertThat(recordedPojo, is(requestPojo));
    }
}
