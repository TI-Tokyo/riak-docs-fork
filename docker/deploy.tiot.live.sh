#!/bin/bash

if [[ ! -d "output" ]]
then
    echo "Run this from the root of the repo. i.e. './docker/deploy.beta.sh'"
    exit
fi

# empty output folder
sudo rm -r ./output/*

# generate new content
docker compose -f ./docker/docker-compose.generate-riak-docs.yaml up

# remove target incase it already exists
echo "Cleaning previous upload directory..."
ssh peter-clark@www.tiot.jp sudo rm -r /var/www/www-tiot-jp-2025-03-29/riak-docs-new

# copy files over
echo "Copying new files to server..."
rsync -avzh --chown www-data:www-data ./output/* peter-clark@www.tiot.jp:/var/www/www-tiot-jp-2025-03-29/riak-docs-new

# move files from temp dir to to right place
#ssh peter-clark@www.tiot.jp mv /var/www/www-tiot-jp-2025-03-29/riak-docs-new/output/* /var/www/www-tiot-jp-2025-03-29/riak-docs-new

# remove temp dir
#ssh peter-clark@www.tiot.jp rm -r /var/www/www-tiot-jp-2025-03-29/riak-docs-new/output

# remove previous old copy
echo "Removing previous backup..."
ssh peter-clark@www.tiot.jp sudo rm -r /var/www/www-tiot-jp-2025-03-29/riak-docs-old

# move current to old
echo "Making new backup..."
ssh peter-clark@www.tiot.jp sudo mv /var/www/www-tiot-jp-2025-03-29/riak-docs /var/www/www-tiot-jp-2025-03-29/riak-docs-old

# move new to current
echo "Moving new files to right URI..."
ssh peter-clark@www.tiot.jp sudo mv /var/www/www-tiot-jp-2025-03-29/riak-docs-new /var/www/www-tiot-jp-2025-03-29/riak-docs

echo "Done: https://www.tiot.jp/riak-docs/"
