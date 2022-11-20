package com.eemmez.localizationmanagersample

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.eemmez.localization.LocalizationManager

class MainActivity : AppCompatActivity() {

    private val localizationManager = LocalizationManager(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getTextView(R.id.tvNormalString).text = localizationManager.getString(R.string.normal_string)

        getTextView(R.id.tvBoldString).text = localizationManager.getSpannable(R.string.bold_string)

        getTextView(R.id.tvStringWithGenericId).text = localizationManager.getStringByGenericId("string_with_generic_id", R.string::class.java)

        getTextView(R.id.tvGenericString).text =
            localizationManager.getGenericSpannable(
                resId = R.string.generic_string_with_multiple_parameter,
                "number" to "0123456789",
                "name" to "Emirhan"
            )

        getTextView(R.id.tvLinkedString).apply {
            text = localizationManager.getClickableText(
                resId = R.string.linked_string,
                clickableTextColor = android.R.color.holo_red_dark,
                textView = this,
                "number" to {
                    Toast.makeText(this@MainActivity, "Number text clicked!", Toast.LENGTH_SHORT)
                        .show()
                },
                "name" to {
                    Toast.makeText(this@MainActivity, "Name text clicked!", Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }
    }

    private fun getTextView(resId: Int): TextView = findViewById(resId)
}