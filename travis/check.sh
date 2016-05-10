#!/bin/bash
git config --global user.email "hpi.swt2@gmail.com"
git config --global user.name "swt2public"
git checkout -b travis-tmp
git pull -X theirs --no-edit "https://swt2public:wnjxeUn6pQpcnR4V@github.com/hpi-swt2-exercise/java-tdd-challenge-tasks.git" master
mvn test
python ./travis/submit_issue.py