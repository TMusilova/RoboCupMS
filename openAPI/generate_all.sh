#!/usr/bin/env bash

npx @openapitools/openapi-generator-cli generate -i auth_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/auth_api.html 

npx @openapitools/openapi-generator-cli generate -i competition_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/competition_api.html 

npx @openapitools/openapi-generator-cli generate -i discipline_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/discipline_api.html 

npx @openapitools/openapi-generator-cli generate -i match_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/match_api.html 

npx @openapitools/openapi-generator-cli generate -i match_group_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/match_group_api.html 

npx @openapitools/openapi-generator-cli generate -i playground_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/playground_api.html 

npx @openapitools/openapi-generator-cli generate -i robot_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/robot_api.html 

npx @openapitools/openapi-generator-cli generate -i team_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/team_api.html 

npx @openapitools/openapi-generator-cli generate -i team_registration_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/team_registration_api.html 

npx @openapitools/openapi-generator-cli generate -i user_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/user_api.html 

npx @openapitools/openapi-generator-cli generate -i order_management_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/order_management_api.html 

npx @openapitools/openapi-generator-cli generate -i competition_evaluation_api.yaml -g html2 -o ../../DokumentaceAPI/ && mv ../../DokumentaceAPI/index.html ../../DokumentaceAPI/competition_evaluation_api.html 