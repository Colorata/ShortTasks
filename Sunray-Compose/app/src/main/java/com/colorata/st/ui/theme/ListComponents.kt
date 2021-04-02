package com.colorata.st.ui.theme

import com.colorata.st.R

enum class ListComponents(val titles: List<String> = listOf(), val subTitles: List<String> = listOf(), val icons: List<Int> = listOf()) {
    BUBBLE_HELP(
        titles = listOf(
            Strings.bubbleManager,
            Strings.bubbleHelp1,
            Strings.bubbleHelp2
        ),
        subTitles = listOf(
            Strings.bubbleHelpSubTitle,
            "", "", ""
        ),
        icons = listOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_add_circle_24,
            R.drawable.ic_outline_circle_notifications_24,
            R.drawable.ic_outline_check_circle_outline_24
        )
    ),

    POWER_HELP(
        titles = listOf(
            Strings.powerAssistant,
            Strings.powerHelp1,
            Strings.powerHelp2,
            Strings.powerHelp3
        ),
        subTitles = listOf(
            Strings.powerHelpSubTitle,
            "", "", ""
        ),
        icons = listOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_accessibility_24,
            R.drawable.ic_outline_power_settings_new_24,
            R.drawable.ic_outline_check_circle_outline_24,
        )
    ),

    MAIN_SCREEN(
        titles = listOf(
            Strings.main,
            Strings.welcome,
            Strings.bubbleManager,
            Strings.powerAssistant
        ),
        subTitles = listOf(
            Strings.relatedPosts,
            Strings.welcomeMainSubTitle,
            Strings.bubbleMainSubTitle,
            Strings.powerMainSubTitle
        ),
        icons = listOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_announcement_24,
            R.drawable.ic_outline_bubble_chart_24,
            R.drawable.ic_outline_power_settings_new_24
        )
    ),

    FEATURES_SCREEN(
        titles = listOf(
            Strings.features,
            Strings.bubbleManager,
            Strings.weatherDirector,
            Strings.powerAssistant
        ),
        subTitles = listOf(
            Strings.relatedPosts,
            Strings.bubbleManagerSettingsSubTitle,
            Strings.weatherDirectorSettingsSubTitle,
            Strings.powerAssistantSettingsSubTitle
        ),
        icons = listOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_bubble_chart_24,
            R.drawable.ic_outline_cloud_24,
            R.drawable.ic_outline_power_settings_new_24
        )
    ),

    MORE_SCREEN(titles = listOf(
        Strings.more,
        Strings.settings,
        Strings.help,
        Strings.about
    ), subTitles = listOf(
        Strings.relatedPosts,
        Strings.shortTasksSettingsSubTitle,
        Strings.didntUnderstandSubTitle,
        Strings.aboutShortTasksSubTitle
    ), icons = listOf(
        R.drawable.abc_vector_test,
        R.drawable.ic_outline_settings_24,
        R.drawable.ic_outline_help_outline_24,
        R.drawable.ic_outline_info_24
    ))
}