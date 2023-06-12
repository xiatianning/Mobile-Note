package ui.assignments.a4notes

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import ui.assignments.a4notes.viewmodel.NotesViewModel

class EditFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_edit, container, false)

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

        // Set initial state of the fields
        view.findViewById<EditText>(R.id.edit_title).setText(model.editTitle)
        view.findViewById<EditText>(R.id.edit_content).setText(model.editContent)
        spinner.setSelection(spinnerArrayAdapter.getPosition(model.editUrgency))
        view.findViewById<SwitchCompat>(R.id.archive).isChecked = model.editArchived

        // Add listeners to the fields
        view.findViewById<EditText>(R.id.edit_title).addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(title: CharSequence?, start: Int, before: Int, count: Int) {
                    model.updateNoteTitle(model.editID, title.toString())
                }
            }
        )
        view.findViewById<EditText>(R.id.edit_content).addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {}
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(content: CharSequence?, start: Int, before: Int, count: Int) {
                    model.updateNoteContent(model.editID, content.toString())
                }
            }
        )
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {}
            override fun onItemSelected(parent: AdapterView<*>?, v: View?, position: Int, id: Long) {
                view.findViewById<SwitchCompat>(R.id.archive).isChecked =
                    model.updateNoteUrgency(model.editID, parent?.getItemAtPosition(position).toString())!!
            }
        }
        view.findViewById<SwitchCompat>(R.id.archive).setOnCheckedChangeListener { _, isChecked ->
            val urgencyNew = spinnerArrayAdapter.getPosition(
                model.updateNoteArchived(model.editID, isChecked)!!
            )
            spinner.setSelection(urgencyNew)
        }
        return view
    }
}