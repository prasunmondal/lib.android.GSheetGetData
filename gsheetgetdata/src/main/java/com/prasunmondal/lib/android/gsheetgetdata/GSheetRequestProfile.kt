package com.prasunmondal.lib.android.gsheetgetdata

import android.content.Context
import org.json.JSONObject
import java.lang.reflect.Type

class GSheetRequestProfile {
    private var scriptID: String
    private var sheetID: String
    private var tabname: String
    private var serachColumn: Int
    private var searchKey: String

    private var generateURL: String? = null
    private var volleyObject: VolleyBlockingRequestCreate

    constructor(scriptID: String, sheetID: String, tabname: String, serachColumn: Int, serachKey: String) {
        this.scriptID = scriptID
        this.sheetID = sheetID
        this.tabname = tabname
        this.serachColumn = serachColumn
        this.searchKey = serachKey

        volleyObject = VolleyBlockingRequestCreate()
    }

    private fun getURL(): String {
        generateURL = "https://script.google.com/macros/s/" + scriptID + "/exec?" +
                "id=" + sheetID +
                "&sheet=" + tabname +
                "&matchValue=" + searchKey +
                "&matchCol=" + serachColumn
        return generateURL as String
    }

    fun execute(context: Context, searchKey: String, onComplete: (JSONObject) -> Unit) {
        this.searchKey = searchKey
        volleyObject.startParsingTask(context, getURL(), onComplete)
    }

    fun <T> getParsedDataList(type: Type, arrayLabel: String): List<T>? {
        return volleyObject.parseJSONObject<T>(type, arrayLabel) as List<T>
    }
}