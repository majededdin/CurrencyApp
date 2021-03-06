package com.majed.currencyapp.data.remote

import android.util.Log
import com.google.gson.Gson
import com.majed.currencyapp.BuildConfig
import com.majed.currencyapp.data.consts.Params.Companion.Errors
import com.majed.currencyapp.data.consts.Params.Companion.Message
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.HttpException
import java.lang.reflect.Type
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class ApiResponse<M> {

    var model: M? = null
    lateinit var message: String
    lateinit var apiStatus: ApiStatus
    var errorBodyAsJSON: JSONObject? = null

    private lateinit var parentJSON: JSONObject
    lateinit var modelAsJSONObject: JSONObject


    //------------------------------------------ Public Constructor --------------------------------


    constructor()


    constructor(apiStatus: ApiStatus) {
        this.apiStatus = apiStatus
    }


    /**
     * Used when there is model returned from api
     * with specific key @param jsonKey
     */
    constructor(responseBodyAsString: String, tokenType: Type) {
        try {
            this.parentJSON = JSONObject(responseBodyAsString)
            showResponse(parentJSON.toString())
            this.message = parentJSON.optString(Message)
            try {
                this.modelAsJSONObject = parentJSON
                this.model = getModelFromJSON(parentJSON, tokenType)
            } catch (e: JSONException) {
                showResponse("JSONException", e.message)
            }

        } catch (throwable: Throwable) {
            showResponse("Throwable", throwable.message)
        }
    }


    //------------------------------------------ Public Methods ------------------------------------


    fun getApiResult(): ApiResponse<M> {
        return try {
            handleJsonResult()
        } catch (throwable: Throwable) {
            getErrorBody(throwable)
        }
    }

    //------------------------------------------ Private Methods -----------------------------------


    private fun handleJsonResult(): ApiResponse<M> {
        return when {
            model != null -> {
                if (message.isNotEmpty())
                    ApiResponse(ApiStatus.OnSuccess, modelAsJSONObject, model!!, message)
                else
                    ApiResponse(ApiStatus.OnSuccess, modelAsJSONObject, model!!)
            }

            message.isNotEmpty() -> ApiResponse(ApiStatus.OnSuccess, message)

            else -> ApiResponse(ApiStatus.OnSuccess)
        }
    }


    internal fun getErrorBody(throwable: Throwable): ApiResponse<M> {
        return when (throwable) {
            is HttpException -> {
                val throwableMessage = throwable.response()!!.errorBody()!!.string()
                val jsonObject = JSONObject(throwableMessage)
                when (throwable.code()) {
                    401 -> ApiResponse(ApiStatus.OnAuth)

                    404 -> {
                        showResponse("Not Found", throwableMessage)
                        ApiResponse(ApiStatus.OnNotFound, jsonObject.getString(Message))
                    }

                    400 -> {
                        showResponse("bad Request", throwableMessage)
                        ApiResponse(ApiStatus.OnBadRequest, jsonObject.getString(Message))
                    }

                    422 -> {
                        showResponse("errorBody", throwableMessage)
                        if (jsonObject.has(Errors))
                            ApiResponse(ApiStatus.OnError, jsonObject.getJSONObject(Errors))
                        else
                            ApiResponse(ApiStatus.OnError, jsonObject.getString(Message))
                    }

                    500 -> {
                        showResponse("backEndException", throwableMessage)
                        ApiResponse(ApiStatus.OnBackEndError, throwableMessage)
                    }

                    else -> {
                        showResponse("HttpExceptionMsg", throwableMessage)
                        ApiResponse(ApiStatus.OnHttpException, throwableMessage)
                    }
                }
            }

            is UnknownHostException -> ApiResponse(
                ApiStatus.OnUnknownHost,
                throwable.message!!
            )

            is ConnectException -> ApiResponse(
                ApiStatus.OnConnectException,
                throwable.message!!
            )

            is SocketTimeoutException -> ApiResponse(
                ApiStatus.OnTimeoutException,
                throwable.message!!
            )

            else -> {
                val throwableMsg = throwable.message
                showResponse("throwableMsg", throwableMsg)
                ApiResponse(ApiStatus.OnFailure, throwableMsg!!)
            }
        }
    }


    //------------------------------------------ Private Setting Methods ---------------------------

    private fun showResponse(message: String?) {
        if (BuildConfig.DEBUG)
            Log.e("ApiResponse ", "JSONResponse: $message")
    }


    private fun showResponse(key: String, message: String?) {
        if (BuildConfig.DEBUG)
            Log.e("ApiResponse ", "$key: $message")
    }

    //------------------------------------------ Private Parsing Methods ---------------------------


    private fun getModelFromJSON(jsonObject: JSONObject, tokenType: Type): M {
        return Gson().fromJson(jsonObject.toString(), tokenType)
    }


    private fun getListOfModelFromJSON(jsonArray: JSONArray, tokenType: Type): ArrayList<M> {
        return Gson().fromJson(
            jsonArray.toString(),
            tokenType
        )
    }


    //------------------------------------------ Private Constructor --------------------------------


    private constructor(apiStatus: ApiStatus, message: String) {
        this.apiStatus = apiStatus
        this.message = message
    }


    private constructor(apiStatus: ApiStatus, jsonObject: JSONObject, model: M) {
        this.apiStatus = apiStatus
        this.modelAsJSONObject = jsonObject
        this.model = model
    }


    private constructor(apiStatus: ApiStatus, jsonObject: JSONObject, model: M, message: String) {
        this.apiStatus = apiStatus
        this.modelAsJSONObject = jsonObject
        this.model = model
        this.message = message
    }


    private constructor(apiStatus: ApiStatus, errorBodyAsJSON: JSONObject) {
        this.apiStatus = apiStatus
        this.errorBodyAsJSON = errorBodyAsJSON
    }

}