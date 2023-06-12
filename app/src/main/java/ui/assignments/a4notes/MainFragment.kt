package ui.assignments.a4notes

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.SwitchCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import ui.assignments.a4notes.viewmodel.NotesViewModel

class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_main, container, false)

        // Obtain the ViewModel
        val model : NotesViewModel by activityViewModels { NotesViewModel.Factory }

        // Add listener to show archived switch
        view.findViewById<SwitchCompat>(R.id.archive_switch).setOnCheckedChangeListener { _, isChecked ->
            model.setViewArchived(isChecked)
        }

        // Add listener to add button
        view.findViewById<Button>(R.id.add_button).setOnClickListener {
            findNavController().navigate(R.id.action_mainFragment_to_addFragment)
        }

        // Display notes based on the ViewModel
        model.getNotes().observe(viewLifecycleOwner) {
            view.findViewById<LinearLayout>(R.id.linear_scroll).removeAllViews()
            it.forEach { note ->
                val item = inflater.inflate(R.layout.note_item, container, false)
                item.findViewById<TextView>(R.id.note_title).text = note.value?.title
                item.findViewById<TextView>(R.id.note_content).text = note.value?.content
                if (note.value?.urgency == "Medium") {
                    item.setBackgroundColor(Color.YELLOW)
                } else if (note.value?.urgency == "High") {
                    item.setBackgroundColor(Color.RED)
                } else if (note.value?.archived!!) {
                    item.setBackgroundColor(Color.LTGRAY)
                }
                item.setOnClickListener {
                    model.editID = note.value?.id!!
                    model.editTitle = note.value?.title!!
                    model.editContent = note.value?.content!!
                    model.editUrgency = note.value?.urgency!!
                    model.editArchived = note.value?.archived!!
                    findNavController().navigate(R.id.action_mainFragment_to_editFragment)
                }
                item.findViewById<Button>(R.id.A_button).setOnClickListener {
                    model.updateNoteArchived(note.value?.id!!, !(note.value?.archived!!))
                }
                item.findViewById<Button>(R.id.D_button).setOnClickListener {
                    model.removeNote(note.value?.id!!)
                }
                view.findViewById<LinearLayout>(R.id.linear_scroll).addView(item)
            }
        }
        return view
    }
}
