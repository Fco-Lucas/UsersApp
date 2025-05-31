// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.hilt.gradle) apply false // Usará hilt = "2.51.1" do toml
    alias(libs.plugins.ksp.gradle) apply false   // Usará ksp = "2.0.0-1.0.21" do toml
}