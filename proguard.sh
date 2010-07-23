#!/bin/bash

NAME=BitcoinDroid
SRCS=src/com/opposablebrain/android/bitcoindroid/*.java
# always keep bin/classes. Add other stuff if needed.
#LIBS=bin/classes:libs/something-1.2.3.jar
LIBS=bin/classes

SDK=/Developer/android-sdk-mac_86/
PROGUARD=~/dev/proguard/lib/proguard.jar
KEYSTORE=/Users/rodin/dev/Android/rodin-lyasoff-android.keystore
KEYALIAS=ob

PLATFORM=$SDK/platforms/android-8/
AAPT=$PLATFORM/tools/aapt
DX=$PLATFORM/tools/dx
AJAR=$PLATFORM/android.jar
PKRES=bin/resources.ap_
OUT=$NAME-unalign.apk
ALIGNOUT=$NAME.apk

set -e #exit on error
mkdir -p bin/classes gen

$AAPT package -f -m -J gen -M AndroidManifest.xml -S res -I $AJAR -F $PKRES
javac -d bin/classes -classpath $LIBS -sourcepath src:gen -target 1.6 -bootclasspath $AJAR -g $SRCS
java -jar $PROGUARD -injars $LIBS -outjar bin/obfuscated.jar -libraryjars $AJAR @proguard.opts
$DX --dex --output=bin/classes.dex bin/obfuscated.jar
apkbuilder bin/$OUT -u -z $PKRES -f bin/classes.dex
jarsigner -keystore $KEYSTORE bin/$OUT $KEYALIAS
zipalign -f 4 bin/$OUT bin/$ALIGNOUT
