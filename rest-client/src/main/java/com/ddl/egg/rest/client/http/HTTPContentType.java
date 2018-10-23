package com.ddl.egg.rest.client.http;

import com.ddl.egg.log.util.CharacterEncodings;
import org.apache.http.entity.ContentType;

import java.nio.charset.Charset;


public class HTTPContentType {
    static final Charset DEFAULT_CHARSET = CharacterEncodings.CHARSET_UTF_8;
    static final String DEFAULT_MIME_TYPE = HTTPConstants.CONTENT_TYPE_JSON;

    private ContentType contentType;

    public void setContentType(ContentType contentType) {
        this.contentType = contentType;
    }

    public String getMimeType() {
        if (contentType == null || contentType.getMimeType() == null) {
            return DEFAULT_MIME_TYPE;
        } else {
            return contentType.getMimeType();
        }
    }

    public Charset getCharset() {
        if (contentType == null || contentType.getCharset() == null) {
            return DEFAULT_CHARSET;
        }
        return contentType.getCharset();
    }

}
