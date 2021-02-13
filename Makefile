### BITRISE COMMANDS ###
change-file-permission:
	chmod +x scripts/ci/set-java-11.sh

install-java-11: change-file-permission
	scripts/ci/set-java-11.sh

### BUILD ####
buildDebug:
	./gradlew assembleDebug

### INSTALL ###
installDebug:
	adb install app/build/outputs/apk/debug/app-debug.apk

buildInstallDebug: buildDebug installDebug

### TEST ###
testDebug:
	./gradlew testDebug

testFileDebug:
	./gradlew testDebug --tests $(file)

androidTestDebug:
	./gradlew :$(module):connectedDebugAndroidTest

androidTestFileDebug:
	./gradlew :$(module):connectedDebugAndroidTest -Pandroid.testInstrumentationRunnerArguments.class=$(file)