package np.com.sulakshan.fieldnote.ui.navigation

object Routes {
    const val LIST = "reports"
    const val DETAIL = "report/{id}"
    const val EDITOR = "editor?id={id}"

    fun detail(id: String) = "report/$id"
    fun editor(id: String? = null) = if (id == null) "editor" else "editor?id=$id"
}