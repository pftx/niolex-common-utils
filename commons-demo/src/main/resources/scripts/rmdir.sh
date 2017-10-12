#!/bin/sh
if [ -z $1 ]; then
  echo "Please specify the DIR you want to remove?"
  exit -2;
fi
cd $1 || {
  echo "Failed to changed dir, exit."
  exit -1
}
dir=`date -d -2day +"%Y-%m-%d"`
rm -rf $dir
echo "Delete old directory: $dir OK."
