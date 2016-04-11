$(document).ready(function() {

  $("textarea").keydown(function() {
    $("#output").removeClass("show");
    $("#output-success").hide();
  });

  $("button").click(function() {
    var url = $("textarea").val();
    $("#output-url").html(url);

    var handler = function(data) {
      if (!data.err) {
        $("#output-newurl").html(data.short);
        $("#output-success").show();
      }
      $("#output").addClass("show");
      $("#output-resp").html(JSON.stringify(data));
    };

    $.ajax("/new?url=" + url, {
      error: handler,
      success: handler
    });
  });
});
