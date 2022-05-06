#!/usr/bin/env bash

npx @openapitools/openapi-generator-cli generate -i auth_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/auth_api

npx @openapitools/openapi-generator-cli generate -i competition_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/competition_api

npx @openapitools/openapi-generator-cli generate -i competition_evaluation_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/competition_evaluation_api

npx @openapitools/openapi-generator-cli generate -i discipline_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/discipline_api

npx @openapitools/openapi-generator-cli generate -i match_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/match_api

npx @openapitools/openapi-generator-cli generate -i match_group_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/match_group_api

npx @openapitools/openapi-generator-cli generate -i order_management_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/order_management_api

npx @openapitools/openapi-generator-cli generate -i playground_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/playground_api

npx @openapitools/openapi-generator-cli generate -i robot_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/robot_api

npx @openapitools/openapi-generator-cli generate -i team_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/team_api

npx @openapitools/openapi-generator-cli generate -i team_registration_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/team_registration_api

npx @openapitools/openapi-generator-cli generate -i user_api.yaml -g typescript-angular -o ../../RoboCupMS-API-Client/user_api