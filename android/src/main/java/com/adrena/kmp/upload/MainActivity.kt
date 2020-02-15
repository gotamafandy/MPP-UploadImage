package com.adrena.kmp.upload

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import androidx.core.content.FileProvider
import permissions.dispatcher.NeedsPermission
import permissions.dispatcher.RuntimePermissions

@RuntimePermissions
class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }

    private var mCameraUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.let {
            FileUtils.deleteDir(it)
        }

        showCameraWithPermissionCheck()

        findViewById<Button>(R.id.button).setOnClickListener {
            showCameraWithPermissionCheck()
        }
    }

    @NeedsPermission(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
    fun showCamera() {
        val uri = createUriProvider()

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION)
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)

        startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)

        mCameraUri = uri
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        onRequestPermissionsResult(requestCode, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        val uri = mCameraUri

        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK && uri != null) {

            val intent = Intent(this, ResultActivity::class.java)

            intent.putExtra(ResultActivity.URI, mCameraUri)

            startActivity(intent)
        }
    }

    private fun createUriProvider(directory: String? = null): Uri? {
        val fileProvider = "$packageName.files"

        val path = FileUtils.createFile(this, directory)

        if (path != null) {
            return FileProvider.getUriForFile(this, fileProvider, path)
        }

        return null
    }
}

