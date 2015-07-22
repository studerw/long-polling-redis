package com.studerw.appMsg;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.async.DeferredResult;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Controller
public class AppMsgController {
    private static final Logger LOG = LoggerFactory.getLogger(AppMsgController.class);

    @Autowired AppMsgHandler appMsgHandler;
    @Autowired AppMsgRepo appMsgRepo;

    @Value("${app.poll.time}")
    private Long pollTime;
    @Value("${app.async.timeout}")
    private Long asyncTimeout;


    @RequestMapping("/index")
    public String index(ModelMap modelMap) {
        LOG.info("Setting times: [Naive pollTime={}, asyncTimeout={}", pollTime, asyncTimeout);
//        this.appMsgHandler.clear();
//        this.appMsgRepo.deleteAll();
        modelMap.addAttribute("pollTime", pollTime);
        modelMap.addAttribute("asyncTimeout", asyncTimeout);
        return "index";
    }

    /**
     * @param msg
     * @param request
     * @return the created appMsg with id and timeStamp set
     */
    @RequestMapping(value = "/appMsgs", method = RequestMethod.POST, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<AppMsg> create(String msg, HttpServletRequest request) {
        LOG.info("/create......{}", msg);
        AppMsg appMsg = appMsgRepo.create(msg);
        return new ResponseEntity<AppMsg>(appMsg, HttpStatus.OK);
    }

    /**
     * Async
     *
     * @param startId
     * @return all or a subset of {@code AppMsgs} (depending on {@code startId} parameter
     */
    @RequestMapping(value = "/appMsgsAsync", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public
    @ResponseBody
    DeferredResult<List<AppMsg>> appMsgsAsync(@RequestParam(value = "startId", required = false) Integer startId) {
        LOG.debug("/appMsgsAsync......(startId=[{}])", startId);
        startId = startId == null ? 0 : startId;

        //create the deferred result with an empty collection in case of error, no timeout is set
        final DeferredResult<List<AppMsg>> deferredResult = new DeferredResult<List<AppMsg>>(asyncTimeout, Collections.EMPTY_LIST);

        //If there are messages that have yet to be processed, no need to deal with the pubsub - just get them. The client will
        //next have to make a new request that should immediately block.
        List<AppMsg> messages = this.appMsgRepo.readSubset(startId);
        if (!messages.isEmpty()) {
            deferredResult.setResult(messages);
        }
        //No new messages - let's wait for pubsub alert
        else {
            appMsgHandler.addAsyncRequest(deferredResult, startId);
        }

        return deferredResult;
    }

    /**
     * Synchronous
     *
     * @param startId
     * @return all or a subset (depending on {@code startId} parameter
     */
    @RequestMapping(value = "/appMsgs", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AppMsg>> appMsgs(@RequestParam(value = "startId", required = false) Integer startId) {
        LOG.debug("/appMsgs......(startId=[{}])", startId);
        List<AppMsg> appMsgs = (startId == null) ? appMsgRepo.readAll() : appMsgRepo.readSubset(startId);
        return new ResponseEntity<List<AppMsg>>(appMsgs, HttpStatus.OK);
    }


    /**
     * Delete all the messages in the repository.
     *
     * @return a count of deleted messages
     */
    @RequestMapping(value = "/appMsgs", method = RequestMethod.DELETE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> appMsgs() {
        Integer count = this.appMsgRepo.deleteAll();
        LOG.debug("Deleted all {} messages", count);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }

    /**
     *
     * @return a count of waiting async requests
     */
    @RequestMapping(value = "/asyncRequests", method = RequestMethod.GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Integer> asyncCount() {
        Integer count = this.appMsgHandler.count();
        LOG.debug("Count of waiting async requests: {}", count);
        return new ResponseEntity<Integer>(count, HttpStatus.OK);
    }


}
