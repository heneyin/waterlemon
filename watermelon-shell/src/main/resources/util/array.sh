#!/usr/bin/env bash

var="henvealf.com"
towDcLowers=(${var//./ })
echo "0 ${towDcLowers[0]}"
echo "1 ${towDcLowers[1]}"