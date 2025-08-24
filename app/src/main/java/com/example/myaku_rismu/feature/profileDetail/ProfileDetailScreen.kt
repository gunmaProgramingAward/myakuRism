package com.example.myaku_rismu.feature.profileDetail

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myaku_rismu.R
import com.example.myaku_rismu.core.AppState
import com.example.myaku_rismu.core.ui.BaseProfileCardLayout
import com.example.myaku_rismu.core.ui.TitleAndSubComponent
import com.example.myaku_rismu.core.ui.TopBar
import com.example.myaku_rismu.data.model.ProfileSwitchType
import com.example.myaku_rismu.ui.theme.customTheme

@Composable
fun ProfileDetailScreen(
    modifier: Modifier = Modifier,
    appState: AppState,
    viewModel: ProfileDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    fun eventHandler(event: ProfileDetailUiEvent) {
        when(event) {
            is ProfileDetailUiEvent.OnClickSwitch -> {
                viewModel.toggleSwitch(event.switchType)
            }
            is ProfileDetailUiEvent.OnClickSetting -> {
                appState.navigateToSetting()
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.refreshProfileDetail()
    }

    Scaffold(
        topBar = {
            TopBar(
                title = stringResource(R.string.top_bar_setting),
                titleTextStyle = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.background(MaterialTheme.customTheme.myakuRismuBackgroundColor)
            )
        },
        modifier = modifier
    ) { innerPadding ->
        ProfileDetail(
            uiState = uiState,
            onClickSwitch = { switchType: ProfileSwitchType ->
                eventHandler(ProfileDetailUiEvent.OnClickSwitch(switchType))
            },
            onClickSetting = {
                eventHandler(ProfileDetailUiEvent.OnClickSetting)
            },
            modifier = Modifier
                .padding(top = innerPadding.calculateTopPadding())
        )
    }
}

@Composable
fun ProfileDetail(
    uiState: ProfileDetailState,
    onClickSwitch: (ProfileSwitchType) -> Unit,
    onClickSetting: () -> Unit,
    modifier: Modifier = Modifier
) {
    val switchableCardItems = listOf(
            ProfileSwitchType.INCLUDE_LYRICS to CardItem(
                iconResId = R.drawable.include_lyrics,
                title = R.string.include_lyrics,
                isSwitchEnabled = uiState.display.includeLyricsSwitchEnabled
            ),
            ProfileSwitchType.MUSIC_GENERATION_NOTIFICATION to CardItem(
                iconResId = R.drawable.music_generation_notification,
                title = R.string.music_generation_notification,
                isSwitchEnabled = uiState.display.musicGenerationNotificationSwitchEnabled
            ),
            ProfileSwitchType.COLLABORATION_WITH_HEALTHCARE to CardItem(
                iconResId = R.drawable.collaboration_with_healthcare,
                title = R.string.collaboration_with_healthcare,
                isSwitchEnabled = uiState.display.collaborationWithHealthcareSwitchEnabled
            ),
            ProfileSwitchType.SYNC_WITH_YOUR_SMARTWATCH to CardItem(
                iconResId = R.drawable.sync_with_your_smartwatch,
                title = R.string.sync_with_your_smartwatch,
                isSwitchEnabled = uiState.display.syncWithYourSmartwatchSwitchEnabled
            )
        )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.customTheme.myakuRismuBackgroundColor)
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        ProfileTitleSubcomponentCard(
            modifier = Modifier
                .height(103.dp)
                .clickable { onClickSetting() },
            icon = R.drawable.profile,
            title = R.string.setting_profile,
            subComponentText = R.string.personal_information_and_health_goal_setting
        )
        Text(
            text = stringResource(R.string.advanced_settings),
            style = MaterialTheme.typography.titleMedium,
        )
        switchableCardItems.forEach { (switchType, item) ->
            ProfileSwitchCard(
                modifier = Modifier,
                icon = item.iconResId,
                title = item.title,
                switchChecked = item.isSwitchEnabled,
                onSwitchCheckedChange = { onClickSwitch(switchType) }
            )
        }
    }
}



@Composable
fun ProfileSwitchCard(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes title: Int,
    switchChecked: Boolean,
    onSwitchCheckedChange: () -> Unit
) {
    BaseProfileCardLayout(
        modifier = modifier,
        icon = icon,
        title = title
    ) {
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.weight(1f))
        Switch(
            modifier = Modifier,
            checked = switchChecked,
            onCheckedChange = { onSwitchCheckedChange() },
            colors = SwitchDefaults.colors(
                checkedThumbColor = MaterialTheme.customTheme.switchCheckedThumbColor,
                uncheckedThumbColor = MaterialTheme.customTheme.switchUncheckedThumbColor,
                checkedTrackColor = MaterialTheme.customTheme.bottomNavigationBarSelectedColor,
                uncheckedTrackColor = MaterialTheme.customTheme.switchUncheckedTrackColor
            )
        )
    }
}

@Composable
fun ProfileTitleSubcomponentCard(
    modifier: Modifier = Modifier,
    @DrawableRes icon: Int,
    @StringRes title: Int,
    @StringRes subComponentText: Int,
) {
    BaseProfileCardLayout(
        modifier = modifier,
        icon = icon,
        title = title
    ) {
        TitleAndSubComponent(
            title = stringResource(title),
            subComponent = {
                Text(
                    text = stringResource(subComponentText),
                    style = MaterialTheme.typography.titleSmall
                )
            },
            titleTextStyle = MaterialTheme.typography.titleMedium,
            spacerHeight = 2.dp
        )
    }
}


@Preview(showBackground = true)
@Composable
fun ProfileDetailPreview() {
    ProfileDetail(
        uiState = ProfileDetailState(),
        onClickSwitch = {},
        onClickSetting = {},
    )
}