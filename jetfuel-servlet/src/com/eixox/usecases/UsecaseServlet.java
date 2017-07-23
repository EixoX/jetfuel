package com.eixox.usecases;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * A all URL api servlet that maps to actual usecase implementations.
 * 
 * @author Rodrigo Portela
 *
 */
@WebServlet("/*")
@MultipartConfig
public class UsecaseServlet extends HttpServlet {

	/**
	 * The package root of the usecases that can be consumed through this api;
	 */
	private static final String PACKAGE_ROOT = "com.eixox.usecases";

	/**
	 * The generated serial version UID;
	 */
	private static final long serialVersionUID = 7485393457646486178L;

	/**
	 * Enables CORS (Cross-origin resource sharing) for the servlet;
	 * 
	 * @param response
	 */
	private void setCors(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Credentials", "true");
		response.setHeader("Access-Control-Allow-Headers", "x-requested-with, Authorization");
	}

	/**
	 * Executes the usecase in a GET request;
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

		setCors(resp);

		UsecaseExecutionServlet executionServlet = new UsecaseExecutionServlet(req, resp, PACKAGE_ROOT);
		executionServlet.run();
	}

	/**
	 * Just says ok, send me shit.
	 */
	@Override
	protected void doOptions(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setCors(resp);
		resp.setStatus(HttpServletResponse.SC_OK);
	}

	/**
	 * Executes the usecase in a POST request;
	 */
	@SuppressWarnings("rawtypes")
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		setCors(resp);

		UsecaseExecutionServlet executionServlet = new UsecaseExecutionServlet(req, resp, PACKAGE_ROOT);
		executionServlet.run();
	}

}
