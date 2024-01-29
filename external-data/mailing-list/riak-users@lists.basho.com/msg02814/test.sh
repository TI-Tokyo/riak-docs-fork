BUCKET=${1:-"bucket"}
PORT=${2:-8098}
KEY_COUNT=${3:-100}

JOB="{
    \"inputs\":\"$BUCKET\",
    \"query\":[{\"map\":
              {\"language\":\"javascript\",
               \"source\":\"function(value, keyData, arg) { var data = Riak.mapValuesJson(value)[0]; if(data.age >= \\\"0\\\" && data.age <= \\\"10\\\") return [value.key]; else return [];}\"}}]
}"

echo "===== Loading Data ($BUCKET) ====="
for (( i=0; i<=$KEY_COUNT; i++ ))
do
    curl http://127.0.0.1:$PORT/riak/$BUCKET/$i?w=0\&dw=0 -XPUT -H 'content-type: application/json' -d "{\"age\":$i}"
done
echo "Done"

echo "===== Running MapReduce ($BUCKET) ====="
echo $JOB|curl http://127.0.0.1:$PORT/mapred -XPOST -H 'content-type: application/json' --data-binary @-
echo ""
