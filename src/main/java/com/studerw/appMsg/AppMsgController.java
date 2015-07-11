package com.studerw.appMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class AppMsgController {
    private static final Logger LOG = LoggerFactory.getLogger(AppMsgController.class);

    @Autowired ConcurrentHashMap<DeferredResult, String> waitingRequests;
    @Autowired AppMsgRepo appMsgRepo;

    @Value("${app.poll.time}")
    private Integer pollTime;

    @ModelAttribute
    public void initModel(ModelMap map) {
        map.addAttribute("pollTime", pollTime);
    }

    @RequestMapping("/index")
    public String index(ModelMap modelMap) {
        return "index";
    }

    @RequestMapping(value = "/appMsgs", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AppMsg> create(String msg, HttpServletRequest request) {
        LOG.debug("/create......{}", msg);
        AppMsg appMsg = appMsgRepo.create(msg);;
        return new ResponseEntity<AppMsg>(appMsg, HttpStatus.OK);
    }

    @RequestMapping(value = "/poll", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppMsg>> poll(@RequestParam(value = "startId", required = false) Integer startId) {
        LOG.debug("/poll......(startId=[{}])", startId);
        List<AppMsg> appMsgs = (startId == null) ? appMsgRepo.readAll() : appMsgRepo.readSubset(startId);
        return new ResponseEntity<List<AppMsg>>(appMsgs, HttpStatus.OK);
    }

    /**
     * Synchronous
     * @param startId
     * @return all or a subset (depending on {@code startId} parameter
     */
    @RequestMapping(value = "/appMsgs", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppMsg>> appMsgs(@RequestParam(value = "startId", required = false) Integer startId) {
        LOG.debug("/appMsgs......(startId=[{}])", startId);
        List<AppMsg> appMsgs = (startId == null) ? appMsgRepo.readAll() : appMsgRepo.readSubset(startId);
        return new ResponseEntity<List<AppMsg>>(appMsgs, HttpStatus.OK);
    }


}
