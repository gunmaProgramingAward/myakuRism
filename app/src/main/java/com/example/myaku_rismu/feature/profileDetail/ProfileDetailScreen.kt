package com.example.myaku_rismu.feature.profileDetail

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.ui.TitleAndSubComponent
import com.example.myaku_rismu.ui.theme.customTheme

@Composable
fun ProfileDetailScreen(
    appState: AppState,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier) { innerPadding ->
        ProfileDetail(
//            appState = appState,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
                .background(MaterialTheme.customTheme.settingScreenBackgroundColor)
        )
    }
}

@Composable
fun ProfileDetail(
//    appState: AppState,
    modifier: Modifier = Modifier
) {

    data class CardItem(
        @DrawableRes val iconResId: Int,
        @StringRes val title: Int,
        val isSwitchEnabled: Boolean
    )

    val switchableCardItems = remember {
        mutableStateListOf(
            CardItem(
                iconResId = R.drawable.include_lyrics,
                title = R.string.include_lyrics,
                isSwitchEnabled = false
            ),
            CardItem(
                iconResId = R.drawable.music_generation_notification,
                title = R.string.music_generation_notification,
                isSwitchEnabled = true
            ),
            CardItem(
                iconResId = R.drawable.collaboration_with_healthcare,
                title = R.string.collaboration_with_healthcare,
                isSwitchEnabled = false
            ),
            CardItem(
                iconResId = R.drawable.sync_with_your_smartwatch,
                title = R.string.sync_with_your_smartwatch,
                isSwitchEnabled = true
            )
        )
    }

    Column(
        modifier = modifier
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ProfileDetailCard(
            modifier = Modifier
                .height(103.dp)
                .clickable { /*settingScreen*/ },
            icon = R.drawable.profile,
            title = R.string.profile,
        )
        Text(
            text = stringResource(R.string.advanced_settings),
            style = MaterialTheme.typography.titleMedium,
        )
        switchableCardItems.forEachIndexed { index, item ->
            ProfileDetailCard(
                modifier = Modifier,
                icon = item.iconResId,
                title = item.title,
                switchChecked = item.isSwitchEnabled,
                onSwitchCheckedChange = {
                    switchableCardItems[index] = item.copy(isSwitchEnabled = it)
                }
            )
        }
    }
}

@Composable
fun ProfileDetailCard(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes title: Int,
    switchChecked: Boolean? = null,
    onSwitchCheckedChange: ((Boolean) -> Unit)? = null,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(73.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = MaterialTheme.shapes.extraSmall
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(icon),
                contentDescription = stringResource(title),
                modifier = Modifier.size(46.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            if (switchChecked != null && onSwitchCheckedChange != null) {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    modifier = Modifier,
                    checked = switchChecked,
                    onCheckedChange = onSwitchCheckedChange,
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = MaterialTheme.customTheme.switchCheckedThumbColor,
                        uncheckedThumbColor = MaterialTheme.customTheme.switchUncheckedThumbColor,
                        checkedTrackColor = MaterialTheme.customTheme.switchCheckedTrackColor,
                        uncheckedTrackColor = MaterialTheme.customTheme.switchUncheckedTrackColor
                    )
                )
            } else {
                TitleAndSubComponent(
                    title = stringResource(title),
                    subComponent = {
                        Text(
                            text = stringResource(R.string.personal_information_and_health_goal_setting),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    titleTextStyle = MaterialTheme.typography.titleMedium,
                    spacerHeight = 4.dp
                )
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun ProfileDetailPreview() {
    ProfileDetail(
        modifier = Modifier.fillMaxSize()
    )
}