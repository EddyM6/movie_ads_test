package com.eddy.jedymovieapptest.presentation.screens.favotites

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.eddy.jedymovieapptest.R
import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.presentation.screens.favotites.contract.FavoritesEffect
import com.eddy.jedymovieapptest.presentation.screens.favotites.contract.FavoritesIntent
import com.eddy.jedymovieapptest.presentation.screens.filmdetails.contract.FilmDetailsIntent
import com.eddy.jedymovieapptest.presentation.screens.main.components.FilmListItemView
import com.eddy.jedymovieapptest.presentation.screens.main.components.PullToRefreshBox
import com.eddy.jedymovieapptest.presentation.screens.main.components.SearchBar
import com.eddy.jedymovieapptest.presentation.screens.main.contract.MainEffect
import com.eddy.jedymovieapptest.presentation.screens.main.contract.MainIntent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    viewModel: FavoritesViewModel = koinViewModel(),
    navigateUp: () -> Unit,
) {

    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    val filmsListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                FavoritesEffect.NavigateBack -> navigateUp()
                FavoritesEffect.ShowErrorToast ->
                    Toast.makeText(context, context.getString(R.string.error_fetch_data), Toast.LENGTH_SHORT).show()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(R.string.favorites))
                },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.handleIntent(FavoritesIntent.BackClicked) }
                    ) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = state.value.isLoading,
            onRefresh = { viewModel.handleIntent(FavoritesIntent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(innerPadding),
            content = {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    HorizontalDivider()
                    LazyColumn(
                        state = filmsListState
                    ) {
                        items(
                            items = state.value.films,
                            key = { it.title },
                        ) { film ->
                            FilmListItemView(
                                film = film,
                                onClick = {
                                    // do nothing, may be improved
                                }
                            )
                        }
                    }
                }
            }
        )
    }
}
