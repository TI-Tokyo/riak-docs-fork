#!/bin/bash

path="./www.mail-archive.com/riak-users@lists.basho.com/"
urlPrefix="https://www.mail-archive.com/riak-users@lists.basho.com"

for (( i=0; i<=18642; i++ ))
#for (( i=0; i<=1; i++ ))
do
	file="msg$(printf %05d $i).html"
	url="$urlPrefix/$file"

	if test -f "$path/$file"; then
		# echo "Skipping: $file"
		wget -r -l 1 -np -nc -i "$path/$file" -F -B "$urlPrefix" --accept-regex "^https:\/\/www.mail-archive.com\/riak-users\@lists.basho.com\/.*$"
		continue
	else
		if [ -s "$path/$file" ]; then
			echo "Skipping (but zero-length): $file"
		else
			echo "$url"
			#wget -r -l 1 -p -np -nc "$url"
			#curl -o "$path/$file" "$url"
			#wget -i "$path/$file" -F -B "$urlPrefix" -nc
		fi
	fi
done
