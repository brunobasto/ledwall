ffmpeg -t 14 -i DSCF8312.AVI -vf scale=320:-1 -r 10 -f image2pipe -vcodec ppm - | \
  convert -delay 5 -loop 0 - output.gif