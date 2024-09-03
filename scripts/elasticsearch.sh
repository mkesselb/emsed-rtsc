# docker elasticsearch
docker pull docker.elastic.co/elasticsearch/elasticsearch:8.10.2
docker run --name elasticsearch -d -p 9200:9200 -p 9300:9300 -e "discovery.type=single-node" docker.elastic.co/elasticsearch/elasticsearch:8.10.2

curl -X PUT "localhost:9200/incidents" -H "Content-Type: application/json" -d'
{
"settings" : {
"index" : {
"number_of_shards" : 1,
"number_of_replicas" : 1
}
},
"mappings": {
"properties": {
"incidentType": { "type": "keyword" },
"location": { "type": "geo_point" },
"timestamp": { "type": "date" },
"severityLevel": { "type": "keyword" }
}
}
}'

curl -X GET "localhost:9200/_cat/indices?v"
