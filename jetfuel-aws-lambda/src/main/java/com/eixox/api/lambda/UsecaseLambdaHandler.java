package com.eixox.api.lambda;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.LinkedHashMap;
import java.util.Map;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestStreamHandler;
import com.eixox.restrictions.RestrictionAspect;
import com.eixox.restrictions.RestrictionAspectField;
import com.eixox.restrictions.RestrictionValidation;
import com.eixox.usecases.UsecaseExecution;
import com.eixox.usecases.UsecaseImplementation;
import com.eixox.usecases.UsecaseResultType;
import com.fasterxml.jackson.databind.ObjectMapper;

public class UsecaseLambdaHandler implements RequestStreamHandler {

	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String PACKAGE_ROOT = "com.eixox.usecases";

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void handleRequest(InputStream inputStream, OutputStream outputStream, Context context) throws IOException {

		Map<String, Object> input = MAPPER.readValue(inputStream, Map.class);
		String usecase_name = (String) input.get("path");
		if ("/Debug".equalsIgnoreCase(usecase_name)) {
			handleDebugRequest(input, outputStream, context);
			return;
		}
		usecase_name = usecase_name.replace('/', '.');
		UsecaseExecution execution = new UsecaseExecution<>();
		try {
			execution.usecase = (UsecaseImplementation) Class.forName(PACKAGE_ROOT + usecase_name).newInstance();
		} catch (Exception e) {
			execution.validation = new RestrictionValidation(usecase_name, false, e.getMessage());
			execution.result_type = UsecaseResultType.EXCEPTION;
			writeOutput(outputStream, execution);
			return;
		}

		try {
			execution.params = createParams(input, execution.usecase.getParamsClass());
		} catch (Exception e) {
			execution.validation = new RestrictionValidation("params", false, e.getMessage());
			execution.result_type = UsecaseResultType.VALIDATION_FAILED;
			writeOutput(outputStream, execution);
			return;
		}

		execution.headers = (Map<String, Object>) input.get("headers");
		execution.run();

		writeOutput(outputStream, execution);
	}

	private void writeOutput(OutputStream output, UsecaseExecution<?, ?> execution) throws IOException {
		Map<String, Object> responseJson = new LinkedHashMap<>();
		responseJson.put("statusCode", 200);
		responseJson.put("body", MAPPER.writeValueAsString(execution));
		MAPPER.writeValue(output, responseJson);
	}

	private void handleDebugRequest(Map<String, Object> input, OutputStream outputStream, Context context)
			throws IOException {
		Map<String, Object> responseJson = new LinkedHashMap<>();
		try {
			responseJson.put("statusCode", 200);
			responseJson.put("body", MAPPER.writeValueAsString(input));

		} catch (Exception pex) {
			responseJson.put("statusCode", "500");
			responseJson.put("exception", pex);
		}
		MAPPER.writeValue(outputStream, responseJson);
	}

	/**
	 * Holds the content types to be checked for parsing json.
	 */
	private static final String[] JSON_CONTENT_TYPES = new String[] { "application/json", "application/x-javascript",
			"text/plain", "text/javascript", "text/x-javascript", "text/x-json" };

	@SuppressWarnings("unchecked")
	private <TParams> TParams createParams(Map<String, Object> request, Class<TParams> paramsClass) throws Exception {

		// Nothing to do if the paramsClass is void;
		if (Void.class.equals(paramsClass) || Void.TYPE.equals(paramsClass))
			return null;

		RestrictionAspect<TParams> aspect = RestrictionAspect.getInstance(paramsClass);
		Map<String, Object> headers = (Map<String, Object>) request.get("headers");
		String contentType = (String) headers.get("content-type");
		String method = (String) request.get("httpMethod");

		if ("post".equalsIgnoreCase(method)) {

			// parse json
			for (int i = 0; i < JSON_CONTENT_TYPES.length; i++)
				if (contentType.startsWith(JSON_CONTENT_TYPES[i])) {
					String json = (String) request.get("body");
					return MAPPER.readValue(json, aspect.dataType);
				}

			// parse body parameters
			return parseBodyParams(aspect, (String) request.get("body"));
		} else
			return parseQueryString(request, aspect);
	}

	@SuppressWarnings("unchecked")
	private <TParams> TParams parseQueryString(Map<String, Object> request, RestrictionAspect<TParams> aspect)
			throws Exception {
		Map<String, Object> qs = (Map<String, Object>) request.get("queryStringParameters");
		TParams params = aspect.dataType.newInstance();
		if (qs != null)
			for (RestrictionAspectField field : aspect) {
				Object val = qs.get(field.name);
				if (val != null)
					field.setValue(params, val);
			}
		return params;
	}

	private <TParams> TParams parseBodyParams(RestrictionAspect<TParams> aspect, String queryString) throws Exception {
		final Map<String, String> query_pairs = new LinkedHashMap<String, String>();
		final String[] pairs = queryString.split("&");
		for (String pair : pairs) {
			final int idx = pair.indexOf("=");
			final String key = idx > 0 ? URLDecoder.decode(pair.substring(0, idx), "UTF-8")
					: URLDecoder.decode(pair, "UTF-8");
			final String value = idx > 0 && pair.length() > idx + 1
					? URLDecoder.decode(pair.substring(idx + 1), "UTF-8") : null;
			final String other = query_pairs.get(key);
			query_pairs.put(key, other == null ? value : (other + ", " + value));
		}
		TParams params = aspect.dataType.newInstance();
		for (RestrictionAspectField field : aspect) {
			String val = query_pairs.get(field.name);
			if (val != null)
				field.setValue(params, val);
		}
		return params;
	}
}
