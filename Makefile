help: ## Show this help
	@egrep -h '\s##\s' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "\033[36m%-20s\033[0m %s\n", $$1, $$2}'

99_tests: ## Execute the tests
	./gradlew clean build

1_Run_customer-details-api_with_its_database: ## Run application with all dependencies (after it's running open new console tab to continue 'make' steps)
	 ./gradlew :customer-details-api:jibDockerBuild
	docker compose up

2_Issue_POST_request_to_create_new_customer: ## Run one example to check post API is working (success is 201)
	 curl -v --location 'localhost:8080/v1/customers' --header 'Content-Type: application/json' --data '{"customer_ref": "xc84fb90-12c4-11e1-840d-7b25c5ee775a", "customer_name": "John Doe", "address_line_one": "99 Cranberry Avenue", "address_line_two": "Strawberry Prospect", "town": "Sunny Town", "county": "Perfect County", "country": "Neverland", "post_code": "NL2 20GS"}'

3_Issue_GET_request_to_retrieve_the_customer_created_at_step_2: ## Run one example to check get API is working (success is 200)
	curl -v --location 'localhost:8080/v1/customers/xc84fb90-12c4-11e1-840d-7b25c5ee775a'

4_Build_customer-details-file-importer.jar:
	./gradlew :customer-details-file-importer:clean :customer-details-file-importer:build

5_Import_file_using_jar_and_load_into_API: ## Ensure all steps 1-4 are working export will be generated in same directory
	@echo "Copy your file into project directory, and give filename OR leave blank to use existing sample.csv"; \
    read userInput; \
    userInput=$${userInput:-sample.csv}; \
    printf "Using file: %s\n" "$$userInput"; \
	java -jar -Dcustomer-details-api.url=http://localhost:8080 ./customer-details-file-importer/build/libs/customer-details-file-importer-0.0.1-SNAPSHOT.jar $$userInput


0.Stop_and_remove_ALL_running_docker_containers._Remove_the_volume.: ## Stops and removes all containers, deletes customer-details-processor_db volume
	-docker ps -aq| xargs docker stop
	-docker ps -aq| xargs docker rm
	-docker volume rm customer-details-processor_db

