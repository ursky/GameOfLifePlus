#!/usr/bin/env bash
# This is a macro to generate a GIF-making command given a set of frame captures.
# This does not actually run the ffmpeg command.

FILE_PATERN=$1
FPS=$2
OUTPUT=$3

CMD="ffmpeg -framerate $FPS -pattern_type glob -i '${FILE_PATERN}*.png' -r $FPS -vf scale=512:-1 $OUTPUT"
echo $CMD

