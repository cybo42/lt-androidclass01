set SOURCE=C:\Course2771\exercises\ex81-files-from-ex71-solution\app\src\main
set DEST=.\app\src\main

rem copy the expenses form layout
copy %SOURCE%\res\layout\expenses_form.xml %DEST%\res\layout\.

rem copy strings and color resources
copy %SOURCE%\res\values\strings.xml %DEST%\res\values\.
copy %SOURCE%\res\values\colors.xml %DEST%\res\values\.
copy %SOURCE%\res\values\theme.xml %DEST%\res\values\.

rem copy the menu
copy %SOURCE%\res\menu\menu.xml %DEST%\res\menu\.

rem create drawable directories
mkdir %DEST%\res\drawable-hdpi
mkdir %DEST%\res\drawable-mdpi
mkdir %DEST%\res\drawable-xhdpi
mkdir %DEST%\res\drawable-xxhdpi
mkdir %DEST%\res\drawable-xxxhdpi
mkdir %DEST%\res\values-v23

REM copy the drawables
copy %SOURCE%\res\drawable\*.* %DEST%\res\drawable\.
copy %SOURCE%\res\drawable-hdpi\*.* %DEST%\res\drawable-hdpi\.
copy %SOURCE%\res\drawable-mdpi\*.* %DEST%\res\drawable-mdpi\.
copy %SOURCE%\res\drawable-v21\*.* %DEST%\res\drawable-v21\.
copy %SOURCE%\res\drawable-xhdpi\*.* %DEST%\res\drawable-xhdpi\.
copy %SOURCE%\res\drawable-xxhdpi\*.* %DEST%\res\drawable-xxhdpi\.
copy %SOURCE%\res\drawable-xxxhdpi\*.* %DEST%\res\drawable-xxxhdpi\.

#cp the missing theme files
copy %SOURCE%/res/values-v23/theme.xml %DEST%/res/values-v23/.

mkdir %DEST%\java\com\ltree\expenses\data

rem copy support java files (data and camera packages)
copy %SOURCE%\java\com\ltree\expenses\data\*.* %DEST%\java\com\ltree\expenses\data\.
rem [Causes problems} copy %SOURCE%\java\com\ltree\expenses\camera\*.* %DEST%\java\com\ltree\expenses\camera\.
copy %SOURCE%\java\com\ltree\expenses\ExpensesListActivity.java.txt %DEST%\java\com\ltree\expenses\.

rem copy the manifest

copy %SOURCE%\Ex71AndroidManifest.xml %DEST%\.
