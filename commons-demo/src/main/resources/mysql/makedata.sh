#!/bin/sh

LIMIT=1000000
for ((a=100001; a <= LIMIT ; a++))
do
        name=\'$a"G"\'
	mysql --socket=/home/lex/local/mysql/logs/my.sock -p271613912 -D test -e "insert into cs(id, name) values($a, $name);"
done

echo "done"
