function pageLoader(page, pageSize){
    jsRoutes.controllers.Application.tickles(page, pageSize).ajax({
        success: function(data) {
            if (data.success) {
                var list = data.success;
                for(var i = 0; i < list.length; i++) {
                    $("#tickles").html('<span class="alert alert-success">' + list.text + '</span>')
                }
            }
            if (data.failure) {
                emsg('Error retrieving information, reason: ' + data.failure.reason)
            }
        }
    });
}