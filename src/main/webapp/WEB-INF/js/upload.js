 $(document).ready(function() {

$("#btnSubmit").click(function(event) {

    // stop submit the form, we will post it manually.
    event.preventDefault();

    fire_ajax_submit();

});

});

 function fire_ajax_submit() {

// Get form
var form = $('#fileUploadForm')[0];

var data = new FormData(form);

data.append("CustomField", "This is some extra data, testing");

// $("#btnSubmit").prop("disabled", true);
var token = $("meta[name='_csrf']").attr("content");
var header = $("meta[name='_csrf_header']").attr("content");

$(document).ajaxSend(function(e, xhr, options) {
    xhr.setRequestHeader(header, token);
});

$.ajax({
    method : "POST",
    enctype : 'multipart/form-data',
    url : "lotConfig/lotImage",
    data : data,
    // http://api.jquery.com/jQuery.ajax/
    // https://developer.mozilla.org/en-US/docs/Web/API/FormData/Using_FormData_Objects
    processData : false, // prevent jQuery from automatically
    // transforming the data into a query string
    contentType : false,
    cache : false,
    timeout : 600000,
    success : function(data) {

        jQuery('#lotImageget').html('');
        getAllLotiamges();
        $('#updateImage').modal('hide');

        /*
         * $("#result").text(data); console.log("SUCCESS : ", data);
         * $("#btnSubmit").prop("disabled", false);
         */

    },
    error : function(e) {

        $("#result").text(e.responseText);
        console.log("ERROR : ", e);
        // $("#btnSubmit").prop("disabled", false);

    }   
});
}