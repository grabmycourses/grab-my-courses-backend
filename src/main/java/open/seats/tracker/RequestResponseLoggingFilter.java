package open.seats.tracker;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpStatus;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;

import lombok.extern.log4j.Log4j2;
import open.seats.tracker.dto.RequestResponse;

@Log4j2
public class RequestResponseLoggingFilter implements Filter {

	private static final String MULTIPART_CONTENT_TYPE = "multipart/form-data";
	private static final String APPLICATION_JSON_CONTENT_TYPE = "application/json";
	private static final String APPLICATION_TEXT_CONTENT_TYPE = "text/plain";

	@Override
	public void init(FilterConfig config) throws ServletException {
		log.info("Initializing RequestResponseLoggingFilter ...");
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) request;

		RequestResponse requestResponse = new RequestResponse();

		requestResponse.setPath(httpServletRequest.getRequestURI());
		requestResponse.setHttpMethod(httpServletRequest.getMethod());
		requestResponse.setQueryString(httpServletRequest.getQueryString());

		Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
		JsonObject jsonObject = new JsonObject();

		while (headerNames.hasMoreElements()) {
			String currentHeaderName = headerNames.nextElement();
			jsonObject.addProperty(currentHeaderName, httpServletRequest.getHeader(currentHeaderName));
		}

		requestResponse.setHeaders(jsonObject);

		ReadHttpServletRequest requestWrapper = null;

		String requestContentType = httpServletRequest.getContentType();

		LocalDateTime start = LocalDateTime.now();
		if (StringUtils.isNotBlank(requestContentType) && !requestContentType.contains(MULTIPART_CONTENT_TYPE)
				&& !requestContentType.contains("x-www-form-urlencoded")) {
			requestWrapper = new ReadHttpServletRequest((HttpServletRequest) request);
		} else {
			String payloadMessage = "\"" + "Not logging payload because Content-Type of request is "
					+ requestContentType + "\"";
			requestResponse.setPayload(payloadMessage);
		}

		HttpServletResponse httpServletResponse = (HttpServletResponse) response;
		HttpServletResponseCopier responseCopier = new HttpServletResponseCopier(httpServletResponse);

		try {
			chain.doFilter(requestWrapper != null ? requestWrapper : request, responseCopier);
		} catch (Exception exception) {
			log.error("Internal server error in {} {} ", requestResponse.getPath(), exception);
			responseCopier.setStatus(HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseCopier.setContentType(APPLICATION_JSON_CONTENT_TYPE);
			responseCopier.setCharacterEncoding("UTF-8");
			Map<String, Object> responseData = new HashMap<>();
			Map<String, Object> status = new HashMap<>();
			status.put("statusCode", -1002);
			status.put("message", "Internal Server Error");
			status.put("localizedMessage", "Internal Server Error");
			status.put("httpStatusCode", HttpStatus.SC_INTERNAL_SERVER_ERROR);
			responseData.put("status", status);
			responseData.put("responseData", new HashMap<>());
			responseCopier.getWriter().write((new ObjectMapper().writer()).writeValueAsString(responseData));
		}

		if (requestWrapper != null) {
			String payload = IOUtils.toString(requestWrapper.getInputStream());

			if (!requestContentType.contains(APPLICATION_JSON_CONTENT_TYPE)) {
				payload = "\"" + payload + "\"";
			}

			requestResponse.setPayload(payload);
		}

		responseCopier.flushBuffer();
		byte[] copy = responseCopier.getCopy();
		String apiResponse = new String(copy, responseCopier.getCharacterEncoding());

		requestResponse.setHttpStatus(responseCopier.getStatus());
		String responseContentType = responseCopier.getContentType();

		if (StringUtils.isNotBlank(responseContentType) && (responseContentType.contains(APPLICATION_JSON_CONTENT_TYPE)
				|| responseContentType.contains(APPLICATION_TEXT_CONTENT_TYPE))) {
			String responseMessage = "";
			if (apiResponse.length() > 12000) {
				responseMessage = apiResponse.substring(0, 12000);
				responseMessage += ".......";
			} else {
				responseMessage = apiResponse;
			}
			requestResponse.setResponse(responseMessage);
		} else {
			String responseMessage = "\"" + "Not logging response because Content-Type of response is "
					+ responseContentType + "\"";
			requestResponse.setResponse(responseMessage);
		}

		requestResponse.setTimeTakenMillis(ChronoUnit.MILLIS.between(start, LocalDateTime.now()));
		log.info("{}", requestResponse);
	}

	@Override
	public void destroy() {
		log.info("Destroying RequestResponseLoggingFilter ...");
	}

}
