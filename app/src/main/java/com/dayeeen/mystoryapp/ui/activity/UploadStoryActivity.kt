package com.dayeeen.mystoryapp.ui.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dayeeen.mystoryapp.R
import com.dayeeen.mystoryapp.databinding.ActivityUploadStoryBinding
import com.dayeeen.mystoryapp.utils.StateResult
import com.dayeeen.mystoryapp.utils.getImageUri
import com.dayeeen.mystoryapp.utils.reduceFileImage
import com.dayeeen.mystoryapp.utils.uriToFile
import com.dayeeen.mystoryapp.viewmodel.UploadStoryViewModel
import com.dayeeen.mystoryapp.viewmodel.ViewModelFactory

class UploadStoryActivity : AppCompatActivity() {

    private var currentImageUri: Uri? = null
    private lateinit var binding: ActivityUploadStoryBinding
    private val usvm by viewModels<UploadStoryViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUploadStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.cameraButton.setOnClickListener { startCamera() }
        binding.uploadButton.setOnClickListener { uploadStory() }
    }

    private fun startCamera() {
        currentImageUri = getImageUri(this)
        currentImageUri?.let { uri ->
            launcherIntentCamera.launch(uri)
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun showImage() {
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.thumbnail.setImageURI(it)
        }
    }

    private fun uploadStory() {
        currentImageUri?.let { uri ->
            val imageFile = uriToFile(uri, this).reduceFileImage()
            Log.d("Image File", "showImage: ${imageFile.path}")
            val description = binding.edtCaption.text.toString()

            usvm.uploadStory(imageFile, description).observe(this) { result ->
                if (result != null) {
                    when (result) {
                        is StateResult.InProgress -> {
                            showLoading(true)
                        }

                        is StateResult.Success -> {
                            result.data.message?.let { showToast(it) }
                            val intent = Intent(this, MainActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                            startActivity(intent)
                            showLoading(false)
                        }

                        is StateResult.Failure -> {
                            showToast(result.message)
                            showLoading(false)
                        }
                    }
                }
            }
        } ?: showToast(getString(R.string.empty_image_alert))
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}