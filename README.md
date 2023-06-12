# Mobile Note Taking Application
* kotlin.jvm 1.7.20
* Java SDK corretto-11
* Android API 32 (Sv2)

## Description
This is a mobile note-taking application. A note has a title and a text content, and it can be marked as “archived” or has an "urgency" level (high, medium, low). They are displayed in a scrollable list, from which they can be archived or deleted. Only note with “urgency” status "low" can be deleted. The note changes to “urgency” status "low" when archived. And setting “urgency” status to “high” or “medium” remove "archived" status. You can filter to show “non-archived” notes only. Notes are sorted according to the last-edit time (opening the edit screen, setting the “urgency” and the “archived” status, and changing the title and the content of the note all count as an "edit").