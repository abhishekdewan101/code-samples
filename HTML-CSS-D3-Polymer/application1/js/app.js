window.addEventListener("load",function(){
    		try{
    			var widgetAPI = new Common.API.Widget();
            	widgetAPI.sendReadyEvent();
            } catch(e){
            	widgetAPI.sendReadyEvent = function(){};
            }
            webapis.multiscreen.Device.getCurrent(function(device){
              console.log(device);
              var channelID = "myChannelID";
              var clientAttributes = {name:"Alan"};

              device.openChanel(channelID,clientAttributes,
                            function(channel){
                                console.log("Channel Open")
                            },
                            function(error){
                                console.error(error);
                            });
            });
           });

