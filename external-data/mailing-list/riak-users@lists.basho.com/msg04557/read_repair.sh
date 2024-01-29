#!/bin/sh
# By Tony Bussieres, Sept 2011
# Released under APL 2.0, http://www.apache.org/licenses/LICENSE-2.0.txt
# Feel free to use, modify and share

if [ -z "$1" ] 
then 
	echo "Please provide riak URL" 
	exit 1 
fi
URL=$1

curl -s -L ${URL}?buckets=true  | sed 's/"buckets":\[//g' | sed 's/\]//g' | sed 's/{//g' | sed 's/}//g'  | sed 's/""/","/g' | tr ',' '\n' | while read BUCKET
do
	BUCKET=`echo ${BUCKET} | sed 's/.*"\(.*\)".*/\1/g'`
	echo "Reading all entries from ${BUCKET}"
	curl -s -L ${URL}/${BUCKET}?keys=stream\&props=false  | sed 's/"keys":\[//g' | sed 's/\]//g' | sed 's/{//g' | sed 's/}//g'  | sed 's/""/","/g' | tr ',' '\n' | while read KEY
	do	 
		KEY=`echo ${KEY} | sed 's/.*"\(.*\)".*/\1/g'`
		curl -s -L ${URL}/${BUCKET}/${k} > /dev/null
		echo -n .
	done
	echo
done
echo "\n"Read-repair completed
