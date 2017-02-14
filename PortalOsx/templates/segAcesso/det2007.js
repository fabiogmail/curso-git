function iniciar(){	
	MostrarMenu();
	ocultar('help');
	ocultar('tooltip');	
}
function help(){
	mostrar('help');
}
function rethelp(valor){
	$('helptext').innerHTML = valor;
}

function mostrar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="block";
}
function ocultar(idObjeto) {
	var estilo = $(idObjeto).style;
	estilo.display="none";
}
function isVisible(idObjeto){
	var estilo = $(idObjeto).style;
	if(estilo.display != "none"){
		return true;
	}else{
		return false;
	}
}
function habilitar(idObjeto){
	$(idObjeto).disabled=false;
	$(idObjeto).style.background = '#FFFFFF';
}
function desabilitar(idObjeto){
	$(idObjeto).disabled=true;
	$(idObjeto).style.background = '#E6E6E6';
}
function isEnabled(){
	if($(idObjeto).disabled){
		return false;
	}else{
		return true;
	}
}


function getSelecionado(id){
	var valor;
	for(var i = 0; i < $(id).options.length; i++){
		if($(id).options[i].selected){
			valor = $(id).options[i].value;
		}
	}
	return valor;
}
function getSelecionadoText(id){
	var valor;
	for(var i = 0; i < $(id).options.length; i++){
		if($(id).options[i].selected){
			valor = $(id).options[i].text;
		}
	}
	return valor;
}
function getSelecionadoOption(id){
	var valor;
	for(var i = 0; i < $(id).options.length; i++){
		if($(id).options[i].selected){
			valor = $(id).options[i];
		}
	}
	return valor;
}
function getComboOption(id,valor){
	var retorno;
	for(var i = 0; i < $(id).options.length; i++){
		if($(id).options[i].value == valor){
			retorno = $(id).options[i];
		}
	}
	return retorno;
}
function selecionarCombo(id,tipo){
	for(var i = 0; i < $(id).options.length; i++){
		if($(id).options[i].value == tipo){
			$(id).options[i].selected = true;
		}else{
			$(id).options[i].selected = false;
		}
	}
}

function listdouble(from, to){
	fromList = $(from);
  	toList = $(to);
	var lista = new Array();
	for(var i = 0; i < fromList.options.length; i++){
		if(fromList.options[i].selected){
			var text = fromList.options[i].text;
			var value = fromList.options[i].value;
			toList.options[toList.length] = new Option(text,value);
			fromList.options[i] = null;
			i--;
		}
	}
}
function mover(id, to){
	fromList = $(id);
	if(to == 'up'){
		for(var i = 0; i < fromList.options.length; i++){
			if(fromList.options[i].selected){
				if(i != 0){
					var text = fromList.options[i].text;
					var value = fromList.options[i].value;
					fromList.options[i].text = fromList.options[i-1].text;
					fromList.options[i].value = fromList.options[i-1].value;
					fromList.options[i-1].text = text;
					fromList.options[i-1].value = value;
					fromList.options[i].selected = false;
					fromList.options[i-1].selected = true;
				}
			}
		}
	}else{
	 	for(var i = 0; i < fromList.options.length; i++){
			if(fromList.options[i].selected){
				if(i != fromList.options.length-1){
					var text = fromList.options[i].text;
					var value = fromList.options[i].value;
					fromList.options[i].text = fromList.options[i+1].text;
					fromList.options[i].value = fromList.options[i+1].value;
					fromList.options[i+1].text = text;
					fromList.options[i+1].value = value;
					fromList.options[i].selected = false;
					fromList.options[i+1].selected = true;
					break;
				}
			}
		}
	}
}

function checkNumero(event,texto){
	if(event.keyCode == 8 || (event.keyCode >= 35 && event.keyCode <= 40) || event.keyCode == 46 || event.keyCode == 9
		|| event.keyCode == 13){
		return true; // aki é para o backspace,as setas, e o end e home
	}
	if( !((event.keyCode >= 96 && event.keyCode <= 105) || (event.keyCode >= 48 && event.keyCode <= 57)) ){
			$('toolText').innerHTML = texto;
			mostrar('tooltip');
			setTimeout(esconderTool,3000);
			return false;
	}
	return true;
}

function checkIntervaloNumero(event,texto,inicio,fim,objeto){
	var campo = event.currentTarget;	
	if(campo == null){
		campo = objeto;
	}
	if(campo.value == ''){
		return;
	}
	if((campo.value < inicio) || (campo.value > fim)){		
		$('toolText').innerHTML = texto+' '+inicio+' e '+fim;
		mostrar('tooltip');
		setTimeout(esconderTool,3000);
	}else{		
		if(campo.style.backgroundColor == "rgb(238, 45, 98)"){
			campo.style.backgroundColor = "#FFFFFF";
		}
		if(campo.style.backgroundColor == "#ee2d62"){
			campo.style.backgroundColor = "#FFFFFF";
		}
	}
}

function checkDataTime(valor){
	var reDate = /^((0?[1-9]|[12]\d)\/(0?[1-9]|1[0-2])|30\/(0?[13-9]|1[0-2])|31\/(0?[13578]|1[02]))\/(19|20)?\d{2}$/;
	var dataTime = new Array();
	if(valor == '') return true;
	dataTime = valor.split(' ');
	data = dataTime[0];
	time = dataTime[1];
	if (reDate.test(data)) {
		return checkTime(time);
	} else {
		return false;
	}
}
function checkTime(time){
	var reTime = /^([0-1]\d|2[0-3]):([0-5]\d):([0-5]\d)$/;
	if (reTime.test(time)) {
		return true;
	} else {
		return false
	}
}

function esconderTool(){
	ocultar('tooltip');
}

// Removes leading whitespaces
function LTrim( value ) {
	
	var re = /\s*((\S+\s*)*)/;
	return value.replace(re, "$1");
	
}

// Removes ending whitespaces
function RTrim( value ) {
	
	var re = /((\s*\S+)*)\s*/;
	return value.replace(re, "$1");
	
}

// Removes leading and ending whitespaces
function trim( value ) {
	
	return LTrim(RTrim(value));
	
}

function mudarPaginaLink(pagina,voltar){
	HelpAjax.buscar(rethelp,pagina,voltar);
}

/********************************************************************************

**  
SideScrollMenu
*   Copyright (C) 2001 <a href="/dhtmlcentral/thomas_brattli.asp">Thomas Brattli</a>
*   This script was released at DHTMLCentral.com
*   Visit for more great scripts!
*   This may be used and changed freely as long as this msg is intact!
*   We will also appreciate any links you could give us.
*
*   Made by <a href="/dhtmlcentral/thomas_brattli.asp">Thomas Brattli</a>  and modified by Michael van Ouwerkerk
********************************************************************************

*/

function lib_bwcheck(){ //Browsercheck (needed)
    this.ver=navigator.appVersion
    this.agent=navigator.userAgent
    this.dom=document.getElementById?1:0
    this.opera5=this.agent.indexOf("Opera 5")>-1
    this.ie5=(this.ver.indexOf("MSIE 5")>-1 && this.dom && !this.opera5)?1:0;
    this.ie6=(this.ver.indexOf("MSIE 6")>-1 && this.dom && !this.opera5)?1:0;
	this.ie7=(this.ver.indexOf("MSIE 7")>-1 && this.dom && !this.opera5)?1:0;
    this.ie4=(document.all && !this.dom && !this.opera5)?1:0;
    this.ie=this.ie4||this.ie5||this.ie6||this.ie7
    this.mac=this.agent.indexOf("Mac")>-1
    this.ns6=(this.dom && parseInt(this.ver) >= 5) ?1:0;
    this.ns4=(document.layers && !this.dom)?1:0;
    this.bw=(this.ie7 ||this.ie6 || this.ie5 || this.ie4 || this.ns4 || this.ns6 || this.opera5)
	
    return this
}
var bw=new lib_bwcheck()


/**************************************************************************
Variables to set.
***************************************************************************/
sLeft = 10         //The left placement of the menu
sTop = 50       //The top placement of the menu
sMenuheight = 20  //The height of the menu
sArrowwidth = 20  //Width of the arrows
sScrollspeed = 20 //Scroll speed: (in milliseconds, change this one and the next variable to change the speed)
sScrollPx = 8     //Pixels to scroll per timeout.
sScrollExtra = 15 //Extra speed to scroll onmousedown (pixels)

/**************************************************************************
Scrolling functions
***************************************************************************/
var tim = 0
var noScroll = true
var oMenu = null;
function mLeft(){
    if (!noScroll && oMenu.x<sArrowwidth){
        oMenu.moveBy(sScrollPx,0)
        tim = setTimeout("mLeft()",sScrollspeed)
    }
}
function mRight(){
    if (!noScroll && oMenu.x>-(oMenu.scrollWidth-(pageWidth))-sArrowwidth){
        oMenu.moveBy(-sScrollPx,0)
        tim = setTimeout("mRight()",sScrollspeed)
    }
}
function noMove(){
    clearTimeout(tim);
    noScroll = true;
    sScrollPx = sScrollPxOriginal;
}
/**************************************************************************
Object part
***************************************************************************/
function makeObj(obj,nest,menu){
    nest = (!nest) ? "":'document.'+nest+'.';
	
    this.elm = bw.ns4?eval(nest+"document.layers." +obj):bw.ie4?document.all[obj]:document.getElementById(obj);
    this.css = bw.ns4?this.elm:this.elm.style;
    this.scrollWidth = bw.ns4?this.css.document.width:this.elm.offsetWidth;
    this.x = bw.ns4?this.css.left:this.elm.offsetLeft;
    this.y = bw.ns4?this.css.top:this.elm.offsetTop;
    this.moveBy = b_moveBy;
    this.moveIt = b_moveIt;
    this.clipTo = b_clipTo;	
	
    return this;
}
/*var px = bw.ns4||window.opera?"":"px";*/
var px = "px";
function b_moveIt(x,y){	
    if (x!=null){this.x=x; this.css.left=this.x+px;}
    if (y!=null){this.y=y; this.css.top=this.y+px;}
}
function b_moveBy(x,y){this.x=this.x+x; this.y=this.y+y; this.css.left=this.x+px; this.css.top=this.y+px;}
function b_clipTo(t,r,b,l){
    if(bw.ns4){this.css.clip.top=t; this.css.clip.right=r; this.css.clip.bottom=b; this.css.clip.left=l;}
    else this.css.clip="rect("+t+"px "+r+"px "+b+"px "+l+"px)";
}
/**************************************************************************
Object part end
***************************************************************************/

/**************************************************************************
Init function. Set the placements of the objects here.
***************************************************************************/
var sScrollPxOriginal = sScrollPx;
function MostrarMenu(){
    //Width of the menu, Currently set to the width of the document.
    //If you want the menu to be 500px wide for instance, just
    //set the pageWidth=500 in stead.
    pageWidth = 470; //(bw.ns4 || bw.ns6 || window.opera)?innerWidth:document.body.clientWidth;
    
    //Making the objects...
    oBg = new makeObj('divBg')
    oMenu = new makeObj('divMenu','divBg',1)
    oArrowRight = new makeObj('divArrowRight','divBg')
	oArrowLeft = new makeObj('divArrowLeft','divBg')
    
    //Placing the menucontainer, the layer with links, and the right arrow.
    oBg.moveIt(sLeft,sTop) //Main div, holds all the other divs.
    oMenu.moveIt(sArrowwidth,null)
    oArrowRight.css.width = sArrowwidth;
    oArrowRight.moveIt(pageWidth-sArrowwidth,null);
	//oArrowLeft.css.width = sArrowwidth;
    //oArrowLeft.moveIt(pageWidth-sArrowwidth,null);
    
    //Setting the width and the visible area of the links.
    if (!bw.ns4) oBg.css.overflow = "hidden";
    if (bw.ns6) oMenu.css.position = "relative";
    oBg.css.width = pageWidth+px;
    oBg.clipTo(0,pageWidth,sMenuheight,0)
    oBg.css.visibility = "visible";
}
