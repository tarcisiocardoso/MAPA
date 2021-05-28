function checkBrowserVersion() {
	var firefoxMinVersion = 3;
	var chromeMinVersion = 9;
	var safariMinVersion = 3;
	var ieMinVersion = 8;
	var operaMinVersion = 5;
	
	var nAgt = navigator.userAgent;
	var fullVersion  = ''+parseFloat(navigator.appVersion); 
	var nameOffset,verOffset;
	var isOldVersion = false;
	
	// In Opera, the true version is after "Opera" or after "Version"
	if ((verOffset=nAgt.indexOf("Opera"))!=-1) {
		fullVersion = nAgt.substring(verOffset+6);
		if ((verOffset=nAgt.indexOf("Version"))!=-1) { 
			fullVersion = nAgt.substring(verOffset+8);
		}
		if (fullVersion < operaMinVersion) {
			isOldVersion = true;
		}
	}
	// In MSIE, the true version is after "MSIE" in userAgent
	else if ((verOffset=nAgt.indexOf("MSIE"))!=-1) {
		fullVersion = nAgt.substring(verOffset+5);
		if (fullVersion < ieMinVersion) {
			isOldVersion = true;
		}
	}
	// In Chrome, the true version is after "Chrome" 
	else if ((verOffset=nAgt.indexOf("Chrome"))!=-1) {
		fullVersion = nAgt.substring(verOffset+7);
		if (fullVersion < chromeMinVersion) {
			isOldVersion = true;
		}
	}
	// In Safari, the true version is after "Safari" or after "Version" 
	else if ((verOffset=nAgt.indexOf("Safari"))!=-1) {
		fullVersion = nAgt.substring(verOffset+7);
		if ((verOffset=nAgt.indexOf("Version"))!=-1) { 
			fullVersion = nAgt.substring(verOffset+8);
		}
		if (fullVersion < safariMinVersion) {
			isOldVersion = true;
		}
	}
	// In Firefox, the true version is after "Firefox" 
	else if ((verOffset=nAgt.indexOf("Firefox"))!=-1) {
		fullVersion = nAgt.substring(verOffset+8);
		fullVersion = trimFullVersion(fullVersion);
		if (fullVersion < firefoxMinVersion) {
			isOldVersion = true;
		}
	}
	// In most other browsers, "name/version" is at the end of userAgent 
	else if ( (nameOffset=nAgt.lastIndexOf(' ')+1) < (verOffset=nAgt.lastIndexOf('/')) ){
		var browserName = nAgt.substring(nameOffset,verOffset);
		fullVersion = nAgt.substring(verOffset+1);
		if (browserName.toLowerCase()==browserName.toUpperCase()) {
			browserName = navigator.appName;
		}
	}
	
	if (isOldVersion){
		$("#divVersaoBrowser").show("slow");
	}
	
	
	if (!isCertificacaoDigitalSuportada()) {
		$('#link-login-certificado-digital_ok').hide();
	}
}

function trimFullVersion (fullVersion) {
	// trim the fullVersion string at semicolon/space if present
	if ((ix=fullVersion.indexOf(";"))!=-1)
		fullVersion=fullVersion.substring(0,ix);
	if ((ix=fullVersion.indexOf(" "))!=-1)
		fullVersion=fullVersion.substring(0,ix);
	
	var majorVersion = parseInt(''+fullVersion,10);
	if (isNaN(majorVersion)) {
		fullVersion  = ''+parseFloat(navigator.appVersion); 
		majorVersion = parseInt(navigator.appVersion,10);
	}
	return fullVersion;
}

function isCertificacaoDigitalSuportada() {
	if (isChrome() || isIE()) {
		return true;
	}
	return false;
}

function isChrome() {
	var browser = navigator.version;
	var browserName = browser.split(' ')[0];
	return browserName == "Chrome";
}

function isIE() {
	var browser = navigator.version;
	var browserName = browser.split(' ')[0];
	return (parseInt(browserVersion) > 8 && (browserName == "MSIE" || browserName == "IE"));
}

navigator.version= (function(){
    var ua= navigator.userAgent, tem, 
    M= ua.match(/(opera|chrome|safari|firefox|msie|trident(?=\/))\/?\s*(\d+)/i) || [];
    if(/trident/i.test(M[1])){
        tem=  /\brv[ :]+(\d+)/g.exec(ua) || [];
        return 'IE '+(tem[1] || '');
    }
    if(M[1]=== 'Chrome'){
        tem= ua.match(/\b(OPR|Edge)\/(\d+)/);
        if(tem!= null) return tem.slice(1).join(' ').replace('OPR', 'Opera');
    }
    M= M[2]? [M[1], M[2]]: [navigator.appName, navigator.appVersion, '-?'];
    if((tem= ua.match(/version\/(\d+)/i))!= null) M.splice(1, 1, tem[1]);
    return M.join(' ');
})();






