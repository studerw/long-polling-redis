APP.syncPoll = function (index) {
    return $.ajax({
        url: SERVLET_CONTEXT + "/appMsgs?startId=" + index,
        accepts: {
            text: "application/json"
        }

    });
};

APP.asyncPoll = function (index) {
    return $.ajax({
        url: SERVLET_CONTEXT + "appMsgsAsync?startId=" + index,
        accepts: {
            text: "application/json"
        }
    });
};

APP.updateSync = function (data) {
    _.each(data, function (appMsg) {
        var id = appMsg.id;
        var latency = Date.now() - appMsg.timeStamp;
        if (latency > 1000){
            latency = Math.floor(latency / 1000)  + ' second(s)'
        }
        else {
            latency = latency +  ' ms';
        }
        APP.syncIndex = (id >= APP.syncIndex) ? id + 1 : APP.syncIndex;
        $('#syncTable tbody').append('<tr><td>'+appMsg.id+'</td><td>'+appMsg.message+'</td><td>'+latency+'</td></tr>');
    });
    ($('#syncTable tr').length > 0) ? $('#syncTable').show() : $('#syncTable').hide();

}

APP.updateAsync = function (data) {
    _.each(data, function (appMsg) {
        var id = appMsg.id;
        var latency = Date.now() - appMsg.timeStamp;
        if (latency > 1000){
            latency = Math.floor(latency / 1000)  + ' second(s)'
        }
        else {
            latency = latency +  ' ms';
        }
        APP.asyncIndex = (id >= APP.asyncIndex) ? id + 1 : APP.asyncIndex;
        $('#asyncTable tbody').append('<tr><td>'+appMsg.id+'</td><td>'+appMsg.message+'</td><td>'+latency+'</td></tr>');
    });
    ($('#asyncTable tr').length > 0) ? $('#asyncTable').show() : $('#asyncTable').hide();

};

APP.create = function () {
    var newMsg = $('#postMsg').val();
    if (_.isEmpty(newMsg)) {
        bootbox.alert("The messsage cannot be empty.", function () {});
        return false;
    }

    $.ajax("/appMsgs", {
        type: "POST",
        data: {
            msg: newMsg
        },
        accepts: {
            text: "application/json"
        },
        success: function (data) {
            $.bootstrapGrowl("Added message (" + data.id + "): " + data.message, {
                align: 'right',
                type: 'success',
                width: 'auto',
                offset: {from: 'bottom', amount: 20}, // 'top', or 'bottom'

            });
        },
        error: function () {
            bootbox.alert("Error", function () {
            });
        },
        complete: function () {
            $('#postMsg').val('');
        }

    });
};

//APP.deleteMsgs = function () {
//    $.ajax("/appMsgs", {
//        type: "DELETE",
//        accepts: {
//            text: "application/json"
//        },
//        success: function (data) {
//            $.bootstrapGrowl("Deleted " + data + ' messages.', {
//                align: 'right',
//                type: 'success',
//                width: 'auto',
//                offset: {from: 'bottom', amount: 20}, // 'top', or 'bottom'
//            });
//        },
//        error: function () {
//            bootbox.alert("Error", function () {
//            });
//        },
//    });
//};


var readAll = function () {
    $.ajax("/appMsgs", {
        type: "GET",
        accepts: {
            text: "application/json"
        },
        success: function (data) {
            console.dir(data);
            bootbox.alert(JSON.stringify(data), function () {});
        },
        error: function () {
            alert("Error");
        }
    });
};

$(document).ready(function () {
    $('#postBtn').click(function () {
        APP.create();
    });
    //$('#deleteBtn').click(function () {
    //    APP.deleteMsgs();
    //});

    //setup the sync poller
    setInterval(function () {
        $('#syncSpinner').show();
        APP.syncPoll(APP.syncIndex).done(function (result) {
            console.dir(result);
            APP.updateSync(result)
        }).always(function (result) {
            $('#syncSpinner').hide();
            var node = $('#syncCount');
            var count = $(node).attr('data-count');
            count = parseInt(count) + 1;
            $(node).attr('data-count', count.toString()).text(count.toString());
        });
    }, APP.pollTime);

    APP.recurseAsync = function(){
        $('#asyncSpinner').show();
        APP.asyncPoll(APP.asyncIndex).
            done(function (result) {
                console.dir(result);
                APP.updateAsync(result);

            }).always(function (result) {
                $('#asyncSpinner').hide();
                var node = $('#asyncCount');
                var count = $(node).attr('data-count');
                count = parseInt(count) + 1;
                $(node).attr('data-count', count.toString()).text(count.toString());
                APP.recurseAsync();
            });
    };
    APP.recurseAsync();
});
