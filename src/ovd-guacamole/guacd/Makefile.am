# ***** BEGIN LICENSE BLOCK *****
# Version: MPL 1.1/GPL 2.0/LGPL 2.1
#
# The contents of this file are subject to the Mozilla Public License Version
# 1.1 (the "License"); you may not use this file except in compliance with
# the License. You may obtain a copy of the License at
# http://www.mozilla.org/MPL/
#
# Software distributed under the License is distributed on an "AS IS" basis,
# WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
# for the specific language governing rights and limitations under the
# License.
#
# The Original Code is guacd.
#
# The Initial Developer of the Original Code is
# Michael Jumper.
# Portions created by the Initial Developer are Copyright (C) 2010
# the Initial Developer. All Rights Reserved.
#
# Contributor(s):
#
# Alternatively, the contents of this file may be used under the terms of
# either the GNU General Public License Version 2 or later (the "GPL"), or
# the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
# in which case the provisions of the GPL or the LGPL are applicable instead
# of those above. If you wish to allow use of your version of this file only
# under the terms of either the GPL or the LGPL, and not to allow others to
# use your version of this file under the terms of the MPL, indicate your
# decision by deleting the provisions above and replace them with the notice
# and other provisions required by the GPL or the LGPL. If you do not delete
# the provisions above, a recipient may use your version of this file under
# the terms of any one of the MPL, the GPL or the LGPL.
#
# ***** END LICENSE BLOCK *****

AUTOMAKE_OPTIONS = foreign 
initdir = @init_dir@
defaultdir = @default_dir@

AM_CFLAGS = -Werror -Wall -pedantic -Iinclude

sbin_PROGRAMS = guacd
init_SCRIPTS = init.d/guacd.debian init.d/guacd.redhat init.d/guacd.suse
default_SCRIPTS = default/guacd
man_MANS = man/guacd.8

noinst_HEADERS = include/client.h include/log.h
guacd_SOURCES = src/daemon.c src/client.c src/log.c

EXTRA_DIST = init.d/guacd.debian.in init.d/guacd.redhat.in init.d/guacd.suse.in default/guacd man/guacd.8 LICENSE
CLEANFILES = $(init_SCRIPTS)

init.d/guacd.debian: init.d/guacd.debian.in
	sed -e 's,[@]sbindir[@],$(sbindir),g' init.d/guacd.debian.in | sed -e 's,[@]defaultdir[@],$(defaultdir),g'  > init.d/guacd.debian
	chmod +x init.d/guacd.debian

init.d/guacd.redhat: init.d/guacd.redhat.in
	sed -e 's,[@]sbindir[@],$(sbindir),g' init.d/guacd.redhat.in | sed -e 's,[@]defaultdir[@],$(defaultdir),g'  > init.d/guacd.redhat
	chmod +x init.d/guacd.redhat

init.d/guacd.suse: init.d/guacd.suse.in
	sed -e 's,[@]sbindir[@],$(sbindir),g' init.d/guacd.suse.in | sed -e 's,[@]defaultdir[@],$(defaultdir),g'  > init.d/guacd.suse
	chmod +x init.d/guacd.suse
