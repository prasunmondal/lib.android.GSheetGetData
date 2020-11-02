package com.truiton.volleyblockingrequest.com.prasunmondal.lib.GSheetGetRequest

import android.content.Context
import com.google.gson.reflect.TypeToken
import com.truiton.volleyblockingrequest.Res
import org.json.JSONObject
import java.lang.reflect.Type
import kotlin.reflect.KClass

class GSheetRequestProfile {
    private var scriptID: String
    private var sheetID: String
    private var tabname: String
    private var serachColumn: Int
    private var serachKey: String

    private var generateURL: String? = null
    private var volleyObject: VolleyBlockingRequestCreate

    constructor(scriptID: String, sheetID: String, tabname: String, serachColumn: Int, serachKey: String) {
        this.scriptID = scriptID
        this.sheetID = sheetID
        this.tabname = tabname
        this.serachColumn = serachColumn
        this.serachKey = serachKey

        volleyObject = VolleyBlockingRequestCreate()
    }

    private fun getURL(): String {
        if(generateURL != null) {
            return generateURL!!
        }
        generateURL = "https://script.google.com/macros/s/" + scriptID + "/exec?" +
                "id=" + sheetID +
                "&sheet=" + tabname +
                "&matchValue=" + serachKey +
                "&matchCol=" + serachColumn
        return generateURL as String
    }

    fun execute(context: Context, onComplete: (JSONObject) -> Unit) {
        volleyObject.startParsingTask(context, getURL(), onComplete)
    }

    fun <T> getParsedDataList(type: Type, arrayLabel: String): List<T>? {
        return volleyObject.parseJSONObject<T>(type, arrayLabel) as List<T>
    }
}