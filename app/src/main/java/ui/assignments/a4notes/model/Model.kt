package ui.assignments.a4notes.model

import android.icu.util.GregorianCalendar
import androidx.databinding.BaseObservable
import androidx.databinding.ObservableArrayList
import kotlin.math.sign

class Model {

    /**
     *  A Note. Observe to receive notifications about changes of its properties.
     *  @param id unique id of the note
     *  @param title title of the note
     *  @param content content or body of the note
     *  @param urgency indicates the urgency of the note
     *  @param archived indicates if the note is archived
     */
    data class ModelNote(
        val id: Int,
        var title: String,
        var content: String,
        var urgency : String = "Low",
        var archived : Boolean = false
    ) : BaseObservable() {
        val timestamp = GregorianCalendar.getInstance().time.time
        var lastedit = timestamp
    }

    // Counter for note ids
    private var idCounter = 0

    // Returns a new unique note id
    private fun generateID(): Int {
        return idCounter++
    }

    /**
     * List of all notes. Observe to receive notifications about changes of its content.
     */
    val notes = ObservableArrayList<ModelNote>()

    /**
     * Attempts to add a non-archived note to the model. Observe [notes] to receive notifications about the success of this operation.
     * @param title title of the note
     * @param content content / body of the note
     * @param urgency indicates the urgency of the note
     * @see notes
     */
    fun addNote(title: String, content: String, urgency : String = "Low") {
        ModelNote(generateID(), title, content, urgency).apply {
            notes.add(this)
        }
    }

    /**
     * Attempts to remove a note from the model. Observe [notes] to receive notifications about the success of this operation.
     * @param id id of the note to be removed
     * @see notes
     */
    fun removeNote(id: Int) {
        notes.find { it.id == id }?.apply {
            if (urgency == "Low")
                notes.remove(this)
        }
    }

    /**
     * Attempts to update the title of a note. Observe the [ModelNote] to receive notifications about the success of this operation.
     * @param id id of the note to be updated
     * @param title new title of the note
     * @see ModelNote
     */
    fun updateNoteTitle(id: Int, title: String) {
        notes.find { it.id == id }.apply {
            this?.title = title
            this?.lastedit = GregorianCalendar.getInstance().time.time
            this?.notifyPropertyChanged(id)
        }
    }

    /**
     * Attempts to update the content / body of a note. Observe the [ModelNote] to receive notifications about the success of this operation.
     * @param id id of the note to be updated
     * @param content new content / body of the note
     * @see ModelNote
     */
    fun updateNoteContent(id: Int, content: String) {
        notes.find { it.id == id }.apply {
            this?.content = content
            this?.lastedit = GregorianCalendar.getInstance().time.time
            this?.notifyPropertyChanged(id)
        }
    }

    /**
     * Attempts to update the archived-state of a note. Observe the [ModelNote] to receive notifications about the success of this operation.
     * @param id id of the note to be updated
     * @param archived new archived-state of the note
     * @see ModelNote
     */
    fun updateNoteArchived(id: Int, archived: Boolean) {
        notes.find { it.id == id }.apply {
            this?.urgency = if (archived) "Low" else this!!.urgency
            this?.archived = archived
            this?.lastedit = GregorianCalendar.getInstance().time.time
            this?.notifyPropertyChanged(id)
        }
    }

    /**
     * Attempts to update the urgency of a note. Observe the [ModelNote] to receive notifications about the success of this operation.
     * @param id id of the note to be updated
     * @param urgency new urgency of the note
     * @see ModelNote
     */
    fun updateNoteUrgency(id: Int, urgency: String) {
        notes.find { it.id == id }.apply {
            this?.urgency = urgency
            this?.archived = if (urgency != "Low") false else this!!.archived
            this?.lastedit = GregorianCalendar.getInstance().time.time
            this?.notifyPropertyChanged(id)
        }
    }

    // Compare two notes by last edit time
    fun compareNotesEdit(idA: Int, idB: Int) : Int {
        val a = notes.find { it.id == idA }
        val b = notes.find { it.id == idB }
        return if (b == null) -1
        else if (a == null) 1
        else {
            sign((b.lastedit - a.lastedit).toDouble()).toInt()
        }
    }

    // Helper function that populates the Model with some notes. The thread sleeps for 10 ms after each addition, so
    //   that all notes have different time stamps. (This makes explaining sorting behaviour more obvious.)
    fun addSomeNotes() {
        notes.add(ModelNote(generateID(), "First Note", "This is the oldest, yet, URGENT (!) note", urgency = "High", archived = false))
        Thread.sleep(10L)
        notes.add(ModelNote(generateID(), "Second Note","This is an archived note", urgency = "Low", archived = true))
        Thread.sleep(10L)
        notes.add(ModelNote(generateID(), "Third Note","This is a pretty boring note; not a lot to see here...", urgency = "Medium", archived = false))
        Thread.sleep(10L)
        notes.add(ModelNote(generateID(), "Fourth Note","This is a note with a reasonably long content / body. Yep, there are quite a few characters in this text; enough so that this note has to be displayed over multiple lines on the Edit-screen.", urgency = "Low", archived = false))
        Thread.sleep(10L)
        notes.add(ModelNote(generateID(), "This note (5) has an excitingly long title: W00t, W00t!","But not a lot of content.", urgency = "Low", archived = true))
        Thread.sleep(10L)
        notes.add(ModelNote(generateID(), "SIXTH NOTE","THIS. IS. URGENT!!!", urgency = "High", archived = false))
    }
}