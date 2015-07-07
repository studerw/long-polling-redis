package com.studerw;

import com.studerw.support.web.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.concurrent.ConcurrentHashMap;

@Controller
public class AppController {
    private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    @Autowired
    ConcurrentHashMap<DeferredResult, String> waitingRequests;


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome(Principal principal) {
        return "redirect:/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/ping/{message}", method = RequestMethod.GET)
    public ResponseEntity<Message> ping(@PathVariable String message, HttpServletRequest request) {
        LOG.debug("/ping......{}", message);
        Message response = new Message(message, Message.Type.SUCCESS);
        return new ResponseEntity<Message>(response, HttpStatus.OK);
    }

    @RequestMapping(value = "/poll", method = RequestMethod.GET)
    public ResponseEntity<Message> poll(HttpServletRequest request) {
        LOG.debug("/poll......");
        Message message = new Message("OK", Message.Type.SUCCESS);
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

}
