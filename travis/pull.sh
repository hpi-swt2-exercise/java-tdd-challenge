#!/usr/bin/expect -f
spawn git pull -X theirs --no-edit "git@github.com:hpi-swt2-exercise/java-tdd-challenge-tasks.git" master
expect "Enter passphrase for key '/home/travis/.ssh/id_rsa'"
send "\n";
interact