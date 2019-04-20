package sochan.ifsms.activity

import android.Manifest
import android.app.AlertDialog
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import sochan.ifsms.R

open class BaseActivity : AppCompatActivity() {

    open val TAG = javaClass.simpleName
    private val REQUEST_PERMISSION_CODE = 101

    lateinit var pref: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        if (needRequestPermissions())
            requestNeededPermissions()
        else
            initAll()
    }


    fun initAll() {
        loadPreferences()
        initData()
        initUI()
    }


    private fun initData() {

    }

    open fun initUI() {
    }

    open fun loadPreferences() {
        pref = PreferenceManager.getDefaultSharedPreferences(this)
    }

    open fun savePreferences() {
    }

    private val neededPermissions =
        arrayOf(Manifest.permission.RECEIVE_SMS, Manifest.permission.CAMERA, Manifest.permission.VIBRATE)
    // , Manifest.permission.WAKE_LOCK

    private fun needRequestPermissions(): Boolean {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            for (permission in neededPermissions) {
                if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                    return true
                }
            }
        }
        return false
    }

    private fun requestNeededPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestPermissions(neededPermissions, REQUEST_PERMISSION_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION_CODE) {
            if (needRequestPermissions()) {

                Toast.makeText(this, R.string.permission_denied_close_app, Toast.LENGTH_LONG).show()
                finish()
            } else initAll()
        }
    }

    override fun onBackPressed() {
        if (exitDialog == null || !exitDialog!!.isShowing)
            showConfirmEditDialog()
    }

    var exitDialog: AlertDialog? = null


    fun showConfirmEditDialog() {

        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(R.string.confirm_close_app)
        builder1.setCancelable(false)

        builder1.setPositiveButton(
            android.R.string.ok
        ) { dialog, id ->
            finish()
        }
        builder1.setNegativeButton(android.R.string.cancel) { dialog, which ->
            exitDialog!!.dismiss()
        }

        exitDialog = builder1.create()
        exitDialog!!.show()

    }
}