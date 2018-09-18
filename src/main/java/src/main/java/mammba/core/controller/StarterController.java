/**
 * MainController.java - MAMMBA Application
 * 2018 All rights reserved.
 *
 */
package src.main.java.mammba.core.controller;

import java.io.IOException;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import src.main.java.mammba.core.exception.ServiceException;
import src.main.java.mammba.core.service.UserService;
import src.main.java.mammba.model.Member;

/**
 * This class serves as the main controller of the basic restful functions.
 *
 * @author Mardolfh Del Rosario
 *
 */
@RestController
public class StarterController {

	@Autowired
	@Qualifier(value="userMember")
	private UserService userMemberService;

	private static final Logger LOGGER = Logger.getLogger(StarterController.class);

    /**
     * Initializes tokens and creates cookies.
     *
     * @param session           Session where to get token.
     * @param response          Response reference.
     * @throws IOException      Error in generating cookies.
     */
    @GetMapping(value= {"/init"})
    public String initialize(HttpSession session, HttpServletResponse response) throws IOException {
        CsrfToken token = (CsrfToken)
          session.getAttribute("org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository.CSRF_TOKEN");

        if (token != null) {
            LOGGER.info(session.getId() + " has logged in having: " + token.getToken());
            Cookie cookie = new Cookie("code", token.getToken());
            response.addCookie(cookie);
            return token.getToken();
        } else {
            response.sendError(404);
        }

        return null;

    }

    /**
     * Login session to the Mammba API.
     *
     * @param session           Session object.
     */
    @PostMapping(value = {"/login"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void login(HttpSession session) {
        LOGGER.info(session.getId() + " has logged in.");

    }

	/**
     * Logout session to the Mammba API.
     *
     * @param session           Session object.
     */
    @PostMapping(value = {"/mammba-user/logout"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void logout(HttpSession session) {
        LOGGER.info(session.getId() + " has logged out.");
        session.invalidate();
    }

	/**
	 * Registers a new Mammba user - member.
	 *
	 * @param member           Member reference from request.
	 * @return                 ResponseEntity - bad request or success.
	 */
	@PostMapping(value = { "/register" })
	public ResponseEntity<?> registerNewMember(@RequestBody(required = true) Member member) {
		LOGGER.info("registerNewMember()-start");
		LOGGER.info(member);
	    try {
		    this.userMemberService.register(member);
		} catch (ServiceException e) {
			return ResponseEntity.badRequest()
			        .body("MAMMBA[MC]-01-Exception occurred on register.");
		}

	    LOGGER.info("registerNewMember()-end");
		return ResponseEntity.ok("MAMMBA[MC]-02-SUCCESS");
	}

	/**
     * Get Mammba user info details.
     *
     * @param username         Member reference from request.
     * @return                 ResponseEntity - bad request or success.
     */
    @PostMapping(value = { "/mammba-user" })
    public ResponseEntity<?> getUserInfo(@RequestParam String username) {
        LOGGER.info("getUserInfo()-start");
        LOGGER.info(username);

        LOGGER.info("getUserInfo()-end");
        return ResponseEntity.ok("MAMMBA[MC]-03-Got user details.");
    }

}