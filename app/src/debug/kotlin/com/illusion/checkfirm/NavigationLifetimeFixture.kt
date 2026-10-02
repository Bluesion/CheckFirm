package com.illusion.checkfirm

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.ui.Modifier
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import com.illusion.checkfirm.core.navigation.Navigator

/** Exercises the same entry decorators as MainActivity, without services or repositories. */
@Composable
internal fun NavigationLifetimeFixture() {
    val navigator = remember { Navigator("root") }
    NavDisplay(
        modifier = Modifier.systemBarsPadding(),
        backStack = navigator.backStack,
        onBack = navigator::goBack,
        entryDecorators = listOf(rememberSaveableStateHolderNavEntryDecorator(), rememberViewModelStoreNavEntryDecorator()),
        entryProvider = entryProvider {
            entry<String> { route ->
                if (route == "root") Column {
                    Text("Root cleared: ${LifetimeFixtureViewModel.cleared}")
                    Button({ navigator.goTo("first") }) { Text("Open first") }
                } else {
                    val model: LifetimeFixtureViewModel = viewModel()
                    var draft by rememberSaveable { mutableStateOf("") }
                    Column {
                        Text("Entry $route VM ${model.identity}")
                        TextField(draft, { draft = it }, label = { Text("Draft") })
                        if (route == "first") Button({ navigator.goTo("second") }) { Text("Open second") }
                        Button(navigator::goBack) { Text("Back fixture") }
                    }
                }
            }
        },
    )
}

class LifetimeFixtureViewModel : ViewModel() {
    val identity = ++nextIdentity
    override fun onCleared() { cleared++ }
    companion object {
        private var nextIdentity = 0
        var cleared by mutableIntStateOf(0)
    }
}
