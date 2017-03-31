var socket;
var server, port, st;
var canvas, context, array, id;
var ct, rd, dc;
boolclose=1;
window.onload = function() {
    canvas = document.getElementById("screen");
    ct = document.getElementById("connect");
    rd = document.getElementById("ready");
    dc = document.getElementById("disconnect");
    context = canvas.getContext("2d");
    window.onkeydown = processKey;
};
function processKey(e) {
    if (e.keyCode == 87) {sendUp();}
    if (e.keyCode == 83) {sendDown()}
    if (e.keyCode == 65) {sendLeft()}
    if (e.keyCode == 68) {sendRight()}
}
function servers() {
    server = document.getElementById("server").value;
    port = document.getElementById("port").value;
}
function sendUp(){socket.send("up");}
function sendDown() {socket.send("down");}
function sendLeft() {socket.send("left");}
function sendRight() {socket.send("right");}
function sendDisconnect() {socket.close();}
function sendReady() {
    socket.send("ready");
    rd.disabled = true;
    drawText('Ожидание других игроков',50,300,'bold 40px Arial');
}
function check(text) {
    text !== "" ? ct.disabled = false : ct.disabled = true;
}

function connect() {
    servers();
    st = 'ws://'+server+':'+port+'/ws';
    socket = new WebSocket(st);
    socket.onopen = function () {
        drawText('Соединение установлено',50,300,'bold 40px Arial');
        ct.disabled = true;
        rd.disabled = false;
        dc.disabled = false;

    };
    socket.onclose = function(event) {
        if (boolclose==1){
            if (event.wasClean) {
                drawText('Соединение закрыто чисто',30,300,'bold 40px Arial');
                ct.disabled = false;
                rd.disabled = true;
                dc.disabled = true;
            } else {
                drawText('Обрыв соединения',100,300,'bold 40px Arial');
            }
        }
    };
    socket.onmessage = function(event) {
        var s = event.data;
        var con =s.split("-");
        if (con[0]=='1'){
            array = con[1].split(";").map(function (x) { return x.split(","); });
            drawFrame();
        }else if (con[0]=='2'){
            drawText(con[1],200,300,'bold 40px Arial');
            boolclose=2;
            sendDisconnect();
        }
        else if (con[0]=='3'){
            drawText(con[1],200,300,'bold 40px Arial');
            boolclose=2;
            sendDisconnect();
        }else if (con[0]=='4'){
            id = con[1];
        }
    };
    socket.onerror = function(error) {
        alert("Ошибка " + error.message);
    };
}
function drawText(text, x, y, font) {
    context.clearRect(0, 0, canvas.width, canvas.height);
    context.beginPath();
    context.font = font;
    context.fillStyle = '#000000';
    context.fillText(text, x, y);
}
function div(val) {
    return (val - val % 10) / 10;
}
function drawFrame() {
    context.clearRect(0, 0, canvas.width, canvas.height);
    context.beginPath();
    var arrayd = array;
    for (i=0;i<arrayd.length;i++){
        for (j=0;j<arrayd[0].length;j++) {
            if (arrayd[j][i]==3){
                context.fillStyle = '#008000';
                context.fillRect(i*20,j*20,20,20);
            }else if (div(arrayd[j][i])==id && arrayd[j][i]%10==1){
                context.fillStyle = '#000080';
                context.fillRect(i*20,j*20,20,20);
            }else if (div(arrayd[j][i])==id && arrayd[j][i]%10==2){
                context.fillStyle = '#0000FF';
                context.fillRect(i*20,j*20,20,20);
            }else if (div(arrayd[j][i])!=id && arrayd[j][i]%10==1){
                context.fillStyle = '#AF0000';
                context.fillRect(i*20,j*20,20,20);
            }else if (div(arrayd[j][i])!=id && arrayd[j][i]%10==2){
                context.fillStyle = '#FF0000';
                context.fillRect(i*20,j*20,20,20);
            }
        }
    }
}


