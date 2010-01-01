rem call mvn install:install-file -DgroupId=saxon8 -DartifactId=saxon8-dom -Dversion=8.6.1 -Dpackaging=jar -Dfile=lib/saxon-8-6-1/saxon8-dom.jar -DgeneratePom=true -o
rem call mvn install:install-file -DgroupId=saxon8 -DartifactId=saxon8 -Dversion=8.6.1 -Dpackaging=jar -Dfile=lib/saxon-8-6-1/saxon8.jar -DgeneratePom=true -o
call mvn install:install-file -DgroupId=net.sf.saxon -DartifactId=saxon-dom -Dversion=9 -Dpackaging=jar -Dfile=lib/saxon-9-0-0-4/saxon9-dom.jar -DgeneratePom=true -o
call mvn install:install-file -DgroupId=net.sf.saxon -DartifactId=saxon -Dversion=9 -Dpackaging=jar -Dfile=lib/saxon-9-0-0-4/saxon9.jar -DgeneratePom=true -o
