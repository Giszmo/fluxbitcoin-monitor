#!/bin/bash

i=bitcoin-vpn.png
convert -background none $i -resize 36x36 ../res/drawable-ldpi/icon.png
convert -background none $i -resize 48x48 ../res/drawable-mdpi/icon.png
convert -background none $i -resize 72x72 ../res/drawable-hdpi/icon.png
convert -background none $i -resize 96x96 ../res/drawable-xhdpi/icon.png
convert -background none $i -resize 36x36 ../res/drawable/icon_extension.png
convert -background none $i -resize 48x48 ../res/drawable/icon_extension48.png
convert -colorspace gray -background none $i -resize 18x18 ../res/drawable-nodpi/icn_18x18_black_white.png
convert -background none $i -resize 512x512 uhdpi_icon.png

