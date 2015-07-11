var create = function () {
    var newMsg = $('#postMsg').val();
    if (_.isEmpty(newMsg)) {
        bootbox.alert("The messsage cannot be empty.", function () {
        });
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
            console.dir(data);
            bootbox.alert(JSON.stringify(data), function () {
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

var readAll = function () {
    $.ajax("/appMsgs", {
        type: "GET",
        accepts: {
            text: "application/json"
        },
        success: function (data) {
            console.dir(data);
            bootbox.alert(JSON.stringify(data), function () {
            });
        },
        error: function () {
            alert("Error");
        }
    });
};

$(document).ready(function () {
    $('#postBtn').click(function () {
        create();
    });
});
