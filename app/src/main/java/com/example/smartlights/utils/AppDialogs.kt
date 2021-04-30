package com.example.smartlights.utils;

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Typeface
import android.view.inputmethod.InputMethodManager
import com.example.smartlights.R


/**
 * @since 27/2/17.
 * Common dialogs used across app
 */

object AppDialogs {

    /**
     * Simple interface can be implemented for confirm an action via dialogs
     */
    interface ConfirmListener {
        fun yes()
    }

    interface OptionListener : ConfirmListener {
        fun no()
    }

    private var progress: ProgressDialog? = null

    /**
     * Confirm actions that are critical before proceeding
     *
     * @param c
     * @param text
     * @param l
     */
    fun confirmAction(c: Context, text: String, l: ConfirmListener?) {
        val builder = AlertDialog.Builder(c, R.style.AppCompatAlertDialogStyle)
        builder.setMessage(text)
        builder.setPositiveButton(c.resources.getString(android.R.string.yes)) { dialog, _ ->
            l?.yes()
            dialog.dismiss()
        }
        builder.setNegativeButton(c.resources.getString(android.R.string.no)) { dialog, _ -> dialog.dismiss() }
        builder.setOnCancelListener { dialogInterface -> dialogInterface.dismiss() }
        builder.create().show()
    }

    /**
     * Confirm actions that are critical before proceeding
     *
     * @param c
     * @param text
     * @param l
     */
    fun optionalAction(c: Context, text: String, l: OptionListener?, yes: String? = "YES", no: String? = "NO") {
        val builder = AlertDialog.Builder(c, R.style.AppCompatAlertDialogStyle)
        builder.setMessage(text)
        builder.setPositiveButton(yes) { dialog, _ ->
            l?.yes()
            dialog.dismiss()
        }

        builder.setNegativeButton(no) { dialog, _ ->
            l?.no()
            dialog.dismiss()
        }

        builder.setOnCancelListener { dialogInterface -> dialogInterface.dismiss() }
        builder.create().show()
    }

    /**
     * Confirm actions that are critical before proceeding
     *
     * @param c
     * @param text
     */
    fun okAction(c: Context, text: String) {
        val alertDialog: AlertDialog
        val builder = AlertDialog.Builder(c, R.style.AppCompatAlertDialogStyle)
        builder.setMessage(text)
        builder.setPositiveButton(c.resources.getString(android.R.string.ok)) { dialog, _ -> dialog.dismiss() }
        builder.setOnCancelListener { dialogInterface -> dialogInterface.dismiss() }
        alertDialog = builder.create()
        alertDialog.show()

    }

    /**
     * Title will be app name
     *
     * @param context
     * @param desc
     */
    fun showProgressDialog(context: Context, desc: String = "Please wait...") {
        hideProgressDialog()

        progress = ProgressDialog(context, R.style.AppAlertDialogStyle)
        progress!!.setMessage(desc)
        progress!!.setCancelable(false)
        progress!!.show()
    }

    fun hideProgressDialog() {
        if (progress != null) {
            progress!!.dismiss()
        }
    }

    /**
     * Hides the soft keyboard
     *
     * @param a
     */
    fun hideSoftKeyboard(a: Activity?) {
        if (a!!.currentFocus != null) {
            val inputMethodManager = a.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(a.currentFocus!!.windowToken, 0)
        }
    }
}