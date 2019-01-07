var singleFlg = false;

function ItsAjax(_url, flag) {
    this.url = _url;
    this.onComplet = null;
    if (flag == null || flag == undefined) {
        flag = true;
    }

    this.send = function(_query) {
        var xmlHttp = new ActiveXObject("Msxml2.XMLHTTP");
        xmlHttp.open("POST", this.url, true);
        xmlHttp.setRequestHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8" );

        var funcComplet = this.onComplet;

        var onReady = function() {
            if (xmlHttp.readyState==4) {
                singleFlg = false;
                if (xmlHttp.status == 200) {
                    if (flag) {
                        hideModal();
                    }
                    if(checkResponse(xmlHttp.responseText) && funcComplet != null) {
                        funcComplet(decode(xmlHttp.responseText));
                    }
                }
            }
        };

        xmlHttp.onreadystatechange=function() {
            onReady();
        }

        if (flag) {
            showModal();
        }
        xmlHttp.send(_query);
    };
}

function showModal() {
    var height = document.body.clientHeight;
    var width = document.body.clientWidth;

    if (height <= 48) {
        height = 800;
    }

    var modal = document.createElement("div");
    modal.style.position="absolute";
    modal.style.width = width + "px";
    modal.style.height = (height / 2 + 48) + "px";
    modal.style.background = "#fff";
    modal.style.zIndex = "2000";
    modal.style.top = "15px";
    modal.style.left = "15px";
    modal.style.filter = "alpha(opacity=50)";
    modal.style.opacity="0.5";
    modal.style.textAlign = "center";
    modal.style.paddingTop = (height / 2 - 48) + "px";
    document.body.insertBefore(modal, document.body.firstChild);

    var img = document.createElement("img");
    img.src = "./image/loader.gif";
    modal.appendChild(img);
}

function hideModal() {
    document.body.removeChild(document.body.firstChild);
}