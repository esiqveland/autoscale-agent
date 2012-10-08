#!/bin/sh
# Custom script for developing purpose.
# Creates the .jar-file, and copies into cassandra-folder

# Perform the maven clean install on the project
install() 
{
mvn clean install
}


# Perform complete installation of the commong-project
install_common()
{
    mvn clean install -f ../autoscale-common/pom.xml
}

# Copy the new Java-file into lib-folder of cassandra
copy_to_cassandra()
{
 cp target/autoscale-slave-1.0.0.jar ../cassandra/lib
 cp ../autoscale-common/target/autoscale-common-1.0.0.jar ../cassandra/lib
}


install_common && install && copy_to_cassandra
