package com.creepersan.bingimage.api.v1

object V1Response{

    enum class ErrorCode(val code: Int){
        Forbidden(403),
        NotFound(404),
        ServerError(500),
        ParamsError(502),
    }

    fun ErrorCode.getErrorStringPrefix(): String {
        return when(this){
            ErrorCode.Forbidden -> "功能未开放"
            ErrorCode.NotFound -> "资源未找到"
            ErrorCode.ServerError -> "内部错误"
            ErrorCode.ParamsError -> "参数错误"
        }
    }

    fun createResponse(code: Int, message: String, data: Any? = null): Map<String, Any>{
        val returnData = HashMap<String, Any>()
        returnData["flag"] = code
        returnData["message"] = message
        if (data != null){
            returnData["data"] = data
        }
        return returnData
    }

    fun createSuccessResponse(data: Any): Map<String, Any>{
        return createResponse(200, "操作成功", data)
    }

    fun createFailResponse(errorCode: ErrorCode, message: String? = null): Map<String, Any>{
        val messageString = if (message == null) "" else "，$message"
        return createResponse(errorCode.code, "${errorCode.getErrorStringPrefix()}$messageString", null)
    }

}
