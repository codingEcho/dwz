package com.olink.hsh.action.common;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.olink.hsh.dao.IShortUrlDao;
import com.olink.hsh.dao.impl.ShortUrlDao;

/**
 * Servlet implementation class TinyUrlServlet
 */
public class TinyUrlServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TinyUrlServlet() {
		super();
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		StringBuffer requestUrl = request.getRequestURL();
		String sequence = requestUrl.toString().substring(requestUrl.toString().lastIndexOf("/") + 1);
		IShortUrlDao shortUrlDao = new ShortUrlDao();
		String longUrl = shortUrlDao.findLongUrlByShortUrl(sequence);
		logger.info("sequence:" + sequence + "  longUrl:" + longUrl);
		response.sendRedirect(longUrl);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
