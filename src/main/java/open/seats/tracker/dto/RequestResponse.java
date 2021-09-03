package open.seats.tracker.dto;

import com.google.gson.JsonObject;

public class RequestResponse {

	String path;
	String queryString;
	JsonObject headers;
	String httpMethod;
	String payload;
	int httpStatus;
	String response;
	long timeTakenMillis;

	public JsonObject getHeaders() {
		return headers;
	}

	public void setHeaders(JsonObject headers) {
		this.headers = headers;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String requestPath) {
		path = requestPath;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getHttpMethod() {
		return httpMethod;
	}

	public void setHttpMethod(String requestMethod) {
		this.httpMethod = requestMethod;
	}

	public String getResponse() {
		return response;
	}

	public void setResponse(String response) {
		this.response = response;
	}

	public int getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(int httpStatus) {
		this.httpStatus = httpStatus;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public long getTimeTakenMillis() {
		return timeTakenMillis;
	}

	public void setTimeTakenMillis(long timeTakenMillis) {
		this.timeTakenMillis = timeTakenMillis;
	}

	@Override
	public String toString() {
		return "{ \"" + "timeTakenMillis" + "\":" + timeTakenMillis + ", \"" + "path\": \""
				+ (path != null && !path.trim().isEmpty() ? path : "") + "\", \"" + "queryString\": \""
				+ (queryString != null && !queryString.trim().isEmpty() ? queryString : "") + "\", \"" + "headers\": "
				+ (headers != null && !headers.isJsonNull() ? headers : "{}") + ", \"" + "httpMethod\": \""
				+ (httpMethod != null && !httpMethod.trim().isEmpty() ? httpMethod : "") + "\", \"" + "payload\": "
				+ (payload != null && !payload.trim().isEmpty() ? payload : "{}") + ", \"" + "httpStatus\": \""
				+ httpStatus + "\", \"" + "response\": "
				+ (response != null && !response.trim().isEmpty() ? response : "{}") + "}";
	}

}
