$("#setIpButton").click(function () {
    var ipAddress = $("#ipValue").val();
   localStorage.setItem("meteoserverAddress", ipAddress);
   console.log(ipAddress + " saved to localStorage.");
})

$( document ).ready(function() {
    const ipAddress = localStorage.getItem("meteoserverAddress");

    if(ipAddress !== undefined){
        $("#ipValue").val(ipAddress);
    }
});