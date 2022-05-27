package uk.co.tmgergo.userstechtest.utils

import android.content.Context
import uk.co.tmgergo.userstechtest.R

class AndroidTextProvider(private val context: Context) : TextProvider {

    override fun getGenericErrorMessage() =
        context.getString(R.string.something_went_wrong)

    override fun getDeletionFailedMessage() =
        context.getString(R.string.could_not_remove_user)

    override fun getAdditionFailedMessage()=
        context.getString(R.string.could_not_add_new_user)

}
