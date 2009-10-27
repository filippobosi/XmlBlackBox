/*-----------------------------------------------------
  Copyright (c) 2008 McAfee, Inc.  All Rights Reserved.
  -----------------------------------------------------*/

var GOOGLE = 1;
var YAHOO = 2;
var MSN = 3;
var AOL = 4;
var ASK = 5;
var UOL = 6;
var GOO = 7;
var MYWAY = 8;
var BAIDU = 9;
var LIVE = 10;
var TERRA = 11;
var COMCAST = 12;
var WEB = 13;
var BIGLOBE = 14;
var SONET = 15;
var COX = 16;
var GMX = 17;
var EXCITE = 18;
var SKY = 19;
var NATE = 20;
var NAVER = 21;
var NIFTY = 22;
var req = null;
on_search_page = "(nav_href.match(/^http:\\/\\/w+\\.google\\.[a-z\\.]+\\/(search\\?|custom\\?|sponsoredlinks\\?|mac\\?|bsd\\?|linux\\?|microsoft\\?|unclesam\\?)/i) || nav_href.match(/^http:\\/\\/([a-z][a-z]\\.)?(www|search|myweb|myweb2\\.search|espanol\\.search)\\.yahoo\\.[a-z\\.]+\\/(search|custom)/i) || nav_href.match(/^http:\\/\\/(adcenter\\.)?search\\.(sympatico\\.)?(nine)?msn\\.[a-z\\.]+\\/(results)/i) || nav_href.match(/^http:\\/\\/(search|(www\\.)?recherche|busqueda|suche|buscador|aolsearch|(www|search)\\.jp)\\.aol\\.[a-z\.]+\\/(aol\\/search\\?|aol(com)?\\/search\\?|index\\.php\\?|search\\?|web\\?|web\\.adp\\?|rech\\?|advhandler\\.adp|suche\\/web\\/search\\.jsp\\?)/i) || nav_href.match(/^http:\\/\\/((www|search|fr|de|uk|it|nl|es)\\.)?ask\\.(com|jp)\\/web(\\.asp)?\\?/i) || nav_href.match(/^http:\\/\\/busca\\.uol\\.com\\.br\\/(www|uol|br)\\/index\\.html\\?/i)) != null";
var localescale = {"en-US":"1.0","en-CA":"1.0","en-GB":"1.0","en-AU":"1.0","en-IE":"1.0","es-ES":"1.3","es-MX":"1.3","es-AR":"1.3","es-CL":"1.3","es-PE":"1.3",
                   "fr-FR":"1.3","fr-CA":"1.3","pt-PT":"1.3","pt-BR":"1.3","zh-CN":"1.3","zh-TW":"1.3","cs-CZ":"1.3","de-DE":"1.3","it-IT":"1.3","ja-JP":"1.3",
		   "nl-NL":"1.3","nb-NO":"1.3","no-NO":"1.3","pl-PL":"1.3","da-DK":"1.3","fi-FI":"1.3","tr-TR":"1.3","sv-SE":"1.3","ko-KR":"1.3","el-EL":"1.3",
		   "hu-HU":"1.3","ru-RU":"1.3","sk-SK":"1.3"};

function validlink_google(link) {
	if (link.innerHTML.match(/(^Yahoo&nbsp;Finance$|^MSN&nbsp;Money$|^MarketWatch$|^CNN&nbsp;Money$|^Reuters$)/)) return null;
	if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.id && link.parentNode.parentNode.id.match(/gbar/)) return null;	
	if(link.innerHTML.match(/Kurs fýr/) && link.parentNode.className=="e" && link.parentNode.tagName=="P")
		return null;
	if(link.parentNode.tagName=="FONT" && link.parentNode.className=="a")
		return null;
	if (link.id && link.id.match(/^fbql/)) return null;
	
	function checkMouseover() {
		if (!link.id) return null;
		if (link.id.match(/^a[wn]/)) {
			linkTrav = link;
			while (linkTrav.nextSibling && linkTrav.nextSibling.tagName != "A") {
				linkTrav = linkTrav.nextSibling;
				if (linkTrav.tagName == "FONT") return linkTrav.textContent;
			}
			if (link.href.match(/\/aclk\?/) && link.href.match(/\.google\./))
			{
				/*var tNode = link.parentNode;
				while(tNode)
				{
					if(tNode.tagName=="SPAN" && tNode.className && tNode.className=="a")
						return tNode.textContent;
					tNode = tNode.nextSibling;
				}*/
				href = link.href.slice(link.href.indexOf("q=")+2);
				var idx = href.indexOf("&sig=");
				if (idx != -1)
					href = href.substring(0,idx);
				var argsPos = href.indexOf("%3F");
				if(argsPos>0)
					href = href.slice(0,argsPos);
				return href;
			}			
			return null;
		} else if (link.id.match(/^pa/)) {
			try
			{
				var href = link.href.slice(link.href.indexOf("q=")+2);
				var argsPos = href.indexOf("%3F");
				if(argsPos>0)
					href = href.slice(0,argsPos);
				return href;
				/*var idx = href.indexOf("&sig=");
				if (idx != -1)
				{
					href = href.substring(0,idx);
					return href;
				}
				else
				{		
					spans = link.parentNode.getElementsByTagName("SPAN");
					for (var s = 0; s < spans.length; s++)
						if (spans[s].getAttribute("class") && spans[s].getAttribute("class") == "a")
							return spans[s].textContent;
				}
				return null;*/
			}
			catch(e)
			{
			}
		} else
			return null;
	}
	href = checkMouseover();
	if (href) return href;
	href = unescape(link.href);
    if (href == null || href.length < 4 || !href.match(/^http/) || href.match(/oi=dict/) || href.match(/oi=map/) || href.match(/oi=smap/) || href.match(/oi=stock/) || href.match(/oi=showtimes/) || (href.match(/oi=prbx/) && !href.match(/ct=title/)) || (href.match(/http:\/\/www\.chillingeffects\.org\/(dmca512\/)?notice\.cgi\?/) && !link.className)  || href.match(/\?q=cache:/) || href.match(/127\.0\.0\.1/) || href.match(/localhost/) || href.indexOf("//") == -1)
		return null;
	if(link.className && link.className.match(/fl/) && link.parentNode.tagName=="FONT") return null;
	var noprotocol = href.slice(href.indexOf("//")+2);
	var hrefpath = "/";
	firstslash = noprotocol.indexOf("/");
	if (firstslash != -1) hrefpath = noprotocol.slice(firstslash);
	if (hrefpath.match(/^\/pagead\/iclk\?/) && href.match(/google/)) {
		href = href.slice(href.indexOf("adurl=")+6);
		var idx = href.indexOf("&");
		if (idx != -1)
			href = href.substring(0,idx);
		return href;
	}
	if (hrefpath.match(/^\/url\?/) && href.match(/google/)) {
		href = href.slice(href.indexOf("q=")+2);
		var idx = href.indexOf("&");
		if (idx != -1)
			href = href.substring(0,idx);
		return href;
	}
	if (hrefpath.match(/^\/interstitial\?/) && href.match(/google/)) {
                var urlmatch = href.match(/[\?&]url=([^&]*)/);
                if (urlmatch) return urlmatch[1];
        }
	return href;
}

function validlink_yahoo(link) {
	function ct(ctr,cte) {
		while(cte) {
			if (ctr == cte) return true;
			cte = cte.parentNode;
		}
		return false;
	}
	if(link.ownerDocument.location.host.match(/kr\.search\.yahoo\.com/) && link.parentNode && link.parentNode.tagName=="SPAN")
			return null;
	if(link.ownerDocument.location.host.match(/hk\.search\.yahoo\.com/) && link.parentNode && link.parentNode.className && (link.parentNode.className=="linkblock" || link.parentNode.className=="eysm"))
		return null;
	if(link.parentNode && link.parentNode.className && link.parentNode.className=="active")
		return null;
	if(link.parentNode && link.parentNode.className && link.parentNode.className.match(/ext/))
		return null;
	if(link.ownerDocument.location.href.match(/http:\/\/(us\.)?search\.yahoo\.com/) && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/ql/))
		return null;
	if(link.ownerDocument.location.href.match(/http:\/\/(us\.)?search\.yahoo\.com/) && link.className && link.className.match(/pp/))
		return null;
	try
	{
		if(link.firstChild && link.firstChild.hasAttribute && link.firstChild.getAttribute("title") && link.firstChild.getAttribute("title").match(/PayPal/))
		{
			return null;
		}
		if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "I" && link.parentNode.hasAttribute && link.parentNode.getAttribute("title") && link.parentNode.getAttribute("title").match(/PayPal/))
		{
			return null;
		}		
	}
	catch(e)
	{
	}
	if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/yschmsgz/))
		return null;
	if(link.parentNode && link.parentNode.className && link.parentNode.className.match(/yschactive/))
		return null;
		
	if (link.ownerDocument.location.href.match(/(search\.cn\.yahoo\.com)|(search\.p4p\.yahoo\.com\.cn)/))
	{
		if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className.match(/g|by2/))
			return null;
		if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="H3")
		{
			if(link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/p4p/))
			{
				var allEM = link.parentNode.getElementsByTagName("EM");
				return allEM.item(0).textContent.split(/\s/)[0];
			}
			else
			{
				return link.href;
			}
		}
		if(link.className && link.className.match(/pa/))
		{
			var allDIV = link.getElementsByTagName("DIV");
			return allDIV.item(1).textContent;
		}
		if(link.className && link.className.match(/st1/))
		{
			var allTD = link.parentNode.parentNode.parentNode.getElementsByTagName("TD");
			for(var i=0;i<allTD.length;i++)
			{
				if(allTD.item(i).className && allTD.item(i).className.match(/copy/))
				{
					return allTD.item(i).textContent;
				}
			}
		}
	}
	if(link.ownerDocument.location.host.match(/au\.search\.yahoo\.com/) && link.className=="yschttl")
	{
		var traverseNode=link.parentNode;
		traverseNode=traverseNode.nextSibling;
		while(traverseNode)
		{
			if(traverseNode.tagName=="SPAN" && traverseNode.className=="yschurl")
				return traverseNode.textContent;
			traverseNode=traverseNode.nextSibling;
		}
	}
	if (ct(link.ownerDocument.getElementById("ygma"),link)) return null;
	if(link.ownerDocument.getElementById("yschweb"))
	{
		if(link.parentNode && link.parentNode.tagName=="DIV")
		{
			if(link.parentNode.parentNode && link.parentNode.parentNode.tagName=="LI")
			{
				if(link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.tagName=="UL")
				{
					if(link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.tagName=="DIV" && link.parentNode.parentNode.parentNode.parentNode.hasAttribute && link.parentNode.parentNode.parentNode.parentNode.getAttribute("class")=="yschspns")
					{
						var traverseNode=link
						while(traverseNode)
						{
							if(traverseNode.tagName=="EM")
								return traverseNode.textContent
							traverseNode=traverseNode.nextSibling
						}
					}
				}
			}
		}
		if(link.parentNode && link.parentNode.tagName=="LI")
		{
			if(link.parentNode.parentNode && link.parentNode.parentNode.tagName=="UL")
			{
				if(link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.tagName=="DIV" && link.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.id=="yschsec")
				{
					var traverseNode=link
					while(traverseNode)
					{
						if(traverseNode.tagName=="EM")
							return traverseNode.textContent
						traverseNode=traverseNode.nextSibling
					}
				}
			}
		}
	}
	
	if(link.ownerDocument.location.href.match(/\/\/((in|us)\.)?search\.yahoo\.com/))
	{
		if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.id.match(/sec|east/))
		{
			try
			{
				var tNode = link.nextSibling.nextSibling;
				var urlStr = tNode.innerHTML.split(/\<[/a-zA-Z]+\>/);
				var url = "";
				if(urlStr)
				{
					for(var i=0;i<urlStr.length;i++)
					{
						url = url + urlStr[i];
					}
				}
				return url;
			}
			catch(e)
			{
			}
		}
		else
		{
			if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.parentNode.parentNode.id.match(/sec|east/))
			{
				try
				{
					var tNode = link.parentNode.parentNode.nextSibling.nextSibling;
					var urlStr = tNode.innerHTML.split(/\<[/a-zA-Z]+\>/);
					var url = "";
					if(urlStr)
					{
						for(var i=0;i<urlStr.length;i++)
						{
							url = url + urlStr[i];
						}
					}
					return url;
				}
				catch(e)
				{
				}
			}
			else
			{
				if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/spns/))
				{
					try
					{
						var allEM = link.parentNode.getElementsByTagName("EM");
						var tNode = allEM.item(0);
						var urlStr = tNode.innerHTML.split(/\<[/a-zA-Z]+\>/);
						var url = "";
						if(urlStr)
						{
							for(var i=0;i<urlStr.length;i++)
							{
								url = url + urlStr[i];
							}
						}
						return url;
					}
					catch(e)
					{
					}
				}
				else
				{
					if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.parentNode.className.match(/spns/))
					{
						try
						{
							var allEM = link.parentNode.parentNode.getElementsByTagName("EM");
							var tNode = allEM.item(0);
							var urlStr = tNode.innerHTML.split(/\<[/a-zA-Z]+\>/);
							var url = "";
							if(urlStr)
							{
								for(var i=0;i<urlStr.length;i++)
								{
									url = url + urlStr[i];
								}
							}
							return url;
						}
						catch(e)
						{
						}
					}
				}
			}
		}
	}	
	if(link.ownerDocument.location.href.match(/http:\/\/(us\.)?search\.yahoo\.com/) && link.className && link.className.match(/yschttl/))
	{
		try
		{
			if(link.href.match(/(yahoo\.com\/click)|(http:\/\/rds\.yahoo\.com)/) && !(link.id && link.id.match(/oMvLnk/)))
			{
				var allSpan = link.parentNode.parentNode.parentNode.getElementsByTagName("SPAN");
				return allSpan[0].textContent;
			}
			else
			{
				return link.href;
			}
		}
		catch(e)
		{
		}
	}
	if (ct(link.ownerDocument.getElementById("yschweb"),link)) {
		if (link.getAttribute("class")) {
			if (!link.className.match(/yschttl/))
				return null;
		} else return null;
	}
	if (link.parentNode && link.parentNode.getAttribute("class") && link.parentNode.getAttribute("class") == "yschprom") return null;
	if (link.id) {
            var idhref = link.id.match(/^http:\/\/.*/);
            if (idhref) return idhref[0];
        }
        href = unescape(link.href);
	if (href.match(/\.pdf#search/) || href.match(/\.pdf$/))
	{
		var regpos = href.indexOf("**");
		if (regpos != -1)
		{
			href = href.slice(regpos+2);
			if (href.match(/\.yahoo\./) && href.match(/click\?u=/))
			{
				href = href.slice(href.indexOf("click?u=")+8);
				return href;
			}
			else return href;
		}
		else
			return href;	
	}
	if (href.length < 4 || !href.match("^http") || href.match(/cache\?/) || !href.match(/\*[\*-]/))
		return null;
	var adpos = href.indexOf("&yargs=");
	if (adpos != -1)
		return href.slice(adpos+7);
	var regpos = href.indexOf("**");
	if (regpos == -1) regpos = href.indexOf("*-");
	if (regpos != -1) {
		href = 	href.slice(regpos+2);
		var secRegPos = href.slice(4).indexOf("http");
		if(secRegPos!=-1 && !(href.match(/\.yahoo\./) && href.match(/click\?u=/)))
			href = href.slice(0,secRegPos+4);		
		if (href.match(/\.yahoo\./) && href.match(/click\?u=/)) {
			href = href.slice(href.indexOf("click?u=")+8);
			return href;
		}
		else return href;
	}
	return null;
}

function validlink_msn(link) {
	if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className=="image_container") return null;
	var href = unescape(link.href);
	if (href.length < 4 || !href.match(/^http/) || href.match(/\.msnscache\./) || href.match(/(\.live\.com|http:\/\/live\.com)/) || href.match(/pointdecontact\.net\/contact\.asp/) || (link.ownerDocument.location.href.match(/^http:\/\/search\.msn\.(com|co\.(in|uk|jp)|fr)\/results\.aspx|^http:\/\/search\.live\.com\/results\.aspx\?(q=.*&)?FORM=MSNH/i) && ((link.parentNode && link.parentNode.tagName.match(/DIV|LI/) && link.parentNode.parentNode && link.parentNode.parentNode.id && link.parentNode.parentNode.id.match(/ar|sw_footL|sw_footR/)) || (link.parentNode && link.parentNode.tagName.match(/DIV/) && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className.match(/sb_adsN/)))))
		return null;
	if (link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "LI" && link.parentNode.parentNode && link.parentNode.parentNode.tagName && link.parentNode.parentNode.tagName == "UL" && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.tagName && link.parentNode.parentNode.parentNode.tagName == "LI")
		return null;		
	if(link.parentNode && link.parentNode.className && link.parentNode.className=="itm")
		return null;
	if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.id && link.parentNode.parentNode.id.match(/ads_rightC/))
		return null;
	if(link.parentNode && link.parentNode.className && link.parentNode.className=="mt")
		return null;		
	if(link.parentNode && link.parentNode.tagName == "P" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/sb_results/))
		return null;
	if (href.match(/g\.msn\./)) {
		var idx = href.slice(1).indexOf("http://");
		if (idx != -1)
			href = href.slice(idx+1);
		var adpos = href.indexOf("&yargs=");
		if (adpos != -1)
			return href.slice(adpos+7);
	}
	if (href.match(/act2\.aspx/) || href.match(/\.r\.msn\./) || href.match(/ads\.msn\./) || (href.match(/overture\.com/) && href.match(/yargs=/)) || href.match(/triclick\.em\.vip\./)) {
		if (link.nextSibling) {
			href = link.nextSibling.innerHTML;
			sp_idx = href.lastIndexOf(" ");
			if (sp_idx != -1)
				href = href.slice(sp_idx+1);
		} else {
			try
			{
				if(link.ownerDocument.location.href.match(/^http:\/\/search\.msn\.(com|co\.(in|uk|jp)|fr)\/results\.aspx/) || link.ownerDocument.location.href.match(/^http:\/\/search\.live\.com\/results\.aspx\?(q=.*&)?FORM=MSNH/i))
				{
					try
					{
						if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/sb_adsW/))
						{
							var citeTag = link.getElementsByTagName("CITE");
							return citeTag[0].textContent.slice(3);
						}
						if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/sb_adsN/))
						{
							var citeTag = link.getElementsByTagName("CITE");
							return citeTag[0].textContent;
						}
					}
					catch(e)
					{
					}
				}
				else
				{
					href = link.parentNode.nextSibling.nextSibling.innerHTML;
					sp_idx = href.lastIndexOf(" ");
					if (sp_idx != -1)
						href = href.slice(sp_idx+1);
				}
			}
			catch(e)
			{
			}
		}
	}
	return href;
}

function validlink_aol(link) {
    if (!link.href) return null;
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="P" && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className=="fr")
		return null;
	if(link.parentNode && link.parentNode.className && link.parentNode.className.match(/resc webscr/))
		return null;		
	if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className.match(/site/))
		return null;
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "TD" && link.parentNode.className && link.parentNode.className.match(/bbm/))
		return null;
	if(link.id.match(/redirLink/))
	{
		if(link.ownerDocument.location.href.match(/(search\.aol\.(com|in))|(suche\.aol\.de)/))
		{
			var traverseNode=link.firstChild;
			traverseNode=traverseNode.nextSibling
			while(traverseNode)
			{
				if(traverseNode.hasAttribute && traverseNode.getAttribute("class") && traverseNode.getAttribute("class").match(/durl/))
					return traverseNode.textContent
				traverseNode=traverseNode.nextSibling;
			}
		}
		if(link.ownerDocument.location.href.match(/search\.aol\.co\.uk/))
		{
			if(link.parentNode && link.parentNode.className && link.parentNode.className=="title")
			{
				var traverseNode = link.parentNode;
				while(traverseNode)
				{
					if(traverseNode && traverseNode.className && traverseNode.className=="durl")
						return traverseNode.firstChild.textContent;
					traverseNode=traverseNode.nextSibling;
				}
			}
		}		
	}
	if(link.ownerDocument.location.href.match(/zoek\.aol\.nl/) && link.id.match(/title\.link/))
		return link.href;
	if (link.parentNode && link.parentNode.getAttribute("class") && link.parentNode.getAttribute("class").match(/^(grnlnk|sponsoredLinks|smallLink small|lienurl)$/)) return null;
	if (link.href.match(/adwords\.google\.com\.au/)) return null;
	if (link.ownerDocument.location.host.match(/aol\.de/) && link.getAttribute("onmouseover"))
	{
		var m = link.getAttribute("onmouseover").match(/window\.status\s*=\s*'(.*)';/);
		if (m) return m[1];
	}
	if(link.ownerDocument.location.href.match(/aol\.com\.mx/))
	{
		if(link.className && link.className.match(/URLlink/))
		{
			var unesHref = unescape(link.href);
			uInd = unesHref.indexOf("urn=");
			end = unesHref.indexOf("&source=");
			return unesHref.slice(uInd + 4 , end);
		}		
	}	
	if(link.ownerDocument.location.host.match(/aol\.(fr|com)/))
	{
		if(link.id && link.id.match(/redirLink/) && link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="SPAN")
		{
			var unesHref = unescape(link.href);
			var uInd = unesHref.indexOf("url%3D");
			if(uInd==-1) 
				uInd = unesHref.indexOf("url=");			
			var end = unesHref.indexOf("usg");
			if(uInd!=-1)
			{
				var allLinks = link.parentNode.parentNode.getElementsByTagName("A");
				return allLinks[2].textContent;
			}
			else
			{
				uInd = unesHref.indexOf("&q=");
				end = unesHref.indexOf("&usg=");
				return unesHref.slice(uInd + 3 , end);
			}
		}
		if(link.className && link.className.match(/find/))
		{
			var unesHref = unescape(link.href);
			uInd = unesHref.indexOf("&s_cu=");			end = unesHref.indexOf("&s_cd=");			return unesHref.slice(uInd + 6 , end);		}
	}
    if (link.ownerDocument.location.host.match(/aol\.ca/) && link.nextSibling && link.nextSibling.nextSibling && link.nextSibling.nextSibling.tagName && link.nextSibling.nextSibling.tagName == "DIV" && link.nextSibling.nextSibling.getAttribute("class")&& link.nextSibling.nextSibling.getAttribute("class") == "resURL") return link.nextSibling.nextSibling.textContent;
    if (link.ownerDocument.location.host.match(/aol\.(com\.au|fr)/)) return validlink_google(link);
	var href = unescape(link.href);
	var srclen = 4;
	var srcidx = href.indexOf("src=");
	if (srcidx == -1) {srcidx = href.indexOf("source="); srclen=7;}
	if (srcidx == -1) return null;
	var srcend = href.slice(srcidx).indexOf("&");
	if (srcend == -1) srcend = href.slice(srcidx).length;
	var src = href.substring(srcidx+srclen,srcend+srcidx);
	if (src == "websearch" || src=="google" || src=="AstWebSearch" || src=="eu_websearch" || src=="afe"|| (src=="content_module" && link.parentNode && link.parentNode.tagName=="H3")) {
		var urnlen = 15;
		var itemidx = href.indexOf("clickedItemURN=");
		if (itemidx == -1) {itemidx = href.indexOf("urn="); urnlen = 4;}
		if (itemidx == -1) return null;
		var endidx = href.slice(itemidx).indexOf("&");
		if (endidx == -1) endidx = href.slice(itemidx).length;
		var item = href.substring(itemidx+urnlen,endidx+itemidx);
		return item;
	} else if (src == "sponsored" || src == "dmn" || src=="Google")  {
		href = unescape(href);
                var itemidx = -1;
                var itemlen = 0;
                if (href.match(/\/pagead\/iclk\?/) && link.ownerDocument.location.host != "search.aol.com") {itemidx = href.indexOf("adurl="); itemlen = 6;}
                else if (href.match(/\/url\?/) && link.ownerDocument.location.host != "search.aol.com") {itemidx = href.indexOf("q="); itemlen = 2;}
		if (itemidx == -1) {
			if (!link.parentNode) return null;
			btags = link.parentNode.getElementsByTagName("b");
			if (btags.length < 1) return null;
			try{ return btags.item(btags.length-1).firstChild.nodeValue; }
			catch (perr) {return null;}
		}
		endidx = href.slice(itemidx).indexOf("&");
		if (endidx == -1) endidx = href.length;
		else endidx = endidx+itemidx;
		return href.substring(itemidx+itemlen,endidx);
	} else if (src.match(/GoogleSL/)) {
		if (link.parentNode && link.parentNode.parentNode) {
			var sldivs = link.parentNode.parentNode.getElementsByTagName("DIV");
			if (sldivs.length == 3) {
				if (sldivs[2].firstChild) return sldivs[2].firstChild.textContent.replace(/^\s*|\s*$/g, "");
			} else return null;
		} else return null;
	} else return null;
}

function validlink_ask(link) {
	function checkMouseover() {
		if (!link.getAttribute("onmouseover")) return null;
		var mouseovertext = link.getAttribute("onmouseover").toString().toLowerCase();
		var gototxt = mouseovertext.indexOf('"');
		if (gototxt == -1) gototxt = mouseovertext.indexOf("'");
		if (gototxt == -1) return null;
		mouseovertext = mouseovertext.slice(gototxt+1);
		var endq = mouseovertext.indexOf('"');
		if (endq == -1) endq = mouseovertext.indexOf("'");
		if (endq == -1) return null;
		var res = mouseovertext.substring(0,endq);
		if (res.match(/\[/) && res.match(/\]/)) res = res.substring(res.indexOf("[")+1,res.indexOf("]")).replace(/^\s*|\s*$/g, "");
		if (!res.match(/\./)) return null;
		return res;
	}
	if (!link.href) return null;
	if (!link.textContent.match(/[^\s]/)) return null;
        if (link.getAttribute("class") && link.getAttribute("class") == "ul") return null;
        if (link.parentNode && link.parentNode.getAttribute("id") && link.parentNode.getAttribute("id") == "menu") return null;
	if(link.ownerDocument.location.host.match(/ask\.jp/) && link.parentNode && link.parentNode.tagName=="SPAN")
	{
		var traverseNode=link.parentNode;
		traverseNode=traverseNode.nextSibling;
		while(traverseNode)
		{
			if(traverseNode.id && traverseNode.id.match(/resultAbstract/))
			{
				var tNode = traverseNode.firstChild
				while(tNode)
				{
					if(tNode.className && tNode.id && tNode.className=="url_c" && tNode.id.match(/resultBottom/))
						return tNode.firstChild.textContent;
					tNode=tNode.nextSibling;
				}
			}
			traverseNode=traverseNode.nextSibling;
		}
	}
	href = checkMouseover(link);
	if (href) return href;
	href = unescape(link.href);
	var id = link.getAttribute("id");
	if (id && id.charAt(0) == "r" && id.substring(id.length-2,id.length) == "_t")
		return href
	if(link.className && link.className=="L4 b")
		return link.href
	if(link.className && link.className=="L2")
		return null;
	return null;
}

function validlink_uol(link) {
	if (link.parentNode.tagName == "DD" && link.parentNode.className && link.parentNode.className == "prod_title")
	{
		var href = link.getAttribute("rel").toString();
		if (href)
			return href;
	}
	if (link.parentNode.tagName != "DT") return null;
	var traverseNode=link.parentNode;
	traverseNode=traverseNode.nextSibling;
	while(traverseNode)
	{
		if(traverseNode.tagName=="DD" && traverseNode.className && traverseNode.className=="link linklp")
		{
			return traverseNode.firstChild.textContent;
		}
		if(traverseNode.tagName=="DD" && traverseNode.className && traverseNode.className=="link linkuol")
			return null;
		if(traverseNode.tagName=="DD" && traverseNode.className && traverseNode.className=="link")
		{
			try
			{		
				if(traverseNode.firstChild.firstChild.tagName=="SPAN")
					return traverseNode.firstChild.firstChild.nextSibling.textContent;
				else
					return traverseNode.firstChild.firstChild.textContent;
			}
			catch(e)
			{
			}
		}
		traverseNode=traverseNode.nextSibling;
	}
	return null;
}

function validlink_goo(link){
        var clsName;
        clsName=null;
		if(link.id && link.id.match(/a[0-9]+/))
			return link.href;
		if(link.parentNode && link.parentNode.tagName=="DIV" && link.nextSibling && link.nextSibling.nextSibling && link.nextSibling.nextSibling.tagName=="FONT")
			return link.href;
        if(link.parentNode.hasAttribute)
            clsName=link.parentNode.className
		if(clsName && clsName=="title")
		{
			if(link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.id=="search-results")
			{
				var urlMatch=link.href.match(/[\?&]DEST=([^&]*)/)
				if(urlMatch)
					return unescape(urlMatch[1])
			}
			if(link.parentNode.parentNode.parentNode.parentNode && (link.parentNode.parentNode.parentNode.parentNode.id=="sponsor" || link.parentNode.parentNode.parentNode.parentNode.className=="sponsor"))
			{
				var traverseNode=link.parentNode;
				while(traverseNode)
				{
					if(traverseNode.className=="url")
					{
						var tempNode=traverseNode.getElementsByTagName('a')
						return tempNode[0].textContent
					}
					traverseNode=traverseNode.nextSibling;
				}
			}
		}
        return null
}

function validlink_myway(link) {
	if(link.ownerDocument.location.href.match(/AJ(main|web|advr)\.jsp/) && link.href.match(/tmsyn\.wc\.ask\.com/) && link.parentNode.tagName=="TD")
	{
			var traverseNode=link.nextSibling;
            var clr;
			while(traverseNode)
			{
				clr=null;
				if(traverseNode.hasAttribute)
				clr=traverseNode.getAttribute('color')
				if(clr && clr=="#008000")
						return traverseNode.textContent;
				traverseNode=traverseNode.nextSibling;
			}
	}
	if(link.ownerDocument.location.pathname.match(/(AJ|GG|AW|LS)(main|web|advr)\.jsp/) && link.protocol=="http:" && !link.hostname.match(/^www.\askcareers\.com$/))
	{
		if(link.href.match(/redir\.jsp\?/) && !link.href.match(/email_redir\.jsp/))
		{
			if(link.style && link.style.cssText && link.style.cssText.match(/text-decoration\s*:\s*none/i))
				return null
			var traverseNode=link.nextSibling;
			var movertext;
			var intext;
			var clr;
			while(traverseNode)
			{
				clr=null;
				if(traverseNode.childNodes.length > 0)
					clr=traverseNode.childNodes[0].getAttribute("color")
				if(clr && clr=="#008000")
					return traverseNode.textContent;
				traverseNode=traverseNode.nextSibling;
			}
		}
		return link.href;
	}
	if(!link.id)
		return null
	if(link.ownerDocument.location.pathname.match(/GGadvr\.jsp$/) && link.protocol=="http:" && !link.hostname.match(/^www.\askcareers\.com$/))
		return link.href;
	if(link.id.match(/^rst/) || link.id.match(/^rsb/) || link.id.match(/^rw/))
	{
		if(link.id.match(/^rs(t|b)/))
			var traverseNode=link.firstChild.firstChild.nextSibling;
		else
			var traverseNode=link.nextSibling;
		var clsName;
		while(traverseNode)
		{
			if(traverseNode.id && traverseNode.id=="link")
				return null;
			clsName=null;
			if(traverseNode.hasAttribute)
				clsName=traverseNode.getAttribute("class");
			if(clsName && clsName=="uri")
				return traverseNode.textContent;
			traverseNode=traverseNode.nextSibling;
		}
	}
	return null;
}

function validlink_baidu(link)
{
	var clsName;
	var traverseNode;
	if(link.parentNode)
	{
		if(link.parentNode.tagName=="DIV")
		{
			clsName=null;
			if(link.parentNode.hasAttribute)
				clsName=link.parentNode.className;
			if(clsName && clsName=="r")
			{
				traverseNode=link.nextSibling;
				while(traverseNode)
				{
					if(traverseNode.tagName=="SPAN" && traverseNode.id.match(/bdfs/))
					{
						var tempNode=traverseNode.firstChild;
						var clrName;
						while(tempNode)
						{
							if(tempNode.tagName=="FONT")
							{
								if(tempNode.hasAttribute)
									clrName=tempNode.getAttribute("color")
								if(clrName && clrName=="#008000")
									return tempNode.textContent
							}
							tempNode=tempNode.nextSibling;
						}
					}
					traverseNode=traverseNode.nextSibling
				}
			}
		}
		if(link.parentNode.tagName=="TD")
		{
			clsName=null;
			if(link.parentNode.hasAttribute)
				clsName=link.parentNode.className
			if(clsName && clsName=="f")
			{
				traverseNode=link.nextSibling;
				while(traverseNode)
				{
					if(traverseNode.tagName=="FONT")
					{
						var tempNode=traverseNode.firstChild;
						var clrName;
						while(tempNode)
						{
							if(tempNode.tagName=="FONT")
							{
								if(tempNode.hasAttribute)
									clrName=tempNode.getAttribute("color")
								if(clrName && clrName=="#008000")
									return tempNode.textContent
							}
							tempNode=tempNode.nextSibling;
						}
					}
					traverseNode=traverseNode.nextSibling
				}
			}
		}
		if(link.id && link.id.match(/aw/))
		{
			clsName=null;
			if(link.parentNode.getAttribute)
					clsName=link.parentNode.className
			if(clsName && clsName=="f")
			{
				traverseNode=link.nextSibling;
				while(traverseNode)
				{
					if(traverseNode.tagName=="FONT" && traverseNode.getAttribute("color") && traverseNode.getAttribute("color") == "#008000")
					{
						var lIdx;
						lIdx=traverseNode.textContent.indexOf(" ");
						return traverseNode.textContent.slice(0,lIdx);
					}
					traverseNode=traverseNode.nextSibling;
				}
			}
		}		
		return null;
	}
	return null;
}
function validlink_live(link)
{
	var traverseNode;
	var tempNode;
	var clsName;
	if(link.parentNode && link.parentNode.tagName=="H3")
	{
		if(link.parentNode.parentNode && link.parentNode.parentNode.tagName=="LI")
		{
			if(link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.tagName=="UL")
			{
				if(link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/sb_results/))
				{
					return link.href;
				}
				if(link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.tagName=="DIV")
				{
					if(link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/answer_news_body/))
					{
						return link.href;
					}					
				}
			}
		}
	}
	if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/sb_adsN/))
	{
		try
		{
			var citeTag = link.getElementsByTagName("CITE");
			return citeTag[0].textContent;
		}
		catch(e)
		{
		}
	}
	if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/sb_adsW/))
	{
		try
		{
			return link.getElementsByTagName("CITE")[0].textContent.slice(3);
			
		}
		catch(e)
		{
		}
	}
	return null;
}

function validlink_terra(link)
{
	if(link.parentNode && link.parentNode.tagName=="STRONG")
	{
		if(link.parentNode.parentNode && link.parentNode.parentNode.tagName=="LI")
		{
			if(link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.tagName=="UL")
			{
				var traverseNode=link.parentNode;
				traverseNode=traverseNode.nextSibling;
				var inText;
				var href;
				var ind;
				while(traverseNode)
				{
					inText=null;
					href=null;
					inText=traverseNode.textContent;
					if(inText)
					{
						inText=unescape(inText)
						inText=inText.toLowerCase();
						var indSlash=inText.lastIndexOf("/")
						if(indSlash>0)
							inText=inText.slice(0,indSlash);
						if(traverseNode.hasAttribute && traverseNode.getAttribute("href"))
						{
							href=traverseNode.getAttribute("href")
							href=unescape(href)
							href=href.toLowerCase();
							orighref=href
							var indSlash=href.lastIndexOf("/")
							if(indSlash>0)
								href=href.slice(0,indSlash);
							if(href.indexOf(inText)>=0)
							{
								return inText;
							}
							if(href.match("adurl"))
							{
								indSlash=href.indexOf("adurl")
								return href.slice(indSlash+6)
							}
							if(href.match("q="))
							{
								indSlash=href.indexOf("q=")
								if (href.slice(indSlash+2)=="http:/")
									return orighref.slice(indSlash+2)
								else
									return href.slice(indSlash+2)
							}
						}
					}
					traverseNode=traverseNode.nextSibling;
				}
			}
		}
	}
	return null;
}

function validlink_comcast(link)
{
	if (link.className && link.className.match(/adresults/))
	{
		var movertext = link.getAttribute("onmouseover").toString();
		if (movertext)
		{
			var m = movertext.match (/window\.status='((http:\/\/)?[^']+)'/i)
			return m[1].toString();
		}
	}
	if (link.className && link.className.match(/adl1/)&& link.parentNode && link.parentNode.nextSibling && link.parentNode.nextSibling.nextSibling && link.parentNode.nextSibling.nextSibling.tagName=="DD" && link.parentNode.nextSibling.nextSibling.firstChild && link.parentNode.nextSibling.nextSibling.firstChild.nextSibling && link.parentNode.nextSibling.nextSibling.firstChild.nextSibling.firstChild && link.parentNode.nextSibling.nextSibling.firstChild.nextSibling.firstChild.nextSibling && link.parentNode.nextSibling.nextSibling.firstChild.nextSibling.firstChild.nextSibling.nextSibling && link.parentNode.nextSibling.nextSibling.firstChild.nextSibling.firstChild.nextSibling.nextSibling.nextSibling && link.parentNode.nextSibling.nextSibling.firstChild.nextSibling.firstChild.nextSibling.nextSibling.nextSibling.nextSibling )
	{
		return (link.parentNode.nextSibling.nextSibling.firstChild.nextSibling.firstChild.nextSibling.nextSibling.nextSibling.nextSibling.textContent);
	}
	if(link.parentNode.tagName=="DT" && link.parentNode.className=="title")
	{
		var traverseNode=link.parentNode;
		while(traverseNode)
		{
			if(traverseNode.tagName=="DD" && traverseNode.className=="source")
			{
				var slashIdx;
				slashIdx=traverseNode.textContent.lastIndexOf("/")
				if(slashIdx!=-1)
					return traverseNode.textContent.slice(0,slashIdx);
				else
					return traverseNode.textContent;
			}
			traverseNode=traverseNode.nextSibling;
		}
	}
	return null;
}

function validlink_web(link)
{
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="H3" && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className=="sponsoredLink")
	{
		var movertext = link.getAttribute("onmouseover").toString();
		if(movertext)
		{
			var m = movertext.match (/setStatus\('((http:\/\/)?[^']+)'\)/i)
			return m[1].toString();
		}
	}
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="H3" && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className=="advertLink")
	{
		var allSpan = link.parentNode.parentNode.getElementsByTagName("SPAN");
		return allSpan[0].textContent;
	}	
	if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className=="resultContent")
	{
		if(link.href)
			return link.href;
	}
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="H3" && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className=="smartinfoContent")
	{
		if(link.href)
			return link.href;
	}	
	if(link.parentNode && link.parentNode.tagName=="SPAN" && link.parentNode.className && link.parentNode.className=="siLink")
		return link.href;
	return null;
}

function validlink_biglobe(link)
{
	if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.id.match(/footerNavi/))
		return null;
	if(link.parentNode && link.parentNode.className && link.parentNode.className=="hit01")
	{
		var tNode=link.parentNode.nextSibling.nextSibling;
		tNode=tNode.firstChild;
		var traverseNode=tNode.firstChild;
		while(traverseNode)
		{
			if(traverseNode.tagName && traverseNode.tagName=="SPAN" && traverseNode.className && traverseNode.className=="hitURL")
				return traverseNode.textContent;
			traverseNode=traverseNode.nextSibling;
		}
	}
	if(link.parentNode && link.parentNode.className && link.parentNode.className.match(/hitTitle/))
		return link.href;
	return null;
}

function validlink_sonet(link)
{
	if(link.parentNode && link.parentNode.tagName && (link.parentNode.tagName=="BODY" || link.parentNode.tagName=="DIV"))
	{
		if(!link.getAttribute("onmouseover"))
			return null;
		var movertext=link.getAttribute("onmouseover").toString().toLowerCase();
		var m = movertext.match (/ss\('(http(?:s)?:\/\/[^']+)'\)/i);
		if (m != null && m.length == 2)
			return m[1].toString();
	}
	return null;
}

function validlink_cox(link)
{
	if(link.parentNode && link.parentNode.getAttribute("vAlign") && link.parentNode.getAttribute("vAlign")=="center")
	{
		if(link.className && link.className=="blue-link")
			return link.textContent;
		return null;
	}
	if(link.parentNode && link.parentNode.getAttribute("colSpan") && link.parentNode.getAttribute("colSpan")=="2" && !link.parentNode.getAttribute("vAlign"))
	{
		if(link == link.parentNode.firstChild.nextSibling)
			return link.href
		return null;
	}
	if(link.href && link.href.match(/google/i) && link.parentNode.tagName=="TD")
	{
		if(link == link.parentNode.firstChild.nextSibling && link.firstChild && link.firstChild.className && link.firstChild.className=="blue-link")
		{
			var allLinks = link.parentNode.getElementsByTagName("A")
			if(allLinks[allLinks.length-1].textContent.toLowerCase()!="next" && allLinks[allLinks.length-1].textContent.toLowerCase()!="previous")
				return allLinks[allLinks.length-1].textContent;
		}
		return null;
	}
	return null;
}

function validlink_gmx(link)
{
	return validlink_web(link);
}

function validlink_excite(link)
{
	if(link.id && link.id.match(/(awg|jls)/))
	{	
		var traverseNode = link;
		while(traverseNode)
		{
			if(traverseNode.tagName && traverseNode.tagName=="FONT")
				return traverseNode.textContent;
			traverseNode = traverseNode.nextSibling;
		}
	}
	if(link.parentNode && link.parentNode.className && link.parentNode.className=="hit" && link.href && link.href.match(/http:\/\/www\.excite\.co\.jp\/relocate\/co=jword/))
	{
		var traverseNode = link.nextSibling;
		while(traverseNode)
		{
			if(traverseNode.tagName && traverseNode.tagName=="FONT")
			{
				return traverseNode.textContent;
			}
			traverseNode = traverseNode.nextSibling;
		}
	}	
	if(link.parentNode && link.parentNode.className && link.parentNode.className=="hit")
	{
		return link.href;
	}
}

function validlink_sky(link)
{
	if(link.ownerDocument.location.href.match(/search\.sky\.com\/search/))
	{
		if(link.parentNode && link.parentNode.tagName == "H2" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/sponsoredlinks/))
		{
			var mOverText = link.getAttribute("onmouseover").toString();
			var m = mOverText.match (/google_ss\('(.*)'\)/i);
			if(m)
				return m[1].toString();
		}
		if(link.parentNode && link.parentNode.tagName == "H2" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/searchresults/))
		{
			return link.href;
		}
	}
	if(link.ownerDocument.location.href.match(/(www\.sky\.com\/portal\/site\/skycom\/)|(search[0-9]\.sky\.com\/web\?)/))
	{
		if(link.className && link.className.match(/epi-link2 underline-hvr/) && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/sponsored-list/))
		{
			href = unescape(link.href);
			var urlIdx = href.indexOf("&url=");
			if(urlIdx >= 0)
			{
				var sigIdx = href.indexOf("&sig=");
				if(sigIdx!=-1)
					return href.slice(urlIdx + 5);
				else
					return href.slice(urlIdx + 5, sigIdx);
			}
			else
			{
				var qIdx = href.indexOf("&q=");
				if(qIdx >= 0)
				{
					var sigIdx = href.indexOf("&sig=");
					if(sigIdx!=-1)
						return href.slice(qIdx + 3);
					else
						return href.slice(qIdx + 3, sigIdx);
				}
			}
		}
		if(link.className && link.className.match(/epi-link2 underline-hvr/) && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/result-list/))
		{
			return link.href;
		}
	}
}

function validlink_nate(link)
{
	if(link.parentNode && link.parentNode.className && link.parentNode.className.match(/font13|vd_txt/) && !(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.parentNode.className.match(/conts2/)))
		return null;
	if(link.href && link.className && link.className.match(/link13/))
	{
		var indFindSeq = link.href.lastIndexOf("/*");
		if(indFindSeq>=0)
		{
			return link.href.slice(indFindSeq + 2,link.href.length)
		}
	}
	if(link.href && link.className && link.className.match(/resGoogle/))
	{
		var indFindSeq = link.href.lastIndexOf("&yargs=");
		if(indFindSeq>=0)
		{
			return link.href.slice(indFindSeq + 7,link.href.length)
		}
		else
		{
			var movertext = link.getAttribute("onmouseover").toString();
			if(movertext)
			{
				var m = movertext.match (/\('go\sto\s([^']+)'\)/i)
				return m[1].toString();
			}
		}
	}
	if(link.parentNode && link.parentNode.className && link.parentNode.className.match(/^(text-inline|fl)/))
	{
		var tNode = link.parentNode;
		while(tNode)
		{
			if(tNode.className && tNode.className.match(/text-block/))
			{
				var allLinks = tNode.getElementsByTagName('A');
				return allLinks.item(0).textContent;
			}
			tNode = tNode.nextSibling;
		}
	}
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "DT" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/search-list/) && link.parentNode.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.parentNode.className.match(/special|book|video|web|job|dic/))
	{
		try
		{
			var ind = link.href.lastIndexOf("/*");
			if(ind!=-1)
				return link.href.slice(ind + 2);
		}
		catch(e)
		{
		}
	}
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "DT" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/goto/))
	{
		try
		{
			var ind = link.href.lastIndexOf("/*");
			if(ind!=-1)
				return link.href.slice(ind + 2);
		}
		catch(e)
		{
		}
	}
	return null;
}

function validlink_naver(link)
{
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="DT" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.parentNode.parentNode.id.match(/plus|power|sponsor|biz/))
	{
		try
		{
			var m = link.getAttribute("onclick").toString();
			var x = m.match(/urlencode\('(http(s)?:\/\/[^']+)'\)/i);
			if(x)
				return x[1].toString();
		}
		catch(e)
		{
		}
	}
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="DT" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/type01/))
		return link.href;
	/*if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="DT" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.parentNode.className.match(/dic_100/))
		return link.href;*/
	if(link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "DT" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.id.match(/videoItem|site_preview_layerlist|goto/))
		return link.href;
	if(link.href && link.className && link.className=="link")
		return link.href;
	return null;
}

function validlink_nifty(link)
{
	if(link.href && link.className && link.className == "l" && link.parentNode && link.parentNode.tagName && (link.parentNode.tagName == "LI" || link.parentNode.tagName == "DIV"))
	{		var href = link.href;		var qIdx = href.indexOf("&q=");		if(qIdx != -1)		{			return href.slice(qIdx + 3);		}		else		{			return href;		}			}	else if(link.href && link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "H3" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className=="searchList")	{		var href = link.href;		return href;	}	else if(link.href && link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "DT" && link.parentNode.parentNode &&  link.parentNode.parentNode.className && link.parentNode.parentNode.className=="adsList")	{
		var href = link.href;
		var qIdx = href.indexOf("&q=");
		if(qIdx != -1)
		{
			return href.slice(qIdx + 3);
		}
		else
		{
			return href;
		}		
	}
}

var SEDomainSpecs = [null, "google.com", "yahoo.com", "msn.com", "aol.com", "ask.com","uol.com","goo.ne.jp", "myway.com", "baidu.com", "live.com", "terra.com", "comcast.net", "web.de", "biglobe.ne.jp", "so-net.ne.jp", "cox.net", "gmx.net", "excite.co.jp", "sky.com", "empas.com", "naver.com", "nifty.com"];
var SEValidLinks = [null, validlink_google, validlink_yahoo, validlink_msn, validlink_aol, validlink_ask, validlink_uol, validlink_goo, validlink_myway, validlink_baidu, validlink_live, validlink_terra, validlink_comcast, validlink_web, validlink_biglobe, validlink_sonet, validlink_cox, validlink_gmx, validlink_excite, validlink_sky, validlink_nate, validlink_naver, validlink_nifty];
var SEExceptions = [null, /\.google(syndication)?\./, /\.yahoo\./, /\.(nine|t1)?msn\./, /\.(aol|primusonline)\./, /\.ask\./, /\.uol\./, /\.goo\./, /\.myway\./, /\.baidu\./, /\.live\./, /\.terra\./, /\.comcast\./, /\.web\./, /\.biglobe\./, /\.so\-net\./, /\.cox\.net/, /\.gmx\./, /\.excite\.co\.jp/, /\.sky\./, /\.(cyworld|empas|nate)\./, /\.naver\./, /\.nifty\./];
var LinkContainer;

var pageTimeout = null;
var terraURL = null;
var mainjs = "main.js";
var isSA28 = false;
var customSSBalloon = false;
var initialized = false;
var postSA27 = false;

var globalCountCert = 0;
var globalCountOk = 0;
var globalCountInfo = 0;
var globalCountWarn = 0;
var globalCountUnknown = 0;

function get_reset_pop_cert() { var countCert; countCert=globalCountCert; globalCountCert=0; return countCert;}
function get_reset_pop_ok() { var countOk; countOk=globalCountOk; globalCountOk=0; return countOk;}
function get_reset_pop_info() { var countInfo; countInfo=globalCountInfo; globalCountInfo=0; return countInfo;}
function get_reset_pop_warn() { var countWarn; countWarn=globalCountWarn; globalCountWarn=0; return countWarn;}
function get_reset_pop_unknown() { var countUnknown; countUnknown=globalCountUnknown; globalCountUnknown=0; return countUnknown;}


function terraAnnotation(e,doc,terraURL)
{
	if(terraURL!=doc.location.href)
	{
		terraURL=doc.location.href;
		save_me(e);
	}
	if (pageTimeout) clearTimeout(pageTimeout);
	if(doc.location.href.match(/(buscador\.terra\.com\.(br|mx|pe|co)|(buscador\.terra\.cl\/default\.aspx\?))/))
		pageTimeout = setTimeout(terraAnnotation , 1000 , e , doc , terraURL)
}

function set_uid(auid) {uid = ""+ auid;}
function set_dss(dss) {if(dss!=null) globaldss = dss;}
function set_enabled(true_or_false) {buttonon = true_or_false;}
function set_icons(true_or_false) {on = true_or_false;}
function set_highlite(true_or_false) {highliteon = true_or_false;}
function set_restricted(true_or_false) {restricted = true_or_false;}
function set_affid(str) {affid = str;}
function set_premium(true_or_false) {premium = true_or_false;}
function set_usessl(true_or_false) {doSSL = true_or_false;}
function set_showUpsellMessage(true_or_false) {showUpsellMessage = true_or_false;}
function set_upsellMessage(str) {upsellMessage = str;}
function set_upsellUrl(str) {upsellUrl = str;}
function set_version(str) {client_ver = str;}
function set_suite(true_or_false) {is_suite = true_or_false;}
function set_CustomSSIcon(true_or_false) {customSSIcon = true_or_false;}
function set_CustomSSBalloon(true_or_false) {customSSBalloon = true_or_false;}

function set_locale(str) {
	localeParts = str.split("-");
	if (localeParts.length == 1) {
		locale = str.toLowerCase();
	} else if (localeParts.length == 2) {
		locale = localeParts[0].toLowerCase()+"-"+localeParts[1].toUpperCase();
	} else if (localeParts.length == 3) {
		locale = localeParts[0].toLowerCase()+"-"+localeParts[1].toUpperCase()+"-"+localeParts[2].toLowerCase();
	} else locale = str;
}
function set_whitelist(wlstring) {
	whiteList = "";
	if(wlstring)
		whiteList = wlstring.split(';');
}

function set_safesearch(true_or_false) {globalSS = true_or_false;}

function set_httponedotone(using) {
    if(!using || using=="false") globalUsing = false; 
    else globalUsing = true;
}

var DefaultUpsellUrl = "http://us.mcafee.com/root/campaign.asp?cid=20209";

var hsafeSite = 0;
var HackerSafeAnnotations = false;
var doHackerSafe = false;
var globalClickedOn = "";
var globalHSString = "";

function set_HS(HS) { HackerSafeAnnotations = HS; }

var globalYahooFilter = true;
var globalSAOnYahoo = true;
function set_SAOnYahoo(SAOnYahoo) { globalSAOnYahoo = SAOnYahoo; }
function readSettings()
{
	if(!initialized)
	{
		set_dss(getSystemOptionString("DSS", "http://dss1.siteadvisor.com/DSS/"));		
		set_affid(getSystemOptionString("*Affid", "0"));
		set_premium(getSystemOptionBool("*Premium", false));
		set_usessl(0 != getOptionInt("UseSSL", 0));
		set_showUpsellMessage(getSystemOptionBool("*ShowSafeSearchUpsell", true));
		set_upsellMessage(getSystemOptionString("*SafeSearchUpsellMessage", ""));
		set_upsellUrl(getSystemOptionString("*SafeSearchUpsellUrl", ""));
		set_version(getSystemOptionString("version", ""));
		set_suite(getSystemOptionBool("*Suite", false));
		set_locale(getSystemOptionString("Locale", "en-us"));
		set_safesearch(getSystemOptionBool("*SafeSearch", true));
		set_httponedotone("false");
		set_CustomSSBalloon(getSystemOptionInt("CustomSSBalloon", 0));
		set_uid(getSystemOptionString("*Token", ""));
		initialized = true;
	}
	set_enabled(getOptionBool("*Enabled",true));
    set_whitelist(getOptionString("*WhiteList",""));
	set_icons(getOptionBool("*Icons",true));
	set_highlite(getOptionBool("*Highlight",false));
	set_restricted(getSystemOptionBool("*RestrictedBrowsingEnabled", false));
	postSA27 = true;
	set_SAOnYahoo(getSystemOptionBool("*AnnotateSERP_Yahoo", false));	
}

function parseURLParameter( name, url )
{
  name = name.replace(/[\[]/,'\\\[').replace(/[\]]/,'\\\]');
  var regexS = '[\\?&]'+name+'=([^&#]*)';
  var regex = new RegExp( regexS );
  var results = regex.exec( url );
  if( results == null )
    return '';
  else
    return results[1];
}

function enableWhiteListButton(doc)
{
    try
    {
        var regex = new RegExp( '^http:\/\/www.siteadvisor\.com\/' );
        if( doc.location.href && regex.test(doc.location.href) )
        {
            dontwarnDivButton = doc.getElementById("DontWarn");
            dontwarnDivButton.style.visibility = "visible";
            blockOverrideDiv = doc.getElementById("blockOverride");
            blockOverrideDiv.style.visibility = "hidden";
            tokenString = getSystemOptionString( "*Token", "" );
            dontwarnDivButton.href = 'http://sa/whitelist?token=' + tokenString + '&whitelisturl=' + parseURLParameter('domain', doc.location.href) + '&redirectURL=' + parseURLParameter('originalURL', doc.location.href);
        }
    }
    catch(e)
    {
	}
    return;
}

function safesearch(domEvent) {
	try	{   
	    readSettings();
	}catch(e){
	}
	mainjs = "mainff.js";
	isSA28 = true;
	if (!globalSS)	return;

	save_me(domEvent);
	globaldoc = domEvent.originalTarget;
	enableWhiteListButton(globaldoc);	
}

var hotmailTimeout = null;
var yahooTimeout = null;

function checkPage(doc)
{
	var allLinks = doc.links;
	for(var i = 0; i < allLinks.length; i++)
	{
		link = allLinks[i];
		try
		{
			if(link.className && link.className.match(/yschttl/) && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.id.match(/web/))
			{
				orighref = link.getAttribute("orighref");
				if(orighref)
					return true;
				else
					return false;
			}
		}
		catch(e)
		{
			return false;
		}
	}
}

function save_me(e) {
	try
	{
		if(!getSafeSearch()) return;
	}
	catch(e)
	{
	}
	if(!buttonon) return;

	var doc = e.originalTarget;
	
	var dssurl = getDss();
	//if(doc.location.href.match(/(buscador\.terra\.com\.(br|mx|pe|co)|(buscador\.terra\.cl\/default\.aspx\?))/))
		//terraURL = doc.location.href
	overlay_objects = new Array();
	highlite_colors = new Array();
	pagedom = null;

	var nav_href = doc.location.href;
	try{
	globalSAOnYahoo = getGlobalSAOnYahoo();
	}catch(e){}
	if(nav_href.match(/http:\/\/(us\.)?search\.yahoo\.com/) && !globalSAOnYahoo) return;
	
	var keyword_search = nav_href.match(/^keyword:(.+)$/i);
	if (keyword_search) {
		var pref = Components.classes["@mozilla.org/preferences-service;1"].getService(Components.interfaces.nsIPrefBranch);
		nav_href = pref.getCharPref("keyword.URL") + keyword_search[1];
	}
	var search_page = 0;
	if (nav_href.match(/^http:\/\/(w+|america|www\.l)\.google(syndication)?\.[a-z\.]+\/(search\?|custom\?|sponsoredlinks\?|mac\?|bsd\?|linux\?|microsoft\?|unclesam\?)/i))
		search_page = GOOGLE;
	else if (nav_href.match(/(^http:\/\/([a-z][a-z]\.)?(www|search|myweb|myweb2\.search|espanol\.search|search\.cn)\.yahoo\.[a-z\.]+\/(search|custom|s))|(http:\/\/search\.p4p\.yahoo\.com\.cn)/i))
	{
		startOrNot = checkPage(doc);
		if(startOrNot || !nav_href.match(/http(s)?:\/\/(us\.)?search\.yahoo\.com/))
		{
			search_page = YAHOO;
			if(yahooTimeout) clearTimeout(yahooTimeout);
		}
		else
		{			yahooTimeout = setTimeout(function() {save_me(e);}, 2000);
			search_page = 0;
		}
	}
	else if (nav_href.match(/^http:\/\/(adcenter\.)?(live)?search\.(sympatico\.|prodigy\.)?(nine)?msn\.[a-z\.]+\/(results)/i) || nav_href.match(/http:\/\/search\.live\.com\/results\.aspx\?(q=.*&)?FORM=MSNH/i))
	{
		if (doc.links.length == 0)
		{
			return;
		}	
		search_page = MSN;
	}
	else if (nav_href.match(/^http:\/\/((aolsearcht[0-9]+|aim)\.)?(search|(www\.)?recherche|busqueda|suche|buscador|aolsearch|(www|search)\.jp)\.aol\.[a-z\.\-\/]+\/(aol\/(search|afe)\?|aol(com)?\/(weboffers|search)\?|search\?|index\.php\?|(web|moresl)\?|web\.adp\?|rech\?|advhandler\.adp|suche\/web\/(search|sponsored_search)\.jsp\?)/i) || nav_href.match(/^http:\/\/zoek\.aol\.nl/i))
		search_page = AOL;
	else if (nav_href.match(/^http:\/\/((www|search|fr|de|uk|it|nl|es)\.)?ask\.(com|jp)\/web(\.asp)?\?/i))
		search_page = ASK;
	else if (nav_href.match(/^http:\/\/((mundo|brasil)\.)?busca\.uol\.com\.br\/(((www|uol|br)\/index\.html\?)|(buscar\.html))/i))
			search_page = UOL;
	else if (nav_href.match(/^http:\/\/search\.goo\.ne\.jp\/web\.jsp\?/i))
			search_page = GOO;
	else if (nav_href.match(/^http:\/\/(search|mysearch)\.myway\.com\/(search|jsp)\/(AJ|GG|AW|LS|GG)(main|web|advr)\.(jsp|jhtml)/i))
		search_page =  MYWAY;
	else if (nav_href.match(/^http:\/\/www\.baidu\.com\/(s|baidu)\?/i))
		search_page = BAIDU;
	else if (nav_href.match(/^http:\/\/search\.live\.com\/results\.aspx\?/i))
	{
		if (doc.links.length == 0)
		{
			return;
		}
		search_page = LIVE;
	}
	else if (nav_href.match(/^http:\/\/buscador\.terra\.com\.(br|mx|pe|co)\/((default|results)\.aspx)?\?(srchBak)?/i) || nav_href.match(/^http:\/\/buscador\.terra\.cl\/(default|results)\.aspx\?/i)) {
		search_page = TERRA;
		//pageTimeout = setTimeout(terraAnnotation , 1000 , e , doc , terraURL);
	}
	else if (nav_href.match(/^http:\/\/search\.comcast\.net\/(search)?\?/i))
		search_page = COMCAST;
	else if (nav_href.match(/^http:\/\/suche\.web\.de\/search\/web/i))
		search_page = WEB;
	else if(nav_href.match(/^http:\/\/cgi\.search\.biglobe\.ne\.jp\/cgi-bin\//i))
		search_page = BIGLOBE;
	else if(nav_href.match(/^http:\/\/(so\-net\.)?search\.goo\.ne\.jp\/so\-net\/web\.jsp/))
		search_page = SONET;
	else if(nav_href.match(/^http:\/\/[a-z]+\.cox\.net\/cci\/(search\/)?google/))
		search_page = COX;		
	else if (nav_href.match(/^http:\/\/suche\.gmx\.net\/search\/web/i))
		search_page = GMX;
	else if (nav_href.match(/^http:\/\/www\.excite\.co\.jp\/search\.gw\?/i))
		search_page = EXCITE;
	else if (nav_href.match(/^http:\/\/search\.sky\.com\/search\/skynews\/google\?/i))
		search_page = SKY;
	else if (nav_href.match(/(^http:\/\/www\.sky\.com\/portal\/site\/skycom\/((template\.PAGE)|(googlesearch\?)))|(^http:\/\/search[0-9]\.sky\.com\/web\?)/i))
		search_page = SKY;		
	else if(nav_href.match(/^http:\/\/(nate|cyworld)\.search\.empas\.com\/search/))
		search_page = NATE;		
	else if(nav_href.match(/^http:\/\/(web\.)?search\.naver\.com\/search\.naver\?/))
		search_page = NAVER;		
	else if(nav_href.match(/^http:\/\/search\.nifty\.com\/cgi\-bin\/search\.cgi\?/i) || nav_href.match(/^http:\/\/search\.nifty\.com\/websearch\/search/i))		search_page = NIFTY;
	else if(nav_href.match(/^http(s)?:\/\/mail\.google\.com\/mail\/h\//i))
		attachWebmailWatcher(gmailBasicHtmlWatcher,dssurl,doc);
	else if (nav_href.match(/^http(s)?:\/\/mail\.google\.com\/mail\//i))
		attachWebmailWatcher(gmailWatcher,dssurl,doc);
	else if (nav_href.match(/^http:\/\/[a-z0-9\-\.]+\.mail\.yahoo\.[a-z\.]+\/(ym|mc)\/(ShowLetter|showMessage)(\?)?/i))
		attachWebmailWatcher(yahooMailWatcher,dssurl,doc);
	else if (nav_href.match(/^http:\/\/[a-z0-9\-\.]+\.mail\.yahoo\.[a-z\.]+\/dc\/launch\??/i))
		attachWebmailWatcher(yahooAjaxMailWatcher,dssurl,doc);
	else if (nav_href.match (/^http:\/\/[a-z0-9]+\.[a-z0-9]+\.hotmail\.msn\.com\/cgi-bin\/getmsg\?/i))
		attachWebmailWatcher(hotmailMailWatcher,dssurl,doc);
	else if (nav_href.match (/^http:\/\/[a-z0-9]+\.[a-z0-9]+\.mail\.live\.com\/mail\/mail\.aspx\?/i))
		attachWebmailWatcher(newHotmailMailWatcher,dssurl,doc);		
	else if (nav_href.match (/^http:\/\/[a-z0-9]+\.bay[0-9]+\.mail\.live\.com\/mail\/mail\.aspx\?/i))
		attachWebmailWatcher(windowsLiveMailWatcher,dssurl,doc);
	else if (nav_href.match (/^http:\/\/((webmail\-[a-z0-9]+|[a-z0-9\-]+)\.webmail|webmail)\.aol\.(com|in)\/[0-9]+\/(aim|aolpd|aol)?\/[a-z\-]+\/(mail\/display(-)?message\.aspx|RPC\/GetMessage\.aspx|Suite.aspx|Lite\/MsgRead\.aspx)/i))
		attachWebmailWatcher(aolMailWatcher,dssurl,doc);
	else if (nav_href.match(/^http:\/\/[a-z0-9]+\.[a-z0-9]+\.mail\.live\.com\/Mail\/(ReadMessage|Inbox)Light\.aspx/i))
	{
		if(doc.getElementById("MsgContainer"))
		{
			if(hotmailTimeout)
				clearTimeout(hotmailTimeout);
			attachWebmailWatcher(hotmailClassicWatcher,dssurl,doc);
		}
		else
		{
			try
			{
				hotmailTimeout = setTimeout(save_me, 4000, e);
			}
			catch(e)
			{
			}
		}
	}

	if (search_page != 0 && (on || highliteon)) {
		domain_specifier = SEDomainSpecs[search_page];
		if(search_page == NATE && doc.links.length<50) return;
		LinkContainer = new Array();
		hrefContainer = new Array();
		fullHrefContainer = new Array();
		var links = doc.links;
		// Form the DSS query and send it out.
		var client_version = "FF_unknown";
		try{
			client_version = "FF_" + getVersion() + "_" + getBuild();
		} catch(vererr) {}

		var dssquery = "Type=domain&version=2&client_ver=" + client_version+"&locale="+getLocale()+"&aff_id="+affid;
		var cnt = 1;
		hrefContainer = new Array();
		for(var i = 0; i < links.length && cnt <= 200; i++) {
			var link = links[i];
			if (link.getElementsByTagName("IMG").length > 0) continue;
			var href = SEValidLinks[search_page](link);
			if(href)
			{
				href=href.toLowerCase();
				var hrefTemp = href;
				href = extractHostName(UnescapeAndStripargs(href));
			}
			if(search_page==GOOGLE)
			{
				try
				{
					if(window.parent._content.document.location.href.match(/http:\/\/www\.uol\.com\.ar\/buscar/))
					{
						try
						{
							if(link.textContent.match(/^Anuncios Google$|^Pýginas similares$|^\sSiguiente/)) continue;
							if(link.firstChild && link.firstChild.className && link.firstChild.className.match(/nr/)) continue;
							if(link.className && link.className.match(/p/) && link.previousSibling && link.previousSibling.className && link.previousSibling.className.match(/p/)) continue;
							if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.parentNode.parentNode.id.match(/navbar/)) continue;
							if(link.className && link.className.match(/fl/) && link.textContent.match(/^Mýs resultados/)) continue;
						}
						catch(e)
						{
						}
						if (!href || href.match(SEExceptions[UOL])) continue;
						if (href.match(/^(http(s)?:\/\/uol|uol)\.com/)) continue;
					}
					else
					{
						if (!href || href.match(SEExceptions[search_page])) continue;
					}
				}
				catch(e)
				{
				}
			}
			else
			{
				if (!href || href.match(SEExceptions[search_page])) continue;
			}
			if(search_page==YAHOO && href.match(/^http(s)?:\/\/yahoo\./))continue;
			if(search_page==WEB && (href.match(/^http(s)?:\/\/web\.de/) || href == "web.de"))continue;
			if(search_page==ASK && (href.match(/^http(s)?:\/\/ask\./) || href == "ask.com"))continue;	
			if(search_page==MYWAY && (href.match(/^http(s)?:\/\/myway\./) || href == "myway.com")) continue;
			if((search_page==MSN || search_page==LIVE) && (href.match(/^http(s)?:\/\/msn\./) || href.match(/\.msn\./) || href.match(/^http(s)?:\/\/live\./) || href.match(/\.live\./)))continue;
			if(search_page==AOL && href.match(/aol\.com/)) continue;
			if(search_page==GOOGLE && href.match(/http(s)?:\/\/google\.com/)) continue;
			if(search_page==BIGLOBE && href.match(/^(http(s)?:\/\/)?biglobe\.ne\.jp/)) continue;
			if(search_page==BAIDU && href.match(/baidu\.com/)) continue;
			if(search_page==COMCAST && href.match(/^(http(s)?:\/\/)?comcast\.net/)) continue;
			LinkContainer.push(link);
			fullHrefContainer.push(hrefTemp);
			dssquery += "&Name_" + cnt + "=" + escape(extractHostName(UnescapeAndStripargs(hrefTemp)));
			var idx = hrefTemp.indexOf("//")
			if(idx>0)
			{
				if(hrefTemp.lastIndexOf("/") != (hrefTemp.length - 1))
					hrefContainer.push(hrefTemp.slice(idx+2,hrefTemp.length));
				else
					hrefContainer.push(hrefTemp.slice(idx+2,hrefTemp.length-1));
			}
			else
				hrefContainer.push(hrefTemp);
			cnt++;
		}
		if(dssquery.match(/&Name_/))
		{
			if(!doc.getElementById('BorderLayer'))
				insert_meta_info(doc);
			remove_styles(doc);
		}
		dssquery = dssquery + "&count=" + (cnt - 1);
		//sendDssRequest(dssquery,search_page,doc);
		var dssurl = getDss();
		sendDssRequestPriv(dssurl,dssquery,search_page,doc);
	} else {
		if (nav_href != window._content.document.location.href) return;
		if (nav_href != loc) changebutton();
	}
}

function UnescapeAndStripargs(href)
{
	href = unescape(href);
	return stripargs(href);
}

function buildArgStringSS_comp(str)
{
	if(!postSA27)
	{
		try
		{
			var argStr = buildArgStringSS(str);
			if(argStr) return argStr;
		}
		catch(e)
		{
			var client_version = "FF_unknown";
			try
			{
				client_version = "FF_" + getVersion() + "_" + getBuild();
			}
			catch(vererr)
			{
			}
			var argStr = "ref=" + str + "&client_ver=" + client_version + "&locale=" + getLocale() + "&premium=" + premium + "&aff_id=" + affid;
			return argStr;
		}
	}
	else
	{
		var argStr = "ref=" + str + "&aff_id=" + affid + "&premium=" + premium + "&suite=" + is_suite + "&client_ver=" + client_ver + "&locale=" + locale;
		return argStr;
	}
}

function contains (container, containee) {
  while (containee) {
    if (container == containee) {
      return true;
    }
    containee = containee.parentNode;
  }
  return false;
}

function sendDssRequestPriv(dssurl, dssquery, engine, doc) {
	if(!dssquery.match(/&Name_/)) return;
	httpPostRequestPriv(dssurl, dssquery, true, true, engine,doc);
}

function httpPostRequestPriv(dssurl,dssquery,retry,timeout,engine,doc) {
	if (req != null) {
		if (req.timeoutId) clearTimeout(req.timeoutId);
		req.onreadystatechange = null;
		req.abort();
	}
	req = new XMLHttpRequest();
	if (timeout) {
		req.timeoutId = setTimeout(retryDssRequestPriv,10000,dssurl,dssquery,engine,doc);
		req.onprogress = function () {
			clearTimeout(req.timeoutId);
			req.timeoutId = setTimeout(retryDssRequestPriv,10000,dssurl,dssquery,engine,doc);
		}
	}
	if (doSSL)
		req.open('POST',dssurl.replace(/^http:/,"https:") + "MultiQuery", true);
	else
		req.open('POST',dssurl + "MultiQuery", true);
	req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	req.overrideMimeType('text/xml');
	req.onreadystatechange = function () {
		if (req.readyState == 4) {
			var stat;
			try {stat = req.status;}
			catch(staterr) {stat = 500;}
			if (stat == 200 && req.responseXML.documentElement.nodeName != "parsererror") {
				if (timeout) clearTimeout(req.timeoutId);
				DSSHandlerPriv(dssurl, req.responseXML, doc, engine);
			}
			else if (retry) retryDssRequestPriv(dssurl,dssquery,engine,doc);
		}
	};
	req.send(dssquery);
}

function extractHostName(url)
{
	var idxDSlash = url.indexOf("//")
	if(idxDSlash > 0)
		url = url.slice(idxDSlash+2,url.length)
	var idxSSlash = url.indexOf("/")
	if(idxSSlash > 0)
		url = url.slice(0,idxSSlash)
	return url;
}

function customizeRegEx(inRegEx)
{
	var outRegEx = inRegEx.replace(/{/g,"(?:")
	outRegEx = outRegEx.replace(/}/g,")")
	return outRegEx;
}

var ignoreArray = new Array();

var hd = [null, "^(.*[0-9]{1,3}[\-][0-9]{1,3}[\-][0-9]{1,3}[\-][0-9]{1,3}.*)$", "^(.*\.static\..*\.net.*)$", "^(.*\.adsl\..*)$", "^(.*\.customer\..*)$"];
var hp = "^(?:(?:(www[a-zA-Z0-9]*)|(ftp))\.)*([a-zA-Z0-9][a-zA-Z0-9\-\._]+\/~[^\/?:&]+).*";

var globalRestricted = false;
								
function DSSHandlerPriv(dssurl,xml,doc,search_page)
{
	var domaininfo = xml.getElementsByTagName("DomainInfo");
	var client_version = "FF_unknown";
	try
	{
		client_version = "FF_" + getVersion() + "_" + getBuild();
	}
	catch(vererr)
	{
	}	
	var dssquery = "Type=domain&version=2&client_ver=" + client_version+"&locale="+getLocale()+"&aff_id="+affid;
	var cnt = 1;
	ignoreArray = new Array();
	var firstReq=true;
	for (var i = 0; i < domaininfo.length; i++)
	{
		var domainInfoItems = domaininfo.item(i).childNodes;
		for (var k = 0; k < domainInfoItems.length; k++)
		{
			var domainInfoItem = domainInfoItems.item(k);
			if(domainInfoItem.nodeName == "DomainMetaData")
			{
				var myregex = domainInfoItem.attributes.getNamedItem("domainSpecRegExs").nodeValue;
				var partRegEx = new Array();
				partRegEx=myregex.split(" ")
				for(var m=0;m<partRegEx.length;m++)
				{
					var execregex = new RegExp(customizeRegEx(partRegEx[m]))
					var found = execregex.exec(hrefContainer[i])
					if(found==null)
					{
					}
					else
					{
						if(found!=null)
						{
							if(found[1] && found[1].indexOf("/")>0)
							{
								hrefContainer[i]=found[1];
								dssquery += "&Name_" + cnt + "=" + escape(UnescapeAndStripargs(hrefContainer[i]));
								cnt++;
								ignoreArray.push(i);
								firstReq=false;
							}
							if(found[1] && found[1].indexOf("/")<0)
							{
								var foundHP = null;
								var homepageRegEx = new RegExp(hp);
								var foundHP = homepageRegEx.exec(hrefContainer[i]);
								if(foundHP)
								{
									var except = new RegExp(/\/~([^\/?:&]+)/);
									var subdomain = except.exec(foundHP[3]);
									var hn = extractHostName(hrefContainer[i])
									var hnComp = hn.split(".");
									if(subdomain!="site" && subdomain!=hnComp[hnComp.length-2])
									{
										if(foundHP[1])
										{
											hrefContainer[i] = foundHP[1] + "." + foundHP[3];
										}
										else
										{
											if(foundHP[2])
												hrefContainer[i] = foundHP[2] + "." + foundHP[3];
											else
												hrefContainer[i] = foundHP[3];
										}
										dssquery += "&Name_" + cnt + "=" + escape(UnescapeAndStripargs(hrefContainer[i]));
										cnt++;
										ignoreArray.push(i);
										firstReq=false;
									}
								}
								if(!foundHP)
								{
									var foundHD;
									for(var iter=1;iter<5;iter++)
									{
										var hostingDomainRegEx = new RegExp(hd[iter]);
										foundHD = hostingDomainRegEx.exec(extractHostName(hrefContainer[i]));
										if(foundHD)
										{
											hrefContainer[i]=foundHD[0]
											dssquery += "&Name_" + cnt + "=" + escape(UnescapeAndStripargs(hrefContainer[i]));
											cnt++;
											ignoreArray.push(i);
											firstReq=false;
										}
									}								
								}
							}							
							break;
						}
					}
				}
			}
		}
	}
	globalRestricted = restricted;	
	if(!firstReq)
	{
		dssquery = dssquery + "&count=" + (cnt - 1);
		sendDssRequest(dssurl, dssquery, search_page, doc,firstReq,xml);
	}
	else
	{
		DSSHandler(xml,doc,search_page,true,xml);
	}
}

function retryDssRequestPriv(dssurl,dssquery,engine,doc) {
	if (dssurl.match(/dss1/)) dssurl = dssurl.replace("dss1","dss2");
	else if (dssurl.match(/dss2/)) dssurl = dssurl.replace("dss2","dss1");
	setDss(dssurl);
	httpPostRequestPriv(dssurl, dssquery, false, false, engine,doc);
}

function sendDssRequest(dssurl,dssquery,engine,doc,firstReq,xml) {
	httpPostRequest(dssurl, dssquery, true, true, engine,doc,firstReq,xml);
}
function retryDssRequest(dssurl,dssquery,engine,doc,firstReq,xml) {
	if (dssurl.match(/dss1/)) dssurl = dssurl.replace("dss1","dss2");
	else if (dssurl.match(/dss2/)) dssurl = dssurl.replace("dss2","dss1");
	setDss(dssurl);
	httpPostRequest(dssurl, dssquery, false, false, engine,doc,firstReq,xml);
}
function httpPostRequest(dssurl,dssquery,retry,timeout,engine,doc,firstReq,xml) {
	if (req != null) {
		if (req.timeoutId) clearTimeout(req.timeoutId);
		req.onreadystatechange = null;
		req.abort();
	}
	req = new XMLHttpRequest();
	if (timeout) {
		req.timeoutId = setTimeout(retryDssRequest,10000,dssurl,dssquery,engine,doc,firstReq,xml);
		req.onprogress = function () {
			clearTimeout(req.timeoutId);
			req.timeoutId = setTimeout(retryDssRequest,10000,dssurl,dssquery,engine,doc,firstReq,xml);
		}
	}
	if (doSSL)
		req.open('POST',dssurl.replace(/^http:/,"https:") + "MultiQuery", true);
	else
		req.open('POST',dssurl + "MultiQuery", true);
	req.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	req.overrideMimeType('text/xml');
	req.onreadystatechange = function () {

		if (req.readyState == 4) {
			var stat;
			try {stat = req.status;}
			catch(staterr) {stat = 500;}
			if (stat == 200 && req.responseXML.documentElement.nodeName != "parsererror") {
				if (timeout) clearTimeout(req.timeoutId);
				DSSHandler(req.responseXML, doc, engine,firstReq,xml);
			}
			else if (retry) retryDssRequest(dssurl,dssquery,engine,doc,firstReq,xml);
		}
	};
	req.send(dssquery);
}

function remove_styles(doc) {
    if (doc.location.host.match(/tw\.search\.yahoo\.com/)) {
        for (var s = 0; s < doc.styleSheets.length; s++) {
            if (doc.styleSheets[s].href.match(/generic\.css$/)) {
                for (var r = 0; r < doc.styleSheets[s].cssRules.length; r++) {
                    if (doc.styleSheets[s].cssRules[r].selectorText == "td" && doc.styleSheets[s].cssRules[r].cssText.match(/line.height/)) {
                        doc.styleSheets[s].deleteRule(r);
                    }
                }
            }
        }
    }
	if(doc.location.host.match(/\.sky\./))
	{
        for (var s = 0; s < doc.styleSheets.length; s++)
		{
			for (var r = 0; r < doc.styleSheets[s].cssRules.length; r++)
			{
				if (doc.styleSheets[s].cssRules[r].selectorText == ".g-search-results h5 a")
				{
					var oStyle = doc.styleSheets[s].cssRules[r].style;
					var s1;
					for( var i = 0, j; i < oStyle.length; i++ )
					{
						j = oStyle.item(i);
						s1 = oStyle.getPropertyValue(j);
					}
					oStyle.removeProperty('color');
					oStyle.setProperty('color',s1,'');
				}
			}
        }
	}	
}


function insert_meta_info(doc) {
		try
		{
		if(isSA28 == false)
			document.getElementById("SAFE-TB-Combined").setAttribute("image","chrome://safe/content/safe-green.gif");

		var tooltip_style = doc.createElement("link");
		tooltip_style.setAttribute("rel","stylesheet");
		tooltip_style.setAttribute("type","text/css");
		tooltip_style.setAttribute("href","chrome://safe/locale/safe.css");
		doc.getElementsByTagName("head")[0].appendChild(tooltip_style);

		// Load tooltip main script, insert into <HEAD>
		var tooltip_js_main = doc.createElement("script");
		tooltip_js_main.setAttribute("src","chrome://safe/content/" + mainjs);
		doc.getElementsByTagName("head")[0].appendChild(tooltip_js_main);

		// Insert our invisible Background layer (BorderLayer) into the body
		var tooltip_borderlayer = doc.createElement("DIV");
		tooltip_borderlayer.setAttribute("id","BorderLayer");
		tooltip_borderlayer.setAttribute("style","font-size:medium;position:absolute;background-image:url(chrome://safe/content/yellowbubble.gif);z-index:1000;top:-100px;");

		// Insert scalable image (until css 3 comes out and we can scale the background-image)
		var bgimage = doc.createElement("IMG");
		bgimage.setAttribute("id","border_bg_image");
		bgimage.setAttribute("style","width:0px;height:0px;font-size:medium;position:absolute;z-index:1001;top:0px;left:0px;");
		tooltip_borderlayer.appendChild(bgimage);
		doc.body.appendChild(tooltip_borderlayer);

		// Insert arrow layer
		var tooltip_arrowlayer = doc.createElement("DIV");
		tooltip_arrowlayer.setAttribute("id","ArrowLayer");
		tooltip_arrowlayer.setAttribute("style","font-size:medium;visibility:hidden;position:absolute;z-index:1000;top:-100px;");
		var arrowimage = doc.createElement("IMG");
		arrowimage.setAttribute("id","arrow_bg_image");
		arrowimage.setAttribute("style","font-size:medium;width:0px;height:0px;position:absolute;z-index:1001;top:0px;left:0px;");
		tooltip_arrowlayer.appendChild(arrowimage);
		doc.body.appendChild(tooltip_arrowlayer);

		// Insert invisible layer
		var tooltip_invlayer = doc.createElement("DIV");
		tooltip_invlayer.setAttribute("id","InvLayer");
		tooltip_invlayer.setAttribute("style","font-size:medium;visibility:hidden;position:absolute;z-index:1000;top:-100px;background-image:url(chrome://safe/content/inv.gif);");
		doc.body.appendChild(tooltip_invlayer);

		// Insert our toolip layer (BubbleLayer) into the body
		var tooltip_bubblelayer = doc.createElement("DIV");
		tooltip_bubblelayer.setAttribute("id","BubbleLayer");
		tooltip_bubblelayer.setAttribute("style","font-size:medium;text-align:left;visibility:hidden;position:absolute;z-index:1002;top:-100px;line-height: 10px;");
		tooltip_bubblelayer.addEventListener("mousedown",click_handler,false);
		doc.body.appendChild(tooltip_bubblelayer);
		}
		catch(e)
		{
		}
}

function click_handler(e) {
	link = e.target;
	if (link.innerHTML && link.innerHTML == strb('safe.submitTest'))
		ext_feedback("submit",strb('safe.submittedTest'));
	else if (link.id && (link.id == 'submitsitetxt' || link.id == 'submitsiteimg'))
		ext_feedback("submit",strb('safe.submittedTest'));
}

function ext_feedback(type, msg) {
 try{
	try
	{
		if(window._content.document.location.href.match(/http:\/\/www\.uol\.com\.ar/) || window._content.document.location.href.match(/http:\/\/(www\.)?terra\.com\.co/))
		{
			var domain  = window._content.frames[0].document.getElementById("BubbleLayer").firstChild.id;
			msg = msg.replace("%url%", domain);
			window._content.frames[0].document.getElementById("BubbleLayer").firstChild.nextSibling.innerHTML = "<table width='100%' height='100%' style='table-layout:fixed;overflow:hidden;line-height:18px;'><tr VALIGN=CENTER><td style='line-height:normal;' VALIGN=CENTER><h3 class='sastyle_header_submitted' style='color:black;' align=center>" + msg + "</h3></td></tr></table>";
		}
		else
		{
			var domain  = window._content.document.getElementById("BubbleLayer").firstChild.id;
			msg = msg.replace("%url%", domain);
			if(window._content.document.location.href.match(/http:\/\/(nate|cyworld)\.search\.empas\.com/))
			{
				window._content.document.getElementById("BubbleLayer").firstChild.nextSibling.innerHTML = "<table width='100%' height='100%' style='table-layout:fixed;overflow:hidden;line-height:18px;'><tr VALIGN=CENTER><td style='line-height:normal;' VALIGN=CENTER><h2 class='sastyle_header_submitted' style='color:black;' align=center>" + msg + "</h2></td></tr></table>";
			}
			else
			{
				window._content.document.getElementById("BubbleLayer").firstChild.nextSibling.innerHTML = "<table width='100%' height='100%' style='table-layout:fixed;overflow:hidden;line-height:18px;'><tr VALIGN=CENTER><td style='line-height:normal;' VALIGN=CENTER><h3 class='sastyle_header_submitted' style='color:black;' align=center>" + msg + "</h3></td></tr></table>";
			}
		}
	}
	catch(e)
	{
		var domain  = window._content.document.getElementById("BubbleLayer").firstChild.id;
		msg = msg.replace("%url%", domain);
		window._content.document.getElementById("BubbleLayer").firstChild.nextSibling.innerHTML = "<table width='100%' height='100%' style='table-layout:fixed;overflow:hidden;line-height:18px;'><tr VALIGN=CENTER><td style='line-height:normal;' VALIGN=CENTER><h3 class='sastyle_header_submitted' style='color:black;' align=center>" + msg + "</h3></td></tr></table>";
	}

	// Form the DSS query and send it out.
	var client_version = "FF_unknown";

	try{
		client_version = "FF_" + getVersion() + "_" + getBuild();
	} catch(vererr) {}

	url = getDss() + "Feedback?version=2&" + buildArgStringSS_comp('safesearch') + "&Type=submit&name=" + domain;
	var request;
   	request = new XMLHttpRequest();
    request.onreadystatechange = null;
	if (doSSL)
		request.open("GET", url.replace(/^http:/,"https:"), true);
	else
		request.open("GET", url, true);
        request.send(null);
 } catch (e) {}
}

function onmouseclick(e)
{
    try
    {
		var node = e.target;
		while(1)
		{
			if(node.tagName && node.tagName == "A")
				break;
			node = node.parentNode;
		}
        var verdict = node.getAttribute('veridict_' + uid);
        if (postSA27)
		{
            ReportStat("PIP", "ss_" + verdict);
        }	
	    var fnptr = node.getAttribute('linkfptr_' + uid);
	    if (fnptr)
	    {
	        return fnptr(); 
	    }
    }
    catch(e)
    {
        p("onmouseclick exception caught");
    }
}

function setHSDomain(e)
{
   try
    {
		var linkInPage = e.target;
		while(1)
		{
			if(linkInPage.tagName && linkInPage.tagName == "A")
				break;
			linkInPage = linkInPage.parentNode;
		}		
		href = linkInPage.href;
        var idx;
        for(idx=0;i<LinkContainer.length;idx++)
        {
            if(LinkContainer[idx].href==href) break; 
        }
        var isHS = isHSDomain[idx];
		if(isHS!=-1)
		{
			globalClickedOn = hsUDS[idx].slice(0,hsUDS[idx].length-1);
		}
		else
		{
			globalClickedOn = "";
		}
		ReportStat("hs_click", globalClickedOn);
	}
	catch(e)
	{
	}
}

function testShowHS()
{
	try
	{
		doHackerSafe = getHackerSafe();
	}
	catch(e){}
	return (doHackerSafe || HackerSafeAnnotations);
}

function DSSHandler(xml, dom, search_engine,firstReq,firstxml) {
	restricted = globalRestricted;
	if(!firstReq)
	{
		var fdomQueryResponse = firstxml.getElementsByTagName("DomainQueryResponse")
		var domQueryResponse = xml.getElementsByTagName("DomainQueryResponse")
		var domQRArray = firstxml.getElementsByTagName("DomainQueryResponseArray")
		for(var igA=0;igA<ignoreArray.length;igA++)
		{
			try
			{
				if(navigator.userAgent.match(/Firefox\/3\./))
				{
					var nc = domQueryResponse.item(igA).cloneNode(true)
					var nc_import = firstxml.importNode(nc, true);
					domQRArray.item(0).replaceChild(nc_import,fdomQueryResponse.item(ignoreArray[igA]))
				}
				else
				{
					var nc = domQueryResponse.item(igA).cloneNode(true)
					domQRArray.item(0).replaceChild(nc,fdomQueryResponse.item(ignoreArray[igA]))
				}
			}
			catch(e)
			{
			}
		}
		xml = firstxml;
	}
	
	checkForEmergency(xml);
	pagedom = dom;
	var thislocale = getLocale();
	var domaininfo = xml.getElementsByTagName("DomainInfo");

	colors = new Array();
	icons = new Array();
	verdictStore = new Array();
	hsafeIcons = new Array();
	udsStore = new Array();
	hsUDS = new Array();
	isHSDomain = new Array();
	globalHSString = "";
	
	var execHS = testShowHS();
	
	try {
		if(!postSA27)
			var restricted = getPsurfEnabled();
	} catch(e) { //safeguard for older users
	}
	for (var i = 0; i < domaininfo.length; i++) {
		var uds = domaininfo.item(i).attributes.getNamedItem("name").nodeValue;
		var domainInfoItems = domaininfo.item(i).childNodes;
		var overall = "";
		var verdict = "";
		var color = "";
		var hrefurl = "";

		var reds = new Array();
		var yellows = new Array();
		var greens = new Array();
		var unknowns = new Array();

		var images = new Array();
		var descriptions = new Array();
		var image_source = "";

		udsStore[udsStore.length] = uds;
		var hsUDSList;
		
		for (var k = 0; k < domainInfoItems.length; k++) {
			var domainInfoItem = domainInfoItems.item(k);
			if (domainInfoItem.nodeName == "Classification") {
				classification = domainInfoItem.attributes.getNamedItem("code").nodeValue;

				if (classification == "UNKNOWN") {
					color = "white";
					colors[colors.length] = color;
				} else {
					color = domainInfoItem.attributes.getNamedItem("color").nodeValue;
					colors[colors.length] = color;
				}
				hrefurl = "http://www.siteadvisor.com/sites/" + uds;

	                        if (classification == "CERTIFIED") {
				    verdict = "Safe.";
				    image_source = "chrome://safe/content/green.gif";
				    overall = strb('safe.certMsg');
				}
	                        else if (classification == "OK") {
				    verdict = "Safe.";
				    image_source = "chrome://safe/content/green.gif";
				    overall = strb('safe.okMsg');
				}
                	        else if (classification == "INFO") {
				    vedict = "Be careful.";
				    image_source = "chrome://safe/content/yellow.gif";
				    overall = strb('safe.cautionMsg');
				}
	                        else if (classification == "WARN") {
				    verdict = "Unsafe.";
				    image_source = "chrome://safe/content/red.gif";
				    overall = strb('safe.cautionMsg');
				}
                	        else {
				    image_source = "chrome://safe/content/untested.gif";
			            verdict = "Untested.";
				    overall = strb('safe.untestedMsg');
				}
				try
				{
					if(execHS && (classification == "CERTIFIED" || classification == "OK"))
					{
						isHSDomain[i] = -1;
						hsUDS[i] = "";
						hsUDSList = domainInfoItem.attributes.getNamedItem("hackersafe").nodeValue.split(" ");
						if(hsUDSList)
						{
							var matchFound = false;
							for(var iter = 0;iter<hsUDSList.length;iter++)
							{
								//var ldomain = uds.toLowerCase().replace(/\./g,"\\.");
								var ldomain = extractHostName(fullHrefContainer[i]).toLowerCase().replace(/\./g,"\\.");
								ldomain = ldomain.replace(/\//g,"\/");
								//var exp0 = new RegExp("^(www[0-9]?\.)?" + ldomain + "0$");
								var exp0 = new RegExp(ldomain + "0$");
								var lhs = hsUDSList[iter].toLowerCase();
								if(lhs.match(exp0))
								{
									hsUDS[i] = hsUDSList[iter];
									globalHSString = hsUDSList[iter].slice(0,hsUDSList[iter].length-1) + ";" + globalHSString;
									isHSDomain[i] = 0;
									matchFound = true;
								}
								else
								{
									//var exp1 = new RegExp("^(www[0-9]?\.)?" + ldomain + "1$");
									var exp1 = new RegExp(ldomain + "1$");
									if(lhs.match(exp1))
									{
										hsUDS[i] = hsUDSList[iter];
										image_source = "chrome://safe/content/hackerSafe.gif";
										globalHSString = hsUDSList[iter].slice(0,hsUDSList[iter].length-1) + ";" + globalHSString;
										isHSDomain[i] = 1;
										matchFound = true;
									}
								}
							}
						}
					}
				}
				catch(e)
				{
				}
				verdictStore[verdictStore.length] = classification;
			}

			if (domainInfoItem.nodeName == "FacetInfos" && verdict != "Untested.") {
				var infos = domainInfoItem.childNodes;
				var count = 0;
				for (var j = 0; j < infos.length; j++) {
					var infocode = infos.item(j).attributes.getNamedItem("code").nodeValue;
					var facet;
					var facetcolor;

					if (infocode == "OK" || infocode == "CERTIFIED")
						facetcolor = "green";
					else if (infocode == "WARN")
						facetcolor = "red";
					else if (infocode == "INFO")
						 facetcolor = "yellow";
					else facetcolor = "white";

					var subdir;
					var ustr;
					if (infos.item(j).nodeName == "CommerceInfo") {
						subdir = "shopping";
						facet = "E-Shopping";
						ustr = strb('safe.unknownCommerce');
					} else if (infos.item(j).nodeName == "DownloadsInfo") {
						subdir = "downloads";
						facet = "Downloads";
						ustr = strb('safe.unknownDownloads');
					} else if (infos.item(j).nodeName == "PersonalInformationInfo") {
		                                subdir = "email";
						facet = "E-mail";
						ustr = strb('safe.unknownEmail');
					} else if (infos.item(j).nodeName == "LinksInfo") {
		                                subdir = "links";
						facet = "Linking";
						ustr = strb('safe.unknownLinks');
					} else if (infos.item(j).nodeName == "AnnoyanceInfo") {
		                                subdir = "annoyances";
						facet = "Annoyances";
						ustr = strb('safe.unknownAnnoyances');
					} else if (infos.item(j).nodeName == "RogueInfo") {
						subdir = "summary";
						facet = "Rogue";
						ustr = strb('safe.uknownExploits');
					}
					else {
						continue;
					}
					var imgsrc = "chrome://safe/content/safe-facet-" + facetcolor + ".gif";
					var desc = ustr;
					facetInfoChildren = infos.item(j).childNodes;
					for (var fic = 0; fic < facetInfoChildren.length; fic++) {
						if (facetInfoChildren.item(fic).nodeName=="short_desc") {
							if (facetInfoChildren.item(fic).hasChildNodes())
								desc = facetInfoChildren.item(fic).firstChild.nodeValue;
						}
					}
					if (desc == "" || desc.length > 50) desc = ustr;
					var descenc = "";
					for (var c = 0; c < desc.length; c++) {
					    descenc += "&#"+desc.charCodeAt(c);
					}

					if (infocode == "OK" || infocode == "CERTIFIED")
						greens[facet] = new Array(imgsrc,subdir, descenc);
					else if (infocode == "INFO")
						yellows[facet] = new Array(imgsrc,subdir, descenc);
					else if (infocode == "WARN")
						reds[facet] = new Array(imgsrc, subdir, descenc);
					else unknowns[facet] = new Array(imgsrc,subdir,descenc);
					count++;
				}
			}
		}
        
		var overallenc = "";
        for (var c = 0; c < overall.length; c++) {
		    overallenc += "&#"+overall.charCodeAt(c);
		}
        overall = overallenc;
		
		if(uds && hsUDS[i] && execHS)
			hsafeSite = 1;
		else
			hsafeSite = 0;
		
		var order = new Array("reds['Rogue']","reds['Downloads']","reds['E-mail']","reds['E-Shopping']", "reds['Linking']","reds['Annoyances']","yellows['Downloads']","yellows['E-mail']","yellows['E-Shopping']", "yellows['Linking']","yellows['Annoyances']","greens['Downloads']","greens['E-mail']","greens['E-Shopping']", "greens['Linking']","greens['Annoyances']","unknowns['Downloads']","unknowns['E-mail']","unknowns['E-Shopping']", "unknowns['Linking']","unknowns['Annoyances']");
		var iconheight = ".7512em";
		for (var o = 0; o < order.length; o++) {
			evalstr = "if (" + order[o] + ") { images[images.length] = \"<a href='\" + hrefurl + \"/\" + " + order[o] + "[1] + \"?\"+buildArgStringSS_comp('safesearch')+\"' target=_blank ><img style='font-size:medium;height:"+iconheight+";' border = 0 src='\" + " + order[o] + "[0] + \"'></a>\"; descriptions[descriptions.length] = " + order[o] + "[2];}";
			eval(evalstr);
		}

		if (!(uds.match(/siteadvisor\./) && (unescape(LinkContainer[i].href).match(/\/sites\//) || unescape(fullHrefContainer[i]).match(/\/sites\//))))
		{
			if (LinkContainer[i].href.match(/\/sitereport.psp/))
			{
				imageStyle = get_image_style(LinkContainer[i], uds, fullHrefContainer[i]);
				var newImage = make_image(imageStyle,"chrome://safe/content/siteadvisor.gif","","",dom,uds,domaininfo.item(i).xml);
				newImage.setAttribute("border","0");
				icons[icons.length] = newImage;
			}
			else
			{
				var newImage;
				var link = make_link(hrefurl + "?" + buildArgStringSS_comp('safesearch'),"","",dom);
				var linktitenc = "";
				for (var c = 0; c < LinkContainer[i].textContent.length; c++)
					linktitenc += "&#"+LinkContainer[i].textContent.charCodeAt(c);
				imageStyle = get_image_style(LinkContainer[i], uds, fullHrefContainer[i]);
				if(isHSDomain[i] && isHSDomain[i] == 1)
				{
					if(imageStyle.indexOf("width:16px")!=-1)
					{
						imageStyle = imageStyle.replace(/width:16px/,"width:68px");
					}
					else
					{
						imageStyle = imageStyle + "width:68px";
					}
				}
				var adjustLeft = 0;
				var adjustTop = 0;
				var adjustZ = 0;
				
				if(LinkContainer[i].ownerDocument.location.href.match (/(suche\.web\.de)|(suche\.gmx\.net)|(\.live\.)|(\.google\.)|(\.cox\.net)|(\.goo\.)|(\.msn\.)/))
				{
					adjustZ = 99999;
				}
				
				if (verdict != "Untested.")
				{
					var link1 = "<a class='sastyle_link_moreinfo' style='white-space:nowrap;color:#012D6F;padding-right:0.3em;font-weight:normal' href='" + hrefurl + "?" + buildArgStringSS_comp('safesearch') + "' target=_blank>"+strb('safe.moreStr')+"</a>";
					var link2 = "";

					if (showUpsellMessage && upsellUrl != null && upsellMessage != null)
					{
						link2 = "<a style='text-decoration:none;font-weight:normal' target='_blank' href='"+upsellUrl+"'><table border='0'><tr><td style='font-size:medium;line-height:normal;'><img border='0' style='font-size:medium;width:1em;' src='chrome://safe/content/protection.gif'/></td><td class='sastyle_link_upsell' style='text-decoration:none;white-space:nowrap;color:black;line-height:normal;'>"+upsellMessage+"</td></tr></table></a>";
					}

					var popup_impl = make_pop("event","",uds,verdict,overall,images[0],images[1],images[2],descriptions[0],descriptions[1],descriptions[2],link1,link2,color,"chrome://safe/content/",linktitenc,localescale[thislocale]?localescale[thislocale]:"1.3", domaininfo.item(i).xml);
				
					newImage = make_image(imageStyle, image_source, popup_impl, "shut(event)", dom,uds,domaininfo.item(i).xml);
				}
				else
				{
					var link2 = "<a style='text-decoration:none;' href='#' onclick='return false;'><table><tr><td style='line-height:normal;'><img id='submitsiteimg' border='0' style='width:1em;font-size:medium;' src='chrome://safe/content/protection.gif'/></td><td id='submitsitetxt' class='sastyle_link_submittest' style='text-decoration:none;color:black;white-space:nowrap;line-height:normal;'>"+strb('safe.submitTest')+"</td></tr></table></a>";
					
					var popup_impl = make_pop("event","",uds,verdict,overall,"","","","","","","",link2,color,"chrome://safe/content/",linktitenc,localescale[thislocale]?localescale[thislocale]:"1.2",domaininfo.item(i).xml);	
				
					newImage = make_image(imageStyle, image_source, popup_impl, "shut(event)",dom,uds,domaininfo.item(i).xml);
				}
				link.appendChild(newImage);
				link.firstChild.setAttribute("height","16");
				if(!(isHSDomain[i] && isHSDomain[i] == 1))
					link.firstChild.setAttribute("width","16");
				icons[icons.length] = link;
			}
		}
		else
		{
			imageStyle = get_image_style(LinkContainer[i], uds, fullHrefContainer[i]);
			var newImage = make_image(imageStyle,"chrome://safe/content/siteadvisor.gif","","",dom,uds,domaininfo.item(i).xml);
			newImage.setAttribute("border","0");                
			icons[icons.length] = newImage;
		}
		
		if(hsafeSite == 1)
		{
			//hackersafe code goes here
			hsafeIcons[hsafeIcons.length] = icons[icons.length];
	        LinkContainer[i].setAttribute('veridict_' + uid, verdictStore[i]);			
			if(LinkContainer[i].getAttribute("onmousedown"))
		    {
				if(search_engine == LIVE || search_engine == MSN)
				{
					LinkContainer[i].addEventListener("mousedown",setHSDomain,true);
					LinkContainer[i].addEventListener("mousedown",onmouseclick,true);
				}
				else
				{
					var fnptr1=LinkContainer[i].getAttribute("onmousedown");
					LinkContainer[i].setAttribute('linkfptr_' + uid, fnptr1);
					LinkContainer[i].addEventListener("mousedown",setHSDomain,false);
					LinkContainer[i].addEventListener("mousedown",onmouseclick,false);
				}
		    }
			else
		    {
		        if(LinkContainer[i].getAttribute("onclick") && search_engine!=WEB && search_engine!=GMX)
		        {
		            var fnptr2=LinkContainer[i].getAttribute("onclick");
		            LinkContainer[i].setAttribute('linkfptr_' + uid, fnptr2);
		            LinkContainer[i].addEventListener("mouseClick",setHSDomain,false);
					LinkContainer[i].addEventListener("mouseClick",onmouseclick,false);
				}
				else
				{
					LinkContainer[i].setAttribute('linkfptr_' + uid, null);
					LinkContainer[i].addEventListener("mousedown",setHSDomain,false);
					LinkContainer[i].addEventListener("mousedown",onmouseclick,false);
				}
			}
		}
		else
		{
			hsafeIcons[hsafeIcons.length]=0;
	        LinkContainer[i].setAttribute('veridict_' + uid, verdictStore[i]);
			if(LinkContainer[i].getAttribute("onmousedown"))
		    {
				if(search_engine == LIVE || search_engine == MSN)
				{
					LinkContainer[i].addEventListener("mousedown",onmouseclick,true);
				}
				else
				{
					var fnptr1=LinkContainer[i].getAttribute("onmousedown");
					LinkContainer[i].setAttribute('linkfptr_' + uid, fnptr1);
					LinkContainer[i].addEventListener("mousedown",onmouseclick,false);
				}
		    }
			else
		    {
		        if(LinkContainer[i].getAttribute("onclick") && search_engine!=WEB && search_engine!=GMX)
		        {
		            var fnptr2=LinkContainer[i].getAttribute("onclick");
		            LinkContainer[i].setAttribute('linkfptr_' + uid, fnptr2);
					LinkContainer[i].addEventListener("mouseClick",onmouseclick,false);
				}
				else
				{
					LinkContainer[i].setAttribute('linkfptr_' + uid, null);
					LinkContainer[i].addEventListener("mousedown",onmouseclick,false);
				}
			}
		}
		
		var resultlink = LinkContainer[i];
		var domaincolor = colors[i];
		try {
			if (premium && restricted && (domaincolor == "yellow" || domaincolor == "red") && !is_whitelisted(uds)) {
				resultlink.disabled=true;
				resultlink.style.cssText = "COLOR: #808080;";
				var text = resultlink.textContent;
				var href = "http://www.siteadvisor.com/restricted.html?domain="+uds+"&" + buildArgStringSS_comp('safesearch');
				resultlink.href = href;
				if (resultlink.textContent == href)
					resultlink.textContent = text; //fix for bug 307255
				if(search_engine==LIVE && resultlink.parentNode.parentNode.parentNode.parentNode && resultlink.parentNode.parentNode.parentNode.parentNode.id=="ads_topC")
					resultlink.childNodes[0].style.cssText = resultlink.childNodes[0].style.cssText + " COLOR: #808080;";
				if(search_engine==ASK && resultlink.className=="nu")
				{
					var traverseNode = resultlink.childNodes[0];
					while(traverseNode)
					{
						if(traverseNode.tagName && traverseNode.tagName=="SPAN" && traverseNode.id && traverseNode.id.match(/g[gb]/))
							traverseNode.style.cssText = traverseNode.style.cssText + " COLOR: #808080;";
						traverseNode=traverseNode.nextSibling;
					}		
				}
				if(search_engine==COX)
				{
					resultlink.firstChild.style.cssText = "COLOR: #808080;";
				}
				if(search_engine == AOL && resultlink.id && resultlink.id.match(/redirLink/))
				{
					resultlink.firstChild.style.cssText = resultlink.firstChild.style.cssText + "COLOR: #808080;";
				}
			}
		} catch(e) { //safeguard for older users
		}
	}
	if (execHS)
	{
		try
		{
			ReportStat("hs_view", globalHSString);
		}
		catch(e)
		{
		}
	}

	var cnt = 0;
	for (var i = 0; i < LinkContainer.length; i++) {
		var link = LinkContainer[i];
		var verdict = 0;
		var domaincolor = colors[cnt];
		for(iterator = 0; iterator < link.childNodes.length; iterator++)
        {
			if(link.ownerDocument.location.host.match(/jp\.aol\.com/))
			{
				if(link.childNodes[iterator].tagName == "BR")
					link.removeChild(link.childNodes[iterator]);
			}
        }

		var htmlcolor = null;
		var increment = true;
		if (domaincolor == "green")
			htmlcolor = "#CCFFCC";
                else if (domaincolor == "yellow")
                        htmlcolor = "#F7F995";
                else if (domaincolor == "red")
                        htmlcolor = "#FFAAAA";
                else if (domaincolor == "white") {
                        htmlcolor = " ";
			increment = false;
		}
		if (htmlcolor) {
			if (highliteon) {
                                if (link.ownerDocument.location.host.match(/\.ask\./) && link.childNodes) {
                                    for (n = 0; n < link.childNodes.length; n++) {
                                        if (link.childNodes[n].tagName == "DIV")
                                            link.childNodes[n].style.backgroundColor = htmlcolor;
                                    }
                                }
				if(link.ownerDocument.location.href.match(/(search\.aol\.(in|co(m|\.uk)))|(recherche\.aol\.fr\/aol\/search)|(suche\.aol\.de)/) || link.ownerDocument.location.href.match(/search\.myway\.com\/search/) || (search_engine==MSN || search_engine==LIVE) || search_engine == COX || (link.ownerDocument.location.href.match(/search\.yahoo\.com/)))
				{
					if(htmlcolor!= " ")
					{
						if(search_engine==AOL)
						{
							if(link.ownerDocument.location.href.match(/recherche\.aol\.fr\/aol\/search/))
							{
								if(link.className && link.className=="find")
								{
									link.innerHTML = "<span name=\"link_highlight\" font-size = 1.32em color = #000CC0 style=\"background-color: "+htmlcolor+"\">" + link.innerHTML + "</span>";
									link.firstChild.style.cssText = link.firstChild.style.cssText + "color:#000CC0 font-size:1.32em"
								}
								else
								{
									link.style.backgroundColor = htmlcolor;
								}
							}
							else
							{
								if(link.id && link.id.match(/redirLink/) && link.ownerDocument.location.href.match(/(search\.aol\.(com|in))|(suche\.aol\.de)/))
								{
									link.getElementsByTagName("SPAN")[0].style.backgroundColor = htmlcolor;
								}
								else
								{
									link.style.backgroundColor = htmlcolor;
								}
							}	
						}
						if(search_engine==MYWAY)
						{
							if(link.id.match(/^rs(t|b)/))
								link.firstChild.firstChild.nextSibling.style.backgroundColor = htmlcolor;
							else
								link.style.backgroundColor = htmlcolor;
						}
						try
						{
							if(search_engine==MSN || search_engine==LIVE)
							{
								if(link.className && link.className.match(/cbi/))
								{
									link.getElementsByTagName("H3")[0].style.backgroundColor = htmlcolor;
								}
								else
								{
									if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && ((link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/sb_ads(W|N)/)) || (link.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.id.match(/ar/))))
									{
										link.getElementsByTagName("H3")[0].style.backgroundColor = htmlcolor;
									}
									else
									{
										link.style.backgroundColor = htmlcolor;
									}
								}
							}
						}
						catch(e)
						{
						}
						if(search_engine == COX)
						{
							link.style.backgroundColor = htmlcolor;
						}
						if(search_engine==YAHOO)
							link.style.backgroundColor = htmlcolor;						
					}
				}
				else
					link.innerHTML = "<span name=\"link_highlight\" style=\"background-color: "+htmlcolor+"\">" + link.innerHTML + "</span>";
				
				if (search_engine == YAHOO || search_engine == ASK || search_engine == BIGLOBE || search_engine == UOL || search_engine == COMCAST || search_engine == TERRA)
				{
					try
					{
						if(search_engine == ASK && link.className && link.className == "nu")
							link.firstChild.firstChild.style.cssText = link.firstChild.firstChild.style.cssText + "; text-decoration:UNDERLINE;";
						else
							link.firstChild.style.cssText = link.firstChild.style.cssText + "; text-decoration:UNDERLINE;";
					}
					catch(e)
					{
					}
				}
				if (search_engine == MSN || search_engine== LIVE)
				{
					if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && ((link.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.className.match(/sb_ads(W|N)/)) || (link.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.id.match(/ar/))))
						link.getElementsByTagName("H3")[0].style.cssText + "; text-decoration:UNDERLINE;";
					else
						link.style.cssText = link.style.cssText + "; text-decoration:UNDERLINE;";
				}
				
				highlite_colors[highlite_colors.length] = htmlcolor;
			}
			if (on) {
				var endNode;
				if (search_engine == GOOGLE && link.parentNode.tagName.toLowerCase() == "h3") {
					endNode = link.parentNode.nextSibling;
					parentNode = link.parentNode.parentNode;
				} else if (search_engine == ASK && link.getElementsByTagName("DIV").length > 0) {
					parentNode = link.getElementsByTagName("DIV").item(0);
					endNode = parentNode.firstChild;
				} else {
					endNode = link.nextSibling;
					parentNode = link.parentNode;
				}
				while (endNode != null &&
                                       endNode.tagName != "TABLE" &&
                                       endNode.tagName != "BR" &&
                                       endNode.tagName != "P" &&
                                       (endNode.tagName != "A" || search_engine != ASK  || (endNode.previousSibling && endNode.previousSibling.textContent.match(/\[/))) &&
                                       (endNode.tagName != "SPAN" || search_engine != YAHOO  || !endNode.getAttribute("class") || endNode.getAttribute('class') != "sep") &&
                                       (endNode.tagName != "DIV" || !link.ownerDocument.location.hostname.match(/aol\.ca$/)) && (!link.ownerDocument.location.host.match(/search\.cn\.yahoo\.com/) || endNode.tagName!="A"))
					endNode = endNode.nextSibling;

				var imgsrcs = icons[cnt];
				if(link.ownerDocument.location.href.match(/http:\/\/(us\.)?search\.yahoo\.com\/search/) && link.className && link.className=="yschttl" && link.parentNode.nextSibling && link.parentNode.nextSibling.nodeValue && link.parentNode.nextSibling.nodeValue.match(/\(.*\)/))
				{
					parentNode = link.parentNode.parentNode;
					endNode = link.parentNode.nextSibling.nextSibling;
				}
				if(link.ownerDocument.location.href.match(/http:\/\/(us\.)?search\.yahoo\.com\/search/) && link.className && link.className=="yschttl" && link.parentNode.nextSibling && link.parentNode.nextSibling.nextSibling && link.parentNode.nextSibling.nextSibling.tagName && link.parentNode.nextSibling.nextSibling.tagName=="A")
				{
					parentNode = link.parentNode.parentNode;
					endNode = link.parentNode.nextSibling.nextSibling.nextSibling;
				}				
				if(link.ownerDocument.location.href.match(/google\.com\.tw\/search/) && link.className && link.className.match(/l/) && link.parentNode && link.parentNode.nextSibling && link.parentNode.nextSibling.tagName && link.parentNode.nextSibling.tagName=="NOBR")
				{
					parentNode = link.parentNode.parentNode;
					endNode = link.parentNode.nextSibling.nextSibling;
				}
				if(search_engine==GOOGLE && link.parentNode.nextSibling && link.parentNode.nextSibling.tagName=="FONT")
				{
					if(link.id && link.id.match(/pa/))
					{
					}
					else
					{
						if(link.className && link.className=="l" && link.parentNode.parentNode && link.parentNode.parentNode.tagName=="TD")
						{
						}
						else
						{
							if(link.parentNode && link.parentNode.className && link.parentNode.className.match(/sl/))
							{
								parentNode = link.parentNode;
								endNode = link.nextSibling;
							}
							else
							{
								parentNode=link.parentNode.parentNode;
								endNode=link.parentNode.nextSibling.nextSibling;
							}
						}
					}
				}
				if(search_engine==GOOGLE && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className.match(/g/))
				{
					if(link.parentNode && link.parentNode.nextSibling && link.parentNode.nextSibling.className && link.parentNode.nextSibling.className.match(/std/))
					{
						parentNode = link.parentNode.parentNode;
						endNode = link.parentNode.nextSibling.nextSibling;
					}
					else
					{
						parentNode = link.parentNode;
						endNode = link.nextSibling;
					}
				}
				if(search_engine==GOOGLE && (!link.className) && link.parentNode && link.parentNode.className && link.parentNode.className.match(/r/) && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className.match(/g/))
				{
					if(link.parentNode && link.parentNode.nextSibling && link.parentNode.nextSibling.className && link.parentNode.nextSibling.className.match(/std/))
					{
						parentNode = link.parentNode.parentNode;
						endNode = link.parentNode.nextSibling;
					}
					else
					{
						parentNode = link.parentNode;
						endNode = link.nextSibling;
					}
				}
				if(search_engine==GOOGLE && link.className && link.className=="l" && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode &&
				 link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.parentNode.tagName=="TD" )
				{
					parentNode = link.parentNode;
					endNode = link.nextSibling;
				}
				if(search_engine==GOOGLE && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.id=="pfe")
				{
					parentNode=link.parentNode;
					endNode=link.nextSibling;
				}
				if(search_engine==GOOGLE && link.className && link.className=="l" && link.parentNode && link.parentNode.nextSibling && link.parentNode.nextSibling.tagName && link.parentNode.nextSibling.tagName=="NOBR")
				{
					parentNode = link.parentNode.parentNode;
					endNode = link.parentNode.nextSibling.nextSibling;				
				}				
				if(search_engine==GOOGLE && link.parentNode && link.parentNode.className && link.parentNode.className.match(/sl/) && link.parentNode && link.parentNode.nextSibling && link.parentNode.nextSibling.tagName=="FONT")
				{
					parentNode = link.parentNode;
					endNode = link.nextSibling;
				}
				if(search_engine==GOOGLE && link.id && link.id.match(/pa|an/))
				{
					parentNode = link.parentNode;
					endNode = link.nextSibling;
				}
				try{imgsrcs.firstChild.hspace = 5;} catch(saerr){imgsrcs.hspace = 5;}
				imgsrcs.setAttribute("creator","SiteAdvisor");
				if(link.ownerDocument.location.host.match(/(buscador\.terra\.com\.(br|mx|pe|co)|(buscador\.terra\.cl))/) || (LinkContainer[i].ownerDocument.location.host.match(/(search\.aol\.(in|co(m|\.uk)))|(suche\.aol\.de)/) && !LinkContainer[i].id.match(/redirLink/)))
				{
					if(link.className && link.className=="find indent")
					{
						imgsrcs.style.cssText = imgsrcs.style.cssText + "; position : relative; float : left;"
						link.style.cssText = link.style.cssText + " float : left";
						link.style.width = "auto"
					}
					else
					{
						link.style.cssText = link.style.cssText + " float : left";
					}
				}
				if(link.ownerDocument.location.host.match(/search\.cn\.yahoo\.com/) && link.className && link.className=="pa")
				{
					parentNode=link;
					endNode=link.getElementsByTagName("DIV").item(0).nextSibling;
				}				
				if(link.ownerDocument.location.host.match(/ask\.jp/) && link.parentNode.className=="txtl")
				{
					parentNode=link.parentNode.parentNode;
					endNode=link.parentNode;
					endNode=endNode.nextSibling.nextSibling.nextSibling;
				}
				if(link.ownerDocument.location.host.match(/ask\.com/) && link.className && link.className=="L4 b" && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.nextSibling && link.parentNode.parentNode.nextSibling.nextSibling && link.parentNode.parentNode.nextSibling.nextSibling.firstChild && link.parentNode.parentNode.nextSibling.nextSibling.firstChild.className && link.parentNode.parentNode.nextSibling.nextSibling.firstChild.className.match(/T1/))
				{
					parentNode=link.parentNode.parentNode.parentNode;
					if(link.parentNode.parentNode.nextSibling.nextSibling.firstChild.className=="T1")
						endNode=link.parentNode.parentNode.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling;
					else
						endNode=link.parentNode.parentNode.nextSibling.nextSibling.nextSibling;
				}
				if(search_engine  == ASK && link.className == "L2" && link.parentNode && link.parentNode.className=="pl10" && link.nextSibling && link.nextSibling.nextSibling && link.nextSibling.nextSibling.className !="T10" )
				{
					parentNode = link.parentNode;
					endNode = link.nextSibling;
				}
				if(link.ownerDocument.location.host.match(/ask\.com/) && link.className && link.className=="L4 b" && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.nextSibling && link.parentNode.parentNode.nextSibling.nextSibling && link.parentNode.parentNode.nextSibling.nextSibling.tagName=="NOBR")
				{
					parentNode=link.parentNode.parentNode.parentNode;
					endNode=link.parentNode.parentNode.nextSibling.nextSibling.nextSibling;
				}
				if((search_engine==WEB || search_engine==GMX) && !(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className.match(/sponsoredLink|smartinfoContent|advertLink/)))
				{
					parentNode=link.parentNode.parentNode;
					endNode=link.parentNode.nextSibling.nextSibling.nextSibling;
				}
				if(link.ownerDocument.location.host.match(/search\.yahoo\.co\.jp/))
				{
					try
					{
						endNode=link.nextSibling.nextSibling.nextSibling.nextSibling.nextSibling;
					}
					catch(e)
					{
					}
				}
				if(link.ownerDocument.location.host.match(/hk\.search\.yahoo\.com/) && link.nextSibling.nextSibling && link.nextSibling.nextSibling.tagName=="BR")
				{
					parentNode=link.parentNode;
					endNode=link.nextSibling.nextSibling.nextSibling.nextSibling;
				}
				if(link.ownerDocument.location.href.match(/search\.myway\.com\/search/) && link.id.match(/^rs(t|b)/))
				{
					parentNode=link.firstChild;
					endNode=link.firstChild.firstChild.nextSibling.nextSibling;
				}
				if(search_engine == NATE && !(link.className && link.className.match(/link13/)) && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.parentNode.className.match(/data-ad|site|job|special|dic/))
				{
					if(link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/premium/))
					{
						parentNode = link.parentNode.parentNode;
						endNode = link.parentNode.nextSibling;
					}
					else
					{
						parentNode = link.parentNode.parentNode;
						endNode = link.parentNode.nextSibling.nextSibling.nextSibling;
					}
				}
				if(search_engine == NATE && link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "DT" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/goto/))
				{
					parentNode = link.parentNode.parentNode;
					endNode = link.parentNode.nextSibling.nextSibling.nextSibling;
				}
				if(search_engine  == NAVER && link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "DT" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.id.match(/videoItem|goto/))
				{
					parentNode = link.parentNode.parentNode;
					endNode = link.parentNode.nextSibling.nextSibling.nextSibling;
				}
				if(search_engine == NAVER && link.parentNode && link.parentNode.tagName && link.parentNode.tagName=="DT" && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/type01/))
				{
					if(link.parentNode.nextSibling && link.parentNode.nextSibling.nextSibling && link.parentNode.nextSibling.nextSibling.className && link.parentNode.nextSibling.nextSibling.className.match(/txt_inline/))
					{
						parentNode = link.parentNode.parentNode;
						endNode = link.parentNode.nextSibling.nextSibling.nextSibling.nextSibling;
					}
				}				
				if(link.ownerDocument.location.href.match(/\/\/((in|us)\.)?search\.yahoo\.com/) && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/spns/))
				{
					try
					{
						link.parentNode.parentNode.parentNode.parentNode.style.cssText = link.parentNode.parentNode.parentNode.parentNode.style.cssText + "; position : relative; width : auto;";
						link.parentNode.style.cssText = link.parentNode.style.cssText + "; position : relative; width : auto; ";
						link.style.cssText = link.style.cssText + "; position : relative; width : auto; float : left;"
						parentNode = link.parentNode;
						endNode = link.nextSibling;
					}
					catch(e)
					{
					}
				}
				if(search_engine == YAHOO && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.parentNode.parentNode.id.match(/sec|east/))
				{
					link.style.cssText = link.style.cssText + "; width : auto; display:inline; ";
					imgsrcs.style.cssText = imgsrcs.style.cssText + "; display:inline;"
					parentNode = link.parentNode;
					endNode = link.nextSibling;
				}
				if(search_engine == YAHOO && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode &&  link.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.id.match(/sec|east/))
				{
					link.style.cssText = link.style.cssText + "; width : auto; display:inline; ";
					imgsrcs.style.cssText = imgsrcs.style.cssText + "; display:inline;"
					parentNode = link.parentNode;
					endNode = link.nextSibling;
				}
				if(link.ownerDocument.location.href.match(/aol\.fr/) && link.className && link.className=="find")
				{
					parentNode = link.parentNode;
					endNode = link.nextSibling;
					link.style.cssText = link.style.cssText + "float:left";
					imgsrcs.style.cssText = imgsrcs.style.cssText + "; position : relative; float : left;"
				}				
				
				if(search_engine==AOL && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className.match(/mod templ t2/))
				{
					parentNode = link.parentNode;
					endNode = link.nextSibling;
				}				
				if(search_engine == NIFTY && link.className && link.className == "l" && link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "LI")
				{
					parentNode = link.parentNode;
					endNode = link.nextSibling;
				}
				if(search_engine == MYWAY && link.id && link.id.match(/rst|rsb/))
				{
					parentNode = link.parentNode;
					endNode = link.nextSibling;
				}
				if(link.ownerDocument.location.host.match(/(www|de|uk|fr|it|nl|es)\.ask\.com/) && link.className=="nu")
				{
					space = dom.createElement("SPAN");
                    space.innerHTML = "&nbsp;";
					link.parentNode.insertBefore(imgsrcs,link.nextSibling)
					link.parentNode.insertBefore(space,link.nextSibling);
				}
				else
					parentNode.insertBefore(imgsrcs,endNode);

				if(search_engine == NIFTY && link.className && link.className == "l" && link.parentNode && link.parentNode.tagName && link.parentNode.tagName == "LI")
				{
					space = dom.createElement("SPAN");
                    space.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					link.parentNode.insertBefore(space,link.nextSibling.nextSibling);
				}
				if(search_engine == GOOGLE && link.className && link.className == "l" && link.parentNode && link.parentNode.tagName == "TD")
				{
					space = dom.createElement("SPAN");
					if (isHSDomain[i] != -1)
						space.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					else
						space.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
					parentNode.insertBefore(space,endNode);
				}
				if(search_engine == MYWAY && link.id && link.id.match(/rst|rsb/))
				{
					var br = dom.createElement("BR");
					parentNode.insertBefore(br,endNode);
				}
				if((search_engine == MSN || search_engine == LIVE) && link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.className && link.parentNode.parentNode.className=="child")
				{
					var br = dom.createElement("BR");
					parentNode.insertBefore(br,endNode);
				}
				if(search_engine==GOOGLE && link.className && link.className.match(/l/) && link.parentNode && link.parentNode.className && link.parentNode.className.match(/sl/))
				{
					space = dom.createElement("SPAN");
                    space.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					link.parentNode.insertBefore(space,link.nextSibling.nextSibling)
				}
				
				if(link.parentNode && link.parentNode.parentNode && link.parentNode.parentNode.parentNode && link.parentNode.parentNode.parentNode.id && link.parentNode.parentNode.parentNode.id.match(/at/))
				{
					var lb = dom.createElement("BR");
					link.parentNode.insertBefore(lb,link.nextSibling.netxSibling);
				}
				
				if(search_engine==MSN || search_engine==LIVE)
				{
					try
					{
						if(OffsetLeft(imgsrcs)==23)
						{
							var lineBreak = pagedom.createElement("BR");
							parentNode.insertBefore(lineBreak,imgsrcs.nextSibling);
						}
					}
					catch(e)
					{
					}
				}
				if((search_engine==LIVE || search_engine==MSN) && link.parentNode.parentNode.parentNode.parentNode.className && link.parentNode.parentNode.parentNode.parentNode.className.match(/answer_news_body/))
				{
					space = dom.createElement("SPAN");
                    space.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;";
					link.parentNode.insertBefore(space,endNode);
				}
				if(link.ownerDocument.location.href.match(/(search\.aol\.(in|co(m|\.uk)))|(suche\.aol\.de)/) && link.className && link.className=="find indent")
				{
					space = pagedom.createElement("SPAN");
					space.innerHTML = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
					link.parentNode.insertBefore(space,imgsrcs)
				}
				if(link.ownerDocument.location.host.match(/(buscador\.terra\.com\.(br|mx|pe|co)|(buscador\.terra\.cl))/) || (link.ownerDocument.location.host.match(/ask\.jp/) && link.parentNode.className=="lbox"))
				{
					space = dom.createElement("BR");
                    parentNode.insertBefore(space,endNode);
				}
				if(search_engine==BIGLOBE && link.parentNode.className && link.parentNode.className.match(/hitTitle/))
				{
					space = dom.createElement("SPAN");
					space.innerHTML = "&nbsp;&nbsp;";
					parentNode.insertBefore(space,endNode);
				}				
                if (link.ownerDocument.location.host.match(/jp\.aol\.com/))
				{
					parentNode.insertBefore(pagedom.createElement("BR"),endNode);
					var linebreak_new = pagedom.createElement("BR");
					var parentNode_new = link.parentNode.parentNode.parentNode;
					var endNode_new = link.parentNode.parentNode.nextSibling;
					parentNode_new.insertBefore(linebreak_new,endNode_new);					
				}
				if(link.ownerDocument.location.host.match(/search\.cn\.yahoo\.com/) && link.parentNode.className=="rc" && link.nextSibling.nextSibling.href.match(/search.p4p.yahoo.com.cn/))
				{
						var linebreak = pagedom.createElement("BR");
                        parentNode.insertBefore(linebreak,endNode);
				}
				if(LinkContainer[i].ownerDocument.location.href.match(/(search\.aol\.com\/aol(com)?\/(search|afe))|(search\.aol\.in)|(aim\.search\.aol\.com)|(suche\.aol\.de)/) && LinkContainer[i].id.match(/redirLink/))
				{
					var linebreak = pagedom.createElement("BR");
					parentNode.insertBefore(linebreak,endNode);
				}
				if(link.ownerDocument.location.href.match(/search\.yahoo\.com/) && link.className && link.className == "yschttl withvidthmb")
				{
					var space = dom.createElement("SPAN");
					space.innerHTML = "&nbsp;";
					link.parentNode.insertBefore(space,imgsrcs);
				}				
                if (link.ownerDocument.location.host.match(/tw\.search\.yahoo\.com/)) imgsrcs.firstChild.style.top = imgsrcs.offsetTop + "px";
				overlay_objects[overlay_objects.length] = imgsrcs;
				try
				{
					if (imgsrcs.previousSibling && imgsrcs.previousSibling.getAttribute && imgsrcs.previousSibling.getAttribute("creator") == "SiteAdvisor")
						imgsrcs.firstChild.hspace = imgsrcs.previousSibling.firstChild.hspace + 27;

					if(search_engine==TERRA && imgsrcs.previousSibling && imgsrcs.previousSibling.hasAttribute && imgsrcs.previousSibling.getAttribute("href"))
					{
						try
						{
							if(imgsrcs.getAttribute("href")==imgsrcs.previousSibling.getAttribute("href"))
							{
								imgsrcs.parentNode.removeChild(imgsrcs.previousSibling)
								imgsrcs.parentNode.removeChild(imgsrcs.nextSibling)
							}
						}
						catch(e)
						{
						}
					}
					if(search_engine==TERRA && imgsrcs.previousSibling && imgsrcs.previousSibling.tagName.toLowerCase()=="img")
					{
						try
						{
							if(imgsrcs.getAttribute("src")==imgsrcs.previousSibling.getAttribute("src"))
							{
								imgsrcs.parentNode.removeChild(imgsrcs.previousSibling)
								imgsrcs.parentNode.removeChild(imgsrcs.nextSibling)
							}
						}
						catch(e)
						{
						}
					}
				}
				catch(e)
				{
				}
			}
		}
		cnt++;
	}
	if(search_engine==TERRA && cnt>0)
	{
		var lb1 = pagedom.createElement("BR");
		var ifr = pagedom.getElementById("rodape")
		try
		{
			ifr.parentNode.insertBefore(lb1,ifr);
			//ifr.parentNode.insertBefore(lb2,ifr);
		}
		catch(e)
		{
		}
	}	
}

function TestYahooBanner(search_engine, link)
{
	try
	{
		if(search_engine == YAHOO && link.parentNode && link.parentNode.nextSibling && link.parentNode.nextSibling.id && link.parentNode.nextSibling.id.match(/yss/))
		{
			return true;
		}
		return false;
	}
	catch(e)
	{
	}
	return false;
}

function get_image_style(linkInPage, uds, fullHref)
{
	var imageStyle = "";
	if (!(uds.match(/siteadvisor\./) && (unescape(linkInPage.href).match(/\/sites\//) || unescape(fullHref).match(/\/sites\//))))
	{
		if (linkInPage.href.match(/\/sitereport.psp/))
		{
			if(linkInPage.ownerDocument.location.host.match(/(buscador\.terra\.com\.(br|mx|pe|co)|(buscador\.terra\.cl))/) || (linkInPage.ownerDocument.location.host.match(/(search\.aol\.(in|co(m|\.uk)))|(suche\.aol\.de)/) && !linkInPage.id.match(/redirLink/)) || linkInPage.ownerDocument.location.host.match(/search\.comcast\.net/) || (linkInPage.ownerDocument.location.host.match(/hk\.search\.yahoo\.com/)  && linkInPage.nextSibling.nextSibling && linkInPage.nextSibling.nextSibling.tagName=="BR") || (linkInPage.ownerDocument.location.href.match(/http:\/\/((in|us)\.)?search\.yahoo\.com/) && linkInPage.parentNode && linkInPage.parentNode.parentNode && linkInPage.parentNode.parentNode.parentNode))
			{
				imageStyle = "font-size:medium;height:16px;z-index:999;";
			}
			else
			{
				imageStyle = "font-size:medium;height:16px;position:absolute;z-index:999;";
			}
		}
		else
		{
			if(linkInPage.ownerDocument.location.host.match(/(buscador\.terra\.com\.(br|mx|pe|co)|(buscador\.terra\.cl))/) || (linkInPage.ownerDocument.location.host.match(/(search\.aol\.(in|co(m|\.uk)))|(suche\.aol\.de)/) && !linkInPage.id.match(/redirLink/)) || linkInPage.ownerDocument.location.host.match(/search\.comcast\.net/) || (linkInPage.ownerDocument.location.host.match(/hk\.search\.yahoo\.com/)  && linkInPage.nextSibling.nextSibling && linkInPage.nextSibling.nextSibling.tagName=="BR") || (linkInPage.ownerDocument.location.href.match(/http:\/\/((in|us)\.)?search\.yahoo\.com/) && linkInPage.parentNode && linkInPage.parentNode.parentNode && linkInPage.parentNode.parentNode.parentNode))
			{
				imageStyle = "font-size:medium;height:1em;z-index:999;margin-left:2px;";
			}
			else
			{
				imageStyle = "font-size:medium;height:1em;position:absolute;z-index:999;";
			}
		}
	}
	else
	{
		if(linkInPage.ownerDocument.location.host.match(/(buscador\.terra\.com\.(br|mx|pe|co)|(buscador\.terra\.cl))/) || (linkInPage.ownerDocument.location.host.match(/(search\.aol\.(in|co(m|\.uk)))|(suche\.aol\.de)/) && !linkInPage.id.match(/redirLink/)) || linkInPage.ownerDocument.location.host.match(/search\.comcast\.net/) || (linkInPage.ownerDocument.location.host.match(/hk\.search\.yahoo\.com/)  && linkInPage.nextSibling.nextSibling && linkInPage.nextSibling.nextSibling.tagName=="BR") || (linkInPage.ownerDocument.location.href.match(/http:\/\/((in|us)\.)?search\.yahoo\.com/) && linkInPage.parentNode && linkInPage.parentNode.parentNode && linkInPage.parentNode.parentNode.parentNode))
		{
			imageStyle = "font-size:medium;height:16px;z-index:999;";
		}
		else
		{
			imageStyle = "font-size:medium;height:16px;position:absolute;z-index:999;";
		}
	}
	if(linkInPage.ownerDocument.location.href.match(/ask\.com/))
	{
		imageStyle = "height:16px;width:16px;position:absolute;z-index:0;"
	}	
	return imageStyle;
}

function OffsetLeft(elm) {
	var l = elm.offsetLeft;
	while (elm.offsetParent) {
		elm = elm.offsetParent;
		l += elm.offsetLeft;
	}
	return l;
}

function make_link(ref, mover, mout, dom) {
	var link = dom.createElement("A");
	link.setAttribute("style","font-size:medium;");
	link.setAttribute("href",ref);
	link.setAttribute("onmouseover",mover);
	link.setAttribute("onmouseout",mout);
	link.setAttribute("target","_blank");
	link.addEventListener("click",function (e) {e.cancelBubble = true;}, true);
	return link;
}

function make_image(sty,source,mover,mout,dom,uds,xml) {
	var image = dom.createElement("IMG");
	image.setAttribute("border","0");
	image.setAttribute("style",sty);
	image.setAttribute("src",source);
    if (customSSBalloon){
        try {
			var imageSrc = GetImage(source,uds,xml);
            image.setAttribute("src", "chrome://safe/content/" + imageSrc);
        }catch(e){
        }
    }	
	image.setAttribute("onmouseover",mover);
	image.setAttribute("onmouseout",mout);
	return image;
}

function make_pop(e,tit,domain,verdict,overall,image1,image2,image3,desc1,desc2,desc3,link1,link2,col,p,linktitenc,locscale,xml)
{
    try
    {
        if (customSSBalloon)
        {
            return GetPopupJS(e,tit,domain,verdict,overall,image1,image2,image3,desc1,desc2,desc3,link1,link2,col,p,linktitenc,locscale, xml);
        }
    }
    catch(e)
    {
    }

    return  'pop(' 
                + e
                + ',' 
                + '"' + domain + '"' 
                + ',' 
                + '"' + verdict + '"' 
                + ',' 
                + '"' + overall + '"' 
                + ',' 
                + '"' + image1 + '"' 
                + ',' 
                + '"' + image2 + '"' 
                + ',' 
                + '"' + image3 + '"' 
                + ',' 
                + '"' + desc1 + '"'
                + ',' 
                + '"' + desc2 + '"'
                + ',' 
                + '"' + desc3 + '"' 
                + ',' 
                + '"' + link1 + '"' 
                + ',' 
                + '"' + link2 + '"' 
                + ',' 
                + '"' + col + '"' 
                + ',' 
                + '"' + linktitenc + '"' 
                + ',' 
                + locscale + ')';

}

function checkForEmergency(xml) {
	if ((typeof(alreadyWarned) != "undefined") && (alreadyWarned == true)) return;
	var urr = xml.getElementsByTagName("UpgradeRequiredResponse");
	if (urr.length == 0) return;
	urr = urr[0].childNodes;
	for (var i = 0; i < urr.length; i++) {
		var c = urr.item(i);
		if (c.nodeName.toLowerCase() == "description") {
			p(c.firstChild.nodeValue);
			alreadyWarned = true;
		}
	}
}


var affid = getAffid();
var showUpsellMessage = getShowUpsellMessage();
var upsellMessage = getUpsellMessage();
var upsellUrl = getUpsellUrl();

completejavascript = true;

//*******************************************
//Start webmail code

var debuglevel = 0;
var restricted = psurf;
var dssurl = getDss();
var webmailTimeout = null;

var LinkContainer = new Array();
var hrefContainer = new Array();
var whiteList = whitelistedDomains.split(";");

function is_whitelistedURL(url) {
	try
	{
		var wldomain = url.toLowerCase().replace(/\./g,"\\.")
		wldomain = wldomain.replace(/\//g,"\/");
		var exp = new RegExp(wldomain);
		for(var i = 0; i < whiteList.length; i++)
		{
			var ldomain = whiteList[i].toLowerCase();
			if(ldomain.match(exp))
			{
				return true;
			}
		}
	}
	catch(e)
	{
	}
	return false;
}

function is_whitelisted(wdomain) {
	var wldomain = wdomain.toLowerCase().replace(/\./g,"\\.");
	wldomain = wldomain.replace(/\//g,"\/");
	var exp = new RegExp("^([^/]*\\.)?"+wldomain+"(/.*)?$");
	for(var i = 0; i < whiteList.length; i++)
	{
		if(whiteList[i] == "") continue;
		var ldomain = whiteList[i].toLowerCase();
		if(ldomain.match(exp))
		{
			return true;
		}
	}
	return false;
}

var linkInfo = function(link, domain_specifier, score, popularity, ref, isPhishing) {
	this.link = link;
	this.href = link.getAttribute('real_href');
	
	this.whitelisted = is_whitelistedURL(this.href);

	if(this.whitelisted)
	{
	}
	else
	{
		this.whitelisted = is_whitelisted(domain_specifier);
	}

	this.ref = ref;
	this.classification = score;
	this.domain_specifier = domain_specifier;
	this.score = score;

	if(isPhishing && !this.whitelisted) {
		this.dossier_href = "http://www.siteadvisor.com/phishing.html?domain=" + this.href + "&" + buildArgStringSS_comp('safehook');
		this.isPhishing = isPhishing;
		score = "WARN";
	} else {
		this.dossier_href = "http://www.siteadvisor.com/sites/" + domain_specifier + "?" + buildArgStringSS_comp('safehook');
		this.isPhishing = false;
	}
	this.isUnsafe = false;
	if (score == "CERTIFIED") {
		this.image_source = "chrome://safe/content/green.gif";
		this.color = "GREEN";
		this.value = 4;
		this.background_color = "#D9EAE2";
		this.foreground_color = "#00753B";
	}
	else if (score == "OK") {
		this.image_source = "chrome://safe/content/safe-green.gif";
		this.color = "GREEN";
		this.value = 3;
		this.background_color = "#D9EAE2";
		this.foreground_color = "#00753B";
	}
	else if (score == "INFO") {
		this.image_source = "chrome://safe/content/yellow.gif";
		this.color = "YELLOW";
		this.value = 2;
		if(!this.whitelisted)
			this.isUnsafe = true;
		this.background_color = "#FFF8E0";
		this.foreground_color = "#FFCC00";
	}
	else if (score == "WARN") {
		this.image_source = "chrome://safe/content/red.gif";
		this.color = "RED";
		this.value = 1;
		if(!this.whitelisted)
			this.isUnsafe = true;
		this.background_color = "#F9E2E8";
		this.foreground_color = "#CC0033";
	}
	else {
		this.image_source = "chrome://safe/content/untested.gif";
		this.color = "GREY";
		this.value = 5;
		this.background_color = "#E1E1E1";
		this.foreground_color = "#999999";
	}
	return this;
}

function linkPresent(listed_links,dom_spec)
{
	for(var i = 0; i < listed_links.length; i++)
	{
		if(listed_links[i] == dom_spec)
		{
			return true;
		}
	}
}

function create_banner(doc, links) {

	globaldoc = document;
	var newDiv = globaldoc.createElement('DIV');
	newDiv.id = "SAWARN_BANNER"+uid;
	newDiv.name = "SAWARN_BANNER"+uid;
	var custom = globaldoc.createElement('SA');
	custom.id = "SAWARN_DELETE";
	newDiv.appendChild(custom);
	var newSpan = globaldoc.createElement('SPAN');
	newSpan.style.cssText ="FONT-WEIGHT: BOLD; FONT-SIZE: 11px; FONT-FAMILY: verdana; FLOAT: none; COLOR: black;";
	newSpan.appendChild(globaldoc.createTextNode(strb('safehook.bannerWarn')));
	var textregex = '\\s*'+strb('safehook.bannerWarn')+'\\s*<br>';
	newSpan.appendChild(globaldoc.createElement("BR"));
	newSpan.appendChild(globaldoc.createTextNode(strb('safehook.unsafeLinks')));
	textregex += '\\s*'+strb('safehook.unsafeLinks')+'\\s*<br>';
	newSpan.appendChild(globaldoc.createElement("BR"));

	var scoring_link = null;
	var imagesOn = true;
	var listed_links = new Array();
	var hasPhishing = false;
	var linkString = "";
	for (var i = 0; i < links.length; i++)
	{
		var link_info = links[i];
		var link = link_info.link;
		var isPhishing = link_info.isPhishing;
		try {
			var href = link.real_href;
		} catch(e) {
			//the dom may have changed, so don't annotate
			return;
		}
		if(scoring_link == null || link_info.value < scoring_link.value)
			scoring_link = link_info;

		if (linkPresent(listed_links,link_info.domain_specifier)) continue;
	
		if(isPhishing || link_info.isUnsafe) {
			var linkSpan = globaldoc.createElement('SPAN');
			linkSpan.style.cssText = "FONT-WEIGHT: NORMAL; FONT-FAMILY: verdana; FONT-SIZE: 11px;  PADDING-LEFT: 15px;";
			linkSpan.appendChild(globaldoc.createTextNode(link_info.domain_specifier+" "));
			textregex += '\\s*'+link_info.domain_specifier+'\\s*<br>';
			var linkLink = globaldoc.createElement("A");
			linkLink.id='SAWARN'+uid;
			linkLink.setAttribute('href',link_info.dossier_href);
			linkLink.setAttribute('title',strb('safehook.moreInfoElipses'));
			linkLink.setAttribute('target',"_blank");
			if(imagesOn) {
				var linkImg = globaldoc.createElement("IMG");
				linkImg.setAttribute('src',link_info.image_source);
				linkImg.setAttribute('unselectable',"on");
				linkImg.setAttribute('contenteditable',false);
				linkImg.setAttribute('border',0);
				linkImg.setAttribute('alt',strb('safehook.moreInfoElipses'));
				linkLink.appendChild(linkImg);
			} else {
				linkLink.appendChild(globaldoc.createTextNode("("+strb('safehook.moreInfo')+")"));
			}
			linkSpan.appendChild(linkLink);
			newSpan.appendChild(linkSpan);
			newSpan.appendChild(globaldoc.createElement('br'));
			listed_links.push(link_info.domain_specifier);
		}
	}
	var bannerMessage = strb('safehook.bannerWarn')+"<br>"+strb('safehook.unsafeLinks');
	newDiv.style.cssText = "BACKGROUND-IMAGE: none; VISIBILITY: visible; WIDTH: 96%; BORDER: "+scoring_link.foreground_color+" 5px SOLID; BACKGROUND-COLOR: "+scoring_link.background_color+"; LAYER-BACKGROUND-COLOR:"+scoring_link.background_color+"; PADDING-TOP: 8px;  PADDING-BOTTOM: 8px;  PADDING-LEFT: 9px;  PADDING-RIGHT: 9px;";
	textregex += '\\s*<br>';
	newDiv.appendChild(newSpan);
	newDiv.setAttribute('textregex',textregex);
	return newDiv;
}
function restrict_link(link, newurl, domainSpecifier) {
	if(!link.parentNode) return;
	try {
		//var text = link.textContent;
		link.setAttribute('original_href',link.href);
		link.href = newurl;
		link.setAttribute('original_style',link.style.cssText);
		link.style.cssText = "COLOR: #808080;";
		//link.textContent = text;
		link.setAttribute('original_target',link.target);
		link.setAttribute('target','_blank');
		link.setAttribute('original_title',link.title);
		link.title = strb('safehook.blocked');
	} catch(e) {
		p(e.message);
	}
	return;
}

function Integer(x) {
	return ( x > 0 ? Math.floor( x ) : Math.ceil ( x ) )
}
function p(str) {
	if(debuglevel >= 1) {
		try {
		} catch(e) {
			throw(e);
		}
	}
}

function attachWebmailWatcher(fWatcher,dssurl,doc) {
	try {
		if (webmailTimeout) clearTimeout(webmailTimeout);
		if(!premium) return;
	}catch(e){ //hack for backwards-compatibility
		return;
	}
	fWatcher (dssurl,doc);
}
function hasParent(obj, parentName, stopID) {
	try {
		var parent = obj.parentNode;
	} catch (e) {
		return false;
	}
	if(!parent) {
		return false;
	}
	if(parent.id == parentName) {
		return true;
	} else if(parent.id == stopID) {
		return false;
	}
	return hasParent(parent, parentName, stopID);
}
function gmailBasicHtmlWatcher(dssurl,doc) {
	genericWebmailWatcher(dssurl,doc,function () {
		var divdocs = new Array();
		var alldiv = doc.getElementsByTagName("DIV")
		for(var i = 0; i < alldiv.length ; i++)
		{
			if(alldiv.item(i).className && alldiv.item(i).className=="msg")
				divdocs[divdocs.length]=alldiv.item(i)
		}
		if(divdocs.length > 0)
			return divdocs;
		else
			return null;
	},
		function (link) {try {return link.href;} catch(e) {}});
}

function newGmailWatcher(dssurl,doc)
{
	genericWebmailWatcher(dssurl,doc,function () {
		var framedocs = new Array();
		var framedoc = window._content.frames[1].document;
		if(framedoc)
		{
			var allDiv = framedoc.getElementsByTagName("DIV");
			for(var i = 0; i < allDiv.length; i++)
			{
				if(allDiv.item(i).id && allDiv.item(i).id.match(/1(e|f)/) && allDiv.item(i).className && allDiv.item(i).className.match(/ArwC7c ckChnd/))
				{
					framedocs[framedocs.length] = allDiv.item(i);
				}
			}
			if(framedocs.length > 0)
				return framedocs;
			else
				return null;			
		}
	},
	function (link) {try {return link.href;} catch(e) {}});
}

function gmailWatcher(dssurl,doc) {
		genericWebmailWatcher(dssurl,doc,function () {
			var framedocs = new Array();
			for(var i = 0; i < window._content.frames.length; i++) {
				try {
					var framedoc = window._content.frames[i];
				} catch (e) {
					continue;
				}
				try {
					var framename = framedoc.name;
				} catch (e) {
					continue;
				}
				if(framedoc != null && framename == "main") {
					for(var j = 0; j < framedoc.frames.length; j++) {
						try {
							var nestedFrame = framedoc.frames[j];
						} catch (e) {
							continue;
						}
						try {
							var nestedFrameName = nestedFrame.name;
						} catch (e) {
							continue;
						}
						if(nestedFrame != null && nestedFrameName.match(/v[0-9]/)) {
							try {
								var k = 0;
								var mb0;
								while((mb0 = nestedFrame.document.getElementById("mb_"+k)) != null) {
									framedocs[framedocs.length] = mb0;
									k++;
								}
							} catch(e) {
								continue;
							}
							try {
								//see if edit box exists
								var k = 0;
								for( var k = 0; k < 10; k++) {
									var editBox = nestedFrame.document.getElementById("hc_"+k);
									if(editBox != null) {
										var banner = editBox.contentDocument.getElementById("SAWARN_BANNER"+uid);
										if(banner != null) {
											banner.parentNode.removeChild(banner);
										} else {  //could be text
											try {
												var banner2 = nestedFrame.document.getElementById("SAWARN_BANNER"+uid);
												if(banner2 != null) {
													var regexstr = banner2.getAttribute('textregex');
													var re=new RegExp(regexstr, "gi")
													if(editBox.contentDocument.body.innerHTML.match(re) != null) {
														if(editBox.contentDocument.body.innerHTML != editBox.contentDocument.body.innerHTML.replace(re,''))
														{
															editBox.contentDocument.body.innerHTML = editBox.contentDocument.body.innerHTML.replace(re,'');
														}
													}
												}
											} catch(e) {
												p('error:'+e.message);
											}
										}
										var links = editBox.contentDocument.getElementsByName("SAWARN"+uid);
										for(var m=0; m < links.length; m++) {
											try{
												var link = links[m];

												if(link.getAttribute('original_href')) {
													link.href = link.getAttribute('original_href');
												}
												//link.href = link.textContent;
												link.id = link.getAttribute('original_id');
												link.removeAttribute('original_id');
												link.name = link.getAttribute('original_name');
												link.removeAttribute('original_name');
												link.style.cssText = link.getAttribute('original_style');
												link.removeAttribute('original_style');
												link.target = link.getAttribute('original_target');
												link.removeAttribute('original_target');
												link.title = link.getAttribute('original_title');
												link.removeAttribute('original_title');
												link.removeAttribute('real_href');

												//link.textContent = textContent;
											} catch(e) {
												p('could not restore link: '+e);
												continue;
											}
										}
									}
									var textBox = nestedFrame.document.getElementById("ta_"+k);
									if(textBox != null) { //handle plain text messages
										try {
											var banner2 = nestedFrame.document.getElementById("SAWARN_BANNER"+uid);
											if(banner2 != null) {
												var regexstr = banner2.getAttribute('textregex');
												var re=new RegExp(regexstr.replace(/<br>/ig,'&gt;'), "gi")
												if(textBox.innerHTML.match(re) != null)
												{
													if(textBox.innerHTML != textBox.innerHTML.replace(re,''))
														textBox.innerHTML = textBox.innerHTML.replace(re,'');
												}
											}
										} catch(e) {
											p('error:'+e.message);
										}
									}
								}
							} catch(e) {
								p('could not delete banner'+e.message);
							}
						}
					}
				}
			}
			if(framedocs.length > 0)
				return framedocs;
			else
			{
				var framedocs = new Array();
				for(var d = window._content.frames.length - 1; d >= 0; d--)
				{
					var framedoc = window._content.frames[d].document;
					if(framedoc)
					{
						var allDiv = framedoc.getElementsByTagName("DIV");
						for(var i = 0; i < allDiv.length; i++)
						{
							if(allDiv.item(i).id && allDiv.item(i).id.match(/(1(e|f))|(:[0-9a-zA-Z]+)/) && allDiv.item(i).className && allDiv.item(i).className.match(/ArwC7c ckChnd/))
							{
								framedocs[framedocs.length] = allDiv.item(i);
								break;
							}
						}
					}
				}
				if(framedocs.length > 0)
					return framedocs;
				else
					return null;
			}
		},
		function (link) {try {return link.href;} catch(e) {}});
}

function yahooMailWatcher(dssurl,doc) {
	genericWebmailWatcher(dssurl,doc,function () {
		var framedocs = new Array();
		var allDiv = doc.getElementsByTagName('DIV');
		for(var idx=0;idx<allDiv.length;idx++)
		{
			if(allDiv.item(idx).id && allDiv.item(idx).id.match(/message/) && allDiv.item(idx).className && allDiv.item(idx).className.match(/msgbody|clearfix/))
			{
				framedocs[framedocs.length] = allDiv.item(idx);
			}
		}
		if(framedocs)
			return framedocs;
	}, function (link) {return link.href;});
}

function yahooAjaxMailWatcher(dssurl,doc) {
	genericWebmailWatcher(dssurl,doc,function () {
			var framedocs = new Array();
			for(var i = 0; i < window._content.frames.length; i++) {
				try {
					var framedoc = window._content.frames[i];
				} catch (e) {
					p("ERROR: error when getting framedoc "+i+": " + e.message);
					continue;
				}
				try {
					var framename = framedoc.name;
				} catch (e) {
					p("ERROR: error when getting framedoc name: " + e.message);
					continue;
				}
				if(framedoc && framename == "msgAreaFrame") {
					try {
						var child = framedoc.document.body;
					} catch (e) {
						p("ERROR: error when getting framedoc "+i+" child: " + e.message);
						continue;
					}
					framedocs[framedocs.length] = child;
				}
				else if (framedoc && framename == "message") {
					for(var j = 0; j < framedoc.frames.length; j++) {
						try {
							var nestedFrame = window._content.frames[i].frames[j];
						} catch (e) {
							p("ERROR: error when getting nested framedoc "+i+"."+j+": "+ e.message);
							continue;
						}
						try {
							var nestedFrameName = nestedFrame.name;
						} catch (e) {
							p("ERROR: error when getting nested framedoc "+i+"."+j+" name: "+ e.message);
							continue;
						}
						if(nestedFrame && nestedFrameName == "msgAreaFrame") {
							try {
								var child = nestedFrame.document.body;
							} catch (e) {
								p("ERROR: error when getting nested framedoc "+i+"."+j+" child: "+ e.message);
								continue;
							}
							framedocs[framedocs.length] = child;
						}
					}
				}
			}

			if(framedocs.length > 0)
				return framedocs;
			else
				return null;
		},
		function (link) {return link.href;});
}

function hotmailClassicWatcher(dssurl,doc) {
	genericWebmailWatcher(dssurl, doc, function () {
		return new Array(doc.getElementById("MsgContainer"));
	}, function (link) {return link.href;})
}

function hotmailMailWatcher(dssurl,doc) {
	genericWebmailWatcher(dssurl, doc, function () {
			try {
				var framedocs = new Array();
				var cell;
				for ( var i = 0; i < window._content.document.body.childNodes.length; i++){
					try {
						cell = window._content.document.body.childNodes[i].childNodes[0].childNodes[0].childNodes[1].childNodes[2].childNodes[0].childNodes[0].childNodes[0];
						if(cell != null && cell.tagName == 'TD') {
							framedocs[framedocs.length] = cell;
						}
					} catch(e) {
					}
					try {
						cell = window._content.document.body.childNodes[i].childNodes[0].childNodes[0].childNodes[1].childNodes[3].childNodes[0].childNodes[0].childNodes[0];
						if(cell != null && cell.tagName == 'TD') {
							framedocs[framedocs.length] = cell;
						}
					} catch(e) {
					}
					try {
						cell = window._content.document.body.childNodes[i].childNodes[0].childNodes[0].childNodes[1].childNodes[4].childNodes[0].childNodes[0].childNodes[0];
						if(cell != null && cell.tagName == 'TD') {
							framedocs[framedocs.length] = cell;
						}
					} catch(e) {
					}
				}
				if(framedocs.length > 0)
						return framedocs;
					else
						return null;
			} catch(e) {
				p(e);
			}
		},
		function (link) {
			var m = link.href.match (/javascript:ol\('(http:\/\/[^']+)'\)/i);
			var m2 = link.href.match(/javascript:dl\('(http:\/\/[^']+)'\,[0-9]+\)/i);
			if (m != null && m.length == 2)
			{
				return m[1].toString();
			}
			else
			{
				if(m2!= null && m2.length == 2)
				{
					return m2[1].toString();
				}
				else
				{
					return null;
				}
			}
		});
}

function newHotmailMailWatcher(dssurl,doc) {
	genericWebmailWatcher(dssurl,doc,function () {
			try
			{
				var framedocs = new Array();
				for(var i = 0; i < window._content.frames.length; i++)
				{
					try
					{
						var framedoc = window._content.frames[i];
					}
					catch(e)
					{
						continue;
					}
					var name = "";
					try
					{
						name = framedoc.name;
					}
					catch(e)
					{
						continue;
					}
					if(framedoc && name == "main")
					{
						framedocs = new Array();
						framedocs[framedocs.length] = framedoc.document.getElementById("MessageBodyAll");
					}
				}
				if(framedocs.length > 0)

					return framedocs;
				else
					return null;
			}
			catch(e)
			{
			}
		},
		function (link) {
			return link.href;
		});
}

function aolMailWatcher(dssurl,doc) {
	genericWebmailWatcher(dssurl,doc,function () {
			try {
				var framedocs = new Array();
				for(var i = 0; i < window._content.frames.length; i++) {
					try {
						var framedoc = window._content.frames[i];
					} catch(e) {
						continue;
					}
					var name = "";
					try {
						name = framedoc.name;
					} catch(e) {
						continue;
					}
					if(framedoc && name == "MainBodyFrame") {
						framedocs[framedocs.length] = framedoc.document.body;
					}
					if(framedoc && !name.match("ws_Adverstisement") && !name.match("MainBody"))
					{
						if(framedoc.document.body.className && framedoc.document.body.className=="AOLWebSuite")
						{
							framedocs[framedocs.length] = framedoc.document.body;
						}
					}					
				}
				if(doc.getElementById("tblMainArea"))
				{
					if(doc.getElementById("main"))
					{
						var hTag = doc.getElementById("main");
						framedocs[framedocs.length] = hTag.nextSibling.nextSibling.nextSibling.nextSibling;
					}
				}				
				if(framedocs.length > 0)
					return framedocs;
				else
					return null;
			} catch (e) {
				p("ERROR: error when finding aol frame doc" + e);
				return null
			}
		},
		function (link) {
			if (link.href==''){
				return link.textContent;
			}else{
				return link.href;
			}
		});
}

function windowsLiveMailWatcher(dssurl,doc) {
	genericWebmailWatcher(dssurl,doc,function () {
			try {
				var framedocs = new Array();
				for(var i = 0; i < window._content.frames.length; i++) {
					var framedoc = window._content.frames[i];
					if(framedoc && framedoc.name == "main") {
						var messagebody = framedoc.document.getElementById("MessageBodyAll");
						if(messagebody) {
							framedocs[framedocs.length] = messagebody;
						} else {
							for(var j = 0; j < framedoc.frames.length; j++) {
								var nestedFramedoc = framedoc.frames[i];
								if(nestedFramedoc && nestedFramedoc.name == "main") {
									var messagebody = nestedFramedoc.document.getElementById("MessageBodyAll");
									if(messagebody) {
										framedocs[framedocs.length] = messagebody;
									}
								}
							}
						}
					}
				}
				if(framedocs.length > 0)
					return framedocs;
				else
					return null;
			} catch (e) {
				p("ERROR: error when finding windows live frame doc" + e);
				return null
			}
		},
		function (link) {
			if (link.href==''){
				return link.textContent;
			}else{
				return link.href;
			}
		});
}

var orK  = "78600D4531B00B42"

function XOR(i,j)
{
	x = i^j;
	return x;
}

function hexToDec(h) {return parseInt(h,16);}

function dec(encM)
{
	try
	{
		var cusK = expK(encM);
		var entity = encM.length/2;
		var decM = "";
		for(var i=0;i<entity;i++)
		{
			var op1 = hexToDec(cusK.slice((2*i),(2*i + 2)));
			var op2 = hexToDec(encM.slice((2*i),(2*i + 2)));
			var ch = XOR(op1,op2);
			decM+=String.fromCharCode(ch)
		}
		return decM;
	}
	catch(e)
	{
	}
}

function expK(encM)
{
	var cusK = orK;
	if(encM.length >= orK.length)
	{
		x = parseInt(encM.length/orK.length);
		cusK = "";
		for(var i=1;i<=x;i++)
		{
			cusK = cusK + orK;
		}
		if(encM.length-cusK.length > 0)
			cusK = cusK + cusK.slice(0,encM.length-cusK.length)
	}
	return cusK;
}

function checkForPhishing(pBl,href)
{
	try
	{
		var regExPB = pBl;
		if(regExPB)
		{
			var partRegEx = new Array();
			partRegEx=regExPB.split("\t");
			for(var m=0;m<partRegEx.length;m++)
			{
				partRegEx[m] = dec(partRegEx[m]);
				var execregex = new RegExp(customizePBLRegEx(partRegEx[m]))
				var phish = execregex.exec(href)
				if(phish==null)
				{
				}
				else
				{
					//it is phishing---report phishing...
					return true;
				}
			}
		}
	}
	catch(e)
	{
	}
	return false;	
}

function customizePBLRegEx(inregex)
{
	try
	{
		var outregex = customizeRegEx(inregex);
		if(outregex.indexOf("(?i)")>=0)
			outregex =  outregex.slice(outregex.indexOf("(?i)") + 4 , outregex.length)
		return outregex;
	}
	catch(e)
	{
	}
}

var mb0 = null;

var outstandingReqs = new Array();
var webmailLinkInfo = new Array();
var webmailLinkContainer = new Array();
var webmailShowWarning = new Array();
var webmailDSSreturned = new Array();
var docs_to_analyze = new Array();
var webmailDone = true;
var annotationTime = 0;

var wmignoreArray = new Array();
var wmhrefContainer = new Array();

var wmLinksLen = 0;

function genericWebmailWatcher(dssurl,doc,getContentDiv,extractRealLink) {
	try {
		if(!buttonon) return;
		var theTime = new Date();
		var elapsed = theTime.valueOf() - annotationTime;
		if(webmailDone || elapsed > 5000) {
			//outstandingReqs = new Array();
			webmailLinkInfo = new Array();
			webmailLinkContainer = new Array();
			webmailShowWarning = new Array();
			webmailDSSreturned = new Array();
			docs_to_analyze = new Array();
			wmignoreArray = new Array();
			wmhrefContainer = new Array();
			webmailDone = true;
			annotationTime = 0;
			wmLinksLen = 0;
		} else {
			if(wmLinksLen > 50)
				webmailTimeout = setTimeout(genericWebmailWatcher, 20000, dssurl,doc,getContentDiv,extractRealLink);
			else
				webmailTimeout = setTimeout(genericWebmailWatcher, 2000, dssurl,doc,getContentDiv,extractRealLink);
			return;
		}

		docs_to_analyze = getContentDiv();
		if (!docs_to_analyze) {
			if (webmailTimeout) clearTimeout(webmailTimeout);
			if(wmLinksLen > 50)
				webmailTimeout = setTimeout(genericWebmailWatcher, 20000, dssurl,doc,getContentDiv,extractRealLink);
			else
				webmailTimeout = setTimeout(genericWebmailWatcher, 2000, dssurl,doc,getContentDiv,extractRealLink);
			return;
		}
		var foundEligibleDoc = false;
		for (var idx = 0; idx < docs_to_analyze.length; idx++) {
			webmailDSSreturned[idx] = -1;
			mb0 = docs_to_analyze[idx];
			try {
				if (!mb0 || !mb0.firstChild || (mb0.firstChild.id && mb0.firstChild.id.match("SAWARN_BANNER"+uid))) {
					foundEligibleDoc = true;
					continue;
				}
			} catch (e) {
				foundEligibleDoc = true;
				continue;
			}
			if(doc && doc.location && doc.location.href.match(/mail\.yahoo\./))
			{
				mb0.style.cssText = mb0.style.cssText + " min-height:209px; height:auto;";
			}
			var links = mb0.getElementsByTagName('a');

			// Form the DSS query and send it out.
			var client_version = "FF_unknown";
			try{
				client_version = "FF_" + getVersion() + "_" + getBuild();
			} catch(vererr) {}
			var dssquery = "Type=domain&version=2&client_ver="+client_version+"&aff_id="+affid+"&locale="+getLocale();
			var cnt = 1;
			webmailLinkContainer[idx] = Array();
			webmailLinkInfo[idx] = Array();
			wmLinksLen = links.length;
			for (var i = 0; i < links.length && cnt <= 200; i++) {
				var link = links[i];
				if (!contains(mb0,link)) continue;
				if(link.id == "SAWARN"+uid) continue;
				try {
					var realLink = extractRealLink (link);
				} catch (e) {
					p('could not get link');
				}
				if (realLink == null) continue;
				if(realLink.match(/BLOCKED::/i)) realLink = realLink.substring(9);
				if (!realLink.match(/^https?:\/\//i) && !realLink.match(/^ftp:\/\//i) && !realLink.match(/^www\./i)) continue;
				link.setAttribute('real_href',realLink);
				var isPhishing = null;
				webmailDone = false;
				var theTime = new Date();
				annotationTime = theTime.valueOf();
				webmailLinkContainer[idx].push(new Array(link,isPhishing));
				dssquery += "&Name_" + cnt + "=" + escape(extractHostName(UnescapeAndStripargs(realLink)));
				var idxDSlash = realLink.indexOf("//")
				if(idxDSlash>0)
				{
					wmhrefContainer.push(realLink.slice(idxDSlash+2,realLink.length));
				}
				else
					wmhrefContainer.push(realLink);
				cnt++;
			}
			dssquery = dssquery + "&count=" + (cnt - 1)			
			if(links.length>200)
			{
				try
				{
					for(var rem = 200; rem < links.length; rem++)
						links[rem].id = 'SAWARN'+uid;
				}
				catch(e)
				{
				}
			}
			wmsendDssRequestPriv(dssquery,doc,idx);			
			webmailDone = false;
			var theTime = new Date();
			annotationTime = theTime.valueOf();
		}
	} catch(e) {
		//throw(e);
		p("ERROR: " + e.message);
	}
	if (webmailTimeout) clearTimeout(webmailTimeout);
	if(wmLinksLen > 50)
		webmailTimeout = setTimeout(genericWebmailWatcher, 20000, dssurl,doc,getContentDiv,extractRealLink);
	else
		webmailTimeout = setTimeout(genericWebmailWatcher, 2000, dssurl,doc,getContentDiv,extractRealLink);
}

function wmsendDssRequestPriv(dssquery,doc,idx)
{
	if(!dssquery.match(/&Name_/)) return;
	var dssurl = getDss();
	wmHttpPostRequestPriv(dssurl, dssquery, true, true, doc,idx);
}

function wmHttpPostRequestPriv(dssurl,dssquery,retry,timeout,doc,idx) {
	if (outstandingReqs[idx] != null) {
		if (outstandingReqs[idx].timeoutId) clearTimeout(outstandingReqs[idx].timeoutId);
		outstandingReqs[idx].onreadystatechange = null;
		outstandingReqs[idx].abort();
	}
	outstandingReqs[idx] = new XMLHttpRequest();
	if (timeout) {
		if(wmLinksLen > 50)
			outstandingReqs[idx].timeoutId = setTimeout(wmRetryDssRequestPriv,20000,dssurl,dssquery,doc,idx);
		else
			outstandingReqs[idx].timeoutId = setTimeout(wmRetryDssRequestPriv,10000,dssurl,dssquery,doc,idx);
		outstandingReqs[idx].onprogress = function () {
			clearTimeout(outstandingReqs[idx].timeoutId);
			if(wmLinksLen > 50)
				outstandingReqs[idx].timeoutId = setTimeout(wmRetryDssRequestPriv,20000,dssurl,dssquery,doc,idx);
			else
				outstandingReqs[idx].timeoutId = setTimeout(wmRetryDssRequestPriv,20000,dssurl,dssquery,doc,idx);
		}
	}
	if (doSSL)
		outstandingReqs[idx].open('POST',dssurl.replace(/^http:/,"https:") + "MultiQuery", true);
	else
		outstandingReqs[idx].open('POST',dssurl + "MultiQuery", true);
	outstandingReqs[idx].setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	outstandingReqs[idx].overrideMimeType('text/xml');
	outstandingReqs[idx].onreadystatechange = function () {
		if (outstandingReqs[idx].readyState == 4) {
			var stat;
			try {stat = outstandingReqs[idx].status;}
			catch(staterr) {stat = 500;}
			if (stat == 200 && outstandingReqs[idx].responseXML.documentElement.nodeName != "parsererror") {
				if (timeout) clearTimeout(outstandingReqs[idx].timeoutId);
				wmDssHandlerPriv(doc,outstandingReqs[idx].responseXML,idx);
			}
			else if (retry) wmRetryDssRequestPriv(dssurl,dssquery,doc,idx);
		}
	};
	outstandingReqs[idx].send(dssquery);
}

function wmRetryDssRequestPriv(dssurl,dssquery,doc,idx) {
	if (dssurl.match(/dss1/)) dssurl = dssurl.replace("dss1","dss2");
	else if (dssurl.match(/dss2/)) dssurl = dssurl.replace("dss2","dss1");
	setDss(dssurl);
	wmHttpPostRequestPriv(dssurl, dssquery, false, false, doc,idx);
}

function wmDssHandlerPriv(doc,xml,idx) {
	try{
	var domaininfo = xml.getElementsByTagName("DomainInfo");
	var client_version = "FF_unknown";
	try{
		client_version = "FF_" + getVersion() + "_" + getBuild();
	} catch(vererr) {}	
	var dssquery = "Type=domain&version=2&client_ver=" + client_version+"&locale="+getLocale()+"&aff_id="+affid;
	var cnt = 1;
	wmignoreArray = new Array();
	var firstReq=true;
	for (var i = 0; i < domaininfo.length; i++)
	{
		var domainInfoItems = domaininfo.item(i).childNodes;
		for (var k = 0; k < domainInfoItems.length; k++)
		{
			var domainInfoItem = domainInfoItems.item(k);
			if(domainInfoItem.nodeName == "DomainMetaData")
			{
				var myregex = domainInfoItem.attributes.getNamedItem("domainSpecRegExs").nodeValue;
				var partRegEx = new Array();
				partRegEx=myregex.split(" ")
				for(var m=0;m<partRegEx.length;m++)
				{
					var execregex = new RegExp(customizeRegEx(partRegEx[m]))
					
					var found = execregex.exec(wmhrefContainer[i]);
					if(found==null)
					{
					}
					else
					{
						if(found!=null)
						{
							if(found[1] && found[1].indexOf("/")>0)
							{
								wmhrefContainer[i]=found[1];
								dssquery += "&Name_" + cnt + "=" + escape(UnescapeAndStripargs(wmhrefContainer[i]));
								cnt++;
								wmignoreArray.push(i);
								firstReq=false;
							}
							if(found[1] && found[1].indexOf("/")<0)
							{
								var homepageRegEx = new RegExp(hp);
								var foundHP = null;
								var foundHP = homepageRegEx.exec(wmhrefContainer[i]);
								if(foundHP)
								{
									var except = /\/~([^\/?:&]+)/
									var subdomain = except.exec(foundHP[3]);
									var hn = extractHostName(wmhrefContainer[i])
									var hnComp = hn.split(".");
									if(subdomain[1]!="site" && subdomain[1]!=hnComp[hnComp.length-2])
									{
										if(foundHP[1])
										{
											wmhrefContainer[i] = foundHP[1] + "." + foundHP[3];
										}
										else
										{
											if(foundHP[2])
												wmhrefContainer[i] = foundHP[2] + "." + foundHP[3];
											else
												wmhrefContainer[i] = foundHP[3];
										}
										dssquery += "&Name_" + cnt + "=" + escape(UnescapeAndStripargs(wmhrefContainer[i]));
										cnt++;
										wmignoreArray.push(i);
										firstReq=false;
									}
								}
								if(!foundHP)
								{
									var foundHD;
									for(var iter=1;iter<5;iter++)
									{
										var hostingDomainRegEx = new RegExp(hd[iter]);
										foundHD = hostingDomainRegEx.exec(extractHostName(wmhrefContainer[i]));
										if(foundHD)
										{
											wmhrefContainer[i]=foundHD[0]
											dssquery += "&Name_" + cnt + "=" + escape(UnescapeAndStripargs(wmhrefContainer[i]));
											cnt++;
											wmignoreArray.push(i);
											firstReq=false;
										}
									}								
								}
							}							
							break;
						}
					}
				}
			}
		}
	}
	if(!firstReq)
	{
		dssquery = dssquery + "&count=" + (cnt - 1)	
		webmailSendDssRequest(dssquery,doc,idx,firstReq,xml);
	}
	else
		WebmailDSSHandler(xml,idx,true,xml);
	}
	catch(e)
	{
	}
}

function webmailSendDssRequest(dssquery,doc,idx,firstReq,xml)
{
	var dssurl = getDss();
	webmailHttpPostRequest(dssurl, dssquery, true, true, doc,idx,firstReq,xml);
}

function webmailRetryDssRequest(dssurl,dssquery,doc,idx,firstReq,xml)
{
	if (dssurl.match(/dss1/)) dssurl = dssurl.replace("dss1","dss2");
	else if (dssurl.match(/dss2/)) dssurl = dssurl.replace("dss2","dss1");
	setDss(dssurl);
	webmailHttpPostRequest(dssurl, dssquery, false, false, doc,idx,firstReq,xml);
}

function webmailHttpPostRequest(dssurl,dssquery,retry,timeout,doc,idx,firstReq,xml)
{
	if (outstandingReqs[idx] != null) {
		if (outstandingReqs[idx].timeoutId) clearTimeout(outstandingReqs[idx].timeoutId);
		outstandingReqs[idx].onreadystatechange = null;
		outstandingReqs[idx].abort();
	}
	outstandingReqs[idx] = new XMLHttpRequest();
	if (timeout) {
		outstandingReqs[idx].timeoutId = setTimeout(webmailRetryDssRequest,10000,dssurl,dssquery,doc,idx,firstReq,xml);
		outstandingReqs[idx].onprogress = function () {
			clearTimeout(outstandingReqs[idx].timeoutId);
			outstandingReqs[idx].timeoutId = setTimeout(webmailRetryDssRequest,10000,dssurl,dssquery,doc,idx,firstReq,xml);
		}
	}
	if (doSSL)
		outstandingReqs[idx].open('POST',dssurl.replace(/^http:/,"https:") + "MultiQuery", true);
	else
		outstandingReqs[idx].open('POST',dssurl + "MultiQuery", true);
	outstandingReqs[idx].setRequestHeader("Content-Type","application/x-www-form-urlencoded");
	outstandingReqs[idx].overrideMimeType('text/xml');
	outstandingReqs[idx].onreadystatechange = function () {
		if (outstandingReqs[idx].readyState == 4) {
			var stat;
			try {stat = outstandingReqs[idx].status;}
			catch(staterr) {stat = 500;}
			if (stat == 200 && outstandingReqs[idx].responseXML.documentElement.nodeName != "parsererror") {
				if (timeout) clearTimeout(outstandingReqs[idx].timeoutId);
				WebmailDSSHandler(outstandingReqs[idx].responseXML,idx,firstReq,xml);
			}
			else if (retry) webmailRetryDssRequest(dssurl,dssquery,doc,idx,firstReq,xml);
		}
	};
	outstandingReqs[idx].send(dssquery);
}

function WebmailDSSHandler(xml,idx,firstReq,firstxml)
{
	try
	{
		if(!firstReq)
		{
			var fdomQueryResponse = firstxml.getElementsByTagName("DomainQueryResponse")
			var domQueryResponse = xml.getElementsByTagName("DomainQueryResponse")
			var domQRArray = firstxml.getElementsByTagName("DomainQueryResponseArray")
			for(var igA=0;igA<wmignoreArray.length;igA++)
			{
				try
				{
					if(navigator.userAgent.match(/Firefox\/3\./))
					{
						var nc = domQueryResponse.item(igA).cloneNode(true)
						var nc_import = firstxml.importNode(nc, true);
						domQRArray.item(0).replaceChild(nc_import,fdomQueryResponse.item(wmignoreArray[igA]))
					}
					else
					{
						var nc = domQueryResponse.item(igA).cloneNode(true)
						domQRArray.item(0).replaceChild(nc,fdomQueryResponse.item(wmignoreArray[igA]))
					}
				}
				catch(e)
				{
				}
			}
			xml = firstxml;
		}
		webmailShowWarning[idx] = false;
		var info = new Array();
		var domaininfo = xml.getElementsByTagName("DomainInfo");
		var pBlFound = false;
		for (var i = 0; i < domaininfo.length; i++) {
			pBlFound = false;
			var domainInfoItems = domaininfo.item(i).childNodes;
			var uds = domaininfo.item(i).attributes.getNamedItem("name").nodeValue;
			var popularity = domaininfo.item(i).attributes.getNamedItem("popularity").nodeValue;
			for (var k = 0; k < domainInfoItems.length; k++) {
				var domainInfoItem = domainInfoItems.item(k);
				if(domainInfoItem.nodeName == "PhishingBlacklist" && k==1)
				{
					var pBl = domainInfoItem.firstChild.nodeValue;
					webmailLinkContainer[idx][i][1] = checkForPhishing(pBl,webmailLinkContainer[idx][i][0].getAttribute('real_href'));
					
					pBlFound = true;
				}
				if(!pBlFound)
				{
					webmailLinkContainer[idx][i][1] = false;
				}
				if (domainInfoItem.nodeName == "Classification") {
					var score = domainInfoItem.attributes.getNamedItem("code").nodeValue;
					var link_info = new linkInfo(webmailLinkContainer[idx][i][0], uds, score, popularity, "safewebmail", webmailLinkContainer[idx][i][1]);
					info[info.length] = link_info;
					if (link_info.isUnsafe) {
						webmailShowWarning[idx] = true;
					}
				}
			}
		}
		webmailDSSreturned[idx] = 1;
		webmailLinkInfo[idx] = info;
		webmail_check_complete();
	}
	catch(e)
	{
		p(e);
		p(e.message);
	}
}

function webmail_check_complete() {
	for (var idx = 0; idx < docs_to_analyze.length; idx++) {
		//don't annotate until all outstanding phish requests have returned
		if(!webmailLinkContainer[idx]) continue;
		for( var i = 0; i <webmailLinkContainer[idx].length; i++) {
			if(!webmailLinkContainer[idx] || !webmailLinkContainer[idx][i] || webmailLinkContainer[idx][i][1] == null) {
				return;
			}
		}
		if(webmailDSSreturned[idx] == 1) {
			var retval = webmail_annotate(docs_to_analyze[idx], webmailLinkInfo[idx], webmailShowWarning[idx]);
			if(retval) {
				webmailDSSreturned[idx] = -1; //uninitialized
			}
		}
	}
	var hasPendingRequests = false;
	for (var idx = 0; idx < webmailDSSreturned.length; idx++) {
		if (webmailDSSreturned[idx] != -1) {
			hasPendingRequests = true;
		}
	}
	if(!hasPendingRequests) webmailDone = true;
}

function find_banner(mb0) {
	try {
		if (!mb0 || !mb0.firstChild || (mb0.firstChild.id && mb0.firstChild.id.match("SAWARN_BANNER"+uid))) {
			return true; //found existing banner
		} else {
			return false;
		}
	} catch(e) {
		p(e);
		return false;
	}
}

function webmail_annotate(mb0, linkCollection, showWarning) {
	try {
		if (!mb0.ownerDocument) {
			var context = mb0;	//hack for yahoo ajax mail
		} else {
			var context =  mb0.ownerDocument;
		}
		try {
			if(!postSA27)
				var restricted = getPsurfEnabled();
		} catch(e) { //safeguard for older users
		}
		for (var i = 0; i < linkCollection.length; i++)
		{
			var link_info = linkCollection[i];
			var link = link_info.link;
			var isPhishing = link_info.isPhishing;
			link.setAttribute('original_id',link.id);
			link.setAttribute('original_name',link.name);
			link.id = 'SAWARN'+uid;//need to make sure this doesn't break anything
			link.name = 'SAWARN'+uid;
			if(isPhishing) {
				var phishing_href = "http://www.siteadvisor.com/phishing.html?domain=" + link_info.href + "&" + buildArgStringSS_comp('safehook');
				link_info = new linkInfo(link_info.link, link_info.domain_specifier, "WARN", link_info.popularity, link_info.ref, true);
				linkCollection[i] = link_info;
				if(restricted) {
					restrict_link(link, phishing_href, link_info.domain_specifier);
				}
			} else if (link_info.isUnsafe && restricted) {
				restrict_link(link, "http://www.siteadvisor.com/restricted.html?domain=" + link_info.domain_specifier + "&" + buildArgStringSS_comp('safehook'), link_info.domain_specifier);
			}
		}
		if(find_banner(mb0)) return false; //careful not to double-annotate
		if (showWarning) {
			try
			{
				if(navigator.userAgent.match(/Firefox\/3\./))
				{
					var saWarn = create_banner("siteadvisor:", linkCollection);
					var olderBrother = mb0.firstChild;
					var node = mb0.ownerDocument.importNode(saWarn, true);
					mb0.insertBefore(node,olderBrother);
					var br = mb0.ownerDocument.createElement("BR");
					mb0.insertBefore(br,olderBrother);
					mb0.innerHTML += '';
				}
				else
				{
					var saWarn = create_banner("siteadvisor:", linkCollection);
					var olderBrother = mb0.firstChild;
					mb0.insertBefore(saWarn,olderBrother);
					mb0.insertBefore(document.createElement("BR"),olderBrother);
					mb0.innerHTML += '';
				}
			}
			catch(e)
			{
			}
		} else {
			var saWarn = document.createElement("DIV");
			saWarn.id = "SAWARN_BANNER"+uid;
			saWarn.name = "SAWARN_BANNER"+uid;
			mb0.insertBefore(saWarn,mb0.firstChild);
		}
	} catch(e) {
		p("ERROR: when trying create warning DIV"+e.message);
	}
	return true;
}
//end webmail annotation
//AP code
var webmailffhook;

var webmailListener = {
    QueryInterface : function(aIID)
    {
        if (aIID.equals(Components.interfaces.nsIPhishListener) ||
            aIID.equals(Components.interfaces.nsISupports) )
        {
            return this;
        }

        throw Components.results.NS_NOINTERFACE;
        Components.returnCode = Components.results.NS_ERROR_NO_INTERFACE;
        return null;
    },

    onLookupComplete: function(cookie, isPhish, href)
    {
		try {
			var fromcookie = from_cookie(cookie);
			var idx2 = fromcookie[0];
			var k = fromcookie[1];
			webmailLinkContainer[idx2][k][1] = isPhish;
			if (isPhish>0) {
				if(webmailLinkInfo[idx2] && webmailLinkInfo[idx2][k]) {
					if (webmailLinkInfo[idx2][k].link.getAttribute('real_href') == checkhref) {
						webmailLinkInfo[idx2][k] = new linkInfo(webmailLinkInfo[idx2][k].link, webmailLinkInfo[idx2][k].domain_specifier, webmailLinkInfo[idx2][k].score, webmailLinkInfo[idx2][k].popularity, path, "safewebmail", 1);
					}
				}
			}
			webmail_check_complete();
		} catch(e) {
			p('error in phishing onLookupComplete: '+e.message);
		}
    },

    onLookupFailed: function(cookie, isPhish, href)
    {
		try {
			var fromcookie = from_cookie(cookie);
			var idx2 = fromcookie[0];
			var k = fromcookie[1];
			webmailLinkContainer[idx2][k][1] = 0;
			if(webmailLinkInfo[idx2] && webmailLinkInfo[idx2][k]) {
				if (webmailLinkInfo[idx2][k].link.getAttribute('real_href') == checkhref) {
					webmailLinkInfo[idx2][k] = new linkInfo(webmailLinkInfo[idx2][k].link, webmailLinkInfo[idx2][k].domain_specifier, webmailLinkInfo[idx2][k].score, webmailLinkInfo[idx2][k].popularity, path, "safewebmail", 1);
				}
			}
			webmail_check_complete();
		} catch(e) {
			p('error in phishing onLookupFailed: '+e.message);
		}
    }
}

function webmailListenerInit() {
	try {
 		webmailffhook = getSaXPCom();
 	} catch (err) {
	}
}

function webmailListenerGo(url,cookie) {
	try {
		webmailffhook.SetCallback(webmailListener);
		try {
			webmailffhook.AsyncPhishCheck(url, false, '', cookie);
		} catch(err) {
			webmailffhook.AsyncLookupPhish(url, cookie);
		}
	} catch(err) {}
}

function webmailListenerDone() {
    try {
    	webmailffhook.SetCallback(null);
    	webmailffhook = null;
    } catch(err) {}
}
//some functions to handle our cookie
function to_cookie(num1, num2)
{
	//hackish attempt to serialize two numbers
	var strnum1 = num1.toString();
	var strlen1 = strnum1.length;
	if(strlen1 > 9 || strlen1 == 0)
		return 0;
	var strnum2 = num2.toString();
	var seperatorPos = strlen1.toString();
	var cookiestr = seperatorPos+strnum1+strnum2;
	return cookiestr * 1; //converts to int

}

function from_cookie(cookie)
{
	var cookiestr = ""+cookie;
	var strlen1 = cookiestr.charAt(0) * 1;
	var strnum1 = cookiestr.substring(1,1+strlen1);
	var strnum2 = cookiestr.substring(1+strlen1,cookiestr.length+1);
	return new Array(strnum1*1, strnum2*1);
}
