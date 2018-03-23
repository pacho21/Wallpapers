/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


//on document ready -> get X images from the database and add them to the carrousel.
$(document).ready(function () {
   
    $("body").addClass("loading");
    getFromServlet();
    //on submit ---> send the image data
    $("#subImg").submit(function () {
        testImage($("#linkToImg").val());
        return false;
    });
    $("#upImg").submit(function () {
        uploadToServer();
        return false;
    });


});

function uploadToServer() {
    var image = new FormData($("#upImg")[0]);

    $("body").addClass("loading");
    $("#upload").prop("disabled", true);
    $.ajax({
        type: "POST",
        enctype: 'multipart/form-data',
        url: "UploadImage",
        data: image,
        processData: false,
        contentType: false,
        cache: false,
        timeout: 60000,
        success: function (rsp) {
            $("body").removeClass("loading");
            $("#upload").prop("disabled", false);
            $("#inImg").val("");
            showGoodMsg("Image uploaded successfully! :)")
        },
        error: function (e) {
            if (e["responseJSON"] === undefined) {
                showErrorMsg("Uppss there was an error uploading your image!")
                $("#upload").prop("disabled", false);
                $("body").removeClass("loading");
            } else {
                showErrorMsg(e["responseJSON"]["error"]);
                $("#upload").prop("disabled", false);
                $("body").removeClass("loading");
            }
        }
    });
}
//if success show dialog with congrats (green)
//else show dialog with error msg (red)

function testImage(url) {
    timeout = 10000;
    var timedOut = false, timer;
    var img = new Image();
    img.onerror = img.onabort = function () {
        if (!timedOut) {
            clearTimeout(timer);
            showErrorMsg("The link that you submited doesn't contain an image!");
        }
    };
    img.onload = function () {
        if (!timedOut) {
            clearTimeout(timer);
            //add image to serverlet (recheck on server) ;P            
            sendToServlet();
        }
    };
    img.src = url;
    timer = setTimeout(function () {
        timedOut = true;
        showErrorMsg("Timeout to read the image! Try with another link.");
    }, timeout);
}

function showErrorMsg(msg) {
    $("#bad-content").empty();
    $("#bad-content").append("<strong>Uppsss! </strong>" + msg);
    $("#bad").fadeIn(2500).fadeOut(3000);
}

function showGoodMsg(msg) {
    $("#good-content").empty();
    $("#good-content").append("<strong>Success! </strong>" + msg);
    $("#good").fadeIn(2500).fadeOut(3000);
}

function sendToServlet() {
    var leLink = $("#linkToImg").val();
    $("body").addClass("loading");
    $.ajax({
        type: "POST",
        url: "InsertImage",
        data: {link: leLink},
        success: function (rsp) {
            $("body").removeClass("loading");
            showGoodMsg(rsp["mess"]);
            $("#linkToImg").val("");
            $("#linkToImg").focus();
        },
        error: function (e) {
            $("body").removeClass("loading");
            $("#linkToImg").val("");
            $("#linkToImg").focus();
            if (e["responseJSON"] === undefined)
                showErrorMsg("Somenthing went wrong...");

            else
                showErrorMsg(e["responseJSON"]["mess"]);
        }
    });

}

function getFromServlet() {

    $.ajax({
        type: "GET",
        url: "GetSomeImages",
        datatype: JSON,
        success: function (rsp) {
            loadImages(rsp);
            $("body").removeClass("loading");
        },
        error: function (e) {
            alert("Ups");
            $("body").removeClass("loading");
        }
    });
}

function loadImages(json) {
    var $div = $("<div/>").addClass("carousel-item active");
    var x = 0;
    $.each(json, function (i, imagen) {

        if (i != 0 && i % 5 === 0) {
            {
                x = 0;
                $("#cItems").append($div);
                $div = $("<div/>").addClass("carousel-item");
            }
        }
        if (x < 2) {
            $div.append($("<img  src=" + imagen + " alt='" + i + "'>"));

        } else {

            if (x < 4) {
                $div.append($("<img class='wideSc' src=" + imagen + " alt='" + i + "'>"));

            } else {
                $div.append($("<img  class='support' src=" + imagen + " alt='" + i + "'>"));

            }
        }
        x++;
        //add
    });
    $("#cItems").append($div);
}