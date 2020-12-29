var SelectData = function (api){
    var returnData;
    $.ajax({
        url: api,
        //type、contentType必填,指明传参方式
        type: "POST",
        data: "",
        async : false,
        contentType: "application/json;charset=utf-8",
        success: function(response){
            //前端调用成功后，可以处理后端传回的json格式数据。
            returnData = response;
        }
    });
    return returnData;
}