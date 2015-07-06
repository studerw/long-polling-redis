package com.studerw;

import java.security.Principal;

import com.studerw.support.web.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AppController {
	private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String welcome(Principal principal) {
		return "redirect:/index";
	}

	@RequestMapping(value = "/index", method = RequestMethod.GET)
	public String index() {
		return "index";
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public ResponseEntity<Message> ping(HttpServletRequest request) {
		LOG.debug("/ping......");
		Message message = new Message("OK", Message.Type.SUCCESS);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

	@RequestMapping(value = "/poll", method = RequestMethod.GET)
	public ResponseEntity<Message> poll(HttpServletRequest request) {
		LOG.debug("/poll......");
		Message message = new Message("OK", Message.Type.SUCCESS);
		return new ResponseEntity<Message>(message, HttpStatus.OK);
	}

}
