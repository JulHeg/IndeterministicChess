function sendPressedSquareMessage(x, y) {
	$.ajax({
		type:"POST",
		url: ("http://localhost:8080/squareButtonPressed?xPos="+x+"&yPos="+y)
		});
}

function sendPressedMoveMessage() {
		$.ajax({
			type:"POST",
			url: ("http://localhost:8080/moveButtonPressed")
			});
}

function sendPressedSplitMessage() {
		$.ajax({
			type:"POST",
			url: ("http://localhost:8080/splitButtonPressed")
			});
}

function sendPressedRedetermineMessage() {
		$.ajax({
			type:"POST",
			url: ("http://localhost:8080/redetermineButtonPressed")
			});
}

function sendPressedEndMessage() {
		$.ajax({
			type:"POST",
			url: ("http://localhost:8080/endButtonPressed")
			});
}

$.get("http://localhost:8080/getBoard",
  function(data){
	var rows = data.content.split("\n");
	var content = '<table class="chessboard" id="chessboard">';
 	for(y=1; y <= 8; y++){
 		content += "<tr>";
     	for(x=1; x <= 8; x++){
     		content += '<td><button type="button" class="SquareButtons" id="Square' + x + "_" + y + '" onClick="sendPressedSquareMessage(' + x + "," + y + ');">' + rows[y-1].charAt(x-1) + '</button></td>';
     	}
 		content += "</tr>";
 	}
	content += "</table>";
    $("#chessboardDiv").append(content); 
  });
