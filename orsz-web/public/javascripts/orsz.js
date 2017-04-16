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

function upvoteProposition(id, owner) {
  $("#upvote-" + id).prop("disabled", true);
  $("#downvote-" + id).prop("disabled", true);
  $("#upvote-" + id).prop("style","cursor: pointer; color:green; font:bold;");
  $("#upvote-count-" + id).text(parseInt($("#upvote-count-" + id).text()) + 1);
  console.log("Upvoting proposition!!");
  var refDate    = new Date()
  var vote       = { id:        "<genId>",
                     voter:     "vini",
                     propId:    id,
                     propOwner: owner,
                     voteType:  "upvote",
                     voteDate:  refDate.getTime().toString() };

  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && (this.status == 200 || this.status == 204)) {
      console.log("Upvoting successfully!")
      $("#upvote-" + id).prop("onclick","javascript:unUpvoteProposition()");
      $("#upvote-" + id).prop("disabled",false);
    }
  };
  console.log(JSON.stringify(vote))
  xhttp.open("POST", "http://localhost:9000/orsz/vote/" + vote.id, true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  xhttp.send(JSON.stringify(vote));
}

function downvoteProposition(id, owner) {
  $("#downvote-" + id).prop("disabled", true);
  $("#upvote-" + id).prop("disabled", true);
  $("#downvote-" + id).prop("style","cursor: pointer; color:red; font:bold;");
  $("#downvote-count-" + id).text(parseInt($("#downvote-count-" + id).text()) + 1);
  console.log("Downvoting proposition!!");
  var refDate    = new Date()
  var vote       = { id:        "<genId>",
                     voter:     "vini",
                     propId:    id,
                     propOwner: owner,
                     voteType:  "downvote",
                     voteDate:  refDate.getTime().toString() };

  var xhttp = new XMLHttpRequest();
  xhttp.onreadystatechange = function() {
    if (this.readyState == 4 && (this.status == 200 || this.status == 204)) {
      console.log("Downvoting successfully!")
      $("#downvote-" + id).prop("onclick","javascript:unDownvoteProposition()");
      $("#downvote-" + id).prop("disabled",false);
    }
  };
  xhttp.open("POST", "http://localhost:9000/orsz/vote/" + vote.id, true);
  xhttp.setRequestHeader("Content-Type", "application/json");
  xhttp.send(JSON.stringify(vote));
}

function commentProposition(e, propId, type) {
  if (e.keyCode == 13) {
    var content = $("#comment-" + type + "-" + propId).text();
    console.log("Commenting proposition!!");
    var refDate    = new Date()
    var comment    = { id:          "<genId>",
                       propId:      propId,
                       user:        "vini",
                       commentType: type,
                       content:     content,
                       commentDate: refDate.getTime().toString() };

    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
      if (this.readyState == 4 && (this.status == 200 || this.status == 204)) {
        console.log("Comment successfully!")
      }
    };
    xhttp.open("POST", "http://localhost:9000/orsz/comment/" + comment.id, true);
    xhttp.setRequestHeader("Content-Type", "application/json");
    xhttp.send(JSON.stringify(comment));
  }
}
