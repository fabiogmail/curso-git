set LISTAOLDS=%1%\%2%.txt
dir *.OLD /b > %LISTAOLDS%


for /F %%i IN (%LISTAOLDS%) DO  echo %%i