#!/bin/sh

cd src/site/resources/testsuite/mml

for I in *mml; do
echo $I...
LEN=$(echo $I | wc -c)
if [ $LEN -le 6 ] ; then
  BACK=white
else
  BACK=#CDCDCD
fi

TARGET=../is/$(basename $I .mml).png

# Please note: Font size 16 is for OS X only!
# For Windows / Unix you'll probably have to use size 12.0
sh ../../../../../jeuclid/target/appassembler/bin/mml2xxx $I $TARGET -FontSize 16.0 -BackgroundColor $BACK

done

cd -
