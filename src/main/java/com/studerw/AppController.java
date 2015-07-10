package com.studerw;

import com.studerw.support.web.FlashMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class AppController {
    private static final Logger LOG = LoggerFactory.getLogger(AppController.class);

    @Autowired ConcurrentHashMap<DeferredResult, String> waitingRequests;
    @Autowired MessageRepo messageRepo;

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String welcome(Principal principal) {
        return "redirect:/index";
    }

    @RequestMapping(value = "/index", method = RequestMethod.GET)
    public String index() {
        return "index";
    }

    @RequestMapping(value = "/ping/{msg}", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Message> ping(@PathVariable("msg") String msg, HttpServletRequest request) {
        LOG.debug("/ping......{}", msg);

        Message message = messageRepo.create(msg);;
        return new ResponseEntity<Message>(message, HttpStatus.OK);
    }

    @RequestMapping(value = "/poll", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Message>> poll(@RequestParam(value = "startId", required = false) Integer startId,
                                             HttpServletRequest request) {
        LOG.debug("/poll......(startId=[{}])", startId);
        List<Message> messages = (startId == null) ? messageRepo.readAll() : messageRepo.readSubset(startId);
        return new ResponseEntity<List<Message>>(messages, HttpStatus.OK);
    }

}
