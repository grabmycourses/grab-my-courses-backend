package open.seats.tracker;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
 * custom HttpServletResponseWrapper for logging Response.
 */
public class HttpServletResponseCopier extends HttpServletResponseWrapper {

	private ServletOutputStream outputStream;
	private PrintWriter writer;
	private ServletOutputStreamCopier copier;

	public HttpServletResponseCopier(HttpServletResponse response) throws IOException {
		super(response);
	}

	@Override
	public ServletOutputStream getOutputStream() throws IOException {
		if (writer != null) {
			throw new IllegalStateException("getWriter() has already been called on this response.");
		}

		if (outputStream == null) {
			outputStream = getResponse().getOutputStream();
			copier = new ServletOutputStreamCopier(outputStream);
		}

		return copier;
	}

	@Override
	public PrintWriter getWriter() throws IOException {
		if (outputStream != null) {
			throw new IllegalStateException("getOutputStream() has already been called on this response.");
		}

		if (writer == null) {
			copier = new ServletOutputStreamCopier(getResponse().getOutputStream());
			writer = new PrintWriter(new OutputStreamWriter(copier, getResponse().getCharacterEncoding()), true);
		}

		return writer;
	}

	@Override
	public void flushBuffer() throws IOException {
		if (writer != null) {
			writer.flush();
		} else if (outputStream != null) {
			copier.flush();
		}
	}

	public byte[] getCopy() {
		if (copier != null) {
			return copier.getCopy();
		} else {
			return new byte[0];
		}
	}

	private class ServletOutputStreamCopier extends ServletOutputStream {

		private OutputStream outputStream;
		private ByteArrayOutputStream copy;

		public ServletOutputStreamCopier(OutputStream outputStream) {
			this.outputStream = outputStream;
			this.copy = new ByteArrayOutputStream(1024);
		}

		@Override
		public void write(int b) throws IOException {
			outputStream.write(b);
			copy.write(b);
		}

		public byte[] getCopy() {
			return copy.toByteArray();
		}

		@Override
		public boolean isReady() {

			return false;
		}

		@Override
		public void setWriteListener(WriteListener arg0) {

		}

	}
}
