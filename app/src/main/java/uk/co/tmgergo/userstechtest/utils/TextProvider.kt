package uk.co.tmgergo.userstechtest.utils

interface TextProvider {
    fun getGenericErrorMessage() : String
    fun getDeletionFailedMessage() : String
    fun getAdditionFailedMessage() : String
}
