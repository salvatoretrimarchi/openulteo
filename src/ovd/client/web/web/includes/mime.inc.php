<?php
/**
 * Copyright (C) 2011 Ulteo SAS
 * http://www.ulteo.com
 * Author Julien LANGLOIS <julien@ulteo.com>
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; version 2
 * of the License.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 **/

function getMimeTypefromExtension($extension_) {
	global $Mime_DB;

	if (array_key_exists($extension_, $Mime_DB))
		return $Mime_DB[$extension_];

	return NULL;
}

$Mime_DB = array(
	'323' => 'text/h323',
	'3g2' => 'audio/3gpp2',
	'3gp' => 'video/3gpp',
	'3gp2' => 'video/3gpp2',
	'3gpp' => 'video/3gpp',
	'7z' => 'application/x-7z-compressed',
	'AAC' => 'audio/vnd.dlna.adts',
	'ADT' => 'audio/vnd.dlna.adts',
	'ADTS' => 'audio/vnd.dlna.adts',
	'AddIn' => 'text/xml',
	'M2T' => 'video/vnd.dlna.mpeg-tts',
	'M2TS' => 'video/vnd.dlna.mpeg-tts',
	'M2V' => 'video/mpeg',
	'MOD' => 'video/mpeg',
	'MTS' => 'video/vnd.dlna.mpeg-tts',
	'NMW' => 'application/nmwb',
	'TS' => 'video/vnd.dlna.mpeg-tts',
	'TTS' => 'video/vnd.dlna.mpeg-tts',
	'WMD' => 'application/x-ms-wmd',
	'abw' => 'application/x-abiword',
	'accda' => 'application/msaccess',
	'accdb' => 'application/msaccess',
	'accdc' => 'application/msaccess',
	'accde' => 'application/msaccess',
	'accdr' => 'application/msaccess',
	'accdt' => 'application/msaccess',
	'acrobatsecuritysettings' => 'application/vnd.adobe.acrobat-security-settings',
	'ade' => 'application/msaccess',
	'adp' => 'application/msaccess',
	'adts' => 'audio/vnd.dlna.adts',
	'ai' => 'application/postscript',
	'aif' => 'audio/aiff',
	'aifc' => 'audio/aiff',
	'aiff' => 'audio/x-aiff',
	'air' => 'application/vnd.adobe.air-application-installer-package+zip',
	'alc' => 'chemical/x-alchemy',
	'amr' => 'audio/amr',
	'anx' => 'application/annodex',
	'application' => 'application/x-ms-application',
	'arj' => 'application/x-arj-compressed',
	'art' => 'image/x-jg',
	'asc' => 'text/plain',
	'asf' => 'video/x-ms-asf',
	'asm' => 'text/plain',
	'asn' => 'chemical/x-ncbi-asn1-spec',
	'aso' => 'chemical/x-ncbi-asn1-binary',
	'asx' => 'video/x-ms-asf',
	'atom' => 'application/atom+xml',
	'atomcat' => 'application/atomcat+xml',
	'atomsrv' => 'application/atomserv+xml',
	'au' => 'audio/basic',
	'avi' => 'video/avi',
	'awb' => 'audio/amr-wb',
	'axa' => 'audio/annodex',
	'axv' => 'video/annodex',
	'b' => 'chemical/x-molconn-Z',
	'bak' => 'application/x-trash',
	'bat' => 'application/x-msdos-program',
	'bcpio' => 'application/x-bcpio',
	'bib' => 'text/x-bibtex',
	'bin' => 'application/octet-stream',
	'bmp' => 'image/bmp',
	'boo' => 'text/x-boo',
	'book' => 'application/x-maker',
	'brf' => 'text/plain',
	'bsd' => 'chemical/x-crossfire',
	'c' => 'text/plain',
	'c++' => 'text/x-c++src',
	'c2r' => 'text/vnd-ms.click2record+xml',
	'c3d' => 'chemical/x-chem3d',
	'cab' => 'application/x-cab',
	'cac' => 'chemical/x-cache',
	'cache' => 'chemical/x-cache',
	'cap' => 'application/cap',
	'cascii' => 'chemical/x-cactvs-binary',
	'cat' => 'application/vnd.ms-pki.seccat',
	'cbin' => 'chemical/x-cactvs-binary',
	'cbr' => 'application/x-cbr',
	'cbz' => 'application/x-cbz',
	'cc' => 'text/plain',
	'ccad' => 'application/clariscad',
	'cda' => 'application/x-cdf',
	'cdf' => 'application/x-cdf',
	'cdr' => 'image/x-coreldraw',
	'cdt' => 'image/x-coreldrawtemplate',
	'cdx' => 'chemical/x-cdx',
	'cdy' => 'application/vnd.cinderella',
	'cef' => 'chemical/x-cxf',
	'cer' => 'application/x-x509-ca-cert',
	'chm' => 'chemical/x-chemdraw',
	'chrt' => 'application/x-kchart',
	'cif' => 'chemical/x-cif',
	'class' => 'application/java-vm',
	'cls' => 'text/x-tex',
	'cmdf' => 'chemical/x-cmdf',
	'cml' => 'chemical/x-cml',
	'cod' => 'text/plain',
	'com' => 'application/x-msdos-program',
	'config' => 'application/xml',
	'contact' => 'text/x-ms-contact',
	'cpa' => 'chemical/x-compass',
	'cpio' => 'application/x-cpio',
	'cpp' => 'text/plain',
	'cpt' => 'image/x-corelphotopaint',
	'crl' => 'application/pkix-crl',
	'crt' => 'application/x-x509-ca-cert',
	'csf' => 'chemical/x-cache-csf',
	'csh' => 'text/x-csh',
	'csm' => 'chemical/x-csml',
	'csml' => 'chemical/x-csml',
	'css' => 'text/css',
	'csv' => 'application/vnd.ms-excel',
	'ctab' => 'chemical/x-cactvs-binary',
	'ctx' => 'chemical/x-ctx',
	'cu' => 'application/cu-seeme',
	'cub' => 'chemical/x-gaussian-cube',
	'cur' => 'text/plain',
	'cxf' => 'chemical/x-cxf',
	'cxx' => 'text/plain',
	'd' => 'text/x-dsrc',
	'dat' => 'application/x-ns-proxy-autoconfig',
	'datasource' => 'application/xml',
	'davmount' => 'application/davmount+xml',
	'dcr' => 'application/x-director',
	'deb' => 'application/x-debian-package',
	'def' => 'text/plain',
	'der' => 'application/x-x509-ca-cert',
	'dib' => 'image/bmp',
	'dif' => 'application/vnd.ms-excel',
	'diff' => 'text/x-diff',
	'dir' => 'application/x-director',
	'djv' => 'image/vnd.djvu',
	'djvu' => 'image/vnd.djvu',
	'dl' => 'video/dl',
	'dll' => 'application/x-msdownload',
	'dlm' => 'text/dlm',
	'dmg' => 'application/x-apple-diskimage',
	'dms' => 'application/x-dms',
	'doc' => 'application/msword',
	'docm' => 'application/vnd.ms-word.document.macroEnabled.12',
	'docx' => 'application/vnd.openxmlformats-officedocument.wordprocessingml.document',
	'dot' => 'application/msword',
	'dotm' => 'application/vnd.ms-word.template.macroEnabled.12',
	'dotx' => 'application/vnd.openxmlformats-officedocument.wordprocessingml.template',
	'drw' => 'application/drafting',
	'dsp' => 'text/plain',
	'dsw' => 'text/plain',
	'dtd' => 'application/xml-dtd',
	'dv' => 'video/dv',
	'dvi' => 'application/x-dvi',
	'dwfx' => 'model/vnd.dwfx+xps',
	'dwg' => 'application/acad',
	'dx' => 'chemical/x-jcamp-dx',
	'dxf' => 'application/dxf',
	'dxr' => 'application/x-director',
	'easmx' => 'model/vnd.easmx+xps',
	'edrwx' => 'model/vnd.edrwx+xps',
	'emb' => 'chemical/x-embl-dl-nucleotide',
	'embl' => 'chemical/x-embl-dl-nucleotide',
	'eml' => 'message/rfc822',
	'ent' => 'chemical/x-pdb',
	'eprtx' => 'model/vnd.eprtx+xps',
	'eps' => 'application/postscript',
	'eps2' => 'application/postscript',
	'eps3' => 'application/postscript',
	'epsf' => 'application/postscript',
	'es' => 'application/ecmascript',
	'espi' => 'application/postscript',
	'etx' => 'text/x-setext',
	'exe' => 'application/x-msdownload',
	'ez' => 'application/andrew-inset',
	'f' => 'text/plain',
	'f90' => 'text/plain',
	'fb' => 'application/x-maker',
	'fbdoc' => 'application/x-maker',
	'fch' => 'chemical/x-gaussian-checkpoint',
	'fchk' => 'chemical/x-gaussian-checkpoint',
	'fdf' => 'application/vnd.fdf',
	'fif' => 'application/fractals',
	'fig' => 'application/x-xfig',
	'flac' => 'audio/flac',
	'fli' => 'video/fli',
	'flv' => 'video/x-flv',
	'fm' => 'application/x-maker',
	'frame' => 'application/x-maker',
	'frm' => 'application/x-maker',
	'gal' => 'chemical/x-gaussian-log',
	'gam' => 'chemical/x-gamess-input',
	'gamin' => 'chemical/x-gamess-input',
	'gau' => 'chemical/x-gaussian-input',
	'gcd' => 'text/x-pcs-gcd',
	'gcf' => 'application/x-graphing-calculator',
	'gcg' => 'chemical/x-gcg8-sequence',
	'gen' => 'chemical/x-genbank',
	'gf' => 'application/x-tex-gf',
	'gif' => 'image/gif',
	'gjc' => 'chemical/x-gaussian-input',
	'gjf' => 'chemical/x-gaussian-input',
	'gl' => 'video/gl',
	'gnumeric' => 'application/x-gnumeric',
	'gpt' => 'chemical/x-mopac-graph',
	'group' => 'text/x-ms-group',
	'gsf' => 'application/x-font',
	'gsm' => 'audio/x-gsm',
	'gtar' => 'application/x-gtar',
	'gz' => 'application/x-gzip',
	'h' => 'text/plain',
	'h++' => 'text/x-c++hdr',
	'hdf' => 'application/x-hdf',
	'hh' => 'text/x-c++hdr',
	'hin' => 'chemical/x-hin',
	'hpp' => 'text/plain',
	'hqx' => 'application/mac-binhex40',
	'hs' => 'text/x-haskell',
	'hta' => 'application/hta',
	'htc' => 'text/x-component',
	'htm' => 'text/html',
	'html' => 'text/html',
	'htt' => 'text/webviewhtml',
	'hxa' => 'application/xml',
	'hxc' => 'application/xml',
	'hxd' => 'application/octet-stream',
	'hxe' => 'application/xml',
	'hxf' => 'application/xml',
	'hxh' => 'application/octet-stream',
	'hxi' => 'application/octet-stream',
	'hxk' => 'application/xml',
	'hxq' => 'application/octet-stream',
	'hxr' => 'application/octet-stream',
	'hxs' => 'application/octet-stream',
	'hxt' => 'application/xml',
	'hxv' => 'application/xml',
	'hxw' => 'application/octet-stream',
	'hxx' => 'text/plain',
	'i' => 'text/plain',
	'ica' => 'application/x-ica',
	'ice' => 'x-conference/x-cooltalk',
	'ico' => 'image/x-icon',
	'ics' => 'text/calendar',
	'icz' => 'text/calendar',
	'idl' => 'text/plain',
	'ief' => 'image/ief',
	'iges' => 'model/iges',
	'igs' => 'model/iges',
	'iii' => 'application/x-iphone',
	'inc' => 'text/plain',
	'info' => 'application/x-info',
	'infopathxml' => 'application/ms-infopath.xml',
	'inl' => 'text/plain',
	'inp' => 'chemical/x-gamess-input',
	'ins' => 'application/x-internet-signup',
	'ips' => 'application/x-ipscript',
	'ipx' => 'application/x-ipix',
	'iqy' => 'text/x-ms-iqy',
	'iso' => 'application/x-iso9660-image',
	'isp' => 'application/x-internet-signup',
	'ist' => 'chemical/x-isostar',
	'istr' => 'chemical/x-isostar',
	'jad' => 'text/vnd.sun.j2me.app-descriptor',
	'jam' => 'application/x-jam',
	'jar' => 'application/java-archive',
	'java' => 'text/x-java',
	'jdx' => 'chemical/x-jcamp-dx',
	'jfif' => 'image/jpeg',
	'jmz' => 'application/x-jmol',
	'jng' => 'image/x-jng',
	'jnlp' => 'application/x-java-jnlp-file',
	'jpe' => 'image/jpeg',
	'jpeg' => 'image/jpeg',
	'jpg' => 'image/jpeg',
	'js' => 'application/javascript',
	'jtx' => 'application/x-jtx+xps',
	'kar' => 'audio/midi',
	'key' => 'application/pgp-keys',
	'kil' => 'application/x-killustrator',
	'kin' => 'chemical/x-kinemage',
	'kml' => 'application/vnd.google-earth.kml+xml',
	'kmz' => 'application/vnd.google-earth.kmz',
	'kpr' => 'application/x-kpresenter',
	'kpt' => 'application/x-kpresenter',
	'ksp' => 'application/x-kspread',
	'kwd' => 'application/x-kword',
	'kwt' => 'application/x-kword',
	'latex' => 'application/x-latex',
	'lha' => 'application/x-lha',
	'lhs' => 'text/x-literate-haskell',
	'library-ms' => 'application/windows-library+xml',
	'lin' => 'application/bbolin',
	'lsf' => 'video/x-la-asf',
	'lsp' => 'application/x-lisp',
	'lst' => 'text/plain',
	'lsx' => 'video/x-la-asf',
	'ltx' => 'text/x-tex',
	'lyx' => 'application/x-lyx',
	'lzh' => 'application/x-lzh',
	'lzx' => 'application/x-lzx',
	'm' => 'text/plain',
	'm1v' => 'video/mpeg',
	'm3g' => 'application/m3g',
	'm3u' => 'audio/mpegurl',
	'm4a' => 'audio/mpeg',
	'm4v' => 'video/mp4',
	'mak' => 'text/plain',
	'maker' => 'application/x-maker',
	'man' => 'application/x-troff-man',
	'map' => 'text/plain',
	'mcif' => 'chemical/x-mmcif',
	'mcm' => 'chemical/x-macmolecule',
	'mda' => 'application/msaccess',
	'mdb' => 'application/msaccess',
	'mde' => 'application/msaccess',
	'mdi' => 'image/vnd.ms-modi',
	'mdp' => 'text/plain',
	'me' => 'application/x-troff-me',
	'mesh' => 'model/mesh',
	'mfp' => 'application/x-shockwave-flash',
	'mht' => 'message/rfc822',
	'mhtml' => 'message/rfc822',
	'mid' => 'midi/mid',
	'midi' => 'audio/mid',
	'mif' => 'application/x-mif',
	'mime' => 'www/mime',
	'mk' => 'text/plain',
	'mm' => 'application/x-freemind',
	'mmd' => 'chemical/x-macromodel-input',
	'mmf' => 'application/vnd.smaf',
	'mml' => 'text/mathml',
	'mmod' => 'chemical/x-macromodel-input',
	'mng' => 'video/x-mng',
	'moc' => 'text/x-moc',
	'mol' => 'chemical/x-mdl-molfile',
	'mol2' => 'chemical/x-mol2',
	'moo' => 'chemical/x-mopac-out',
	'mop' => 'chemical/x-mopac-input',
	'mopcrt' => 'chemical/x-mopac-input',
	'mov' => 'video/quicktime',
	'movie' => 'video/x-sgi-movie',
	'mp2' => 'video/mpeg',
	'mp2v' => 'video/mpeg',
	'mp3' => 'audio/x-mp3',
	'mp4' => 'video/mp4',
	'mp4v' => 'video/mp4',
	'mpa' => 'video/mpeg',
	'mpc' => 'chemical/x-mopac-input',
	'mpe' => 'video/mpeg',
	'mpeg' => 'video/x-mpeg2a',
	'mpega' => 'audio/mpeg',
	'mpf' => 'application/vnd.ms-mediapackage',
	'mpg' => 'video/mpeg',
	'mpga' => 'audio/mpeg',
	'mpv' => 'video/x-matroska',
	'mpv2' => 'video/mpeg',
	'ms' => 'application/x-troff-ms',
	'msh' => 'model/mesh',
	'msi' => 'application/x-msi',
	'mvb' => 'chemical/x-mopac-vib',
	'mxu' => 'video/vnd.mpegurl',
	'nb' => 'application/mathematica',
	'nbp' => 'application/mathematica',
	'nc' => 'application/x-netcdf',
	'nix' => 'application/x-mix-transfer',
	'nwc' => 'application/x-nwc',
	'nws' => 'message/rfc822',
	'o' => 'application/x-object',
	'oda' => 'application/oda',
	'odb' => 'application/vnd.sun.xml.base',
	'odc' => 'text/x-ms-odc',
	'odf' => 'application/vnd.oasis.opendocument.formula',
	'odg' => 'application/vnd.oasis.opendocument.graphics',
	'odh' => 'text/plain',
	'odi' => 'application/vnd.oasis.opendocument.image',
	'odl' => 'text/plain',
	'odm' => 'application/vnd.oasis.opendocument.text-master',
	'odp' => 'application/vnd.oasis.opendocument.presentation',
	'ods' => 'application/vnd.oasis.opendocument.spreadsheet',
	'odt' => 'application/vnd.oasis.opendocument.text',
	'oga' => 'audio/ogg',
	'ogg' => 'audio/ogg',
	'ogm' => 'application/ogg',
	'ogv' => 'video/ogg',
	'ogx' => 'application/ogg',
	'old' => 'application/x-trash',
	'ols' => 'application/vnd.ms-publisher',
	'osdx' => 'application/opensearchdescription+xml',
	'otg' => 'application/vnd.oasis.opendocument.graphics-template',
	'oth' => 'application/vnd.oasis.opendocument.text-web',
	'otp' => 'application/vnd.oasis.opendocument.presentation-template',
	'ots' => 'application/vnd.oasis.opendocument.spreadsheet-template',
	'ott' => 'application/vnd.oasis.opendocument.text-template',
	'oxt' => 'application/vnd.openofficeorg.extension',
	'oza' => 'application/x-oz-application',
	'p' => 'text/x-pascal',
	'p10' => 'application/pkcs10',
	'p12' => 'application/x-pkcs12',
	'p7b' => 'application/x-pkcs7-certificates',
	'p7c' => 'application/pkcs7-mime',
	'p7m' => 'application/pkcs7-mime',
	'p7r' => 'application/x-pkcs7-certreqresp',
	'p7s' => 'application/pkcs7-signature',
	'pac' => 'application/x-ns-proxy-autoconfig',
	'pas' => 'text/x-pascal',
	'pat' => 'image/x-coreldrawpattern',
	'patch' => 'text/x-diff',
	'pbm' => 'image/x-portable-bitmap',
	'pcap' => 'application/cap',
	'pcf' => 'application/x-font',
	'pcf.Z' => 'application/x-font',
	'pcx' => 'image/pcx',
	'pdb' => 'chemical/x-pdb',
	'pdf' => 'application/pdf',
	'pdfxml' => 'application/vnd.adobe.pdfxml',
	'pdx' => 'application/vnd.adobe.pdx',
	'pfa' => 'application/x-font',
	'pfb' => 'application/x-font',
	'pfx' => 'application/x-pkcs12',
	'pgm' => 'image/x-portable-graymap',
	'pgn' => 'application/x-chess-pgn',
	'pgp' => 'application/pgp-signature',
	'php' => 'application/x-httpd-php',
	'php3' => 'application/x-httpd-php3',
	'php3p' => 'application/x-httpd-php3-preprocessed',
	'php4' => 'application/x-httpd-php4',
	'phps' => 'application/x-httpd-php-source',
	'pht' => 'application/x-httpd-php',
	'phtml' => 'application/x-httpd-php',
	'pk' => 'application/x-tex-pk',
	'pko' => 'application/vnd.ms-pki.pko',
	'pl' => 'text/x-perl',
	'pls' => 'audio/x-scpls',
	'pm' => 'text/x-perl',
	'png' => 'image/png',
	'pnm' => 'image/x-portable-anymap',
	'pot' => 'application/vnd.ms-powerpoint',
	'potm' => 'application/vnd.ms-powerpoint.template.macroEnabled.12',
	'potx' => 'application/vnd.openxmlformats-officedocument.presentationml.template',
	'ppa' => 'application/vnd.ms-powerpoint',
	'ppam' => 'application/vnd.ms-powerpoint.addin.macroEnabled.12',
	'ppm' => 'image/x-portable-pixmap',
	'pps' => 'application/vnd.ms-powerpoint',
	'ppsm' => 'application/vnd.ms-powerpoint.slideshow.macroEnabled.12',
	'ppsx' => 'application/vnd.openxmlformats-officedocument.presentationml.slideshow',
	'ppt' => 'application/x-mspowerpoint',
	'pptm' => 'application/x-mspowerpoint.macroEnabled.12',
	'pptx' => 'application/x-mspowerpoint.12',
	'ppz' => 'application/vnd.ms-powerpoint',
	'pre' => 'application/x-freelance',
	'prf' => 'application/pics-rules',
	'prt' => 'chemical/x-ncbi-asn1-ascii',
	'ps' => 'application/postscript',
	'psc1' => 'application/PowerShell',
	'psd' => 'image/x-photoshop',
	'pub' => 'application/vnd.ms-publisher',
	'pwz' => 'application/vnd.ms-powerpoint',
	'py' => 'text/plain',
	'pyc' => 'application/x-python-code',
	'pyo' => 'application/x-python-code',
	'pyw' => 'text/plain',
	'qgs' => 'application/x-qgis',
	'qt' => 'video/quicktime',
	'qtl' => 'application/x-quicktimeplayer',
	'ra' => 'audio/x-realaudio',
	'ram' => 'audio/x-pn-realaudio',
	'rar' => 'application/rar',
	'ras' => 'image/x-cmu-raster',
	'rat' => 'application/rat-file',
	'rb' => 'application/x-ruby',
	'rd' => 'chemical/x-mdl-rdfile',
	'rdf' => 'application/rdf+xml',
	'rels' => 'application/vnd.ms-package.relationships+xml',
	'resx' => 'application/xml',
	'rgb' => 'image/x-rgb',
	'rgs' => 'text/plain',
	'rhtml' => 'application/x-httpd-eruby',
	'rm' => 'audio/x-pn-realaudio',
	'rmi' => 'audio/mid',
	'roff' => 'application/x-troff',
	'ros' => 'chemical/x-rosdal',
	'rpm' => 'application/x-redhat-package-manager',
	'rqy' => 'text/x-ms-rqy',
	'rss' => 'application/rss+xml',
	'rtf' => 'application/msword',
	'rtx' => 'text/richtext',
	'rxn' => 'chemical/x-mdl-rxnfile',
	's' => 'text/plain',
	'sc2' => 'application/schdpl32',
	'scala' => 'text/x-scala',
	'scd' => 'application/schdpl32',
	'sch' => 'application/schdpl32',
	'scm' => 'application/x-lotusscreencam',
	'sct' => 'text/scriptlet',
	'sd' => 'chemical/x-mdl-sdfile',
	'sd2' => 'audio/x-sd2',
	'sda' => 'application/vnd.stardivision.draw',
	'sdc' => 'application/vnd.stardivision.calc',
	'sdd' => 'application/x-starimpress',
	'sdf' => 'chemical/x-mdl-sdfile',
	'sdp' => 'application/vnd.stardivision.impress-packed',
	'sds' => 'application/vnd.stardivision.chart',
	'sdw' => 'application/x-starwriter',
	'searchConnector-ms' => 'application/windows-search-connector+xml',
	'ser' => 'application/java-serialized-object',
	'set' => 'application/set',
	'setpay' => 'application/set-payment-initiation',
	'setreg' => 'application/set-registration-initiation',
	'settings' => 'application/xml',
	'sgf' => 'application/x-go-sgf',
	'sgl' => 'application/vnd.stardivision.writer-global',
	'sgm' => 'text/sgml',
	'sgml' => 'text/sgml',
	'sh' => 'text/x-sh',
	'shar' => 'application/x-shar',
	'shp' => 'application/x-qgis',
	'shtml' => 'text/html',
	'shx' => 'application/x-qgis',
	'sid' => 'audio/prs.sid',
	'sik' => 'application/x-trash',
	'silo' => 'model/mesh',
	'sis' => 'application/vnd.symbian.install',
	'sisx' => 'x-epoc/x-sisx-app',
	'sit' => 'application/x-stuffit',
	'sitx' => 'application/x-stuffit',
	'skd' => 'application/x-koan',
	'skm' => 'application/x-koan',
	'skp' => 'application/x-koan',
	'skt' => 'application/x-koan',
	'skype' => 'application/x-skype',
	'sldm' => 'application/vnd.ms-powerpoint.slide.macroEnabled.12',
	'sldx' => 'application/vnd.openxmlformats-officedocument.presentationml.slide',
	'slk' => 'application/vnd.ms-excel',
	'sln' => 'text/plain',
	'slupkg-ms' => 'application/x-ms-license',
	'smf' => 'application/vnd.stardivision.math',
	'smi' => 'application/smil',
	'smil' => 'application/smil',
	'snd' => 'audio/basic',
	'snp' => 'application/msaccess',
	'sol' => 'text/plain',
	'sor' => 'text/plain',
	'sparc' => 'application/x-sparc',
	'spc' => 'application/x-pkcs7-certificates',
	'spl' => 'application/futuresplash',
	'spx' => 'audio/ogg',
	'src' => 'application/x-wais-source',
	'srf' => 'text/plain',
	'sst' => 'application/vnd.ms-pki.certstore',
	'stc' => 'application/vnd.sun.xml.calc.template',
	'std' => 'application/vnd.sun.xml.draw.template',
	'step' => 'application/STEP',
	'sti' => 'application/vnd.sun.xml.impress.template',
	'stl' => 'application/vnd.ms-pki.stl',
	'stp' => 'application/STEP',
	'stw' => 'application/vnd.sun.xml.writer.template',
	'sty' => 'text/x-tex',
	'sv4cpio' => 'application/x-sv4cpio',
	'sv4crc' => 'application/x-sv4crc',
	'svg' => 'image/svg+xml',
	'svgz' => 'image/svg+xml',
	'sw' => 'chemical/x-swissprot',
	'swf' => 'application/x-shockwave-flash',
	'swfl' => 'application/x-shockwave-flash',
	'sxc' => 'application/vnd.sun.xml.calc',
	'sxd' => 'application/vnd.sun.xml.draw',
	'sxg' => 'application/vnd.sun.xml.writer.global',
	'sxi' => 'application/vnd.sun.xml.impress',
	'sxm' => 'application/vnd.sun.xml.math',
	'sxw' => 'application/vnd.sun.xml.writer',
	't' => 'application/x-troff',
	'tar' => 'application/x-tar',
	'tar.gz' => 'application/x-tar-gz',
	'taz' => 'application/x-gtar',
	'tcl' => 'text/x-tcl',
	'tex' => 'text/x-tex',
	'texi' => 'application/x-texinfo',
	'texinfo' => 'application/x-texinfo',
	'text' => 'text/plain',
	'tgf' => 'chemical/x-mdl-tgf',
	'tgz' => 'application/x-compressed',
	'thmx' => 'application/vnd.ms-officetheme',
	'tif' => 'image/tiff',
	'tiff' => 'image/tiff',
	'tk' => 'text/x-tcl',
	'tlh' => 'text/plain',
	'tli' => 'text/plain',
	'tm' => 'text/texmacs',
	'torrent' => 'application/x-bittorrent',
	'tr' => 'application/x-troff',
	'ts' => 'text/texmacs',
	'tsi' => 'audio/TSP-audio',
	'tsp' => 'application/dsptype',
	'tsv' => 'text/tab-separated-values',
	'tts' => 'video/vnd.dlna.mpeg-tts',
	'txt' => 'text/plain',
	'udeb' => 'application/x-debian-package',
	'uls' => 'text/iuls',
	'unv' => 'application/i-deas',
	'ustar' => 'application/x-ustar',
	'val' => 'chemical/x-ncbi-asn1-binary',
	'vcd' => 'application/x-cdlink',
	'vcf' => 'text/x-vcard',
	'vcproj' => 'Application/xml',
	'vcs' => 'text/x-vcalendar',
	'vda' => 'application/vda',
	'vdx' => 'application/vnd.ms-visio.viewer',
	'viv' => 'video/vnd.vivo',
	'vivo' => 'video/vnd.vivo',
	'vmd' => 'chemical/x-vmd',
	'vms' => 'chemical/x-vamas-iso14976',
	'vor' => 'application/vnd.stardivision.writer',
	'vrm' => 'x-world/x-vrml',
	'vrml' => 'x-world/x-vrml',
	'vscontent' => 'application/xml',
	'vsd' => 'application/vnd.ms-visio.viewer',
	'vsi' => 'application/ms-vsi',
	'vsl' => 'application/vnd.visio',
	'vss' => 'application/vnd.ms-visio.viewer',
	'vssettings' => 'text/xml',
	'vst' => 'application/vnd.ms-visio.viewer',
	'vstemplate' => 'text/xml',
	'vsu' => 'application/vnd.visio',
	'vsw' => 'application/vnd.visio',
	'vsx' => 'application/vnd.ms-visio.viewer',
	'vtx' => 'application/vnd.ms-visio.viewer',
	'wad' => 'application/x-doom',
	'wav' => 'audio/wav',
	'wax' => 'audio/x-ms-wax',
	'wbk' => 'application/msword',
	'wbmp' => 'image/vnd.wap.wbmp',
	'wbxml' => 'application/vnd.wap.wbxml',
	'wdp' => 'image/vnd.ms-photo',
	'wiz' => 'application/msword',
	'wk' => 'application/x-123',
	'wm' => 'video/x-ms-wm',
	'wma' => 'audio/x-ms-wma',
	'wmd' => 'application/x-ms-wmd',
	'wml' => 'text/vnd.wap.wml',
	'wmlc' => 'application/vnd.wap.wmlc',
	'wmls' => 'text/vnd.wap.wmlscript',
	'wmlsc' => 'application/vnd.wap.wmlscriptc',
	'wmv' => 'video/x-ms-wmv',
	'wmx' => 'video/x-ms-wmx',
	'wmz' => 'application/x-ms-wmz',
	'wp5' => 'application/vnd.wordperfect5.1',
	'wpd' => 'application/vnd.wordperfect',
	'wpl' => 'application/vnd.ms-wpl',
	'wrl' => 'x-world/x-vrml',
	'wsc' => 'text/scriptlet',
	'wsdl' => 'application/xml',
	'wvx' => 'video/x-ms-wvx',
	'wz' => 'application/x-wingz',
	'xaml' => 'application/xaml+xml',
	'xbap' => 'application/x-ms-xbap',
	'xbm' => 'image/x-xbitmap',
	'xcf' => 'application/x-xcf',
	'xdp' => 'application/vnd.adobe.xdp+xml',
	'xdr' => 'application/xml',
	'xfd' => 'application/vnd.adobe.xfd+xml',
	'xfdf' => 'application/vnd.adobe.xfdf',
	'xht' => 'application/xhtml+xml',
	'xhtml' => 'application/xhtml+xml',
	'xla' => 'application/vnd.ms-excel',
	'xlam' => 'application/vnd.ms-excel.addin.macroEnabled.12',
	'xlb' => 'application/vnd.ms-excel',
	'xlc' => 'application/vnd.ms-excel',
	'xld' => 'application/vnd.ms-excel',
	'xlk' => 'application/vnd.ms-excel',
	'xll' => 'application/vnd.ms-excel',
	'xlm' => 'application/vnd.ms-excel',
	'xls' => 'application/x-msexcel',
	'xlsb' => 'application/vnd.ms-excel.sheet.binary.macroEnabled.12',
	'xlsm' => 'application/vnd.ms-excel.sheet.macroEnabled.12',
	'xlsx' => 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet',
	'xlt' => 'application/vnd.ms-excel',
	'xltm' => 'application/vnd.ms-excel.template.macroEnabled.12',
	'xltx' => 'application/vnd.openxmlformats-officedocument.spreadsheetml.template',
	'xlv' => 'application/vnd.ms-excel',
	'xlw' => 'application/vnd.ms-excel',
	'xml' => 'text/xml',
	'xpi' => 'application/x-xpinstall',
	'xpm' => 'image/x-xpixmap',
	'xps' => 'application/vnd.ms-xpsdocument',
	'xrm-ms' => 'text/xml',
	'xsd' => 'application/xml',
	'xsl' => 'text/xml',
	'xslt' => 'application/xml',
	'xspf' => 'application/xspf+xml',
	'xtel' => 'chemical/x-xtel',
	'xul' => 'application/vnd.mozilla.xul+xml',
	'xwd' => 'image/x-xwindowdump',
	'xyz' => 'chemical/x-xyz',
	'z' => 'application/x-compress',
	'zip' => 'application/x-zip-compressed',
	'zmt' => 'chemical/x-mopac-input',
);
