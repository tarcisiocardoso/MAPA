function callHtml(url, id){
    console.log('>>>>'+url+'<<<<', id);


    var el = document.getElementById(id);
    fetch(url)
    .then(ret =>{
        console.log("sucesso", ret );
        if( ret.ok){
            ret.text().then(txt => el.innerHTML = txt );
        }
    })
    .catch(erro =>{
        console.log('>>>erro<<<<', erro.message);
        el.innerHTML = "<b>Cliente indisponivel:</b> <p style='color:red'>: "+erro.message+"</p>";
    })
}


function calCliente01(){
    console.log('>>>>calCliente01<<<<');
    var el = document.getElementById("cliente01");
    fetch("http://localhost:8081/")
    .then(ret =>{

        console.log("sucesso", ret );
        if( ret.ok){
            ret.text().then(txt => el.innerHTML = txt );
        }
    })
    .catch(erro =>{
        console.log('>>>erro<<<<', erro.message);
        el.innerHTML = "<p style='color:red'>Cliente 01: "+erro.message+"</p>";
    })
}

function calCliente01(){
    console.log('>>>>calCliente01<<<<');
    var el = document.getElementById("cliente01");
    fetch("http://localhost:8081/")
    .then(ret =>{

        console.log("sucesso", ret );
        if( ret.ok){
            ret.text().then(txt => el.innerHTML = txt );
        }
    })
    .catch(erro =>{
        console.log('>>>erro<<<<', erro.message);
        el.innerHTML = "<p style='color:red'>Cliente 01: "+erro.message+"</p>";
    })
}


