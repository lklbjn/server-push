package com.server.filter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.CharArrayWriter;
import java.io.PrintWriter;

/**
 * @author lklbjn
 */
public class ResponseWrapper extends HttpServletResponseWrapper {
    private final PrintWriter cachedWriter;
    private final CharArrayWriter bufferedWriter;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        this.bufferedWriter = new CharArrayWriter();
        this.cachedWriter = new PrintWriter(this.bufferedWriter);
    }

    @Override
    public PrintWriter getWriter() {
        return this.cachedWriter;
    }

    public String getResult() {
        return this.bufferedWriter.toString();
    }
}