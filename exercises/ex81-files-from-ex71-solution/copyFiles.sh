export SOURCE=~/Documents/LTree/2771/2771f1_source_trees/Course2771/exercises/ex81-files-from-ex71-solution/app/src/main
export DEST=~/Documents/LTree/2771/2771f1_source_trees/Course2771/exercises/ex81/app/src/main
# cp the expenses form layout
cp $SOURCE/res/layout/expenses_form.xml $DEST/res/layout/.
# cp strings and color resources
cp $SOURCE/res/values/strings.xml $DEST/res/values/.
cp $SOURCE/res/values/colors.xml $DEST/res/values/.
cp $SOURCE/res/values/theme.xml $DEST/res/values/.
#
# cp the menu
cp $SOURCE/res/menu/menu.xml $DEST/res/menu/.

# create drawable directories
mkdir $DEST/res/drawable-hdpi
mkdir $DEST/res/drawable-mdpi
mkdir $DEST/res/drawable-xhdpi
mkdir $DEST/res/drawable-xxhdpi
mkdir $DEST/res/values-v23


# cp the drawables
cp $SOURCE/res/drawable/*.* $DEST/res/drawable/.
cp $SOURCE/res/drawable-hdpi/*.* $DEST/res/drawable-hdpi/.
cp $SOURCE/res/drawable-mdpi/*.* $DEST/res/drawable-mdpi/.
cp $SOURCE/res/drawable-v21/*.* $DEST/res/drawable-v21/.
cp $SOURCE/res/drawable-xhdpi/*.* $DEST/res/drawable-xhdpi/.
cp $SOURCE/res/drawable-xxhdpi/*.* $DEST/res/drawable-xxhdpi/.
cp $SOURCE/res/drawable-xxxhdpi/*.* $DEST/res/drawable-xxxhdpi/.

#cp the missing theme files
cp $SOURCE/res/values-v23/theme.xml $DEST/res/values-v23/.

mkdir $DEST/java/com/ltree/expenses/data

# cp support java files (data and camera packages)
cp $SOURCE/java/com/ltree/expenses/data/*.* $DEST/java/com/ltree/expenses/data/.
cp $SOURCE/java/com/ltree/expenses/camera/*.* $DEST/java/com/ltree/expenses/camera/.
cp $SOURCE/java/com/ltree/expenses/ExpensesListActivity.java.txt $DEST/java/com/ltree/expenses/.

# cp the manifest

cp $SOURCE/Ex71AndroidManifest.xml $DEST/.

