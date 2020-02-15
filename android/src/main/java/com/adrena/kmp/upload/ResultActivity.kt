package com.adrena.kmp.upload

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.adrena.kmp.data.ocr.OCR
import com.adrena.kmp.data.ocr.OCRCloudService
import com.adrena.kmp.data.ocr.OCRMapper
import com.adrena.kmp.data.repository.RepositoryImpl
import com.adrena.kmp.domain.UseCaseImpl
import com.adrena.kmp.view.ViewModel
import com.adrena.kmp.view.ViewModelBinding
import com.adrena.kmp.view.ViewModelImpl
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.scheduler.mainScheduler
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class ResultActivity : AppCompatActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + mJob

    companion object {
        const val URI = "bundle.uri"
    }

    private lateinit var mCameraUri: Uri
    private lateinit var mJob: Job
    private lateinit var mLoading: View
    private lateinit var mScanResult: View
    private lateinit var mScannedText: TextView
    private lateinit var mConfidenceText: TextView

    private val mBinding = ViewModelBinding()

    private val mViewModel: ViewModel<String, OCR> by lazy {
        val mapper = OCRMapper()
        val service = OCRCloudService("https://ocr-fdvxbwj6ua-an.a.run.app", mapper)
        val repository = RepositoryImpl(service, null)
        val useCase = UseCaseImpl(repository)

        ViewModelImpl(useCase)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_result)

        mLoading = findViewById(R.id.loading)
        mScanResult = findViewById(R.id.result)
        mScannedText = findViewById(R.id.scanned_text)
        mConfidenceText = findViewById(R.id.confidence)

        mJob = Job()

        binding()

        mCameraUri = intent.getParcelableExtra(URI) ?: throw IllegalArgumentException("Uri is null")

        launch {
            val cachePath = FileUtils.createFile(this@ResultActivity, "cache")?.absolutePath
                ?: throw IllegalArgumentException("Unable to create cache path")

            compressImage(cachePath)

            Log.d("DUDIDAM", "THREAD: ${Thread.currentThread()}")
            
            mViewModel.inputs.execute(cachePath)
        }
    }

    override fun onDestroy() {
        mBinding.dispose()
        mJob.cancel()

        super.onDestroy()
    }

    private fun binding() {
        mBinding.subscribe(mViewModel.outputs.result.observeOn(mainScheduler), onNext = ::showResult)
        mBinding.subscribe(mViewModel.outputs.loading.observeOn(mainScheduler), onNext = ::toggleLoading)
    }

    private fun toggleLoading(loading: Boolean) {
        mLoading.visibility = if (loading) {
            View.VISIBLE
        } else {
            View.GONE
        }
    }

    private fun showResult(ocr: OCR) {
        mScanResult.visibility = View.VISIBLE
        mScannedText.text = ocr.result
        mConfidenceText.text = ocr.confidence.toString()
    }

    private suspend fun compressImage(outputPath: String) {
        withContext(Dispatchers.IO) {
            BitmapUtils.compressImage(
                this@ResultActivity,
                mCameraUri,
                destinationPath = outputPath)
        }
    }
}