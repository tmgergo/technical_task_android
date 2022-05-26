package uk.co.tmgergo.userstechtest.utils

import android.content.Context
import uk.co.tmgergo.userstechtest.R

class AndroidTextProvider(private val context: Context) : TextProvider {

    override fun getGenericErrorMessage() =
        context.getString(R.string.something_went_wrong)

}
