# CronParser

This command line program runs the cron express of format `$minutes $hours $dayOfMonth $month $dayOfWeek command` and outputs the times for each part in a tabular form:  

So for the input expression: `"*/15 0 1,15 * 1-5 /usr/bin/find"`

The output is:

```
minute        0 15 30 45
hour          0
day of month  1 15
month         1 2 3 4 5 6 7 8 9 10 11 12
day of week   1 2 3 4 5
command       /usr/bin/find

```

# Pre-req

* Java 11
* Gradle Wrapper (no need to install)

# Building and Running the Code:

* Step 1: Building the jar: `./gradlew jar`
* Step 2: Run the tests: `./gradlew cleanTest test`
* Step 3: Run the program: `java -jar ./build/libs/CronParser-1.0-SNAPSHOT.jar "*/15 0 1,15 * 1-5 /usr/bin/find"` 

# NOT Implemented

## Alternate single value

* Single label based values for Days SUN-MON
* Single label based values for Months JAN-DEC
