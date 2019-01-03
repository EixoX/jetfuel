package com.eixox.usecases;

import java.io.IOException;
import java.io.InputStream;
import java.lang.System.Logger.Level;
import java.lang.reflect.InvocationTargetException;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.Enumeration;
import java.util.TreeMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.eixox.JetfuelException;
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
public class UsecaseExecutionServlet<T, G> extends UsecaseExecution<T, G> implements Runnable {

	/**
	 * A static shared object mapper instance;
	 */
	protected static final ObjectMapper MAPPER = new ObjectMapper();

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
	protected final HttpServletRequest request;

	/**
	 * A private transient instance of the servlet response;
	 */
	protected final HttpServletResponse response;

	/**
	 * A private transient instance of the package name used to lookup usecases;
	 */
	protected final String packageName;

	/**
	 * Creates a new instance of the usecase execution servlet;
	 * 
	 * @param request
	 * @param response
	 * @param packageName
	 */
	public UsecaseExecutionServlet(HttpServletRequest request, HttpServletResponse response, String packageName) {
		this.request = request;
		this.response = response;
		this.packageName = packageName;
		this.execution_start = new Date();
	}

	/**
	 * Locates a usecase by name from the path info;
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private boolean parseUsecase() {
		try {
			this.name = this.packageName + request.getPathInfo().replace('/', '.');
			Class<?> usecaseClass = Class.forName(this.name);
			Object usecaseInstance = usecaseClass.getConstructor().newInstance();
			this.usecase = (UsecaseImplementation<T, G>) usecaseInstance;
			return true;
		} catch (ClassNotFoundException e) {
			// Sends a 404 - not found response if the usecase is not located
			try {
				response.sendError(404, "Usecase not found: " + request.getPathInfo());
			} catch (IOException e1) {
				JetfuelException.log(this, Level.DEBUG, e1);
			}
			return false;
		} catch (InstantiationException | IllegalAccessException e) {
			try {
				response.sendError(404,
						"Usecase can't be instantiated: " + request.getPathInfo() + " (" + e.getMessage() + ")");
			} catch (IOException e1) {
				JetfuelException.log(this, Level.DEBUG, e1);
			}
			return false;
		} catch (Exception e) {
			try {
				response.sendError(404,
						"Usecase is not a correct UsecaseImplementation: " +
								request.getPathInfo() +
								" (" +
								e.getMessage() +
								")");
			} catch (IOException e1) {
				JetfuelException.log(this, Level.DEBUG, e1);
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
			Class<T> paramsClass = usecase.getParamsClass();
			if (!Void.class.equals(paramsClass)) {

				RestrictionAspect<T> restrictionAspect = RestrictionAspect.getInstance(paramsClass);
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

				else
					this.params = parseParameters(restrictionAspect);
			}

			return true;

		} catch (Exception e) {

			// If something fails, fallback to writing to output VALIDATION_FAILED
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
			this.execution_end = new Date();
			return;
		}

		if (!parseParams()) {
			this.execution_end = new Date();
			this.outputUsecaseExecution(this);
			return;
		}

		this.headers = new TreeMap<String, Object>(String.CASE_INSENSITIVE_ORDER);
		for (Enumeration<String> headers = request.getHeaderNames(); headers.hasMoreElements();) {
			String name = headers.nextElement();
			String value = request.getHeader(name);
			this.headers.put(name, value);
		}

		UsecaseExecution<T, G> execution = this.usecase.execute(this.params, this.headers);
		this.outputUsecaseExecution(execution);
	}

	protected void outputUsecaseExecution(UsecaseExecution<T, G> execution) {
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
					JetfuelException.log(this, Level.DEBUG, e);
				}
				return;
			}
		}

		try {
			response.setContentType("application/json");
			MAPPER.writeValue(response.getOutputStream(), execution);
		} catch (Exception e) {
			JetfuelException.log(this, Level.DEBUG, e);
		}
	}

	private T createParameters(RestrictionAspect<T> aspect) {
		try {
			return aspect.dataType.getConstructor().newInstance();
		} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e1) {
			throw new JetfuelException(e1);
		}
	}

	private JetfuelException createFieldException(
			RestrictionAspect<T> aspect,
			RestrictionAspectField field,
			Object val,
			String message) {
		return new JetfuelException(
				String.join(
						"Unable to set value to field",
						aspect.dataType.getSimpleName() + "." + field.name,
						"(" + field.getDataType() + ")",
						"para \"" +
								(val != null
										? val.toString()
										: "NULL") +
								"\":",
						message));
	}

	/**
	 * Parses the request parameters as members of the usecase params class;
	 * 
	 * @param paramsClass
	 * @return
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	private T parseParameters(RestrictionAspect<T> aspect) {
		T target = createParameters(aspect);

		for (RestrictionAspectField field : aspect) {
			String val = request.getParameter(field.name);
			try {
				field.setValue(target, val);
			} catch (Exception e) {
				throw createFieldException(aspect, field, val, e.getMessage());
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
	private T parseMultipart(RestrictionAspect<T> aspect) {
		String encoding = request.getCharacterEncoding();
		Charset charset = Charset.forName(
				encoding == null || encoding.isEmpty()
						? "UTF-8"
						: encoding);
		T target = createParameters(aspect);
		for (RestrictionAspectField field : aspect) {
			try {
				Part part = request.getPart(field.name);
				if (part != null) {
					if (UploadedFile.class.isAssignableFrom(field.getDataType())) {
						field.setValue(target, new UploadedFile(part));
					} else {
						byte[] bytes = new byte[(int) part.getSize()];
						InputStream is = part.getInputStream();
						int r = is.read(bytes);
						is.close();
						String txt = r > 0
								? new String(bytes, charset)
								: null;
						field.setValue(target, txt);
					}
				}
			} catch (Exception e) {
				throw createFieldException(aspect, field, null, e.getMessage());
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
