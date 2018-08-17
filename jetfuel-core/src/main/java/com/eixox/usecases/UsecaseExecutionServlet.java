package com.eixox.usecases;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.eixox.restrictions.RestrictionAspect;
import com.eixox.restrictions.RestrictionAspectField;
import com.eixox.restrictions.RestrictionValidation;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * A servlet based usecase execution;
 * 
 * @author Rodrigo Portela
 *
 */
public class UsecaseExecutionServlet<TParams, TResult> extends UsecaseExecution<TParams, TResult> implements Runnable {

	/**
	 * A static shared object mapper instance;
	 */
	private static final ObjectMapper MAPPER = new ObjectMapper();

	/**
	 * The known content types to tell that the parameters are json.
	 */
	private static final String[] JSON_CONTENT_TYPES = new String[] {
			"application/json",
			"application/x-javascript",
			"text/plain",
			"text/javascript",
			"text/x-javascript",
			"text/x-json" };

	/**
	 * A private transient instance of the serlvet request;
	 */
	private transient final HttpServletRequest request;

	/**
	 * A private transient instance of the servlet response;
	 */
	private transient final HttpServletResponse response;

	/**
	 * A private transient instance of the package name used to lookup usecases;
	 */
	private transient final String package_name;

	/**
	 * Creates a new instance of the usecase execution servlet;
	 * 
	 * @param request
	 * @param response
	 * @param package_name
	 */
	public UsecaseExecutionServlet(HttpServletRequest request, HttpServletResponse response, String package_name) {
		this.request = request;
		this.response = response;
		this.package_name = package_name;
	}

	/**
	 * Locates a usecase by name from the path info;
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean parseUsecase() {
		try {
			// Locates a usecase by name from the path info;
			this.name = this.package_name + request.getPathInfo().replace('/', '.');
			Class<?> usecaseClass = Class.forName(this.name);
			Object usecaseInstance = usecaseClass.getConstructor().newInstance();
			this.usecase = (UsecaseImplementation<TParams, TResult>) usecaseInstance;
			return true;
		} catch (ClassNotFoundException e) {
			// Sends a 404 - not found response if the usecase is not located
			try {
				response.sendError(404, "Usecase not found: " + request.getPathInfo());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		} catch (InstantiationException e) {
			// Sends a 404 - not found if the usecase can't be instantiated;
			try {
				response.sendError(404,
						"Usecase can't be instantiated: " + request.getPathInfo() + " (" + e.getMessage() + ")");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		} catch (IllegalAccessException e) {
			// Sends a 404 - not found if the usecase can't be instantiated;
			try {
				response.sendError(404,
						"Usecase can't be instantiated: " + request.getPathInfo() + " (" + e.getMessage() + ")");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		} catch (Exception e) {
			// Sends a 404 - not found if the usecase can't be cast to an
			// usecase implementation or in any other exception;
			try {
				response.sendError(404,
						"Usecase is not a correct UsecaseImplementation: " + request.getPathInfo() + " ("
								+ e.getMessage() + ")");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			return false;
		}
	}

	/**
	 * Parses the parameters of the usecase execution;
	 * 
	 * @return
	 */
	private boolean parseParams() {
		try {
			Class<TParams> paramsClass = usecase.getParamsClass();
			if (!Void.class.equals(paramsClass)) {

				RestrictionAspect<TParams> restrictionAspect = RestrictionAspect.getInstance(paramsClass);
				String contentType = request.getContentType();

				// no content type?
				if (contentType == null || contentType.isEmpty())
					this.params = this.parseParameters(restrictionAspect);

				// json?
				else if (isJsonContentType(contentType))
					this.params = MAPPER.readValue(request.getInputStream(), paramsClass);

				// multipart?
				else if (contentType.startsWith("multipart/form-data"))
					this.params = parseMultipart(restrictionAspect);

				// Defaults to parsing parameters;
				else
					this.params = parseParameters(restrictionAspect);
			}

			// System.out.println(MAPPER.writeValueAsString(this));

			return true;

		} catch (Exception e) {

			// If something fails, fallback to writing to output
			// VALIDATION_FAILED and returns;
			this.validation = new RestrictionValidation("params", false, e.getMessage());
			this.result_type = UsecaseResultType.VALIDATION_FAILED;
			return false;
		}
	}

	/**
	 * Runs the usecase from the path info of the request, parsing the parameters or
	 * json body and writes the execution result to the response;
	 */
	@Override
	public void run() {

		if (!parseUsecase()) {
			return;
		}

		if (!parseParams()) {
			this.outputUsecaseExecution(this);
			return;
		}

		// Copies headers from the request to the execution;
		this.headers = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		for (Enumeration<String> headers = request.getHeaderNames(); headers.hasMoreElements();) {
			String name = headers.nextElement();
			String value = request.getHeader(name);
			this.headers.put(name, value);
		}

		UsecaseExecution<TParams, TResult> execution = this.usecase.execute(this.params, this.headers);
		this.outputUsecaseExecution(execution);
	}

	protected void outputUsecaseExecution(UsecaseExecution<TParams, TResult> execution) {
		// Special case: SUCCESS
		if (execution.result_type == UsecaseResultType.SUCCESS) {

			// Does the implementation has a custom result writer to write to
			// the output?
			UsecaseResultWriter resultWriter = usecase.getResultWriter(execution.result);
			if (resultWriter != null) {
				try {
					String contentDisposition = resultWriter.getContentDisposition();
					if (contentDisposition != null && !contentDisposition.isEmpty()) {
						response.setHeader("Content-Disposition", contentDisposition);
					}
					response.setContentType(resultWriter.getContentType());
					resultWriter.write(response.getOutputStream());
				} catch (Exception e) {
					e.printStackTrace();
				}
				return;
			}
		}

		try {
			response.setContentType("application/json");
			MAPPER.writeValue(response.getOutputStream(), execution);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parses the request parameters as members of the usecase params class;
	 * 
	 * @param paramsClass
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private TParams parseParameters(RestrictionAspect<TParams> aspect) throws Exception {
		TParams target = aspect.dataType.getConstructor().newInstance();
		for (RestrictionAspectField field : aspect) {
			String val = request.getParameter(field.name);
			try {
				field.setValue(target, val);
			} catch (Exception e) {
				throw new RuntimeException(
						String.join(
								" ",
								"Unable to set value to field",
								aspect.dataType.getSimpleName() + "." + field.name,
								"(" + field.getDataType() + ")",
								"para \"" + val + "\":",
								e.getMessage()));

			}
		}
		return target;
	}

	/**
	 * Parses the content of the input as a series of multipart binary encoded data;
	 * 
	 * @param paramsClass
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ServletException
	 * @throws IOException
	 */
	private TParams parseMultipart(RestrictionAspect<TParams> aspect) throws Exception {
		String encoding = request.getCharacterEncoding();
		Charset charset = Charset.forName(
				encoding == null || encoding.isEmpty()
						? "UTF-8"
						: encoding);
		TParams target = aspect.dataType.getConstructor().newInstance();
		for (RestrictionAspectField field : aspect) {
			try {
				Part part = request.getPart(field.name);
				if (part != null) {
					if (UploadedFile.class.isAssignableFrom(field.getDataType())) {
						field.setValue(target, new UploadedFile(part));
					} else {
						byte[] bytes = new byte[(int) part.getSize()];
						InputStream is = part.getInputStream();
						is.read(bytes);
						is.close();
						String txt = new String(bytes, charset);
						field.setValue(target, txt);
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(
						String.join(
								" ",
								"Unable to set value to field",
								aspect.dataType.getSimpleName() + "." + field.name,
								"(" + field.getDataType() + ")",
								":",
								e.getMessage()));
			}
		}
		return target;
	}

	/**
	 * Checks if the given content type is a JSON_CONTENT_TYPE[]
	 * 
	 * @param contentType
	 * @return
	 */
	private boolean isJsonContentType(String contentType) {
		for (int i = 0; i < JSON_CONTENT_TYPES.length; i++)
			if (contentType.equalsIgnoreCase(JSON_CONTENT_TYPES[i]))
				return true;
		return false;
	}

	/**
	 * Gets the Method Verb of the request;
	 * 
	 * @return
	 */
	public String getVerb() {
		return this.request.getMethod();
	}
}
