package com.qra.project;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.mortbay.log.Log;

import com.google.appengine.api.blobstore.BlobKey;
import com.google.appengine.api.blobstore.BlobstoreServiceFactory;

public class EditProfileServlet extends HttpServlet {
	private static final Logger log = Logger
			.getLogger(CheckinAttendentServlet.class.getName());

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {

		HttpSession session = req.getSession();
		String userIdFromSess = (String) session.getAttribute("userSess");
		String userIdFromCookie = CookieCheck.check(req, resp);


		log.info("userIdFromCookie: " + userIdFromCookie);
		log.info("userIdFromSess: " + userIdFromSess);

		if (userIdFromSess == null && userIdFromCookie == null) {
			resp.sendRedirect("/index");
		} 
		else {
			User myUser = GetUserFromId.getUser(userIdFromSess,
					userIdFromCookie);
			session.setAttribute("user", myUser);
			resp.sendRedirect("editprofile.jsp");
		}

	}

	public void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws IOException {
		resp.setContentType("text/HTML");
		PersistenceManager pm = PMF.get().getPersistenceManager();

		String first_name = req.getParameter("first_name");
		String middle_name = req.getParameter("middle_name");
		String last_name = req.getParameter("last_name");
		String birthdate = req.getParameter("birthdate");
		String profile_img = req.getParameter("profile_img");
		String oldpassword = req.getParameter("oldpassword");
		String newpassword1 = req.getParameter("newpassword1");
		String newpassword2 = req.getParameter("newpassword2");
		String email = req.getParameter("email");
		String username = req.getParameter("username");
		
		Map<String, List<BlobKey>> files_sent = BlobstoreServiceFactory.getBlobstoreService().getUploads(req);
	    BlobKey file_uploaded_key = files_sent.get("profile_img").get(0);
	    profile_img = file_uploaded_key.getKeyString();
		String cookieValue = "";
		cookieValue = CookieCheck.check(req, resp);


//		if (first_name == null || middle_name == null || last_name == null
//				|| profile_img == null || birthdate == null) {
//
			log.info("first_name: " + first_name);
			log.info("middle_name: " + middle_name);
			log.info("last_name: " + last_name);
			log.info("profile_img: " + profile_img);
			log.info("birthdate: " + birthdate);
//
//			resp.sendError(HttpServletResponse.SC_BAD_REQUEST,
//					"A Parameter is null.");
//		} 
//		else {
			Query q = pm.newQuery(User.class, "username == '" + cookieValue
					+ "'");
			List<User> results = (List<User>) q.execute();
			
			log.info("first_name: " + first_name);
			log.info("middle_name: " + middle_name);
			log.info("last_name: " + last_name);
			log.info("profile_img: " + profile_img);
			log.info("birthdate: " + birthdate);

			if (first_name.equalsIgnoreCase("")
					|| first_name == null) {
			} else {
				results.get(0).setFirst_name(first_name);
			}
			if (middle_name.equalsIgnoreCase("")
					|| middle_name == null) {
			} 
			else {
				results.get(0).setMiddle_name(middle_name);
			}
			if (last_name.equalsIgnoreCase("")
					|| last_name == null) {
			} 
			else {
				results.get(0).setLast_name(last_name);
			}
			if (newpassword1.equalsIgnoreCase("")
					|| newpassword1 == null) {
			} 
			else {
				if (!results.get(0).getPassword().equals(oldpassword)) {
				} 
				else if (!newpassword1.equals(newpassword2)) {
				} 
				else {
					results.get(0).setPassword(newpassword1);
				}
			}
			if (birthdate.equalsIgnoreCase("")
					|| birthdate == null
					|| birthdate.length() != 10) {
			} 
			else {
				results.get(0).setBirthdate(birthdate);
			}
			if (profile_img.equalsIgnoreCase("")
					|| profile_img == null) {
			} 
			else {
				results.get(0).setProfile_img(profile_img);
			}
			
			Query q3 = pm.newQuery(User.class, "username == '"  + username + "'");
			List<User> results3 = (List<User>) q.execute();
			Query q2 = pm.newQuery(User.class, "email == '"  + email + "'");
			List<User> results2 = (List<User>) q2.execute();
			//Check for username that already exists
			if(results3.size() == 0){
				results.get(0).setUsername(username);
			}
			//Check for if the email is already used
			if(results2.size() == 0){
				results.get(0).setEmail(email);
			}
			resp.sendRedirect("/index");
		}
//	}

}
