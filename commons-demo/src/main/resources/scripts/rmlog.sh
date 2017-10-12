#!/bin/sh
DIR=$1
cd $DIR || {
  echo "Failed to changed dir, current [$PWD]. System will exit."
  exit -1
}

function del_file()
{
  local oldsuffix=""
  arr=`ls $1*`
  for filename in $arr
  do
    filesuffix=${filename##*.}
    #echo -n "- $filesuffix "
    if [[ "$oldsuffix" < "$filesuffix" ]]; then
		if [ -n "$oldsuffix" ]; then
		  ff="$1.$oldsuffix"
		  #echo "IF $ff"
		  rm $ff
		fi
		oldsuffix=$filesuffix
    else
		ff=$1.$filesuffix
		#echo "EL $ff"
		rm $ff
    fi
  done
}

function log()
{
  echo -n `date +"%Y%m%d %H:%M:%S"`
  echo -n " "
  echo $1
}

log "Start to delete file..."
del_file log
del_file snapshot
log "Delete old logs @ [$PWD] OK."
