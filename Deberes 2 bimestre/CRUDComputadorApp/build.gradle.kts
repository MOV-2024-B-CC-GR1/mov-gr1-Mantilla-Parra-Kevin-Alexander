// Archivo build.gradle.kts en el nivel del proyecto (raíz)
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kapt) apply false // ✅ Asegurar que KAPT está configurado correctamente
}
