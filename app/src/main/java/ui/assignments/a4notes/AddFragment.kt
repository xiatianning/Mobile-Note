package ui.assignments.a4notes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ui.assignments.a4notes.viewmodel.NotesViewModel

class AddFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_add, container, false)

        // Obtain the ViewModel
        val model : NotesViewModel by activityViewModels { NotesViewModel.Factory }

        // Set the urgency spinner
        val urgencyArray = arrayOf("Low", "Medium", "High")
        val spinner = view.findViewById<Spinner>(R.id.urgency_spinner)
        val spinnerArrayAdapter = ArrayAdapter(
            activity?.applicationContext!!,
            android.R.layout.simple_spinner_dropdown_item,
            urgencyArray
        )
        spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = spinnerArrayAdapter

        // Add listener to create button
        view.findViewById<Button>(R.id.create_button).setOnClickListener {
            val title = view.findViewById<EditText>(R.id.add_title).text.toString()
            val content = view.findViewById<EditText>(R.id.add_content).text.toString()
            val urgency = spinner.selectedItem.toString()
            model.addNote(title, content, urgency)
            findNavController().navigate(R.id.add_pop)
        }

        return view
    }
}