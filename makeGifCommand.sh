#!/usr/bin/env bash

FILE_PATERN=$1
FPS=$2
OUTPUT=$3

CMD="ffmpeg -framerate $FPS -pattern_type glob -i '${FILE_PATERN}*.png' -r 15 -vf scale=512:-1 $OUTPUT"
echo $CMD

