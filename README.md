# webmetrics

To build project go to project root directory and run the command: **mvn clean install**

After building the project you can find **web-metrics-jar-with-dependencies.jar** in **target** directory.

To run the program you can give the input files and output directory through the CLI argument as the following command:

**java -jar target/web-metrics-jar-with-dependencies.jar -c clicks.json -i impressions.json -o /tmp**

By running the above command the input files **clicks.json** and **impressions.json** are getting into account for processing and output files including **metrics.json** and **recommendations.json** are written in **/tmp** directory.