function saveProposition() {
  $("#save-prop-button").prop("disabled",true);
  console.log("Saving Proposition!!");
  var refDate    = new Date()
  var content    = $('#prop-content').summernote('code');
  var name       = $('#prop-name').val();
  var prop       = { id: "<genId>",
                     owner: "vini",
                     createDate: refDate.getTime().toString(),
                     name: name,
                     version: "1",
                     content: content,
                     status: "NEW",
                     upvotes: 0,
                     downvotes: 0 ,
                     views: 0 };
  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && this.status == 200) {
      $("#save-prop-progress").hide();
      $("#save-prop-button").prop("disabled",false);
      var savePropToast = document.querySelector('#save-prop-toast');
      savePropToast.MaterialSnackbar.showSnackbar({message: 'Proposition saved successfully!'});
    }
  };
  $("#save-prop-progress").show();
  xhttp.open("POST", "http://localhost:9000/orsz/proposition/" + prop.id, true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  xhttp.send(JSON.stringify(prop));
}
