@echo off

set DISPLAYNAME1=Tomcat APPL01
set DISPLAYNAME2=Tomcat APPL02
set DISPLAYNAME3=Tomcat APPL03

set TOMCATHOME1=O:\Program Files\Apache Software Foundation\Tomcat 5.5
set TOMCATHOME2=P:\Program Files\Apache Software Foundation\Tomcat 5.5
set TOMCATHOME3=R:\Program Files\Apache Software Foundation\Tomcat 5.5



echo Criando Servico para %DISPLAYNAME1%...
"%TOMCATHOME1%\bin\tomcat5" //IS//Tomcat1 --DisplayName="%DISPLAYNAME1%" --Description="Apache Tomcat 5.5.9 Server" --Install="%TOMCATHOME1%\bin\tomcat5.exe" --Jvm=auto --LogLevel=error --LogPath="%TOMCATHOME1%\logs" --StdOutput=auto --StdError=auto --Classpath="%TOMCATHOME1%\bin\bootstrap.jar" --JvmOptions="-Dcatalina.home=%TOMCATHOME1%;-Djava.endorsed.dirs=%TOMCATHOME1%\common\endorsed;-Djava.io.tmpdir=%TOMCATHOME1%\temp;-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager" --StartMode=jvm --StartClass=org.apache.catalina.startup.Bootstrap --StartParams=start --StopMode=jvm --StopClass=org.apache.catalina.startup.Bootstrap --StopParams=stop --StopTimeout=0

echo Criando Servico para %DISPLAYNAME2%...
"%TOMCATHOME2%\bin\tomcat5" //IS//Tomcat2 --DisplayName="%DISPLAYNAME2%" --Description="Apache Tomcat 5.5.9 Server" --Install="%TOMCATHOME2%\bin\tomcat5.exe" --Jvm=auto --LogLevel=error --LogPath="%TOMCATHOME2%\logs" --StdOutput=auto --StdError=auto --Classpath="%TOMCATHOME2%\bin\bootstrap.jar" --JvmOptions="-Dcatalina.home=%TOMCATHOME2%;-Djava.endorsed.dirs=%TOMCATHOME2%\common\endorsed;-Djava.io.tmpdir=%TOMCATHOME2%\temp;-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager" --StartMode=jvm --StartClass=org.apache.catalina.startup.Bootstrap --StartParams=start --StopMode=jvm --StopClass=org.apache.catalina.startup.Bootstrap --StopParams=stop --StopTimeout=0

echo Criando Servico para %DISPLAYNAME3%...
"%TOMCATHOME3%\bin\tomcat5" //IS//Tomcat3 --DisplayName="%DISPLAYNAME3%" --Description="Apache Tomcat 5.5.9 Server" --Install="%TOMCATHOME3%\bin\tomcat5.exe" --Jvm=auto --LogLevel=error --LogPath="%TOMCATHOME3%\logs" --StdOutput=auto --StdError=auto --Classpath="%TOMCATHOME3%\bin\bootstrap.jar" --JvmOptions="-Dcatalina.home=%TOMCATHOME3%;-Djava.endorsed.dirs=%TOMCATHOME3%\common\endorsed;-Djava.io.tmpdir=%TOMCATHOME3%\temp;-Djava.util.logging.manager=org.apache.juli.ClassLoaderLogManager" --StartMode=jvm --StartClass=org.apache.catalina.startup.Bootstrap --StartParams=start --StopMode=jvm --StopClass=org.apache.catalina.startup.Bootstrap --StopParams=stop --StopTimeout=0

echo Fim

