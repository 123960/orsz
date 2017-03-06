function saveSuggestion() {
  console.log("Saving suggestion!!");
  var refDate    = new Date()
  var content    = document.getElementById("textarea-new-sugg-content").value;
  var suggestion = { name: "suggestion-" + refDate.getTime(),
                     id: refDate.getTime(),
                     version: [refDate, "1.0"],
                     content: content,
                     upvotes: [refDate, 0],
                     downvotes: [refDate, 0]};
  createSuggestionDetail(suggestion);
  showSuggestion(suggestion);
  document.getElementById("textarea-new-sugg-content").value = "";
}
function showSuggestion(suggestion) {
    var col = document.createElement("div");
    col.className = "col-sm-5";
    var panel = document.createElement("div");
    panel.className = "panel panel-default";
    var d = document.createElement("div");
    d.className = "panel-body";
    var br = document.createElement("br");
    d.appendChild(document.createTextNode("[#]:" + suggestion.id));
    d.appendChild(br);
    d.appendChild(document.createTextNode("[Nome]: " + suggestion.name));
    d.appendChild(br.cloneNode(true));
    d.appendChild(document.createTextNode("[Versao]: " + suggestion.version[1]));
    d.appendChild(br.cloneNode(true));
    d.appendChild(document.createTextNode("[Conteudo]: " + suggestion.content));
    var detailsButton = document.createElement("button");
    var p = document.createElement("p");
    detailsButton.appendChild(document.createTextNode('Detalhes'));
    detailsButton.className="btn btn-success";
    detailsButton.setAttribute("data-toggle", "modal");
    detailsButton.setAttribute("data-target", "#sugg-detail-modal-" + suggestion.id);
    detailsButton.onclick = function() {

    };
    p.appendChild(detailsButton);
    d.appendChild(p);

    var tableSuggs = document.getElementById("table-suggs");
    panel.appendChild(d);
    col.appendChild(panel);
    tableSuggs.appendChild(col);
};
function cancelSuggestion() {
  console.log("Cleaning textarea!!");
  document.getElementById("textarea-new-sugg-content").value = "";
}
function createSuggestionDetail(suggestion) {
  var modal = document.createElement("div");
  modal.setAttribute("class", "modal fade");
  modal.setAttribute("id", "sugg-detail-modal-" + suggestion.id);
  modal.setAttribute("role", "dialog");

  var modalDialog = document.createElement("div");
  modalDialog.setAttribute("class", "modal-dialog modal-lg");

  var modalContent = document.createElement("div");
  modalContent.setAttribute("class", "modal-content");

  var modalHeader = document.createElement("div");
  modalHeader.setAttribute("class", "modal-header");

  var modalTitle = document.createElement("h4");
  modalTitle.setAttribute("class","modal-title");
  modalTitle.appendChild(document.createTextNode("Detalhes da proposta " + suggestion.name));

  var modalDetailContent = document.createElement("div");
  modalDetailContent.setAttribute("id","sugg-detail-content-" + suggestion.id);
  modalDetailContent.setAttribute("class","jumbotron col-lg-12");
  modalDetailContent.appendChild(document.createTextNode(suggestion.content));

  var modalDetailComments = document.createElement("div");
  modalDetailComments.setAttribute("id","sugg-detail-comments-" + suggestion.id);

  var modalDetailRow = document.createElement("div");
  modalDetailRow.setAttribute("class","row");

  var modalDetailCommentsFavor = document.createElement("div");
  modalDetailCommentsFavor.setAttribute("id","sugg-detail-comments-favor-" + suggestion.id);
  modalDetailCommentsFavor.setAttribute("class","col-lg-6");

  var modalDetailCommentsFavorFormGroup = document.createElement("div");
  modalDetailCommentsFavorFormGroup.setAttribute("class","form-group has-success");

  var modalDetailCommentsFavorLabel = document.createElement("label");
  modalDetailCommentsFavorLabel.setAttribute("class","control-label");
  modalDetailCommentsFavorLabel.value = "Coment�rios favor�veis";

  var modalDetailCommentsFavorInput = document.createElement("input");
  modalDetailCommentsFavorInput.setAttribute("type","text");
  modalDetailCommentsFavorInput.setAttribute("class","form-control");
  modalDetailCommentsFavorInput.setAttribute("onkeydown","saveComment("+suggestion.id+", 'favor', this.value)");
  modalDetailCommentsFavorInput.setAttribute("id","sugg-comment-favor-" + suggestion.id);

  var modalDetailCommentsOpposed = document.createElement("div");
  modalDetailCommentsOpposed.setAttribute("id","sugg-detail-comments-opposed-" + suggestion.id);
  modalDetailCommentsOpposed.setAttribute("class","col-lg-6");

  var modalDetailCommentsOpposedFormGroup = document.createElement("div");
  modalDetailCommentsOpposedFormGroup.setAttribute("class","form-group has-error");

  var modalDetailCommentsOpposedLabel = document.createElement("label");
  modalDetailCommentsOpposedLabel.setAttribute("class","control-label");
  modalDetailCommentsOpposedLabel.value = "Coment�rios contr�rios";

  var modalDetailCommentsOpposedInput = document.createElement("input");
  modalDetailCommentsOpposedInput.setAttribute("type","text");
  modalDetailCommentsOpposedInput.setAttribute("class","form-control");
  modalDetailCommentsOpposedInput.setAttribute("onkeydown","saveComment("+suggestion.id+", 'opposed', this.value)");
  modalDetailCommentsOpposedInput.setAttribute("id","sugg-comment-opposed-" + suggestion.id);

  modalDetailCommentsOpposedFormGroup.appendChild(modalDetailCommentsOpposedLabel);
  modalDetailCommentsOpposedFormGroup.appendChild(modalDetailCommentsOpposedInput);
  modalDetailCommentsOpposed.appendChild(modalDetailCommentsOpposedFormGroup);

  modalDetailCommentsFavorFormGroup.appendChild(modalDetailCommentsFavorLabel);
  modalDetailCommentsFavorFormGroup.appendChild(modalDetailCommentsFavorInput);
  modalDetailCommentsFavor.appendChild(modalDetailCommentsFavorFormGroup);

  modalDetailRow.appendChild(modalDetailCommentsFavor);
  modalDetailRow.appendChild(modalDetailCommentsOpposed);

  modalDetailComments.appendChild(modalDetailRow);

  modalHeader.appendChild(modalTitle);

  modalContent.appendChild(modalHeader);
  modalContent.appendChild(modalDetailContent);
  modalContent.appendChild(modalDetailComments);
  modalDialog.appendChild(modalContent);
  modal.appendChild(modalDialog);

  document.getElementById("sugg-details").appendChild(modal);

}
function saveComment(suggId, orientation, comment) {
  if(event.keyCode == 13) {
    var commentDiv         = document.getElementById("sugg-detail-comments-" + orientation + "-" + suggId);
    var commentBlockQuote  = document.createElement("blockquote");

    var upButton     = document.createElement("a");
    upButton.setAttribute("href", "#");
    upButton.setAttribute("class", "btn btn-success");
    upButton.appendChild(document.createTextNode("UP"));

    var upBadge     = document.createElement("span");
    upBadge.setAttribute("class", "badge");
    upBadge.appendChild(document.createTextNode("0"));
    upButton.appendChild(upBadge);

    var downButton     = document.createElement("a");
    downButton.setAttribute("href", "#");
    downButton.setAttribute("class", "btn btn-danger");
    downButton.appendChild(document.createTextNode("DOWN"));

    var downBadge     = document.createElement("span");
    downBadge.setAttribute("class", "badge");
    downBadge.appendChild(document.createTextNode("0"));
    downButton.appendChild(downBadge);

    if (orientation == "opposed") {
      commentBlockQuote.setAttribute("class", "blockquote-reverse");
    }

    var modalDetailContent = document.getElementById("sugg-detail-comments-" + suggId).children[0];
    var commentP           = document.createElement("p");
    commentP.appendChild(document.createTextNode(comment));
    commentBlockQuote.appendChild(commentP);
    commentBlockQuote.appendChild(upButton);
    commentBlockQuote.appendChild(downButton);
    modalDetailContent.appendChild(commentBlockQuote);
  }
}

/*<div class="modal fade" id="sugg-detail-modal" role="dialog">
  <div class="modal-dialog modal-lg">
    <div class="modal-content">
      <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Detalhes da proposta</h4>
      </div>
      <div id="sugg-detail-content" class="jumbotron col-lg-12"></div>
      <div id="sugg-detail-comments">
        <div class="row">
          <div id="sugg-detail-comments-favor" class="col-lg-6">
            <div class="form-group has-success">
              <label class="control-label" for="inputSuccess">Input success</label>
              <input type="text" class="form-control" id="inputSuccess">
            </div>
            <blockquote>
              <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
              <small>Someone famous in <cite title="Source Title">Source Title</cite></small>
              <a href="#" class="btn btn-success">UP <span class="badge" id="sugg-detail-favor-counter">42</span></a>
              <a href="#" class="btn btn-danger">DOWN <span class="badge" id="sugg-detail-opposed-counter">42</span></a>
            </blockquote>
          </div>
          <div id="sugg-detail-comments-opposed" class="col-lg-6">
            <div class="form-group has-error">
              <label class="control-label" for="inputError">Input error</label>
              <input type="text" class="form-control" id="inputError">
            </div>
            <div class="bs-component">
              <blockquote class="blockquote-reverse">
                <p>Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer posuere erat a ante.</p>
                <small>Someone famous in <cite title="Source Title">Source Title</cite></small>
                <a href="#" class="btn btn-success">UP <span class="badge" id="sugg-detail-favor-counter">42</span></a>
                <a href="#" class="btn btn-danger">DOWN <span class="badge" id="sugg-detail-opposed-counter">42</span></a>
              </blockquote>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>*/
