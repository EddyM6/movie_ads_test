package com.eddy.jedymovieapptest.presentation.screens.main

import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.eddy.jedymovieapptest.R
import com.eddy.jedymovieapptest.domain.models.Film
import com.eddy.jedymovieapptest.presentation.components.NativeAdCard
import com.eddy.jedymovieapptest.presentation.screens.main.components.FilmListItemView
import com.eddy.jedymovieapptest.presentation.screens.main.components.PullToRefreshBox
import com.eddy.jedymovieapptest.presentation.screens.main.components.SearchBar
import com.eddy.jedymovieapptest.presentation.screens.main.contract.MainEffect
import com.eddy.jedymovieapptest.presentation.screens.main.contract.MainIntent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainViewModel = koinViewModel(),
    navigateToDetails: (Film) -> Unit,
    navigateToFavorites: () -> Unit,
) {

    val state = viewModel.state.collectAsState()
    val context = LocalContext.current

    val filmsListState = rememberLazyListState()

    LaunchedEffect(Unit) {
        viewModel.effect.collectLatest { effect ->
            when (effect) {
                is MainEffect.NavigateToDetails -> navigateToDetails(effect.film)
                MainEffect.ShowConnectionErrorToast -> {
                    Toast.makeText(context, context.getString(R.string.error_fetch_data), Toast.LENGTH_SHORT).show()
                }
                MainEffect.ScrollToTop -> filmsListState.scrollToItem(0)
            }
        }
    }

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { navigateToFavorites() },
                icon = {
                    Icon(Icons.Filled.Favorite, "Extended floating action button.") },
                text = { Text(text = stringResource(R.string.favorites)) },
            )
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { paddingValues ->
        PullToRefreshBox(
            isRefreshing = state.value.isLoading,
            onRefresh = { viewModel.handleIntent(MainIntent.Refresh) },
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(paddingValues),
            content = {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    SearchBar(
                        resetSearch = { viewModel.handleIntent(MainIntent.ChangeSearchQuery("")) },
                        searchQuery = state.value.searchQuery,
                        onSearchQueryChanged = { viewModel.handleIntent(MainIntent.ChangeSearchQuery(it)) }
                    )

                    HorizontalDivider()
                    LazyColumn(
                        state = filmsListState
                    ) {
                        val filmsList = state.value.films
                        
                        filmsList.forEachIndexed { index, film ->
                            if (index == 1) {
                                item(key = "ad_1") {
                                    NativeAdCard()
                                }
                            }
                            
                            if (index == 3) {
                                item(key = "ad_2") {
                                    NativeAdCard()
                                }
                            }
                            
                            item(key = film.id) {
                                FilmListItemView(
                                    film = film,
                                    onClick = { viewModel.handleIntent(MainIntent.FilmClicked(film)) }
                                )
                            }
                        }
                    }
                }
            }
        )

    }
}
