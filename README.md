# Java 11, SpringBoot, RabbitMQ, AWS SSM

## Start Test Environment
Before running service, please run test environment to initialize RabbitMQ environment
### In window:
`gradle startTestEnvironment`
### In unix:
`./gradlew startTestEnvironment`

## Start Application within background terminal
Your service must be turned before running the cucumber test
### In window:
`gradle bootRun --args="--spring.profiles.active=<your input>"`
### In unix
`./gradlew bootRun --args="--spring.profiles.active=<your input>"`
