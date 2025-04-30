package vcmsa.projects.smartspend

import android.app.Activity
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import android.widget.Toast.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.io.OutputStream

class ExpenseEntryFragment { class ExpenseEntryFragment : Fragment() {
    private lateinit var viewModel: HomeViewModel
    private lateinit var dateEditText: EditText
    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var descriptionEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var photoImageView: ImageView
    private lateinit var addPhotoButton: Button
    private lateinit var saveExpenseButton: Button
    private lateinit var selectedImageUri: Uri?
    private lateinit var amountEditText: EditText


    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_expense_entry, container, false)
        dateEditText = view.findViewById(R.id.dateEditText)
        startTimeEditText = view.findViewById(R.id.startTimeEditText)
        endTimeEditText = view.findViewById(R.id.endTimeEditText)
        descriptionEditText = view.findViewById(R.id.descriptionEditText)
        categorySpinner = view.findViewById(R.id.categorySpinner)
        photoImageView = view.findViewById(R.id.photoImageView)
        addPhotoButton = view.findViewById(R.id.addPhotoButton)
        saveExpenseButton = view.findViewById(R.id.saveExpenseButton)
        amountEditText = view.findViewById(R.id.amountEditText)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        // Populate the category spinner
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            val categoryNames = categories.map { it.name }
            val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, categoryNames)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            categorySpinner.adapter = adapter

        }
        viewModel.loadHomePageData() // Ensure categories are loaded

        addPhotoButton.setOnClickListener {
            // Launch camera intent
            Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
                takePictureIntent.resolveActivity(requireActivity().packageManager)?.also {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                }
            }
        }

        saveExpenseButton.setOnClickListener {
            // Get the input values
            val date = dateEditText.text.toString()
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()
            val description = descriptionEditText.text.toString()
            val categoryName = categorySpinner.selectedItem.toString()
            val amount = amountEditText.text.toString().toDoubleOrNull() ?: 0.0  // Default to 0 if invalid

            // Get the category ID from the selected name
            val categoryId = viewModel.categories.value?.find { it.name == categoryName }?.id ?: 0 // Default to 0 if not found.  Handle this better in real app
            val photoPath = selectedImageUri?.toString() // Get path from the Uri

            // Validate inputs
            if (date.isNotBlank() && startTime.isNotBlank() && endTime.isNotBlank() && description.isNotBlank() && categoryId != 0 && amount != null) {
                // Call the ViewModel to save the expense
                viewModel.addExpense(date, startTime, endTime, description, categoryId, photoPath, amount)

                // Navigate back
                findNavController().popBackStack()
            } else {
                // Show an error message
                makeText(requireContext(), "Please fill in all fields", LENGTH_SHORT).show()
            }
        }
    }

}
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            val imageBitmap = data?.extras?.get("data") as Bitmap
            photoImageView.setImageBitmap(imageBitmap)
            // Save the image to a file and get the Uri
            selectedImageUri = saveImageToFile(imageBitmap)
        }
    }

    // Helper method to save the image and get a Uri
    private fun saveImageToFile(bitmap: Bitmap): Uri? {
        val wrapper = ContextWrapper(requireContext())
        var file = wrapper.getDir("images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        try {
            val stream: OutputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)
            stream.flush()
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        }
        return Uri.fromFile(file)
    }
}
