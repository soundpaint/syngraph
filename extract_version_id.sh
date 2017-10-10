#!/bin/sh
# $Author: reuter $
# $Date: 2006-11-19 21:50:30 +0100 (Sun, 19 Nov 2006) $
# $Id: extract_version_id.sh 149 2006-11-19 20:50:30Z reuter $

cd $PWD/`dirname $0`
echo -n `grep MAJOR VERSION | cut -d"=" -f2`.
echo -n `grep MINOR VERSION | cut -d"=" -f2`.
echo -n `grep PATCH VERSION | head -1 | cut -d"=" -f2`
