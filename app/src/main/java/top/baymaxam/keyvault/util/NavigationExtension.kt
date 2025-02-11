package top.baymaxam.keyvault.util

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.navigation.BottomSheetNavigator
import androidx.compose.material.navigation.ModalBottomSheetLayout
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.ramcosta.composedestinations.annotation.NavHostGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.spec.DestinationStyle

/**
 * NavigatorExtension
 * @author John
 * @since 18 9月 2024
 */
enum class MainTabDestination(
    val index: Int,
    val icon: ImageVector
) {
    Home(0, Icons.Rounded.Home),
    Settings(1, Icons.Rounded.Settings)
}

val LocalNavigator: ProvidableCompositionLocal<DestinationsNavigator> =
    staticCompositionLocalOf { error("CompositionLocal is null") }


@Composable
fun NavigatorProvider(
    navigator: DestinationsNavigator,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalNavigator providesDefault navigator,
        content = content
    )
}

@Composable
fun BottomSheetNavigation(
    content: @Composable (NavHostController) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        ModalBottomSheetValue.Hidden,
        animationSpec = SpringSpec(),
        skipHalfExpanded = true
    )
    val bottomSheetNavigator = remember(sheetState) { BottomSheetNavigator(sheetState) }
    val navController = rememberNavController(bottomSheetNavigator)
    ModalBottomSheetLayout(
        bottomSheetNavigator = bottomSheetNavigator,
        sheetShape = RoundedCornerShape(15.dp)
    ) {
        content(navController)
    }
}


@NavHostGraph
annotation class AddItemGraph

@NavHostGraph
annotation class AddItemTagGraph


object SlideTransitions : DestinationStyle.Animated() {
    override val enterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
        {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(300)
            )
        }

    override val exitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
        {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Start,
                tween(300)
            )
        }

    override val popEnterTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> EnterTransition? =
        {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(300)
            )
        }

    override val popExitTransition: AnimatedContentTransitionScope<NavBackStackEntry>.() -> ExitTransition? =
        {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.End,
                tween(300)
            )
        }

    override val sizeTransform: AnimatedContentTransitionScope<NavBackStackEntry>.() -> SizeTransform? =
        {
            SizeTransform { _, _ -> tween(300) }
        }
}
