function hide(time) {
    setInterval(function(){
        $("#msg_center").html('')
    }, time)
};

function smsg(str) {
    $("#msg_center").html('<span class="alert alert-success">' + str + '</span>');
    hide(5000);
};

function emsg(str) {
    $("#msg_center").html('<span class="alert alert-error">' + str + '</span>')
    hide(5000);
};

function post() {
    var tickle = $("#tickle").val().trim();
    if (tickle.length > 0) {
        var json = {'text':tickle}
            jsRoutes.controllers.Application.tickle().ajax({
                data: JSON.stringify(json),
                contentType: "application/json",
                success: function(data) {
                    if (data.success) {
                        smsg("Succesfully posted :).")
                        $("#tickles").prepend('<div class="alert alert-success">' + json.text + '</div>');
                    }
                    if (data.failure) {
                        emsg("Posting failed. reason: " + data.failure.reason);
                    }
                }
            });
    } else {
        emsg("Tickle is Empty. Post Something.")
    }
}