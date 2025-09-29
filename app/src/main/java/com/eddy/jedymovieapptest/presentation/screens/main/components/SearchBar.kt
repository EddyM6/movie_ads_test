package com.eddy.jedymovieapptest.presentation.screens.main.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.eddy.jedymovieapptest.R

@Composable
fun SearchBar(
    resetSearch: () -> Unit,
    searchQuery: String,
    onSearchQueryChanged: (query: String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        TextField(
            value = searchQuery,
            onValueChange = { query ->
                onSearchQueryChanged(query)
            },
            modifier = Modifier.weight(1f),
            placeholder = { Text(stringResource(R.string.search_placeholder)) },
            singleLine = true,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = MaterialTheme.colorScheme.surface
            )
        )
        IconButton(onClick = {
            resetSearch()
        }) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close Search"
            )
        }
    }
}