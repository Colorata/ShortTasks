package com.colorata.st.ui.theme

import com.colorata.st.R

object ScreenComponents{

    object PowerHelp {
        val titles = mutableListOf(
            Strings.powerAssistant,
            Strings.powerHelp1,
            Strings.powerHelp2,
            Strings.powerHelp3
        )

        val subTitles = mutableListOf(
        Strings.powerHelpSubTitle,
        "", "", ""
        )

        val icons = mutableListOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_accessibility_24,
            R.drawable.ic_outline_power_settings_new_24,
            R.drawable.ic_outline_check_circle_outline_24,
        )
    }

    object MainScreen {
        val titles = mutableListOf(
            Strings.main,
            Strings.powerAssistant
        )

        val subTitles = mutableListOf(
            Strings.relatedPosts,
            Strings.powerMainSubTitle
        )

        val icons = mutableListOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_power_settings_new_24
        )
    }

    object FeaturesScreen {
        val titles = mutableListOf(
        Strings.features,
        Strings.weatherDirector,
        Strings.powerAssistant
        )

        val subTitles = mutableListOf(
            Strings.relatedPosts,
            Strings.weatherDirectorSettingsSubTitle,
            Strings.powerAssistantSettingsSubTitle
        )

        val icons = mutableListOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_cloud_24,
            R.drawable.ic_outline_power_settings_new_24
        )
    }

    object MoreScreen {
        val titles = mutableListOf(
        Strings.more,
        Strings.settings,
        Strings.help,
        Strings.about
        )

        val subTitles = mutableListOf(
            Strings.relatedPosts,
            Strings.shortTasksSettingsSubTitle,
            Strings.didntUnderstandSubTitle,
            Strings.aboutShortTasksSubTitle
        )

        val icons = mutableListOf(
            R.drawable.abc_vector_test,
            R.drawable.ic_outline_settings_24,
            R.drawable.ic_outline_help_outline_24,
            R.drawable.ic_outline_info_24
        )
    }
}