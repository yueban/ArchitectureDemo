package com.yueban.architecturedemo.data.net.interceptor;

import com.yueban.architecturedemo.util.L;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.concurrent.TimeUnit;
import okhttp3.Connection;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * @author yueban
 * @date 2017/7/29
 * @email fbzhh007@gmail.com
 */
public class HttpLoggingInterceptor implements Interceptor {
  private static final Charset UTF8 = Charset.forName("UTF-8");
  private volatile Level level = Level.NONE;

  public Level getLevel() {
    return level;
  }

  /** Change the level at which this interceptor logs. */
  public HttpLoggingInterceptor setLevel(Level level) {
    if (level == null) throw new NullPointerException("level == null. Use Level.NONE instead.");
    this.level = level;
    return this;
  }

  @Override
  public Response intercept(Chain chain) throws IOException {
    Level level = this.level;

    Request request = chain.request();
    if (level == Level.NONE) {
      return chain.proceed(request);
    }

    boolean logBody = level == Level.BODY;
    boolean logHeaders = logBody || level == Level.HEADERS;

    RequestBody requestBody = request.body();
    boolean hasRequestBody = requestBody != null;

    Connection connection = chain.connection();
    Protocol protocol = connection != null ? connection.protocol() : Protocol.HTTP_1_1;
    String requestStartMessage = "--> " + request.method() + ' ' + request.url() + ' ' + protocol;
    if (!logHeaders && hasRequestBody) {
      requestStartMessage += " (" + requestBody.contentLength() + "-byte body)";
    }
    L.d(requestStartMessage);

    if (logHeaders) {
      if (hasRequestBody) {
        // Request body headers are only present when installed as a network interceptor. Force
        // them to be included (when available) so there values are known.
        if (requestBody.contentType() != null) {
          L.d("Content-AttachmentType: " + requestBody.contentType());
        }
        if (requestBody.contentLength() != -1) {
          L.d("Content-Length: " + requestBody.contentLength());
        }
      }

      Headers headers = request.headers();
      for (int i = 0, count = headers.size(); i < count; i++) {
        String name = headers.name(i);
        // Skip headers from the request body as they are explicitly logged above.
        if (!"Content-AttachmentType".equalsIgnoreCase(name) && !"Content-Length".equalsIgnoreCase(name)) {
          L.d(name + ": " + headers.value(i));
        }
      }

      if (!logBody || !hasRequestBody) {
        L.d("--> END " + request.method());
      } else if (bodyEncoded(request.headers())) {
        L.d("--> END " + request.method() + " (encoded body omitted)");
      } else {
        Buffer buffer = new Buffer();
        requestBody.writeTo(buffer);

        Charset charset = UTF8;
        MediaType contentType = requestBody.contentType();
        if (contentType != null) {
          charset = contentType.charset(UTF8);
        }

        L.d("");
        L.d(buffer.readString(charset));

        L.d("--> END " + request.method() + " (" + requestBody.contentLength() + "-byte body)");
      }
    }

    long startNs = System.nanoTime();
    Response response = chain.proceed(request);
    long tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs);

    ResponseBody responseBody = response.body();
    long contentLength = responseBody.contentLength();
    String bodySize = contentLength != -1 ? contentLength + "-byte" : "unknown-length";
    L.d("<-- " + response.code() + ' ' + response.message() + " (" + tookMs + "ms" + (!logHeaders ? ", " + bodySize + " body"
        : "") + ')');

    L.d(response.request().url().toString());
    if (request.body() != null && request.body() instanceof FormBody) {
      FormBody formBody = (FormBody) request.body();
      L.d("================= formed url: ");
      for (int i = 0; i < formBody.size(); i++) {
        L.d(formBody.name(i) + ":" + formBody.value(i));
      }
    }

    if (logHeaders) {
      //Headers headers = response.headers();
      //for (int i = 0, count = headers.size(); i < count; i++) {
      //    L.d(headers.name(i) + ": " + headers.value(i));
      //}

      if (!logBody) {
        L.d("<-- END HTTP");
      } else if (bodyEncoded(response.headers())) {
        L.d("<-- END HTTP (encoded body omitted)");
      } else {
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = UTF8;
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
          try {
            charset = contentType.charset(UTF8);
          } catch (UnsupportedCharsetException e) {
            L.d("");
            L.d("Couldn't decode the response body; charset is likely malformed.");
            L.d("<-- END HTTP");

            return response;
          }
        }

        if (contentLength != 0) {
          L.d("");
          L.d(buffer.clone().readString(charset));
        }

        L.d("<-- END HTTP (" + buffer.size() + "-byte body)");
      }
    }

    return response;
  }

  private boolean bodyEncoded(Headers headers) {
    String contentEncoding = headers.get("Content-Encoding");
    return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
  }

  public enum Level {
    /** No logs. */
    NONE, /**
     * Logs request and response lines.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1 (3-byte body)
     *
     * <-- 200 OK (22ms, 6-byte body)
     * }</pre>
     */
    BASIC, /**
     * Logs request and response lines and their respective headers.
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-AttachmentType: plain/text
     * Content-Length: 3
     * --> END POST
     *
     * <-- 200 OK (22ms)
     * Content-AttachmentType: plain/text
     * Content-Length: 6
     * <-- END HTTP
     * }</pre>
     */
    HEADERS, /**
     * Logs request and response lines and their respective headers and bodies (if present).
     *
     * <p>Example:
     * <pre>{@code
     * --> POST /greeting http/1.1
     * Host: example.com
     * Content-AttachmentType: plain/text
     * Content-Length: 3
     *
     * Hi?
     * --> END GET
     *
     * <-- 200 OK (22ms)
     * Content-AttachmentType: plain/text
     * Content-Length: 6
     *
     * Hello!
     * <-- END HTTP
     * }</pre>
     */
    BODY
  }
}
