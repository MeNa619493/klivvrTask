package com.example.klivvrtask.ui

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.klivvrtask.domain.model.City
import com.example.klivvrtask.utils.FlagManager
import com.example.klivvrtask.ui.CitySearchSideEffect.ScrollToFirstElement
import com.example.klivvrtask.utils.navigateToGoogleMaps

@Composable
fun CitySearchScreen() {
    val viewModel: CitySearchViewModel = hiltViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.sideEffect.collect { effect ->
            when (effect) {
                is ScrollToFirstElement -> listState.scrollToItem(0)
                is CitySearchSideEffect.OpenGoogleMaps -> {
                    context.navigateToGoogleMaps(
                        effect.city.coordinates.longitude,
                        effect.city.coordinates.latitude
                    )
                }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.LightGray)
    ) {
        when {
            state.isLoading -> {
                LoadingState()
            }

            state.error != null -> {
                ErrorState(state.error ?: "Unknown Error")
            }

            else -> {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CustomSearchBar(
                        searchQuery = state.searchQuery ?: "",
                        onSearchQueryChanged = {
                            viewModel.handleIntent(
                                CitySearchIntent.OnSearchQueryChanged(it)
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    if (state.cityGroups.isEmpty()) {
                        EmptyState()
                    } else {
                        CityList(
                            cities = state.cityGroups,
                            listState = listState,
                            onClick = {
                                viewModel.handleIntent(
                                    CitySearchIntent.OnCityClicked(it)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CityList(
    cities: Map<Char, List<City>>,
    listState: LazyListState,
    onClick: (City) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        state = listState
    ) {
        cities.forEach { (letter, citiesInGroup) ->
            stickyHeader(key = letter) {
                GroupHeader(letter = letter)
            }

            items(
                citiesInGroup.size,
                key = { index -> citiesInGroup[index].id }
            ) { index ->
                CityItem(
                    city = citiesInGroup[index],
                    onClick = onClick
                )
            }
        }
    }
}

@Composable
fun GroupHeader(
    letter: Char
) {
    Box(
        modifier = Modifier
            .size(48.dp)
            .background(color = Color.White, shape = CircleShape)
    ) {
        Text(
            text = letter.toString(),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun CityItem(
    city: City,
    onClick: (City) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 56.dp, end = 4.dp, bottom = 8.dp)
            .clickable { onClick(city) },
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = painterResource(FlagManager.getFlagRes(city.country)),
                contentDescription = city.country,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = city.displayName,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    text = "${city.coordinates.latitude}, ${city.coordinates.longitude}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ErrorState(error: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = error,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

@Composable
fun EmptyState() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = "Search",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "No Data Found",
            fontSize = 24.sp,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
fun LoadingState() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
    }
}
