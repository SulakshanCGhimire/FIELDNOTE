package np.com.sulakshan.fieldnote.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun FieldNoteNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Routes.LIST) {
        composable(Routes.LIST) {
            ListPlaceholder(
                onOpen = { navController.navigate(Routes.detail("demo-1")) },
                onNew = { navController.navigate(Routes.editor()) }
            )
        }
        composable(
            route = Routes.DETAIL,
            arguments = listOf(navArgument("id") { type = NavType.StringType })
        ) { entry ->
            val id = entry.arguments?.getString("id")
            DetailPlaceholder(id = id, onEdit = { navController.navigate(Routes.editor(id)) })
        }
        composable(
            route = Routes.EDITOR,
            arguments = listOf(navArgument("id") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) { entry ->
            EditorPlaceholder(id = entry.arguments?.getString("id"))
        }
    }
}