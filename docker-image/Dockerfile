# The base image is the official tomcat image in version 8 together with the java runtime 8
FROM tomcat:8-jre8

# Import tomcat-users.xml file
ADD tomcat-users.xml /usr/local/tomcat/conf/
RUN rm -rf /usr/local/tomcat/webapps/*

ADD http://geolite.maxmind.com/download/geoip/database/GeoLiteCity.dat.gz /GeoLiteCity.dat.gz

RUN gzip -d /GeoLiteCity.dat.gz

# pack the app files into the container
ADD *.war /usr/local/tomcat/webapps/ROOT.war
ADD 3oh1-config.groovy /usr/local/tomcat/conf/

# add the postgres jdbc library
ADD https://jdbc.postgresql.org/download/postgresql-9.4.1208.jar /usr/local/tomcat/lib/

# external config file
ENV CATALINA_OPTS="-Dm3oh1.config.location=/usr/local/tomcat/conf/3oh1-config.groovy"
