@echo off

set HOMEGERA=c:\ClienteNovo
set DISCOIDLS=c:
set DIRIDLS=\usr\osx\cdrview\serv
set LISTAIDLS=%HOMEGERA%\ListaIDLs.txt
set JAVAHOME=c:\j2sdk1.4.2_04
set GERAPACOTE=%HOMEGERA%\GeraPacote

echo --------------------------- Criando lista de idls --------------------------------------------------
%DISCOIDLS%
cd %DIRIDLS%
dir *.idl /s /b > %LISTAIDLS%

echo --------------------------- Criando diretorios e Limpando temporários ------------------------------
c:
cd %HOMEGERA%\classes
mkdir Interfaces
cd Interfaces
del *.java
mkdir InterfacesORG
cd InterfacesORG
del *.java

echo --------------------------- Gerando IDLs -----------------------------------------------------------
for /F %%i IN (%LISTAIDLS%) DO %JAVAHOME%\bin\idlj -i %DISCOIDLS%%DIRIDLS% %%i
pause
echo --------------------------- Acrescentando pacote em IDLs -------------------------------------------
for %%i in (*.java) DO call %GERAPACOTE% %%i > nul




echo --------------------------- Todas as interfaces ja foram criadas, tecle ENTER para continuar -------
echo Todas as interfaces ja foram criadas, tecle ENTER para continuar
pause

echo --------------------------- Compilando IDLs --------------------------------------------------------
del Interfaces\*.class
%JAVAHOME%\bin\javac Interfaces\*.java
echo --------------------------- Criando JAR ------------------------------------------------------------
del Interfaces\*.java
del Interfaces.jar
%JAVAHOME%\bin\jar cvf Interfaces.jar Interfaces
echo --------------------------- Criando JAR certificado ------------------------------------------------
del sInterfaces2.jar
%JAVAHOME%\bin\jarsigner -keystore %HOMEGERA%\osx -signedjar sInterfaces2.jar Interfaces.jar signapl
rem del Interfaces.jar
echo --------------------------- Movendo JAR para diretorio Jars ----------------------------------------
move /Y sInterfaces2.jar ..\Jars
cd ..\Jars