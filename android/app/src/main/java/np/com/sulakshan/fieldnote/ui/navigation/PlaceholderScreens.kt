package np.com.sulakshan.fieldnote.ui.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ListPlaceholder(onOpen: () -> Unit, onNew: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text("Report list")
        Button(onClick = onOpen) { Text("Open demo report") }
        Button(onClick = onNew) { Text("New report") }
    }
}

@Composable
fun DetailPlaceholder(id: String?, onEdit: () -> Unit) {
    Column(Modifier.padding(16.dp)) {
        Text("Detail for id = $id")
        Button(onClick = onEdit) { Text("Edit") }
    }
}

@Composable
fun EditorPlaceholder(id: String?) {
    Column(Modifier.padding(16.dp)) {
        Text(if (id == null) "Creating a new report" else "Editing report $id")
    }
}