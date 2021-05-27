$(function(){
	$('#validate-on-submit').click(function(){
		validar();
		return false;
	});
	// Dialog exemplo			
	$('#dialog-exemplo').dialog({
		autoOpen  :false,
		modal     :true,
		width     :600,
		height    :400,
		resizable :false
	});
	
	// Dialog exemplo link
	$('#dialog-exemplo-link').click(function(){
		$('#dialog-exemplo').dialog('open');
		return false;
	});

	// Dialog confirm			
	$('#dialog-confirm').dialog({
		autoOpen  :false,
		modal     :true,
		resizable :false,
		buttons: {
			"Não": function() { 
				$(this).dialog("close"); 
			}, 
			"Sim": function() { 
				$(this).dialog("close"); 
				submeter();
			} 
		}
	});
	
	$('#dialog-aviso').dialog({
		autoOpen  :false,
		modal     :true,
		resizable :false,
		width     :700,
		height    :350,
		buttons: {
			"Não": function() { 
				$(this).dialog("close"); 
			}, 
			"Sim": function() { 
				$(this).dialog("close"); 
				acessarAmbiente("/sps/manterQuestionario!abrirFormConsulta.action", "SPS");
				return false;
			} 
		}
	});
	
	// Dialog confirm link
	$('#dialog-confirm-link').click(function(){
		$('#dialog-confirm').dialog('open');
		return false;
	});

	// Dialog funções gerais			
	$('#dialog-funcoes').dialog({
		autoOpen  :false,
		modal     :true,
		width     :600,
		height    :400,
		resizable :false
	});
	
	// Dialog funções gerais link
	$('#dialog-funcoes-link').click(function(){
		$('#dialog-funcoes').dialog('open');
		return false;
	});

	// Dialog funções gerais link
	$('#dialog-situacao-link').click(function(){
		$('#dialog-situacao').dialog('open');
		return false;
	});

	// Datepicker
	$('#campo-data-de').datepicker({
		changeMonth :true,
		changeYear  :true,
		yearRange   :'1900:2010'
	});
	$('#campo-data-a').datepicker({
		changeMonth :true,
		changeYear  :true,
		yearRange   :'1900:2010'
	});
});

function bloquearBrowser(){
	$("#bloquerBrowser").show();
}

function configuraEstiloCombo(select) {
	if (select.selectedIndex == 0) {
		select.style.borderColor='red';
		select.style.borderRadius='0.3em';
	} else {
		select.style.borderColor='';
		select.style.borderRadius='0';
	}
}
function acessarAmbiente(url, aplicacao) {
	document.forms[1].url.value = url;
	document.forms[1].aplicacao.value = aplicacao;
	document.forms[1].action = "login!acessarAmbiente.action";
	document.forms[1].submit();
}

function abrirAjuda() {
	var urlManual = "http://" + window.location.hostname + "/manuais/Manual_de_Acesso_aos_Sistemas/index.html";
	window.open(urlManual, '_blank');
}